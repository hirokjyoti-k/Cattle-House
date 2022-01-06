package in.adbu.cattlehouse.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.adbu.cattlehouse.CattleData;
import in.adbu.cattlehouse.R;
import in.adbu.cattlehouse.ViewCattleData;

public class CattleAdapter extends RecyclerView.Adapter<CattleAdapter.CattleViewHolder> {

    List<CattleData> cattleData;
    Context context;

    public CattleAdapter(List<CattleData> cattleData, Context context) {
        this.cattleData = cattleData;
        this.context = context;
    }

    @NonNull
    @Override
    public CattleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cattle, parent, false);
        return new CattleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CattleViewHolder holder, int position) {

        CattleData cattledata = cattleData.get(position);
        holder.cattleID.setText("Unique ID : "+cattledata.Uid);
        holder.cattleType.setText("Category : "+cattledata.Type);

        switch (cattledata.Type){
            case "Cattle":  holder.cattleImg.setImageResource(R.drawable.cow_image);
            break;
            case "Goat": holder.cattleImg.setImageResource(R.drawable.goat_image);
            break;
            case "Pig" : holder.cattleImg.setImageResource(R.drawable.pig_image);
                break;
            default:
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewCattleData.class);
                intent.putExtra("position", "" + position);
                context.startActivity(intent);
//                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cattleData.size();
    }


    class CattleViewHolder extends RecyclerView.ViewHolder {

        ImageView cattleImg;
        TextView cattleID, cattleType;

        public CattleViewHolder(@NonNull View itemView) {
            super(itemView);

            cattleImg = (ImageView) itemView.findViewById(R.id.Image);
            cattleID = (TextView) itemView.findViewById(R.id.ID);
            cattleType = (TextView) itemView.findViewById(R.id.Type);
        }
    }

}

