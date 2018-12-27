package farkasnorbert.sapientia.ms.androidprojekt.Activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import farkasnorbert.sapientia.ms.androidprojekt.Modell.User;
import farkasnorbert.sapientia.ms.androidprojekt.Other.GlideApp;
import farkasnorbert.sapientia.ms.androidprojekt.R;

public class SettingsActivity extends AppCompatActivity {

    private String phone;
    private User user;
    private final int PICK_IMAGE_REQUEST = 71;
    private DatabaseReference mDatabase;
    private EditText email;
    private EditText fName;
    private EditText lName;
    private EditText adress;
    private EditText uPhone;
    private ImageView pPicture;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent i = new Intent(SettingsActivity.this, AdsActivity.class);
                    i.putExtra("Phone", phone);
                    startActivity(i);
                    return true;
                case R.id.navigation_add:
                    i = new Intent(SettingsActivity.this, AddAdActivity.class);
                    i.putExtra("Phone", phone);
                    startActivity(i);
                    return true;
                case R.id.navigation_settings:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        Intent intent = getIntent();
        phone = intent.getStringExtra("Phone");
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lname);
        email = findViewById(R.id.email);
        adress = findViewById(R.id.email);
        uPhone = findViewById(R.id.phone);
        pPicture = findViewById(R.id.profilePicture);
        loadUser();
        Button save = findViewById(R.id.bSave);
        save.setOnClickListener(v -> {
            //save changes and reload
        });
        Button pPic = findViewById(R.id.bGetPic);
        pPic.setOnClickListener(v -> chooseImage());
    }

    private void loadUser() {
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.child(phone).getValue(User.class);
                fName.setText(user.getfName());
                lName.setText(user.getlName());
                email.setText(user.getEmail());
                uPhone.setText(phone);
                adress.setText(user.getAdress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri filePath;
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                pPicture.setImageBitmap(bitmap);
                GlideApp.with(SettingsActivity.this).load(bitmap).into(pPicture);
            } catch (IOException e) {
                e.printStackTrace();
            }
            user.setpPicture(filePath.toString());
        }
    }
}
