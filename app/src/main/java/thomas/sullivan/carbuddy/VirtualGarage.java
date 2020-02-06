package thomas.sullivan.carbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VirtualGarage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    public ArrayList<String> vehicleInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_garage);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        vehicleInformation = new ArrayList<String>();


        //START OF TOP MENU BAR
        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText("Virtual Garage");
        Toolbar toolbar = findViewById(R.id.app_bar_virtualgarage);
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
                            startActivity(new Intent(VirtualGarage.this, menuOnClicks.get(index)));
                        }
                    });
            bmb.addBuilder(builder);
        }
        //END OF MENU BAR

        Button addVehicle = (Button) findViewById(R.id.virtualGarageAddVehicle);

        addVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVehicleInformation();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            setUpVehicleFromIntent();
        }




    }

    public void getVehicleInformation()
    {
        Intent intent = new Intent(getApplicationContext(),VinScan.class);
        startActivity(intent);
    }

    public void setUpVehicleFromIntent()
    {
        String tempVIN = getIntent().getExtras().getString("VIN");
        String tempYEAR = getIntent().getExtras().getString("YEAR");
        String tempMAKE = getIntent().getExtras().getString("MAKE");
        String tempMODEL = getIntent().getExtras().getString("MODEL");
        String tempENGINE = getIntent().getExtras().getString("ENGINE");
        String tempTANK = getIntent().getExtras().getString("TANK_SIZE");
        String tempHIGHWAY = getIntent().getExtras().getString("HIGHWAY_MILEAGE");
        String tempCITY = getIntent().getExtras().getString("CITY_MILEAGE");
        String tempUID = getIntent().getExtras().getString("USERID");

            vehicleInformation.add(tempVIN);
            vehicleInformation.add(tempYEAR);
            vehicleInformation.add(tempMAKE);
            vehicleInformation.add(tempMODEL);
            vehicleInformation.add(tempENGINE);
            vehicleInformation.add(tempTANK);
            vehicleInformation.add(tempHIGHWAY);
            vehicleInformation.add(tempCITY);
            vehicleInformation.add(tempUID);
            createDBEntry(vehicleInformation);
    }

    public void createDBEntry(ArrayList<String> list)
    {
        Map<String, String> entry = new HashMap<>();
        entry.put("vin", list.get(0));
        entry.put("year", list.get(1));
        entry.put("make", list.get(2));
        entry.put("model", list.get(3));
        entry.put("engine", list.get(4));
        entry.put("tanksize", list.get(5));
        entry.put("highwaymileage", list.get(6));
        entry.put("citymileage", list.get(7));
        entry.put("userID", list.get(8));

        db.collection("Vehicles").add(entry).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("DOCUMENT: ", "Successfully added vehicle");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DOCUMENT: ", "Failed to add vehicle.");
            }
        });
    }





}
