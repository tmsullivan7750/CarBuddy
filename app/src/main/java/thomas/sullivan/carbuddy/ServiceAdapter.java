package thomas.sullivan.carbuddy;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    private ArrayList<Service> data;

    public ServiceAdapter(ArrayList<Service> a) {
        this.data = a;
    }

    @NonNull
    @Override
    public ServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_list, parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceAdapter.ViewHolder holder, final int position) {

        holder.title.setText(data.get(position).getTitle());
        holder.price.setText(data.get(position).getPrice());
        holder.location.setText(data.get(position).getShop()+", "+data.get(position).getLocation());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yy");
        String dateFinal = dateFormat.format(data.get(position).getDate());
        holder.cardDate.setText(dateFinal);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                final CollectionReference serviceRef = db.collection("VehicleServiceHistory");
                Query query = serviceRef.whereEqualTo("vin",data.get(position).getVin()).whereEqualTo("title",data.get(position).getTitle()).whereEqualTo("date",data.get(position).getDate());
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for (DocumentSnapshot document : task.getResult()) {
                                serviceRef.document(document.getId()).delete();
                            }
                        }
                    }
                });
                data.remove(data.get(position));
                notifyDataSetChanged();
            }
        });


        int left = convertDpToPixel(12,holder.divider.getContext());
        int right = convertDpToPixel(12,holder.divider.getContext());
        int bottom = convertDpToPixel(0,holder.divider.getContext());
        int bottomFinal = convertDpToPixel(18,holder.divider.getContext());
        int top = convertDpToPixel(0,holder.divider.getContext());

        switch (data.get(position).getType())
        {
            case "green":
                holder.cardBackground.setBackgroundResource(R.drawable.greenbackground);
                if(position == data.size()-1)
                {
                    holder.divider.setImageResource(R.drawable.servicegreenlinebottom);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)holder.divider.getLayoutParams();
                    lp.setMargins(left,top,right,bottomFinal);
                    holder.divider.setLayoutParams(lp);
                    holder.title.setTextColor(Color.parseColor("#56E39F"));
                } else {
                    holder.divider.setImageResource(R.drawable.servicegreenline);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)holder.divider.getLayoutParams();
                    lp.setMargins(left,top,right,bottom);
                    holder.divider.setLayoutParams(lp);
                    holder.title.setTextColor(Color.parseColor("#56E39F"));
                }
                break;
            case "yellow":
                holder.cardBackground.setBackgroundResource(R.drawable.orangebackground);
                if(position == data.size()-1)
                {
                    holder.divider.setImageResource(R.drawable.serviceyellowlinebottom);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)holder.divider.getLayoutParams();
                    lp.setMargins(left,top,right,bottomFinal);
                    holder.divider.setLayoutParams(lp);
                    holder.title.setTextColor(Color.parseColor("#ECA400"));

                } else {
                    holder.divider.setImageResource(R.drawable.serviceyellowline);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)holder.divider.getLayoutParams();
                    lp.setMargins(left,top,right,bottom);
                    holder.divider.setLayoutParams(lp);
                    holder.title.setTextColor(Color.parseColor("#ECA400"));
                }
                break;
            case "red":
                holder.cardBackground.setBackgroundResource(R.drawable.redbackground);
                if(position == data.size()-1)
                {
                    holder.divider.setImageResource(R.drawable.serviceredlinebottom);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)holder.divider.getLayoutParams();
                    lp.setMargins(left,top,right,bottomFinal);
                    holder.divider.setLayoutParams(lp);
                    holder.title.setTextColor(Color.parseColor("#EE5622"));
                } else {
                    holder.divider.setImageResource(R.drawable.serviceredline);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)holder.divider.getLayoutParams();
                    lp.setMargins(left,top,right,bottom);
                    holder.divider.setLayoutParams(lp);
                    holder.title.setTextColor(Color.parseColor("#EE5622"));
                }
                break;
            default:
                holder.cardBackground.setBackgroundResource(R.drawable.redbackground);
                if(position == data.size()-1)
                {
                    holder.divider.setImageResource(R.drawable.serviceredlinebottom);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)holder.divider.getLayoutParams();
                    lp.setMargins(left,top,right,bottomFinal);
                    holder.divider.setLayoutParams(lp);
                    holder.title.setTextColor(Color.parseColor("#EE5622"));
                } else {
                    holder.divider.setImageResource(R.drawable.serviceredline);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)holder.divider.getLayoutParams();
                    lp.setMargins(left,top,right,bottom);
                    holder.divider.setLayoutParams(lp);
                    holder.title.setTextColor(Color.parseColor("#EE5622"));
                }
                break;
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView price;
        public TextView location;
        public TextView cardDate;
        public ImageView divider;
        public LinearLayout cardBackground;

        public ImageButton attachment;
        public ImageButton delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.serviceCardTitle);
            price = (TextView) itemView.findViewById(R.id.serviceCardPrice);
            location = (TextView) itemView.findViewById(R.id.serviceCardLocation);
            attachment = (ImageButton) itemView.findViewById(R.id.serviceCardAttachment);
            delete = (ImageButton) itemView.findViewById(R.id.serviceCardDelete);
            cardDate = (TextView) itemView.findViewById(R.id.serviceCardDate);
            divider = (ImageView) itemView.findViewById(R.id.serviceCardDivider);
            cardBackground = (LinearLayout) itemView.findViewById(R.id.serviceCardBackground);
        }
    }

    public static int convertDpToPixel(int dp, Context context){
        return dp * ((Integer) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
