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

public class NearMe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me);

        //START OF TOP MENU BAR
        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText("Nearest Station");
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
                            startActivity(new Intent(NearMe.this, menuOnClicks.get(index)));
                        }
                    });
            bmb.addBuilder(builder);
        }
        //END OF MENU BAR


        TextView nearMe = (TextView) findViewById(R.id.nearMeTitle);
        nearMe.setText("Near Me Temp");
    }
}
