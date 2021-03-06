package farkasnorbert.sapientia.ms.androidprojekt.Activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import farkasnorbert.sapientia.ms.androidprojekt.AsyncTask.DataSender;
import farkasnorbert.sapientia.ms.androidprojekt.Modell.Ad;
import farkasnorbert.sapientia.ms.androidprojekt.Other.GlideApp;
import farkasnorbert.sapientia.ms.androidprojekt.Other.OnSwipeTouchListener;
import farkasnorbert.sapientia.ms.androidprojekt.R;

public class AddAdActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private Ad ad;
    private String phone;
    private final int PICK_IMAGE_REQUEST = 71;
    private ImageView image1;
    private int imgindex=-1;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent i = new Intent(AddAdActivity.this, AdsActivity.class);
                    i.putExtra("Phone", phone);
                    startActivity(i);
                    return true;
                case R.id.navigation_settings:
                    i = new Intent(AddAdActivity.this, SettingsActivity.class);
                    i.putExtra("Phone", phone);
                    startActivity(i);
                    return true;
                case R.id.navigation_add:
                    EditText title = findViewById(R.id.title);
                    if(title.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "No title", Toast.LENGTH_LONG).show();
                        return true;
                    }
                    ad.setTitle(title.getText().toString());
                    EditText sdesc = findViewById(R.id.desc);
                    ad.setSdesc(sdesc.getText().toString());
                    EditText ldesc = findViewById(R.id.ldesc);
                    ad.setLdesc(ldesc.getText().toString());
                    EditText uphone = findViewById(R.id.sDesc);
                    ad.setPhone(uphone.getText().toString());
                    EditText location = findViewById(R.id.location);
                    ad.setLocation(location.getText().toString());
                    if(ad.getImagesSize()>0) {
                        Bundle queryBundle = new Bundle();
                        queryBundle.putString("AD", new Gson().toJson(ad));
                        getSupportLoaderManager().restartLoader(0, queryBundle, AddAdActivity.this);
                    }else{
                        Toast.makeText(getApplicationContext(), "No image added", Toast.LENGTH_LONG).show();
                    }
                    return true;
            }
            return false;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ad);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        Intent intent = getIntent();
        phone = intent.getStringExtra("Phone");
        EditText uphone = findViewById(R.id.sDesc);
        uphone.setText(phone);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ImageButton addimg = findViewById(R.id.addimg);
        addimg.setOnClickListener(v -> chooseImage());
        ad = new Ad();
        image1 = findViewById(R.id.image1);
        image1.setOnTouchListener(new OnSwipeTouchListener(AddAdActivity.this) {
            public void onSwipeLeft() {
                if(imgindex<ad.getImages().size()-1) {
                    try {
                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(ad.getImg(imgindex+1)));
                    GlideApp.with(getApplicationContext())
                            .load(bitmap1)
                            .into(image1);
                    imgindex++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            public void onSwipeRight() {
                if(imgindex>0) {
                    try {
                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(ad.getImg(imgindex-1)));
                    GlideApp.with(getApplicationContext())
                            .load(bitmap1)
                            .into(image1);
                    imgindex--;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void chooseImage() {
        if (ContextCompat.checkSelfPermission(AddAdActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(AddAdActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    2121);

            return;
        }
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
                GlideApp.with(AddAdActivity.this).load(bitmap).into(image1);
                imgindex++;
            } catch (IOException e) {
                e.printStackTrace();
            }
            ad.addImg(filePath.toString());
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        String s = "";
        ArrayList<String> imgs;
        if (bundle != null) {
            s = bundle.getString("AD");
        }
        Ad ad = new Gson().fromJson(s, Ad.class);
        return new DataSender(this, ad);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        if (s.equals("Jo")) {
            startActivity(new Intent(this, AdsActivity.class));
        } else {
            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent intent = new Intent(getApplicationContext(),AdsActivity.class);
            intent.putExtra("Phone",phone);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
