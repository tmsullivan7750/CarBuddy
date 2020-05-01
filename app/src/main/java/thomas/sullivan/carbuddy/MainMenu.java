package thomas.sullivan.carbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public String vinDisplayText;
    public Bitmap vinPic;
    public ImageView vinDisplayPicture;
    final int CAMERA_CAPTURE = 1;
    public ArrayList<String> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mAuth = FirebaseAuth.getInstance();

        //Work Around For Request
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //START OF TOP MENU BAR
        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText("");
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.menu);

        ArrayList<Integer> menuOptions = new ArrayList<>();
        menuOptions.add(R.drawable.home);
        menuOptions.add(R.drawable.garage);
        menuOptions.add(R.drawable.vinscan);
        menuOptions.add(R.drawable.warninglights);
        menuOptions.add(R.drawable.nearme);
        menuOptions.add(R.drawable.questions);
        menuOptions.add(R.drawable.notifications);
        menuOptions.add(R.drawable.settings);
        menuOptions.add(R.drawable.logout);

        final ArrayList<Class> menuOnClicks = new ArrayList<>();
        menuOnClicks.add(MainMenu.class);
        menuOnClicks.add(VirtualGarage.class);
        menuOnClicks.add(VinScan.class);
        menuOnClicks.add(WarningLights.class);
        menuOnClicks.add(NearMe.class);
        menuOnClicks.add(Questions.class);
        menuOnClicks.add(Notifications.class);
        menuOnClicks.add(Settings.class);
        menuOnClicks.add(Logout.class);

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {

                    SimpleCircleButton.Builder builder = new SimpleCircleButton.Builder()
                            .normalImageRes(menuOptions.get(i)).highlightedImageRes(menuOptions.get(i)).rippleEffect(true)
                            .normalColor(getResources().getColor(R.color.white)).highlightedColor(getResources().getColor(R.color.pink)).listener(new OnBMClickListener() {
                                @Override
                                public void onBoomButtonClick(int index) {
                                    startActivity(new Intent(MainMenu.this, menuOnClicks.get(index)));
                                }
                            });
                    bmb.addBuilder(builder);
        }
        //END OF MENU BAR

        /*
        Button setUpVehicle = (Button) findViewById(R.id.setUpVehicle);

        setUpVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this,VirtualGarage.class));
            }
        });
        */


    }

}
