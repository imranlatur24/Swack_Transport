package com.swack.transport.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.swack.transport.R;
import com.swack.transport.activities.HistoryActivity;
import com.swack.transport.activities.LoginActivity;
import com.swack.transport.activities.MainActivity;
import com.swack.transport.data.APIUrl;
import com.swack.transport.model.ResponseResult;
import com.swack.transport.model.TransportList;

import java.util.ArrayList;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TransportAdapter extends RecyclerView.Adapter<TransportAdapter.MyViewHolder> {

    private HistoryActivity context;
    private ArrayList<TransportList> arrayLists;
    private ArrayList<TransportList> tempArrayLists;
    private CallbackInterface mCallback;
    private String transid,cus_id;
    public TransportAdapter(HistoryActivity context,String transid) {
        this.context = context;
        this.transid = transid;
        try{
            mCallback = (CallbackInterface) context;
        }catch(ClassCastException ex){
            //.. should log the error or throw and exception
            System.out.println("MyAdapter Must implement the CallbackInterface in the Activity "+ex);
        }
    }

    public void setListArray(ArrayList<TransportList> arrayLists) {
        this.arrayLists = arrayLists;
        tempArrayLists = new ArrayList<>();
        tempArrayLists.addAll(arrayLists);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.finalbilladapter, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final TransportList mOrder = arrayLists.get(position);

        holder.txt_joborderid.setText("Order Id :"+mOrder.getTransport_id()+" - "+mOrder.getTraProductName());
        holder.txt_jobid.setText("TRACKING");
        holder.txtJobDate.setText("Date : "+mOrder.getIs_create());
        holder.txtJobUnique.setText("To District : "+mOrder.getToDistrict_Name());
        holder.txtJobUnique.setEllipsize(TextUtils.TruncateAt.MARQUEE);

        holder.txtJobMobile.setText(mOrder.getTransport_mob());
        holder.txtJobCustomer.setText(mOrder.getTransport_name());
        holder.txtJobCustomer.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.txtJobCustomer.setSelected(true);
        holder.txtJobCustomer.setSingleLine(true);
        holder.txtJobLocation.setText("From District : "+mOrder.getFromDistrict_Name());
        holder.txtJobLocation.setEllipsize(TextUtils.TruncateAt.MARQUEE);

        holder.txtJobDistance.setText(mOrder.getDistance());
        holder.txtJobDistance.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.txtJobDistance.setSelected(true);
        holder.txtJobDistance.setSingleLine(true);
        holder.txt_jobid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", Float.parseFloat(mOrder.getTransport_lat()), Float.parseFloat(mOrder.getTransport_long()));
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    context.startActivity(intent);
                }catch (ActivityNotFoundException e){
                    Toasty.error(context,"Google Map App Not Found").show();
                }
            }
        });
        holder.txtJobStatus.setText("Status : "+mOrder.getStatus_name());

       /* if(mOrder.getStatus_name().equalsIgnoreCase("Pending")){
            holder.txtConfirm.setText("Confirm");
            confirmOrder(mOrder);
        }else if(mOrder.getStatus_name().equalsIgnoreCase("On Process")){
            holder.txtConfirm.setText("Done");
        }else {
            holder.txtConfirm.setVisibility(View.GONE);
        }*/

        //

        holder.txtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mOrder.getStatus_name().equalsIgnoreCase("Pending")){
                    holder.txtConfirm.setText("Confirm");
                    cus_id = mOrder.getTrnreqcusid();
                    System.out.println("&Cus id: - "+cus_id);
                    confirmOrder(mOrder);
                }else if(mOrder.getStatus_name().equalsIgnoreCase("On Process")){
                    holder.txtConfirm.setText("Submit Bill");
                    mCallback.onHandleSelection(mOrder);
                }else if(mOrder.getStatus_name().equalsIgnoreCase("Complete")){
                    holder.txtConfirm.setText("Complete");
                    // mCallback.onHandleSelection(mOrder);
                }else{
                    holder.txtConfirm.setVisibility(View.GONE);
                }
            }
        });

        holder.cvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.info_dialog);
                dialog.setCancelable(true);
                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setLayout(width, height);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                TextView transport_order_id = dialog.findViewById(R.id.transport_order_id);
                transport_order_id.setText(mOrder.getTransport_id());
                TextView transport_from = dialog.findViewById(R.id.transport_from);
                transport_from.setText(mOrder.getFromDistrict_Name());
                TextView transport_to = dialog.findViewById(R.id.transport_to);
                transport_to.setText(mOrder.getToDistrict_Name());
                TextView transport_distance = dialog.findViewById(R.id.transport_distance);
                transport_distance.setText(mOrder.getDistance());
                TextView transport_status = dialog.findViewById(R.id.transport_status);
                transport_status.setText(mOrder.getStatus_name());
                dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();*/
            }
        });

      /* holder.txtConfirm.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if(mOrder.getStatus_name().equalsIgnoreCase("New")){
                    confirmOrder(mOrder);
               }*//*else if(mOrder.getStatus_name().equalsIgnoreCase("On Process")){
                    mCallback.onHandleSelection(mOrder);
               }*//*
           }
       });*/
    }

    @Override
    public int getItemCount() {
        return (arrayLists == null) ? 0 : arrayLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_joborderid,txt_jobid,txtJobDate,txtJobUnique,txtJobCustomer,
                txtJobDistance,txtJobLocation,txtJobStatus,txtConfirm,txtJobMobile;
        public CardView cvCategory;

        public MyViewHolder(View view) {
            super(view);
            txt_jobid = view.findViewById(R.id.txt_jobid);
            txt_joborderid = view.findViewById(R.id.txt_joborderid);
            txtJobDate = view.findViewById(R.id.txtJobDate);
            txtJobUnique = view.findViewById(R.id.txtJobUnique);
            txtJobCustomer = view.findViewById(R.id.txtJobCustomer);
            txtJobLocation = view.findViewById(R.id.txtJobLocation);
            txtJobDistance = view.findViewById(R.id.txtJobDistance);
            txtJobStatus = view.findViewById(R.id.txtJobStatus);
            txtConfirm = view.findViewById(R.id.txtConfirm);
            txtJobMobile = view.findViewById(R.id.txtJobMobile);
            cvCategory = view.findViewById(R.id.cvCategory);
        }
    }

    public interface CallbackInterface{
        void onHandleSelection();
        void onHandleSelection(TransportList transportList);
    }

    private void confirmOrder(final TransportList mOrder) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        System.out.println("#API KEY : " + APIUrl.KEY + "TRANSPORT ID ORDER " + transid + "TRANSPORT TRANS ID " + mOrder.getTrnreqid());
        context.apiService.transportConfirm(APIUrl.KEY, transid, mOrder.getTrnreqid(),cus_id).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                System.out.println("Transport Confirm response " + response.body().getResponse());
                progressDialog.dismiss();
                if (Integer.parseInt(response.body().getResponse()) == 101) {
                    Toasty.success(context, "Transport Order Confirm", Toast.LENGTH_LONG).show();
                    context.startActivity(new Intent(context,HistoryActivity.class));
                    context.finish();
                } else if (Integer.parseInt(response.body().getResponse()) == 102) {
                    Toasty.error(context, context.getResources().getString(R.string.account_block), Toast.LENGTH_LONG).show();
                } else {
                    Toasty.error(context, "Something went wrong, try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                System.out.println("Transport Confirm Throwable " + t);
                context.errorOut(t);
            }
        });
    }
}
