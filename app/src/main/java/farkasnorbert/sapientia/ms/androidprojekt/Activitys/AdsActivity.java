package farkasnorbert.sapientia.ms.androidprojekt.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import farkasnorbert.sapientia.ms.androidprojekt.Modell.Ad;
import farkasnorbert.sapientia.ms.androidprojekt.Other.RecyclerViewAdapter;
import farkasnorbert.sapientia.ms.androidprojekt.R;

public class AdsActivity extends AppCompatActivity {

    private String phone;
    private DatabaseReference mDatabase;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent i;
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_add:
                    i = new Intent(AdsActivity.this, AddAdActivity.class);
                    i.putExtra("Phone", phone);
                    startActivity(i);
                    return true;
                case R.id.navigation_settings:
                    i = new Intent(AdsActivity.this, SettingsActivity.class);
                    i.putExtra("Phone", phone);
                    startActivity(i);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Intent intent = getIntent();
        phone = intent.getStringExtra("Phone");
        mDatabase = FirebaseDatabase.getInstance().getReference("data2");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                collectPhoneNumbers((Map<String, Ad>) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void collectPhoneNumbers(Map<String, Ad> value) {

        ArrayList<Ad> ads = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Ad> entry : value.entrySet()) {

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            Ad e = new Ad((String) singleUser.get("title"), (String) singleUser.get("sdesc"), (String) singleUser.get("ldesc"),
                    (String) singleUser.get("phone"), (String) singleUser.get("location"), (ArrayList<String>) singleUser.get("images"));
            ads.add(e);
        }

        RecyclerView mAdList = findViewById(R.id.recyclerView);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, ads);

        mAdList.setAdapter(adapter);

        mAdList.setLayoutManager(new LinearLayoutManager(this));

    }

}