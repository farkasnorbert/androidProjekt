package farkasnorbert.sapientia.ms.androidprojekt.Activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;

import farkasnorbert.sapientia.ms.androidprojekt.R;

public class SettingsActivity extends AppCompatActivity {

    private String phone;
    private final int PICK_IMAGE_REQUEST = 71;
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
        Button save = findViewById(R.id.bSave);
        save.setOnClickListener(v -> {
            //save changes and reload
        });
        Button pPic = findViewById(R.id.bGetPic);
        pPic.setOnClickListener(v -> chooseImage());
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
            /*try {
                /*Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //image1.setImageBitmap(bitmap);
                GlideApp.with(AddAdActivity.this).load(bitmap).into(image1);
                if (ad.getImagesSize() >= 1) {
                    Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(ad.getImg(ad.getImagesSize() - 1)));
                    //image2.setImageBitmap(bitmap2);
                    GlideApp.with(AddAdActivity.this).load(bitmap2).into(image2);
                    imgindex=ad.getImagesSize() - 1;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //ad.addImg(filePath.toString());*/
        }
    }
}
