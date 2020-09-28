package com.swack.transport.data;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.swack.transport.R;
import com.swack.transport.activities.VehicalDetails;
import com.swack.transport.model.MyVehicleListDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonu on 19/09/16.
 */

public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.MyViewHolder> {

    private VehicalDetails context;
    private ArrayList<MyVehicleListDetails> arrayLists;
    private ArrayList<MyVehicleListDetails> tempArrayLists;
    int sum;
    private float prize;
    private Intent intent;
    String total_amt,id,cus_id,vehical_name;

    Dialog dialog;
    private MyVehicleListDetails myVehicleListDetails;

    public RecyclerViewAdapter2(VehicalDetails context) {
        this.context = context;
    }

    public List<MyVehicleListDetails> getListArray() {
        return arrayLists;
    }
    /*public List<MyVehicleListDetails> getListArray() {
        return arrayLists2;
    }
*/
    public void setListArray(ArrayList<MyVehicleListDetails> arrayLists) {
        this.arrayLists = arrayLists;
        tempArrayLists = new ArrayList<>();
        tempArrayLists.addAll(arrayLists);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.info_vehical, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final MyVehicleListDetails myVehicleListDetails = arrayLists.get(position);

        holder.vehicle_name.setText(String.valueOf(myVehicleListDetails.getVehicle_name()));
        holder.vehicle_fc_expdate.setText(String.valueOf(myVehicleListDetails.getVehicle_fc_expdate()));
        holder.vehicled_regno.setText(String.valueOf(myVehicleListDetails.getVehicled_regno()));
        holder.vehicled_id.setText(String.valueOf(myVehicleListDetails.getVehicled_id()));
        holder.vehicle_cat_name.setText(String.valueOf(myVehicleListDetails.getVehicle_cat_name()));
        holder.loadrang_name.setText(String.valueOf(myVehicleListDetails.getLoadrang_name()));
        holder.vehicle_mody_name.setText(String.valueOf(myVehicleListDetails.getVehicle_mody_name()));
        holder.vehicle_no_tyres.setText(String.valueOf(myVehicleListDetails.getVehicle_no_tyres()));
        holder.vehicled_kmrd.setText(String.valueOf(myVehicleListDetails.getVehicled_kmrd()));
        holder.vehicled_ins_expdate.setText(String.valueOf(myVehicleListDetails.getVehicled_ins_expdate()));

        //holder.vehicle_rc_doc.setimage(String.valueOf());
        System.out.println("pic location"+myVehicleListDetails.getVehicle_doc());
        System.out.println("rc pic location"+myVehicleListDetails.getVehicle_rc_doc());
        System.out.println("ins pic location"+myVehicleListDetails.getVehicle_ins_doc());
        Picasso.with(context).load(myVehicleListDetails.getVehicle_rc_doc()).error(R.drawable.rc_book).into(holder.vehicle_rc_doc);
        Picasso.with(context).load(myVehicleListDetails.getVehicle_ins_doc()).error(R.drawable.van).into(holder.vehicle_ins_doc);
        Picasso.with(context).load(myVehicleListDetails.getVehicle_doc()).error(R.drawable.logo).into(holder.vehicle_doc);


        holder.linear_vehical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "position"+tempArrayLists.get(position).getVehicled_id(), Toast.LENGTH_SHORT).show();

                //Toast.makeText(context, "cus id"+cus_id, Toast.LENGTH_SHORT).show();
                //load(id,cus_id);
            }
        });
    }


    @Override
    public int getItemCount() {
        return (arrayLists == null) ? 0 : arrayLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView vehicle_name,vehicle_fc_expdate,vehicled_regno,vehicled_id,vehicle_cat_name,loadrang_name,vehicle_mody_name,vehicle_no_tyres,
                vehicled_kmrd,vehicled_ins_expdate;
        public LinearLayout linear_vehical;
        public ImageView vehicle_rc_doc,vehicle_doc,vehicle_ins_doc;

        public MyViewHolder(View view) {
            super(view);
            vehicle_ins_doc = view.findViewById(R.id.vehicle_ins_doc);
            vehicle_rc_doc = view.findViewById(R.id.vehicle_rc_doc);
            vehicle_doc = view.findViewById(R.id.vehicle_doc);
            vehicle_name = view.findViewById(R.id.vehicle_name);
            vehicled_ins_expdate = view.findViewById(R.id.vehicled_ins_expdate);
            vehicle_fc_expdate = view.findViewById(R.id.vehicle_fc_expdate);
            vehicled_regno = view.findViewById(R.id.vehicled_regno);
            vehicled_id = view.findViewById(R.id.vehicled_id);
            vehicle_cat_name = view.findViewById(R.id.vehicle_cat_name);
            loadrang_name = view.findViewById(R.id.loadrang_name);
            vehicle_mody_name = view.findViewById(R.id.vehicle_mody_name);
            vehicle_no_tyres = view.findViewById(R.id.vehicle_no_tyres);
            vehicled_kmrd = view.findViewById(R.id.vehicled_kmrd);
            linear_vehical = view.findViewById(R.id.linear_vehical);
        }
    }
}
