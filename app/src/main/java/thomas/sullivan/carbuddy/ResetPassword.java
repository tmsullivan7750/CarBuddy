package thomas.sullivan.carbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    EditText emailEntry;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null)
                {
                    startActivity(new Intent(ResetPassword.this, MainMenu.class));
                }
            }
        };

        emailEntry = (EditText) findViewById(R.id.resetPasswordText);

        Button resetPassword = (Button) findViewById(R.id.resetPasswordButton);

        resetPassword.setOnClickListener(new View.OnClickListener() {

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
                            startActivity(new Intent(ResetPassword.this, Login.class));
                        }
                    });
                }
            }
        });

    }

    public void emailSent()
    {
        Toast.makeText(this,"Password Reset Request has been sent.",Toast.LENGTH_LONG).show();
    }
    public void noEmail()
    {
        Toast.makeText(this,"There is no account with that email",Toast.LENGTH_LONG).show();
    }

}
