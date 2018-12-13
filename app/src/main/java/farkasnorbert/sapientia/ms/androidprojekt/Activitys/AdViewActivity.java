package farkasnorbert.sapientia.ms.androidprojekt.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import farkasnorbert.sapientia.ms.androidprojekt.Modell.Ad;
import farkasnorbert.sapientia.ms.androidprojekt.Other.GlideApp;
import farkasnorbert.sapientia.ms.androidprojekt.R;


public class AdViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adview);
        EditText lDesc = findViewById(R.id.lDesc);
        EditText phone = findViewById(R.id.phone);
        EditText location = findViewById(R.id.location);
        ImageView image = findViewById(R.id.image);
        lDesc.setFocusable(false);
        lDesc.setClickable(false);
        phone.setFocusable(false);
        phone.setClickable(false);
        location.setFocusable(false);
        location.setClickable(false);
        Intent intent = getIntent();
        Ad ad = intent.getParcelableExtra("Ad");
        lDesc.setText(ad.getLdesc());
        phone.setText(ad.getPhone());
        location.setText(ad.getLocation());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReference().child(ad.getImg(0));
        GlideApp.with(this)
                .load(gsReference)
                .into(image);
    }
}
