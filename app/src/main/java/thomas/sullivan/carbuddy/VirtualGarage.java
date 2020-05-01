package thomas.sullivan.carbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.firestore.core.OrderBy.*;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;


public class VirtualGarage extends AppCompatActivity {

    private RecyclerView serviceSelectorView;
    private RecyclerView.LayoutManager serviceSelectorLayout;
    private RecyclerView.Adapter serviceSelectorAdapter;
    private RecyclerView vehicleSelectorView;
    private RecyclerView.LayoutManager vehicleSelectorLayout;
    private VehicleAdapter vehicleSelectorAdapter;
    private ArrayList<Vehicle> dataVehicles;
    private ArrayList<Service> dataServices;

    private DocumentSnapshot lastQueriedVehicle;
    private DocumentSnapshot lastQueriedService;

    public int currentPosition;

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
        ImageButton addService  = (ImageButton) findViewById(R.id.addServiceCard);

        serviceSelectorView = (RecyclerView) findViewById(R.id.vehicleServiceList);
        vehicleSelectorView = (RecyclerView) findViewById(R.id.virtualGarageSelector);

        dataVehicles = new ArrayList<Vehicle>();
        getVehicles();

        if(vehicleSelectorAdapter != null)
        {
            currentPosition = vehicleSelectorAdapter.getVehicleCurrent();
            Log.d("CurrentPosition: ", currentPosition+"");
        }

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

        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getServiceInformation();
            }
        });

        Bundle extrasVehicle = getIntent().getExtras();
        if(extrasVehicle != null)
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
        createVehicleEntry(vehicleInformation);
    }

    public void createVehicleEntry(ArrayList<String> list)
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

    public void getVehicles()
    {
        CollectionReference vehicleRef = db.collection("Vehicles");

        Query query = null;
        if(lastQueriedVehicle != null)
        {
            query = vehicleRef.whereEqualTo("userID", FirebaseAuth.getInstance().getCurrentUser().getUid()).startAfter(lastQueriedVehicle);
        } else {
            query = vehicleRef.whereEqualTo("userID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        }


        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document: task.getResult())
                    {
                        Vehicle vehicle = document.toObject(Vehicle.class);
                        dataVehicles.add(vehicle);
                        Log.d("d", "Vehicle Added, Array size = "+dataVehicles.size());
                    }

                    if(task.getResult().size() != 0)
                    {
                        lastQueriedVehicle = task.getResult().getDocuments().get(task.getResult().size()-1);
                    }
                    Log.d("d", "Final Array size = "+dataVehicles.size());
                }

                vehicleSelectorLayout = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                vehicleSelectorView.setLayoutManager(vehicleSelectorLayout);
                if(dataVehicles.size() == 0)
                {

                }else {
                    vehicleSelectorAdapter = new VehicleAdapter(dataVehicles);
                    vehicleSelectorView.setAdapter(vehicleSelectorAdapter);

                    dataServices = new ArrayList<Service>();
                    getServices();
                }
            }
        });

    }

    public void getServices()
    {
        CollectionReference serviceRef = db.collection("VehicleServiceHistory");

        Query query = null;
        if(lastQueriedService != null)
        {
            query = serviceRef.whereEqualTo("vin", dataVehicles.get(currentPosition).getVin()).orderBy("date", Query.Direction.DESCENDING).startAfter(lastQueriedService);
        } else {
            query = serviceRef.whereEqualTo("vin", dataVehicles.get(currentPosition).getVin()).orderBy("date", Query.Direction.DESCENDING);
        }


        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document: task.getResult())
                    {
                        Service service = document.toObject(Service.class);
                        dataServices.add(service);
                    }

                    if(task.getResult().size() != 0)
                    {
                        lastQueriedService = task.getResult().getDocuments().get(task.getResult().size()-1);
                    }
                }

                serviceSelectorLayout = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                serviceSelectorView.setLayoutManager(serviceSelectorLayout);
                serviceSelectorAdapter = new ServiceAdapter(dataServices);
                serviceSelectorView.setAdapter(serviceSelectorAdapter);
                serviceSelectorAdapter.notifyDataSetChanged();
            }
        });
    }

    public void getServiceInformation()
    {
        Intent intent = new Intent(getApplicationContext(),AddService.class);
        intent.putExtra("VIN",dataVehicles.get(currentPosition).getVin());
        startActivity(intent);
    }

}
