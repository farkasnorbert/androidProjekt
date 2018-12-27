package farkasnorbert.sapientia.ms.androidprojekt.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import farkasnorbert.sapientia.ms.androidprojekt.R;

public class MainActivity extends AppCompatActivity {

    private EditText phone;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String p = sp.getString("Phone", "");
        Log.d("MainActivityL", p);
        if (p == "") {
            Button loginb = findViewById(R.id.login_button);
            phone = findViewById(R.id.phone_number);
            loginb.setOnClickListener(v -> {
                mDatabase = FirebaseDatabase.getInstance().getReference("users");
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(phone.getText().toString()) != true) {
                            Intent send = new Intent(MainActivity.this, RegisterActivity.class);
                            send.putExtra("Phone", phone.getText().toString());
                            startActivity(send);
                        } else {
                            Intent send = new Intent(MainActivity.this, LoginActivity.class);
                            send.putExtra("Phone", phone.getText().toString());
                            startActivity(send);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            });
        } else {
            Log.d("MainActivity", "loaded");
            Intent send = new Intent(this, AdsActivity.class);
            send.putExtra("Phone", p);
            startActivity(send);
        }
    }


}
