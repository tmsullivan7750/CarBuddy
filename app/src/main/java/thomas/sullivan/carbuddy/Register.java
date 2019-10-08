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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    EditText passwordEntry;
    EditText passwordConfirm;
    EditText emailEntry;

    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null)
                {
                    startActivity(new Intent(Register.this, MainMenu.class));
                }
            }
        };

        emailEntry = (EditText) findViewById(R.id.registerEmail);
        passwordEntry = (EditText) findViewById(R.id.registerPassword);
        passwordConfirm = (EditText) findViewById(R.id.confirmPassword);

        Button signUp = (Button) findViewById(R.id.signUp);
        Button signIn = (Button) findViewById(R.id.signIn);

        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(confirmPassword() == true)
                {
                    email = emailEntry.getText().toString();
                    registerAccount();
                } else {
                    invalidPassword();
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

    }

    public void invalidPassword()
    {
        Toast.makeText(this,"Passwords do not match",Toast.LENGTH_LONG).show();
    }
    public void noPassword()
    {
        Toast.makeText(this,"Fields cannot be empty",Toast.LENGTH_LONG).show();
    }
    public void failedRegistration(Task task)
    {
        Toast.makeText(this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
    }

    public void succesfulRegistration(String emailA, String passB)
    {
        mAuth.signInWithEmailAndPassword(emailA,passB).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                startActivity(new Intent(Register.this, MainMenu.class));
            }
        });
    }

    public void registerAccount() {

        if(email.isEmpty() || password.isEmpty())
        {
            noPassword();
        } else {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        succesfulRegistration(email,password);
                    } else {
                        failedRegistration(task);
                    }
                }
            });
        }
    }

    public boolean confirmPassword()
    {
        if(passwordEntry.getText().toString().equalsIgnoreCase(passwordConfirm.getText().toString()))
        {
            password = passwordEntry.getText().toString();
            return true;
        } else {
            return false;
        }
    }

}
