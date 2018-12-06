package farkasnorbert.sapientia.ms.androidprojekt;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;


public class adview extends AppCompatActivity{
    private static final String TAG = "fel";
    EditText lDesc;
    EditText phone;
    EditText location;
    ImageView image;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adview);
        lDesc = findViewById(R.id.lDesc);
        phone = findViewById(R.id.phone);
        location = findViewById(R.id.location);
        image = findViewById(R.id.image);
        lDesc.setFocusable(false);
        lDesc.setClickable(false);
        phone.setFocusable(false);
        phone.setClickable(false);
        location.setFocusable(false);
        location.setClickable(false);
        mDatabase = FirebaseDatabase.getInstance().getReference("data2").child("Test");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Ad ad=dataSnapshot.getValue(Ad.class);
                datacolect(ad);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.");
            }
        });
    }
    private void datacolect(Ad value) {
        lDesc.setText(value.getLdesc());
        phone.setText(value.getPhone());
        location.setText(value.getLocation());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReference().child(value.getImg(0));
        GlideApp.with(this)
                .load(gsReference)
                .into(image);
    }
}
