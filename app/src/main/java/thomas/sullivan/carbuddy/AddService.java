package thomas.sullivan.carbuddy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddService extends AppCompatActivity {

    public ArrayList<Object> serviceInformation;
    private FirebaseFirestore db;
    public RadioButton selectedType;
    public Date selectedDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.addservicepopup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8),(int) (height*.8));

        db = FirebaseFirestore.getInstance();
        serviceInformation = new ArrayList<Object>();

        final EditText datePicker = (EditText) findViewById(R.id.selectDate);
        final EditText location =(EditText) findViewById(R.id.selectLocation);
        final EditText price =(EditText) findViewById(R.id.selectPrice);
        final EditText shop =(EditText) findViewById(R.id.selectShop);
        final  EditText title =(EditText) findViewById(R.id.selectTitle);
        final RadioGroup type =(RadioGroup) findViewById(R.id.selectType);
        Button submit = (Button) findViewById(R.id.submitService);

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MMMM d, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                datePicker.setText(sdf.format(myCalendar.getTime()));

                selectedDate = myCalendar.getTime();
            }

        };

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddService.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempLOCATION = location.getText().toString();
                String tempPRICE = price.getText().toString();
                String tempSHOP = shop.getText().toString();
                String tempTITLE = title.getText().toString();
                String tempTYPE = "";
                int radioID = type.getCheckedRadioButtonId();
                selectedType = findViewById(radioID);
                if(selectedType == null)
                {
                    tempTYPE = "";
                } else {
                    if(selectedType.getText().toString().equalsIgnoreCase("Maintenance"))
                    {
                        tempTYPE = "yellow";
                    } else if(selectedType.getText().toString().equalsIgnoreCase("Courtesy")) {
                        tempTYPE = "red";
                    } else {
                        tempTYPE = "green";
                    }
                }
                String tempVIN = getIntent().getExtras().getString("VIN");

                if(selectedDate != null && !tempLOCATION.equalsIgnoreCase("") && !tempPRICE.equalsIgnoreCase("") && !tempSHOP.equalsIgnoreCase("")
                        && !tempTITLE.equalsIgnoreCase("") && !tempTYPE.equalsIgnoreCase("") && tempVIN != null)
                {
                    serviceInformation.add(selectedDate);
                    serviceInformation.add(tempLOCATION);
                    serviceInformation.add(tempPRICE);
                    serviceInformation.add(tempSHOP);
                    serviceInformation.add(tempTITLE);
                    serviceInformation.add(tempTYPE);
                    serviceInformation.add(tempVIN);

                    createServiceEntry(serviceInformation);
                }

                Intent intent = new Intent(getApplicationContext(),VirtualGarage.class);
                startActivity(intent);
            }
        });


    }

    public void createServiceEntry(ArrayList<Object> list)
    {
        Map<String, Object> entry = new HashMap<>();
        entry.put("date", list.get(0));
        entry.put("location", list.get(1));
        entry.put("price", list.get(2));
        entry.put("shop", list.get(3));
        entry.put("title", list.get(4));
        entry.put("type", list.get(5));
        entry.put("vin", list.get(6));

        db.collection("VehicleServiceHistory").add(entry).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("DOCUMENT: ", "Successfully added service");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DOCUMENT: ", "Failed to add service.");
            }
        });
        serviceInformation.clear();
    }
}
