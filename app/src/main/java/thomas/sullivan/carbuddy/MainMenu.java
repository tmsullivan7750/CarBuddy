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
        title.setText("Home");
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

        TextView text = findViewById(R.id.tempMainMenu);

        TextView vinDisplay = findViewById(R.id.vinDisplay);

        if(vinDisplayText == null)
        {
            vinDisplayText = "";
        }
        vinDisplay.setText(vinDisplayText);

        words = new ArrayList<String>();

        vinDisplayPicture = findViewById(R.id.vinDisplayPicture);

        Button signout = findViewById(R.id.signout);
        Button vinScanner = findViewById(R.id.vinscanner);
        Button vinDecoder = findViewById(R.id.vinDecoder);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }

        });

        vinScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vinScan();
            }
        });

        vinDecoder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    textRecognization();
            }
        });

    }


    private void logOut() {
        mAuth.signOut();
        startActivity(new Intent(MainMenu.this, Login.class));
    }


    public void vinScan()
    {
            //use standard intent to capture an image

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePictureIntent.resolveActivity(getPackageManager()) != null)
            {
                startActivityForResult(takePictureIntent, CAMERA_CAPTURE);
            } else {
                //display an error message
                String errorMessage = "Whoops - your device doesn't support this function!";
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_CAPTURE && resultCode == RESULT_OK) {
            //get the returned data
            Bundle extras = data.getExtras();
            //get the cropped bitmap
            vinPic = (Bitmap) extras.get("data");
            vinDisplayPicture.setImageBitmap(vinPic);
        }
    }


    private void textRecognization() {
        if (vinPic != null)
        {
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(vinPic);
            FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
            detector.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                @Override
                public void onSuccess(FirebaseVisionText texts) {
                    processTextRecognitionResult(texts);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void processTextRecognitionResult(FirebaseVisionText texts) {
        List<FirebaseVisionText.TextBlock> blocks = texts.getTextBlocks();
        if(blocks.size() == 0)
        {
            Toast.makeText(this, "No Text Found", Toast.LENGTH_SHORT).show();
            return;
        }
        for(int i=0;i<blocks.size(); i++){
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            for(int j=0;j<lines.size();j++){
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                for(int k=0;k<elements.size();k++)
                {
                    words.add(elements.get(k).getText());
                }
            }
        }
        determineVIN();
    }

    public void determineVIN()
    {
        for(int i=0; i<words.size();i++)
        {
            //Log.i is for debugging purposes
            Log.i("Word List: ", words.get(i));
            if(words.get(i).length() == 17 )
            {
                vinDisplayText = words.get(i);
                //Log.i is for debugging purposes
                Log.i("Word FINAL: ", words.get(i));
            }
        }
        if(vinDisplayText.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "No VIN detected, please try again!", Toast.LENGTH_SHORT).show();
        } else {
            runRequest();
        }
    }

    public void runRequest()
    {
        //Request to VIN API server to get VIN information
        //href= https://rapidapi.com/vinfreecheck/api/vin-decoder-1?endpoint=5ab0d320e4b084deb4ea9c4a


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://vindecoder.p.rapidapi.com/decode_vin?vin="+vinDisplayText)
                .get()
                .addHeader("x-rapidapi-host", "vindecoder.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "fe69e42eebmsh1632946fb1f46d9p182b24jsn37a263cc78d6")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String resultString = response.body().string();
            Toast.makeText(this, resultString, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
