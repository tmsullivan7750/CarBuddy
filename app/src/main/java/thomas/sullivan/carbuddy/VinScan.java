package thomas.sullivan.carbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VinScan extends AppCompatActivity implements VinScannerPopUp.VinScannerPopUpListener {

    final int CAMERA_CAPTURE = 1;
    private FirebaseAuth mAuth;
    public Bitmap vinPic;
    public ImageView vinDisplayPicture;
    public ArrayList<String> words;
    public String vinDisplayText;
    public ArrayList<String> preparse;
    public ArrayList<String> resultListOfVehicleInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vin_scan);

        mAuth = FirebaseAuth.getInstance();

        if(vinDisplayText == null)
        {
            vinDisplayText = "";
        }

        vinDisplayPicture = findViewById(R.id.vinScannerBackground);

        words = new ArrayList<String>();
        preparse = new ArrayList<String>();
        resultListOfVehicleInformation =  new ArrayList<String>();

        vinScan();

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
            goHome();
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
            textRecognization();

        }
    }

    private void textRecognization()
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

    private void processTextRecognitionResult(FirebaseVisionText texts)
    {
        List<FirebaseVisionText.TextBlock> blocks = texts.getTextBlocks();
        if(blocks.size() == 0)
        {
            Toast.makeText(this, "No Text Found", Toast.LENGTH_SHORT).show();
            goHome();
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
            goHome();
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
            preparse = parseResponse(resultString);
            resultListOfVehicleInformation = analyzeParse(preparse);
            runPopup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList parseResponse(String vin)
    {
        Pattern p = Pattern.compile("([A-Za-z0-9._]+)");
        Matcher m = p.matcher(vin);
        ArrayList<String> tokens = new ArrayList<>();
        while(m.find())
        {
            String token = m.group(1);
            tokens.add(token);
        }
        return tokens;
    }

    public ArrayList analyzeParse(ArrayList<String> list)
    {
        ArrayList<String> res = new ArrayList<String>();
        String tempResult = "";
        for(int i=0;i<list.size();i++)
        {
            String temp = list.get(i);
            switch(temp){
                case "vin":
                    tempResult = list.get(i+1);
                    if(!res.contains(tempResult))
                    {
                        res.add(tempResult);
                    }
                    break;
                case "year":
                    tempResult = list.get(i+1);
                    if(!res.contains(tempResult))
                    {
                        res.add(tempResult);
                    }
                    break;
                case "make":
                    tempResult = list.get(i+1);
                    res.add(tempResult);
                    break;
                case "model":
                    tempResult = list.get(i+1);
                    res.add(tempResult);
                    break;
                case "engine":
                    if(list.get(i+1).equalsIgnoreCase("null") || list.get(i+1).equalsIgnoreCase("N"))
                    {
                        tempResult = "N/A";
                    } else {
                        tempResult = list.get(i+1)+" "+list.get(i+2);
                    }
                    res.add(tempResult);
                    break;
                case "tank_size":
                    if(list.get(i+1).equalsIgnoreCase("null") || list.get(i+1).equalsIgnoreCase("N") )
                    {
                        tempResult = "N/A";
                    } else {
                        tempResult = list.get(i+1)+" gallons";
                    }
                    res.add(tempResult);
                    break;
                case "highway_mileage":
                    if(list.get(i+1).equalsIgnoreCase("null") || list.get(i+1).equalsIgnoreCase("N") )
                    {
                        tempResult = "N/A";
                    } else {
                        tempResult = list.get(i+1)+" miles/gallon";
                    }
                    res.add(tempResult);
                    break;
                case "city_mileage":
                    if(list.get(i+1).equalsIgnoreCase("null") || list.get(i+1).equalsIgnoreCase("N") )
                    {
                        tempResult = "N/A";
                    } else {
                        tempResult = list.get(i+1)+" miles/gallon";
                    }
                    res.add(tempResult);
                    break;
            }

        }
        return res;
    }

    public void runPopup()
    {

        VinScannerPopUp vinScannerPopUp = new VinScannerPopUp().newInstance(resultListOfVehicleInformation.get(0),resultListOfVehicleInformation.get(1),
            resultListOfVehicleInformation.get(2),resultListOfVehicleInformation.get(3),resultListOfVehicleInformation.get(4),resultListOfVehicleInformation.get(5),
            resultListOfVehicleInformation.get(6),resultListOfVehicleInformation.get(7));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(vinScannerPopUp,"Vehicle Information");
        ft.commitAllowingStateLoss();
    }

    @Override
    public void result(Boolean aBoolean) {
        if(aBoolean == true)
        {
            Intent intent = new Intent(getApplicationContext(), VirtualGarage.class);
            intent.putExtra("VIN", resultListOfVehicleInformation.get(0));
            intent.putExtra("YEAR", resultListOfVehicleInformation.get(1));
            intent.putExtra("MAKE", resultListOfVehicleInformation.get(2));
            intent.putExtra("MODEL", resultListOfVehicleInformation.get(3));
            intent.putExtra("ENGINE", resultListOfVehicleInformation.get(4));
            intent.putExtra("TANK_SIZE", resultListOfVehicleInformation.get(5));
            intent.putExtra("HIGHWAY_MILEAGE", resultListOfVehicleInformation.get(6));
            intent.putExtra("CITY_MILEAGE", resultListOfVehicleInformation.get(7));
            intent.putExtra("USERID", mAuth.getUid());
            startActivity(intent);
        } else {
            Intent failedIntent = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(failedIntent);
        }
    }

    public void goHome()
    {
        Intent failedIntent = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(failedIntent);
    }

}
