package farkasnorbert.sapientia.ms.androidprojekt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class adview extends AppCompatActivity{
    private EditText lDesc;
    private EditText phone;
    private EditText location;
    private ImageView image;
    private String ad;
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
        Intent intent = getIntent();
        ad = intent.getStringExtra("Ad");
        mDatabase = FirebaseDatabase.getInstance().getReference("data2").child(ad);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Ad ad=dataSnapshot.getValue(Ad.class);
                datacolect(ad);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.w(TAG, "Failed to read value.");
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
