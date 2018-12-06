package farkasnorbert.sapientia.ms.androidprojekt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ads extends AppCompatActivity {

    private RecyclerView mBlogList;

    private DatabaseReference mDatabase;

    private String TAG = "Ez";
    private RecyclerViewAdapter adapter;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_add:
                    startActivity(new Intent(ads.this, add_ad.class));
                    return true;
                case R.id.navigation_settings:
                    //startActivity(new Intent(ads.this, login_activity.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mDatabase = FirebaseDatabase.getInstance().getReference("data2");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "itt");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "itt");
                //Blog post = dataSnapshot.getValue(Blog.class);
                Log.d(TAG, dataSnapshot.getValue().toString());
                collectPhoneNumbers((Map<String, Ad>) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void collectPhoneNumbers(Map<String, Ad> value) {

        ArrayList<Ad> blogs = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Ad> entry : value.entrySet()) {

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            // phoneNumbers.add((String) singleUser.get("title"));
            Ad e = new Ad((String) singleUser.get("title"), (String) singleUser.get("sdesc"), (String) singleUser.get("ldesc"),
                    (String) singleUser.get("phone"), (String) singleUser.get("location"), (ArrayList<String>) singleUser.get("images"));
            blogs.add(e);
        }
        for (int i = 0; i < blogs.size(); i++) {
            Log.d(TAG, "title: " + blogs.get(i).getTitle());


        }

        mBlogList = findViewById(R.id.recyclerView);

        adapter = new RecyclerViewAdapter(this, blogs);

        mBlogList.setAdapter(adapter);

        mBlogList.setLayoutManager(new LinearLayoutManager(this));

    }

}