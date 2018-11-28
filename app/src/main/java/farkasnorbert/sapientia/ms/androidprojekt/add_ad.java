package farkasnorbert.sapientia.ms.androidprojekt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class add_ad extends AppCompatActivity {
    private Ad ad;
    private final int PICK_IMAGE_REQUEST = 71;
    private ImageView image1;
    private ImageView image2;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(add_ad.this, login_activity.class));
                    return true;
                case R.id.navigation_add:
                    EditText title = findViewById(R.id.title);
                    ad.setTitle(title.getText().toString());
                    EditText sdesc = findViewById(R.id.desc);
                    ad.setSdesc(sdesc.getText().toString());
                    EditText ldesc = findViewById(R.id.ldesc);
                    ad.setLdesc(ldesc.getText().toString());
                    EditText phone = findViewById(R.id.phone);
                    ad.setPhone(phone.getText().toString());
                    EditText location = findViewById(R.id.location);
                    ad.setLocation(location.getText().toString());

                    return true;
                case R.id.navigation_settings:
                    startActivity(new Intent(add_ad.this, login_activity.class));
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
            Log.i("kep", filePath.toString());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image1.setImageBitmap(bitmap);
                if(ad.getImagesSize()>=1) {
                    Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), ad.getImg(ad.getImagesSize() - 1));
                    image2.setImageBitmap(bitmap2);
                }
                ad.addImg(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
