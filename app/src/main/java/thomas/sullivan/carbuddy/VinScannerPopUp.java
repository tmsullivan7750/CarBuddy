package thomas.sullivan.carbuddy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

public class VinScannerPopUp extends AppCompatDialogFragment {

    public TextView vin;
    public TextView year;
    public TextView make;
    public TextView model;
    public TextView engine;
    public TextView tankSize;
    public TextView highwayMileage;
    public TextView cityMileage;
    public VinScannerPopUpListener listener;

    public static VinScannerPopUp newInstance(String avin, String ayear, String amake, String amodel, String aengine, String atanksize, String ahighway, String acity) {

        VinScannerPopUp fragment = new VinScannerPopUp();

        Bundle bundle = new Bundle();
        bundle.putString("vin",avin);
        bundle.putString("year",ayear);
        bundle.putString("make",amake);
        bundle.putString("model",amodel);
        bundle.putString("engine",aengine);
        bundle.putString("tanksize",atanksize);
        bundle.putString("highway",ahighway);
        bundle.putString("city",acity);
        fragment.setArguments(bundle);

        return fragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_vinscan,null);

        builder.setView(view).setTitle("Vehicle Information")
        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                listener.result(false);
            }
        })
        .setPositiveButton("Register Vehicle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                listener.result(true);
            }
        });

        vin = view.findViewById(R.id.scannerPopupVIN);
        year = view.findViewById(R.id.scannerPopupYEAR);
        make = view.findViewById(R.id.scannerPopupMAKE);
        model = view.findViewById(R.id.scannerPopupMODEL);
        engine = view.findViewById(R.id.scannerPopupENGINE);
        tankSize = view.findViewById(R.id.scannerPopupTANKSIZE);
        highwayMileage = view.findViewById(R.id.scannerPopupHIGHWAYMILEAGE);
        cityMileage = view.findViewById(R.id.scannerPopupCITYMILEAGE);

        vin.setText("VIN: "+getArguments().getString("vin"));
        year.setText("YEAR: "+getArguments().getString("year"));
        make.setText("MAKE: "+getArguments().getString("make"));
        model.setText("MODEL: "+getArguments().getString("model"));
        engine.setText("ENGINE: "+getArguments().getString("engine"));
        tankSize.setText("TANK SIZE: "+getArguments().getString("tanksize"));
        highwayMileage.setText("HIGHWAY MILEAGE: "+getArguments().getString("highway"));
        cityMileage.setText("CITY MILEAGE: "+getArguments().getString("city"));

        return builder.create();
    }

    public void onAttach(Context context) {

        super.onAttach(context);

        try {
            listener = (VinScannerPopUpListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+" must implement VinScannerPopUpListener");
        }
    }

    public interface VinScannerPopUpListener{
        void result(Boolean aBoolean);
    }



}
