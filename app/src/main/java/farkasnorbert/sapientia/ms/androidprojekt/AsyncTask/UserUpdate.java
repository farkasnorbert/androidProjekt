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

import farkasnorbert.sapientia.ms.androidprojekt.Modell.User;

public class UserUpdate extends AsyncTaskLoader<String> {
    private User user;
    private boolean pic;

    public UserUpdate(@NonNull Context context, User user,boolean pic) {
        super(context);
        this.user = user;
        this.pic=pic;
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return updateData();
    }

    private String updateData(){
        if (pic) {
            final boolean[] o = new boolean[1];
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            Uri img = Uri.parse(user.getpPicture());
            StorageReference riversRef = mStorageRef.child("images/" + user.getPhone() + user.getpPicture().substring(user.getpPicture().lastIndexOf('/')));
            user.setpPicture("images/" + user.getPhone() + user.getpPicture().substring(user.getpPicture().lastIndexOf('/')));
            riversRef.putFile(img).addOnSuccessListener(taskSnapshot -> o[0] = true).addOnFailureListener(e -> {
                o[0] = false;
            });
        }
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(user.getPhone()).setValue(user);
        return user.getPhone();
    }
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
