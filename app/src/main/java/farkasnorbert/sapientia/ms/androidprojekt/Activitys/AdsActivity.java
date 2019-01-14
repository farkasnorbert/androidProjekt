package farkasnorbert.sapientia.ms.androidprojekt.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
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
    private SwipeRefreshLayout refresh;

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
        mDatabase = FirebaseDatabase.getInstance().getReference("data");
        refresh = findViewById(R.id.swipe_container);
        refresh.setOnRefreshListener(this::loadAds);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refresh.setRefreshing(true);
        loadAds();
    }

    private void loadAds() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    collectPhoneNumbers((Map<String, Ad>) dataSnapshot.getValue());
                }else {
                    refresh.setRefreshing(false);
                }
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
            Map singleAd = (Map) entry.getValue();
            //Get phone field and append to list
            Ad e = new Ad((String) singleAd.get("title"), (String) singleAd.get("sdesc"), (String) singleAd.get("ldesc"),
                    (String) singleAd.get("phone"), (String) singleAd.get("location"), (ArrayList<String>) singleAd.get("images") ,(String) singleAd.get("vNumber"));
            ads.add(e);
        }

        RecyclerView mAdList = findViewById(R.id.recyclerView);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, ads);

        mAdList.setAdapter(adapter);

        mAdList.setLayoutManager(new LinearLayoutManager(this));
        refresh.setRefreshing(false);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            // do something on back.
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}