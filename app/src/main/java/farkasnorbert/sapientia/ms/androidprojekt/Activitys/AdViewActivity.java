package farkasnorbert.sapientia.ms.androidprojekt.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import farkasnorbert.sapientia.ms.androidprojekt.Modell.Ad;
import farkasnorbert.sapientia.ms.androidprojekt.Modell.User;
import farkasnorbert.sapientia.ms.androidprojekt.Other.GlideApp;
import farkasnorbert.sapientia.ms.androidprojekt.R;


public class AdViewActivity extends AppCompatActivity {

    private User user;
    private Ad ad;
    private EditText lDesc;
    private EditText phone;
    private EditText fName;
    private EditText lName;
    private EditText location;
    private ImageView image;
    private ImageView pPicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adview);
        lDesc = findViewById(R.id.lDesc);
        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lName);
        phone = findViewById(R.id.phone);
        location = findViewById(R.id.location);
        image = findViewById(R.id.image);
        pPicture = findViewById(R.id.pPicture);
        lDesc.setFocusable(false);
        lDesc.setClickable(false);
        phone.setFocusable(false);
        phone.setClickable(false);
        location.setFocusable(false);
        location.setClickable(false);
        Intent intent = getIntent();
        ad = intent.getParcelableExtra("Ad");
        lDesc.setText(ad.getLdesc());
        phone.setText(ad.getPhone());
        location.setText(ad.getLocation());
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.child(ad.getPhone()).getValue(User.class);
                if (user != null) {
                    fName.setText(user.getfName());
                    lName.setText(user.getlName());
                    location.setText(user.getAddress());
                    if(!user.getpPicture().equals("")) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference ref = storage.getReference().child(user.getpPicture());
                        Glide.with(getApplicationContext()).load(ref).into(pPicture);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReference().child(ad.getImg(0));
        GlideApp.with(this)
                .load(gsReference)
                .into(image);
    }
}
