package farkasnorbert.sapientia.ms.androidprojekt.Activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import farkasnorbert.sapientia.ms.androidprojekt.BuildConfig;
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
    private StorageReference gsReference;

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
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String p = sp.getString("Phone", "");
        Intent intent = getIntent();
        ad = intent.getParcelableExtra("Ad");
        ImageButton updateAd = findViewById(R.id.updateAd);
        if (ad.getPhone().equals(p)) {
            updateAd.setOnClickListener(v -> {
                //update add
            });
        } else {
            updateAd.setVisibility(View.GONE);
            lDesc.setFocusable(false);
            lDesc.setClickable(false);
            phone.setFocusable(false);
            phone.setClickable(false);
            location.setFocusable(false);
            location.setClickable(false);
            lName.setFocusable(false);
            lName.setClickable(false);
            fName.setFocusable(false);
            fName.setClickable(false);
        }
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
        FirebaseStorage storage = FirebaseStorage.getInstance();
        gsReference = storage.getReference().child(ad.getImg(0));
        GlideApp.with(this)
                .load(gsReference)
                .into(image);
        ImageButton share = findViewById(R.id.share);
        share.setOnClickListener(v -> {
            String message = ad.getTitle() + " \n" + ad.getSdesc();
            /*Uri bmpUri = getLocalBitmapUri(image);
            if (bmpUri != null) {
                Log.d("fel",bmpUri.toString());
                Intent share1 = new Intent();
                share1.setAction(Intent.ACTION_SEND);
                //share1.putExtra(Intent.EXTRA_TEXT, message);
                share1.putExtra(Intent.EXTRA_STREAM, bmpUri);
                share1.setType("image/png");
                //share1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                startActivity(Intent.createChooser(share1, "Share"));
            }*/
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // do something on back.
            Intent intent = new Intent(getApplicationContext(), AdsActivity.class);
            intent.putExtra("Phone", ad.getPhone());
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
