package thomas.sullivan.carbuddy;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import thomas.sullivan.carbuddy.R;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    EditText passwordEntry;
    EditText emailEntry;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null)
                {
                    startActivity(new Intent(Login.this, MainMenu.class));
                }
            }
        };

        emailEntry = (EditText) findViewById(R.id.enterEmail);
        passwordEntry = (EditText) findViewById(R.id.enterPassword);

        Button login = (Button) findViewById(R.id.login);
        Button register = (Button) findViewById(R.id.register);
        Button forgotPassword = (Button) findViewById(R.id.forgotPassword);

        email = emailEntry.getText().toString();
        password = passwordEntry.getText().toString();

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailEntry.getText().toString();
                if(email.isEmpty())
                {
                    noEmail();
                } else {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            emailSent();
                        }
                    });
                }
            }
        });

    }

    private void startSignIn() {

        email = emailEntry.getText().toString();
        password = passwordEntry.getText().toString();

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(Login.this,"Invalid Credentials",Toast.LENGTH_LONG).show();
        } else {
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(!task.isSuccessful()){
                        Toast.makeText(Login.this,"Invalid Credentials",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void emailSent()
    {
        Toast.makeText(this,"An email has been sent to reset your password",Toast.LENGTH_LONG).show();
    }
    public void noEmail()
    {
        Toast.makeText(this,"Enter your email in the field above, then press forgot password.",Toast.LENGTH_LONG).show();
    }

}

