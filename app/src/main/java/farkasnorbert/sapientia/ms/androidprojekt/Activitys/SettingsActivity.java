package farkasnorbert.sapientia.ms.androidprojekt.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
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
import com.google.gson.Gson;

import java.io.IOException;

import farkasnorbert.sapientia.ms.androidprojekt.AsyncTask.UserUpdate;
import farkasnorbert.sapientia.ms.androidprojekt.Modell.Ad;
import farkasnorbert.sapientia.ms.androidprojekt.Modell.User;
import farkasnorbert.sapientia.ms.androidprojekt.Other.GlideApp;
import farkasnorbert.sapientia.ms.androidprojekt.R;

public class SettingsActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<String> {

    private String phone;
    private final int PICK_IMAGE_REQUEST = 71;
    private boolean pc = false;
    private User user;
    private EditText email;
    private EditText fName;
    private EditText lName;
    private EditText address;
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
        ImageButton save = findViewById(R.id.bSave);
        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lname);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        uPhone = findViewById(R.id.phone);
        pPicture = findViewById(R.id.profilePicture);
        loadUser();
        ImageButton logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor Ed = sp.edit();
            Ed.putString("Phone", "");
            Ed.apply();
            startActivity(new Intent(this, MainActivity.class));
        });
        save.setOnClickListener(v -> {
            //save changes and reload
            user.setAddress(address.getText().toString());
            user.setEmail(email.getText().toString());
            user.setfName(fName.getText().toString());
            user.setlName(lName.getText().toString());
            Bundle queryBundle = new Bundle();
            queryBundle.putString("USER", new Gson().toJson(user));
            queryBundle.putBoolean("PC",pc);
            getSupportLoaderManager().restartLoader(0, queryBundle, SettingsActivity.this);
        });
        Button pPic = findViewById(R.id.bGetPic);
        pPic.setOnClickListener(v -> chooseImage());
    }
    private void loadUser() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.child(phone).getValue(User.class);
                if (user != null) {
                    fName.setText(user.getfName());
                    lName.setText(user.getlName());
                    email.setText(user.getEmail());
                    uPhone.setText(phone);
                    address.setText(user.getAddress());
                    if(user.getpPicture()!="") {
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
            pc=true;
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        String s = "";
        Boolean pc = false;
        if (bundle != null) {
            s = bundle.getString("USER");
            pc = bundle.getBoolean("PC");
        }
        User u = new Gson().fromJson(s, User.class);
        return new UserUpdate(this,user,pc);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
