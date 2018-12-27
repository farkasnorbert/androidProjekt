package farkasnorbert.sapientia.ms.androidprojekt.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import farkasnorbert.sapientia.ms.androidprojekt.R;

public class LoginActivity extends AppCompatActivity {
    private String phone;
    private FirebaseAuth mAuth;
    private String codeSent;
    private EditText code;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            if (codeSent == "") {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor Ed = sp.edit();
                Ed.putString("Phone", phone);
                Ed.apply();
                Intent i = new Intent(getApplicationContext(), AdsActivity.class);
                i.putExtra("Phone", phone);
                startActivity(i);
                finish();
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            Toast.makeText(getApplicationContext(), "VerificationFailed", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            codeSent = s;
            Log.d("fel", codeSent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button validate_button = findViewById(R.id.validate_button);
        Intent intent = getIntent();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
        phone = intent.getStringExtra("Phone");
        code = findViewById(R.id.validate);
        codeSent = "";
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+4" + phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        validate_button.setOnClickListener(v -> {

            if (code.length() == 0) {
                Toast.makeText(getApplicationContext(), "Code is empty", Toast.LENGTH_LONG).show();
            } else {
                verifySignInCode();
            }
        });
    }

    private void verifySignInCode() {
        String codes = code.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, codes);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast.makeText(getApplicationContext(), "Invalid Code", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                        SharedPreferences.Editor Ed = sp.edit();
                        Ed.putString("Phone", phone);
                        Ed.apply();
                        Intent i = new Intent(getApplicationContext(), AdsActivity.class);
                        i.putExtra("Phone", phone);
                        startActivity(i);
                        finish();
                    }
                });
    }

}
