package thomas.sullivan.carbuddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ViewHolder> {

    private ArrayList<Vehicle> data;
    public int pos;

    public VehicleAdapter(ArrayList<Vehicle> a) {
        this.data = a;
    }

    @NonNull
    @Override
    public VehicleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.virtual_garage_selector, parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleAdapter.ViewHolder holder, int position) {

        holder.vin.setText(data.get(position).getVin());
        holder.engine.setText(data.get(position).getEngine());
        holder.fuel.setText(data.get(position).getTankSize());
        holder.title.setText(data.get(position).getYear()+" "+data.get(position).getMake()+" "+data.get(position).getModel());
        holder.city.setText(data.get(position).getCityMileage());
        holder.highway.setText(data.get(position).getHighwayMileage());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public CardView cardView;

        public TextView vin;
        public TextView engine;
        public TextView fuel;
        public TextView title;
        public TextView highway;
        public TextView city;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            vin = (TextView) itemView.findViewById(R.id.virtualGarageSelectedVIN);
            engine = (TextView) itemView.findViewById(R.id.virtualGarageSelectedEngine);
            fuel = (TextView) itemView.findViewById(R.id.virtualGarageSelectedTank);
            city = (TextView) itemView.findViewById(R.id.virtualGarageSelectedCity);
            highway = (TextView) itemView.findViewById(R.id.virtualGarageSelectedHighway);
            title = (TextView) itemView.findViewById(R.id.virtualGarageSelectedTitle);

        }

        @Override
        public void onClick(View v) {
            pos = getLayoutPosition();
        }
    }

    public int getVehicleCurrent()
    {
        return pos;
    }
}
