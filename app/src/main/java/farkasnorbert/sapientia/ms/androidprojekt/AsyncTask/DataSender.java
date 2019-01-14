package farkasnorbert.sapientia.ms.androidprojekt.AsyncTask;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import farkasnorbert.sapientia.ms.androidprojekt.Modell.Ad;

public class DataSender extends AsyncTaskLoader<String> {
    private Ad ad;

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
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        for(String i : ad.getImages()){
            Uri img= Uri.parse(i);
            StorageReference riversRef = mStorageRef.child("images/" + ad.getPhone() + i.substring(i.lastIndexOf('/')));
            images.add("images/" + ad.getPhone() + i.substring(i.lastIndexOf('/')));
            riversRef.putFile(img).addOnSuccessListener(taskSnapshot -> ok[0] =true).addOnFailureListener(e -> {
                ok[0] =false;
            });
        }
        for(int i=0;i<ad.getImagesSize();i++){
            ad.setImg(images.get(i),i);
        }
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("data").child(ad.getPhone()+"_"+ad.getTitle()).setValue(ad);
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
