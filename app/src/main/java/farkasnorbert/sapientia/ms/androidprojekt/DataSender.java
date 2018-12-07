package farkasnorbert.sapientia.ms.androidprojekt;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class DataSender extends AsyncTaskLoader<String> {
    private Ad ad;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabase;
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
        ArrayList<String> images = new ArrayList<String>();
        final boolean[] ok = {true};
        mStorageRef = FirebaseStorage.getInstance().getReference();
        for(String i : ad.getImages()){
            Uri img= Uri.parse(i);
            StorageReference riversRef = mStorageRef.child("images/" + ad.getPhone() + i.substring(i.lastIndexOf('/')));
            images.add("images/" + ad.getPhone() + i.substring(i.lastIndexOf('/')));
            Log.d("fel", "images/" + ad.getPhone() + i.substring(i.lastIndexOf('/')));
            riversRef.putFile(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
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
        for(int i=0;i<ad.getImagesSize();i++){
            ad.setImg(images.get(i),i);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("data2").child(ad.getTitle()).setValue(ad);
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
