package farkasnorbert.sapientia.ms.androidprojekt.Activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import farkasnorbert.sapientia.ms.androidprojekt.Modell.Ad;
import farkasnorbert.sapientia.ms.androidprojekt.Modell.User;
import farkasnorbert.sapientia.ms.androidprojekt.Other.GlideApp;
import farkasnorbert.sapientia.ms.androidprojekt.Other.OnSwipeTouchListener;
import farkasnorbert.sapientia.ms.androidprojekt.R;


public class AdViewActivity extends AppCompatActivity {

    private User user;
    private Ad ad;
    private EditText lDesc;
    private EditText fName;
    private EditText lName;
    private EditText location;
    private ImageView image;
    private ImageView pPicture;
    private EditText sDesc;
    private int index = 0;
    private FirebaseStorage storage;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adview);
        lDesc = findViewById(R.id.lDesc);
        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lName);
        sDesc = findViewById(R.id.sDesc);
        location = findViewById(R.id.location);
        image = findViewById(R.id.image);
        pPicture = findViewById(R.id.pPicture);
        ImageButton delete = findViewById(R.id.deleteAd);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String p = sp.getString("Phone", "");
        Intent intent = getIntent();
        ad = intent.getParcelableExtra("Ad");
        ImageButton updateAd = findViewById(R.id.updateAd);
        lName.setFocusable(false);
        lName.setClickable(false);
        fName.setFocusable(false);
        fName.setClickable(false);
        if (ad.getPhone().equals(p)) {
            updateAd.setOnClickListener(v -> {
                //update add
                ad.setLocation(location.getText().toString());
                ad.setLdesc(lDesc.getText().toString());
                ad.setSdesc(sDesc.getText().toString());
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("data").child(ad.getPhone()+"_"+ad.getTitle()).setValue(ad);
            });
            delete.setOnClickListener(v ->{
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("data").child(ad.getPhone()+"_"+ad.getTitle()).removeValue();
                Intent i = new Intent(this,AdsActivity.class);
                i.putExtra("Phone",user.getPhone());
                startActivity(i);
            });
        } else {
            updateAd.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            lDesc.setFocusable(false);
            lDesc.setClickable(false);
            location.setFocusable(false);
            location.setClickable(false);
            sDesc.setFocusable(false);
            sDesc.setClickable(false);
        }
        lDesc.setText(ad.getLdesc());
        sDesc.setText(ad.getSdesc());
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
                    if (!user.getpPicture().equals("")) {
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
        storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReference().child(ad.getImg(index));
        GlideApp.with(this)
                .load(gsReference)
                .into(image);
        image.setOnTouchListener(new OnSwipeTouchListener(AdViewActivity.this) {
            public void onSwipeLeft() {
                if(index<ad.getImages().size()-1) {
                    StorageReference gsReference = storage.getReference().child(ad.getImg(index+1));
                    GlideApp.with(getApplicationContext())
                            .load(gsReference)
                            .into(image);
                    index++;
                }
            }
            public void onSwipeRight() {
                if(index>0) {
                    StorageReference gsReference = storage.getReference().child(ad.getImg(index-1));
                    GlideApp.with(getApplicationContext())
                            .load(gsReference)
                            .into(image);
                    index--;
                }
            }
        });
        ImageButton share = findViewById(R.id.share);
        share.setOnClickListener(v -> {
            OutputStream outStream = null;
            Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            try {
                if (ContextCompat.checkSelfPermission(AdViewActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(AdViewActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            2121);

                    return;

                }
                File file = File.createTempFile("images", ".jpg",this.getExternalCacheDir());
                outStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outStream);
                String message = ad.getTitle() + " \n" + ad.getSdesc();
                Intent share1 = new Intent();
                share1.setAction(Intent.ACTION_SEND);
                share1.putExtra(Intent.EXTRA_TEXT, message);
                share1.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                share1.setType("image/jpeg");
                share1.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                share1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(Intent.createChooser(share1, "Share"),198);
                outStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(getApplicationContext(), AdsActivity.class);
            intent.putExtra("Phone", ad.getPhone());
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
