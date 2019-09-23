package thomas.sullivan.carbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainMenu extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mAuth = FirebaseAuth.getInstance();

        TextView text = findViewById(R.id.tempMainMenu);
        Button signout = findViewById(R.id.signout);

        signout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                logOut();
            }

            private void logOut() {
                mAuth.signOut();
                startActivity(new Intent(MainMenu.this, Login.class));
            }
        });
    }
}
