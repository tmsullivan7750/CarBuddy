package thomas.sullivan.carbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VirtualGarage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_garage);

        //START OF TOP MENU BAR
        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText("Virtual Garage");
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
                            startActivity(new Intent(VirtualGarage.this, menuOnClicks.get(index)));
                        }
                    });
            bmb.addBuilder(builder);
        }
        //END OF MENU BAR

    }

    public ArrayList parseResponse(String vin)
    {
        Pattern p = Pattern.compile("[^\\W\"']+");
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
                    res.add(tempResult);
                    break;
                case "year":
                    tempResult = list.get(i+1);
                    res.add(tempResult);
                    break;
                case "make":
                    tempResult = list.get(i+1);
                    res.add(tempResult);
                    break;
                case "model":
                    tempResult = list.get(i+1);
                    res.add(tempResult);
                    break;
                case "tank_size":
                    tempResult = list.get(i+1)+list.get(i+2)+list.get(i+3);
                    res.add(tempResult);
                    break;
                case "highway_mileage":
                    tempResult = list.get(i+1)+" "+list.get(i+2)+"/"+list.get(+3);
                    res.add(tempResult);
                    break;
                case "city_mileage":
                    tempResult = list.get(i+1)+" "+list.get(i+2)+"/"+list.get(+3);
                    res.add(tempResult);
                    break;
            }
        }
        return res;
    }

}
