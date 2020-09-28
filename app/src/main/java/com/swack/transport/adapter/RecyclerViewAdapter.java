package com.swack.transport.adapter;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.swack.transport.R;
import com.swack.transport.activities.VehicalDetails;
import com.swack.transport.activities.VehicleActivity;
import com.swack.transport.model.MyVehicleList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonu on 19/09/16.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private VehicleActivity context;

    private ArrayList<MyVehicleList> arrayLists;
    private ArrayList<MyVehicleList> tempArrayLists;
    int sum;
    private float prize;
    private Intent intent;
    String total_amt,id,cus_id,vehical_name;

    Dialog dialog;

    public RecyclerViewAdapter(VehicleActivity context, String cus_id) {
        this.context = context;
        this.cus_id = cus_id;
    }

    public List<MyVehicleList> getListArray() {
        return arrayLists;
    }
    /*public List<MyVehicleList> getListArray() {
        return arrayLists2;
    }
*/
    public void setListArray(ArrayList<MyVehicleList> arrayLists) {
        this.arrayLists = arrayLists;
        tempArrayLists = new ArrayList<>();
        tempArrayLists.addAll(arrayLists);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vehical_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final MyVehicleList MyVehicleList = arrayLists.get(position);

        if (holder.txt_vehicalName.equals("sss")) {
            holder.txt_vehicalName.setText("");
        }if (holder.txt_vehicalName.equals("ddd")) {
            holder.txt_vehicalName.setText("");
        }
        holder.txt_vehicalName.setText(String.valueOf(MyVehicleList.getVehicle_name()));
        holder.txt_vehicalName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.txt_vehicalType.setText(String.valueOf(MyVehicleList.getVehicle_cat_name()));
        holder.txt_vehicalType.setEllipsize(TextUtils.TruncateAt.MARQUEE);



        holder.linear_vehical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context, "position"+tempArrayLists.get(position).getVehicled_id(), Toast.LENGTH_SHORT).show();
                id = tempArrayLists.get(position).getVehicled_id();
                vehical_name = tempArrayLists.get(position).getVehicle_name();
                cus_id=cus_id;
                intent = new Intent(context, VehicalDetails.class);
                intent.putExtra("vehical id",id);
                intent.putExtra("vehical name",vehical_name);
                context.startActivity(intent);

                //Toast.makeText(context, "cus id"+cus_id, Toast.LENGTH_SHORT).show();
                //load(id,cus_id);
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_vehicalName,txt_vehicalType;
        public LinearLayout linear_vehical;

        public MyViewHolder(View view) {
            super(view);
            txt_vehicalType = view.findViewById(R.id.txt_vehicalType);
            txt_vehicalName = view.findViewById(R.id.txt_vehicalName);
            linear_vehical = view.findViewById(R.id.linear_vehical);
        }
    }
}
