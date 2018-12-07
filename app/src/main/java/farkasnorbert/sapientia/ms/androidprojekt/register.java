package farkasnorbert.sapientia.ms.androidprojekt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class register extends AppCompatActivity {

    private String phone;
    private String codeSent;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            startActivity(new Intent(register.this, MainActivity.class));
            Toast.makeText(getApplicationContext(), "VerificationFailed", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            codeSent = s;
        }
    };
    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    private EditText code;
    private EditText email;
    private EditText fName;
    private EditText lName;
    private Button validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();
        ref = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
        phone = intent.getStringExtra("Phone");
        code = findViewById(R.id.vCode);
        email = findViewById(R.id.email);
        fName = findViewById(R.id.fname);
        lName = findViewById(R.id.lname);
        validate = findViewById(R.id.bValidate);
        ref = FirebaseDatabase.getInstance().getReference("users");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+4" + phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Code is empty", Toast.LENGTH_LONG).show();
                } else {
                    verifySignInCode();
                }
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
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            writeNewUsers();
                            Intent i = new Intent(getApplicationContext(), ads.class);
                            i.putExtra("Phone", phone);
                            startActivity(i);
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                Toast.makeText(getApplicationContext(), "Invalid Code", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void writeNewUsers() {
        User user = new User(fName.getText().toString(), lName.getText().toString(), email.getText().toString(), "");
        ref.child(phone).setValue(user);
        Toast.makeText(getApplicationContext(), "Registered succssefuly", Toast.LENGTH_LONG).show();
    }

}