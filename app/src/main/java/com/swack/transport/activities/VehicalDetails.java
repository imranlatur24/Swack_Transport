package com.swack.transport.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.swack.transport.R;
import com.swack.transport.data.APIUrl;
import com.swack.transport.data.RecyclerViewAdapter2;
import com.swack.transport.model.MyVehicleListDetails;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicalDetails extends BaseActivity {
    
    private final static String TAG = "VehicalDetails";
    private RecyclerView recyclerView_list;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayout error_layout;
   // private Button btnRetry;
    private ProgressBar progressBar;
    private LinearLayout errorLayout;
    private ArrayList<MyVehicleListDetails> templistview;
    private String cus_id,id,vehical_name;
    private FloatingActionButton fab;
    private Intent intent;
    private RecyclerViewAdapter2 recyclerViewAdapter;
    //
    private TextView vehicled_id,vehicle_name,vehicle_cat_name,loadrang_name,vehicle_mody_name,vehicle_no_tyres,vehicled_regno,vehicled_kmrd,
            vehicled_ins_expdate,vehicle_fc_expdate;
    private ImageView vehicle_rc_doc,vehicle_doc,vehicle_ins_doc;
    private Dialog dialog;    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);
        intent = getIntent();
        id = intent.getStringExtra("vehical id");
        vehical_name = intent.getStringExtra("vehical name");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(vehical_name);

        inti();
        load();

    }

    @SuppressLint("RestrictedApi")
    private void inti() {
        progressBar = (ProgressBar) findViewById(R.id.main_progress1);
//        vehicle_list.setVisibility(View.GONE);

        prefManager.connectDB();
        cus_id = prefManager.getString("transport_id");
        prefManager.closeDB();

       // errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        recyclerView_list = findViewById(R.id.vehicle_list);
        recyclerView_list.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_list.setLayoutManager(linearLayoutManager);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

  /*      fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(VehicalDetails.this,AddVehicleActivity.class),200);
            }
        });
*/
    }

    private void load()
    {
      /*  final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext(), R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage(getApplicationContext().getResources().getString(R.string.loading));
        progressDialog.show();
        progressDialog.setCancelable(false);*/
        templistview = new ArrayList<>();


        callCategory().enqueue(new Callback<MyVehicleListDetails>() {
            @Override
            public void onResponse(Call<MyVehicleListDetails> call, Response<MyVehicleListDetails> response) {
                try {
                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                       // progressDialog.dismiss();
                        System.out.println("response Vehical Details Activity Response :"+response.body().getResponse());

                        recyclerViewAdapter = new RecyclerViewAdapter2(VehicalDetails.this);
                        recyclerView_list.setAdapter(recyclerViewAdapter);
                        recyclerViewAdapter.setListArray(response.body().getMyVehicleDetailsList());

                        templistview = response.body().getMyVehicleDetailsList();
                        System.out.println("vehical response vehical details Arraylist :"+templistview);

                        Log.d("vehical details size", String.valueOf(templistview.size()));

                    } else {
                        //progressDialog.dismiss();
                        //System.out.println(TAG + " Else Close");
                        Toasty.error(VehicalDetails.this, getResources().getString(R.string.serverdown), Toast.LENGTH_LONG).show();
                    }
                } catch (NullPointerException | NumberFormatException e) {
                    //progressDialog.dismiss();
                }
            }


            @Override
            public void onFailure(Call<MyVehicleListDetails> call, Throwable t) {
                //progressDialog.dismiss();
                errorOut(t);
            }
        });
    }
    //categories
    private Call<MyVehicleListDetails> callCategory() {
        System.out.println("API KEY VEHICAL POPDATA" + APIUrl.KEY +"Position ID : "+id + " : CUS ID " + cus_id);
        return apiService.getVehical(
                APIUrl.KEY, cus_id,id
        );
    }

    private void pop() {
        Toast.makeText(this, "hi pop", Toast.LENGTH_SHORT).show();

        dialog = new Dialog(VehicalDetails.this);
        dialog.setContentView(R.layout.info_vehical);
        dialog.setCancelable(true);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //vehicled_id=(TextView) dialog.findViewById(R.id.vehicled_id);
        vehicle_name=(TextView) dialog.findViewById(R.id.vehicle_name);
        vehicle_cat_name=(TextView) dialog.findViewById(R.id.vehicle_cat_name);
        loadrang_name=(TextView) dialog.findViewById(R.id.loadrang_name);
        vehicle_mody_name=(TextView) dialog.findViewById(R.id.vehicle_mody_name);
        vehicle_no_tyres=(TextView) dialog.findViewById(R.id.vehicle_no_tyres);
        vehicled_regno=(TextView) dialog.findViewById(R.id.vehicled_regno);
        vehicled_kmrd=(TextView) dialog.findViewById(R.id.vehicled_kmrd);
        vehicled_ins_expdate=(TextView) dialog.findViewById(R.id.vehicled_ins_expdate);
        vehicle_fc_expdate=(TextView) dialog.findViewById(R.id.vehicle_fc_expdate);
        vehicle_rc_doc=(ImageView) dialog.findViewById(R.id.vehicle_rc_doc);
        vehicle_doc=(ImageView) dialog.findViewById(R.id.vehicle_doc);
        vehicle_ins_doc=(ImageView) dialog.findViewById(R.id.vehicle_ins_doc);
        vehicled_id.setText(templistview.get(0).getVehicled_id());
        vehicle_cat_name.setText(templistview.get(0).getVehicle_cat_name());
        loadrang_name.setText(templistview.get(0).getLoadrang_name());
        vehicle_mody_name.setText(templistview.get(0).getVehicle_mody_name());
        vehicle_no_tyres.setText(templistview.get(0).getVehicle_no_tyres());
        vehicled_regno.setText(templistview.get(0).getVehicled_regno());
        vehicled_kmrd.setText(templistview.get(0).getVehicled_kmrd());
        vehicled_ins_expdate.setText(templistview.get(0).getVehicled_ins_expdate());
        vehicle_fc_expdate.setText(templistview.get(0).getVehicle_fc_expdate());
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200){
            if(resultCode == Activity.RESULT_OK){
                load();
            }
        }
    }

}
