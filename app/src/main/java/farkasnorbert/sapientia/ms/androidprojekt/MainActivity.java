package farkasnorbert.sapientia.ms.androidprojekt;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginb = findViewById(R.id.login_button);
        loginb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView phone = findViewById(R.id.phone_number);
                Intent send = new Intent(MainActivity.this,login_activity.class);
                send.putExtra("Phone",phone.toString());
                startActivity(send);
            }
        });
    }


}
