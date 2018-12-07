package farkasnorbert.sapientia.ms.androidprojekt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

public class add_ad extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private Ad ad;
    private String phone;
    private Intent i;
    private final int PICK_IMAGE_REQUEST = 71;
    private ImageView image1;
    private ImageView image2;
    private ImageView previous;
    private ImageView next;
    private int imgindex;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    i = new Intent(add_ad.this, ads.class);
                    i.putExtra("Phone", phone);
                    startActivity(i);
                    return true;
                case R.id.navigation_settings:
                    i = new Intent(add_ad.this, settings.class);
                    i.putExtra("Phone", phone);
                    startActivity(i);
                    return true;
                case R.id.navigation_add:
                    EditText title = findViewById(R.id.title);
                    ad.setTitle(title.getText().toString());
                    EditText sdesc = findViewById(R.id.desc);
                    ad.setSdesc(sdesc.getText().toString());
                    EditText ldesc = findViewById(R.id.ldesc);
                    ad.setLdesc(ldesc.getText().toString());
                    EditText uphone = findViewById(R.id.phone);
                    ad.setPhone(uphone.getText().toString());
                    EditText location = findViewById(R.id.location);
                    ad.setLocation(location.getText().toString());
                    Bundle queryBundle = new Bundle();
                    queryBundle.putString("AD", new Gson().toJson(ad));
                    getSupportLoaderManager().restartLoader(0, queryBundle, add_ad.this);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ad);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        Intent intent = getIntent();
        phone = intent.getStringExtra("Phone");
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ImageButton addimg = findViewById(R.id.addimg);
        addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        ad = new Ad();
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        previous = findViewById(R.id.buttonprevious);
        next = findViewById(R.id.buttonnext);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ad.getImagesSize() > 2) {
                    try {
                        Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(ad.getImg(imgindex-1)));
                        //image1.setImageBitmap(bitmap1);
                        GlideApp.with(add_ad.this).load(bitmap1).into(image1);
                        Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(ad.getImg(imgindex)));
                        //image2.setImageBitmap(bitmap2);
                        GlideApp.with(add_ad.this).load(bitmap2).into(image2);
                        imgindex--;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ad.getImagesSize() > 2) {
                    try {
                        Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(ad.getImg(imgindex+1)));
                        //image1.setImageBitmap(bitmap1);
                        GlideApp.with(add_ad.this).load(bitmap1).into(image1);
                        Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(ad.getImg(imgindex)));
                        //image2.setImageBitmap(bitmap2);
                        GlideApp.with(add_ad.this).load(bitmap2).into(image2);
                        imgindex++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
                //image1.setImageBitmap(bitmap);
                GlideApp.with(add_ad.this).load(bitmap).into(image1);
                if (ad.getImagesSize() >= 1) {
                    Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(ad.getImg(ad.getImagesSize() - 1)));
                    //image2.setImageBitmap(bitmap2);
                    GlideApp.with(add_ad.this).load(bitmap2).into(image2);
                    imgindex=ad.getImagesSize() - 1;
                }
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
        if (s == "Jo") {
            startActivity(new Intent(this, ads.class));
        } else {
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
