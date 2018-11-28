package farkasnorbert.sapientia.ms.androidprojekt;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DataSender extends AsyncTaskLoader<String> {
    private Ad ad;
    private StorageReference mStorageRef;
    public DataSender(@NonNull Context context,Ad ad) {
        super(context);
        this.ad=ad;
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return sendData();
    }

    private String sendData() {
        final boolean[] ok = {true};
        mStorageRef = FirebaseStorage.getInstance().getReference();
        for(Uri img : ad.getImages()){
            StorageReference riversRef = mStorageRef.child("images/"+img.getLastPathSegment());
            riversRef.putFile(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Uri downloadUrl = taskSnapshot.get
                    ok[0] =true;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("fel", "onFailure: nem mentete el");
                    ok[0] =false;
                }
            });
        }
        if(ok[0]){
            return "Jo";
        }else
            return "nem jo";
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
