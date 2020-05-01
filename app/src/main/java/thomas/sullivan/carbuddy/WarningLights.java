package thomas.sullivan.carbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;

public class WarningLights extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_lights);

        //START OF TOP MENU BAR
        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText("Dashboard Lights");
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
                            startActivity(new Intent(WarningLights.this, menuOnClicks.get(index)));
                        }
                    });
            bmb.addBuilder(builder);
        }
        //END OF MENU BAR

        Button airbag = (Button) findViewById(R.id.lightairbag);
        Button battery = (Button) findViewById(R.id.lightbattery);
        Button brake = (Button) findViewById(R.id.lightbrake);
        Button coolant = (Button) findViewById(R.id.lightcoolant);
        Button fuel = (Button) findViewById(R.id.lightfuel);
        Button hazard = (Button) findViewById(R.id.lighthazard);
        Button highbeam = (Button) findViewById(R.id.lighthighbeam);
        Button lowbeam = (Button) findViewById(R.id.lightlowbeam);
        Button foglight = (Button) findViewById(R.id.lightfoglight);
        Button hood = (Button) findViewById(R.id.lighthood);
        Button trunk = (Button) findViewById(R.id.lighttrunk);
        Button checkengine = (Button) findViewById(R.id.lightcheckengine);
        Button oil = (Button) findViewById(R.id.lightoil);
        Button seatbelt = (Button) findViewById(R.id.lightseatbelt);
        Button warning = (Button) findViewById(R.id.lightwarning);
        Button defrost = (Button) findViewById(R.id.lightdefrost);
        Button washerfluid = (Button) findViewById(R.id.lightwasherfluid);
        Button light = (Button) findViewById(R.id.lightlight);

        airbag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog("Airbag Indicator","Indicator light turns on when the front airbag is switched off. If this lamp lights up or flashes there is a fault in the airbag or seatbelt system.");
            }
        });

        battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog("Battery Charge Warning Light", "Indicator light means that the car’s charging system is short of power or is not charging properly. It normally indicates a problem with the battery itself or the alternator.");
            }
        });

        brake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog("Brake Warning Light","Indicator light turns on when the handbrake is on. If it lit continuously, it means that hydraulic pressure has been lost in one side of the brake system or that the fluid level in the master cylinder is dangerously low (due to a leak somewhere in the brake system).");
            }
        });

        coolant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog("Engine Temperature Warning Light", "Indicator light means the engine temperature has exceeded normal limits. Check coolant level, fan operation, radiator cap, coolant leaks.");
            }
        });

        fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog("Low Fuel Level","Indicator light means that the car is running low on fuel and will soon need a refill.");
            }
        });

        hazard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog("Hazard Lights On","Indicator light means hazard lights are turned on.");
            }
        });

        highbeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog("High Beam Light Indicator", "Indicator light means your car’s high beam headlights are on, or if the high beam flash function is used.");
            }
        });

        lowbeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog("Low Beam Indicator Light","Indicator light means that the vehicles dipped beam is on.");
            }
        });

        foglight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog("Front Fog Lights", "Indicator light means front fog lights are turned on.");
            }
        });

        hood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog("Hood/Bonnet Open","Indicator light means that the car hood is not closed properly.");
            }
        });

        trunk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog("Trunk Open", "Indicator light means that the car trunk is not closed properly.");
            }
        });

        checkengine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog("Check Engine or Malfunction Indicator Light (MIL)","Indicator light turns on whenever the engine is turned on to check the bulb. If the light stays illuminated, the car’s diagnostic systems have detected a malfunction that needs to be investigated.");
            }
        });

        oil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog("Oil Pressure Warning Light", "Indicator light means loss of oil pressure, meaning lubrication is low or lost completely. Immediately check the oil level and pressure.");
            }
        });

        seatbelt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog("Seat Belt Indicator","Indicator light means that a seat belt has not been secured for a passenger in the vehicle.");
            }
        });

        warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog("Master Warning Light","Indicator light usually accompanied by another warning light and indicates that one or more warning systems have been detected.");
            }
        });

        defrost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog("Windshield Defrost","Indicator light means that the window defrost is in operation.");
            }
        });

        washerfluid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog("Washer Fluid Reminder","Indicator light indicates if the windscreen washer fluid reservoir is nearly empty. Fill the washer fluid reservoir.");
            }
        });

        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog("Exterior Light Fault","Indicator light means any exterior light on your car isn’t working.");
            }
        });

    }

    public void makeDialog(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }
}
