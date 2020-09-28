package com.swack.transport.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.swack.transport.BuildConfig;
import com.swack.transport.R;
import com.swack.transport.data.APIUrl;
import com.swack.transport.data.FileUtil;
import com.swack.transport.model.LoadList;
import com.swack.transport.model.ModalYearList;
import com.swack.transport.model.ResponseResult;
import com.swack.transport.model.TyerList;
import com.swack.transport.model.VehicleCatList;
import com.swack.transport.model.VehicleDetailsList;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import view.TextInputAutoCompleteTextView;


public class AddVehicleActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "AddVehicleActivity";
    private static final int REQUEST_CAMERA = 101;
    private static final int SELECT_FILE = 102;
    private TextInputAutoCompleteTextView spinner_company,spinner_type,spinner_load,spinner_tyres,spinner_modal_years;
    private TextInputEditText edt_vehicle_no,edt_vehicle_reading,edt_vehicle_insurance,edt_vehicle_rc;
    private ArrayList<VehicleDetailsList> vehicleLists;
    private ArrayList<VehicleCatList> catTypeLists;
    private ArrayList<ModalYearList> molarYearLists;
    private ArrayList<LoadList> loadLists;
    private ArrayList<TyerList> tyersLists;
    private ArrayList<String> vehicleName,molarYearName,catTypeName,loadName,tyersName;
    private Button btnSubmitVehicle;
    private boolean vehicle,pan,rc;
    private ImageView add_vehicle, add_insurance,add_rc,profile_vehicle,profile_insurance,profile_rc;
    private File destination,newFile,profile_file = null,vehicle_file = null,insurance_file = null,rc_file = null;
    private String vehicleId,vehicleCatId,yearId,tyersId,loadId,vehicle_number,vehicle_reading,vehicleInsurance,vehicleRC,cus_id;
    private DatePickerDialog dateDialog;
    private Calendar dateCalendar;
    private DatePickerDialog.OnDateSetListener date_insurance = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            dateCalendar.set(Calendar.YEAR, year);
            dateCalendar.set(Calendar.MONTH, monthOfYear);
            dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            edt_vehicle_insurance.setText(getDateText(String.valueOf(dateCalendar.getTimeInMillis())));
        }
    };
    private DatePickerDialog.OnDateSetListener date_rc = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            dateCalendar.set(Calendar.YEAR, year);
            dateCalendar.set(Calendar.MONTH, monthOfYear);
            dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            edt_vehicle_rc.setText(getDateText(String.valueOf(dateCalendar.getTimeInMillis())));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Vehicle");*/
        getSupportActionBar().setTitle(R.string.add_vehical);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inti();
        onClickListener();
    }

    private void inti() {
        prefManager.connectDB();
        cus_id = prefManager.getString("transport_id");
        prefManager.closeDB();

        dateCalendar = Calendar.getInstance();

        spinner_company = (TextInputAutoCompleteTextView) findViewById(R.id.spinner_company);
        spinner_type = (TextInputAutoCompleteTextView) findViewById(R.id.spinner_type);
        spinner_load = (TextInputAutoCompleteTextView) findViewById(R.id.spinner_load);
        spinner_modal_years = (TextInputAutoCompleteTextView) findViewById(R.id.spinner_modal_years);
        spinner_tyres = (TextInputAutoCompleteTextView) findViewById(R.id.spinner_tyres);

        edt_vehicle_no = (TextInputEditText) findViewById(R.id.edt_vehicle_no);
        edt_vehicle_reading = (TextInputEditText) findViewById(R.id.edt_vehicle_reading);
        edt_vehicle_insurance = (TextInputEditText) findViewById(R.id.edt_vehicle_insurance);
        edt_vehicle_rc = (TextInputEditText) findViewById(R.id.edt_vehicle_rc);

        add_vehicle = findViewById(R.id.add_vehicle);
        add_insurance = findViewById(R.id.add_insurance);
        add_rc = findViewById(R.id.add_rc);
        profile_vehicle = findViewById(R.id.profile_vehicle);
        profile_insurance = findViewById(R.id.profile_insurance);
        profile_rc = findViewById(R.id.profile_rc);

        btnSubmitVehicle = findViewById(R.id.btnSubmitVehicle);

        getVehicleLists();
        getMolarYearLists();
        getLoadLists();
        getTyersLists();
    }
    private void onClickListener() {
      /*  spinner_company.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vehicleId = vehicleLists.get(position).getVehicled_id();
                getCatTypeLists(vehicleId);
                System.out.println("vehicleId "+vehicleId+ " ");
            }
        });*/
        spinner_company.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < vehicleLists.size(); i++) {
                    if (spinner_company.getText().toString().equals(vehicleLists.get(i).getVehicled_name())) {
                        vehicleId = vehicleLists.get(i).getVehicled_id();
                        getCatTypeLists(vehicleId);
                        System.out.println("spinner company "+vehicleId+ " ");
                        //Toast.makeText(AddVehicleActivity.this, "@Vehical Id "+vehicleId, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });

      /*  spinner_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vehicleCatId = catTypeLists.get(position).getVehicle_cat_id();
                System.out.println("vehicleId "+vehicleCatId+ " ");
            }
        });*/

        spinner_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < catTypeLists.size(); i++) {
                    if (spinner_type.getText().toString().equals(catTypeLists.get(i).getVehicle_cat_name())) {
                        vehicleCatId = catTypeLists.get(i).getVehicle_cat_id();
                        System.out.println("spinner type "+vehicleCatId+ " ");
                        //Toast.makeText(AddVehicleActivity.this, "@vehicleCat Id "+vehicleCatId, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });

        spinner_modal_years.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                yearId = molarYearLists.get(position).getVehicle_mody_id();
                System.out.println("vehicleId "+yearId+ " ");
            }
        });
        spinner_modal_years.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < molarYearLists.size(); i++) {
                    if (spinner_modal_years.getText().toString().equals(molarYearLists.get(i).getVehicle_mody_name())) {
                        yearId = molarYearLists.get(i).getVehicle_mody_id();
                        System.out.println("spinner model years  "+yearId+ " ");
                        //Toast.makeText(AddVehicleActivity.this, "@year Id "+yearId, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });


        spinner_load.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadId = loadLists.get(position).getLoadrang_id();
                System.out.println("vehicleId "+loadId+ " ");
            }
        });
        spinner_load.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < loadLists.size(); i++) {
                    if (spinner_load.getText().toString().equals(loadLists.get(i).getLoadrang_name())) {
                        loadId = loadLists.get(i).getLoadrang_id();
                        //loadId = "0";
                        System.out.println("spinner load id "+loadId+ " ");
                        //Toast.makeText(AddVehicleActivity.this, "@load Id "+loadId, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });


        spinner_tyres.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tyersId = molarYearLists.get(position).getVehicle_mody_id();
                System.out.println("vehicleId "+tyersId+ " ");
            }
        });

        spinner_tyres.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < tyersLists.size(); i++) {
                    if (spinner_tyres.getText().toString().equals(tyersLists.get(i).getTyers_no())) {
                        tyersId = tyersLists.get(i).getTyers_id();
                        System.out.println("spinner tyres id "+tyersId+ " ");
                        //Toast.makeText(AddVehicleActivity.this, "@Tyres Id "+tyersId, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });

        edt_vehicle_insurance.setOnClickListener(this);
        edt_vehicle_rc.setOnClickListener(this);
        profile_vehicle.setOnClickListener(this);
        profile_insurance.setOnClickListener(this);
        profile_rc.setOnClickListener(this);
        btnSubmitVehicle.setOnClickListener(this);
    }

    private void getVehicleLists() {
        //defining a progress dialog to show while signing up
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        vehicleLists = new ArrayList<>();

        apiService.callVehicleLists(APIUrl.KEY).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                //hiding progress dialog
                progressDialog.dismiss();
                System.out.println(TAG+" Response "+ response.body().getResponse());
                if(Integer.parseInt(response.body().getResponse()) == 101) {
                    vehicleLists = response.body().getVehicleList();
                    vehicleAutoCompleteTextView(vehicleLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 102){
                    vehicleAutoCompleteTextView(vehicleLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 103){
                    vehicleAutoCompleteTextView(vehicleLists);
                    System.out.println(TAG+ " Required Parameter Missing");
                }else if(Integer.parseInt(response.body().getResponse()) == 104){
                    vehicleAutoCompleteTextView(vehicleLists);
                    System.out.println(TAG+ " Invalid Key");
                }else if(Integer.parseInt(response.body().getResponse()) == 105){
                    vehicleAutoCompleteTextView(vehicleLists);
                    System.out.println(TAG+ " Login failed");
                }else if(Integer.parseInt(response.body().getResponse()) == 106){
                    vehicleAutoCompleteTextView(vehicleLists);
                    System.out.println(TAG+ " Page Not Found");
                }else {
                    vehicleAutoCompleteTextView(vehicleLists);
                    System.out.println(TAG+ " Else Close");
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                vehicleAutoCompleteTextView(vehicleLists);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

  /*  private void vehicleAutoCompleteTextView(ArrayList<VehicleDetailsList> vehicleLists) {
        vehicleName = new ArrayList<>();
        for (VehicleDetailsList data: vehicleLists){
            vehicleName.add(data.getVehicled_name());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_custom_text, vehicleName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_company.setThreshold(1);
        // attaching data adapter to spinner
        spinner_company.setAdapter(dataAdapter);
    }*/

    private void vehicleAutoCompleteTextView(ArrayList<VehicleDetailsList> loadLists) {
        vehicleName = new ArrayList<>();
        for (VehicleDetailsList data : loadLists) {
            vehicleName.add(data.getVehicled_name());
            if (data.getVehicled_id().equals(vehicleName)) {
                spinner_company.setText(data.getVehicled_name());
                //getState(co   untry);
                Toast.makeText(this, "#vehicleName "+vehicleName, Toast.LENGTH_SHORT).show();
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, vehicleName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_2);
        spinner_company.setThreshold(1);
        // attaching data adapter to spinner
        spinner_company.setAdapter(dataAdapter);
    }

    private void getCatTypeLists(String vehicleId) {
        //defining a progress dialog to show while signing up
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        catTypeLists = new ArrayList<>();

        apiService.callCatTypeLists(APIUrl.KEY,vehicleId).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                //hiding progress dialog
                progressDialog.dismiss();
                System.out.println(TAG+" Response "+ response.body().getResponse());
                if(Integer.parseInt(response.body().getResponse()) == 101) {
                    catTypeLists = response.body().getVehicleCatList();
                    catTypeAutoCompleteTextView(catTypeLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 102){
                    catTypeAutoCompleteTextView(catTypeLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 103){
                    catTypeAutoCompleteTextView(catTypeLists);
                    System.out.println(TAG+ " Required Parameter Missing");
                }else if(Integer.parseInt(response.body().getResponse()) == 104){
                    catTypeAutoCompleteTextView(catTypeLists);
                    System.out.println(TAG+ " Invalid Key");
                }else if(Integer.parseInt(response.body().getResponse()) == 105){
                    catTypeAutoCompleteTextView(catTypeLists);
                    System.out.println(TAG+ " Login failed");
                }else if(Integer.parseInt(response.body().getResponse()) == 106){
                    catTypeAutoCompleteTextView(catTypeLists);
                    System.out.println(TAG+ " Page Not Found");
                }else {
                    catTypeAutoCompleteTextView(catTypeLists);
                    System.out.println(TAG+ " Else Close");
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                catTypeAutoCompleteTextView(catTypeLists);
            }
        });
    }

    private void getTyersLists() {
        //defining a progress dialog to show while signing up
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        tyersLists = new ArrayList<>();

        apiService.callTyersLists(APIUrl.KEY).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                //hiding progress dialog
                progressDialog.dismiss();
                System.out.println(TAG+" Response "+ response.body().getResponse());
                if(Integer.parseInt(response.body().getResponse()) == 101) {
                    tyersLists = response.body().getTyerList();
                    tyersAutoCompleteTextView(tyersLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 102){
                    tyersAutoCompleteTextView(tyersLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 103){
                    tyersAutoCompleteTextView(tyersLists);
                    System.out.println(TAG+ " Required Parameter Missing");
                }else if(Integer.parseInt(response.body().getResponse()) == 104){
                    tyersAutoCompleteTextView(tyersLists);
                    System.out.println(TAG+ " Invalid Key");
                }else if(Integer.parseInt(response.body().getResponse()) == 105){
                    tyersAutoCompleteTextView(tyersLists);
                    System.out.println(TAG+ " Login failed");
                }else if(Integer.parseInt(response.body().getResponse()) == 106){
                    tyersAutoCompleteTextView(tyersLists);
                    System.out.println(TAG+ " Page Not Found");
                }else {
                    tyersAutoCompleteTextView(tyersLists);
                    System.out.println(TAG+ " Else Close");
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                tyersAutoCompleteTextView(tyersLists);
            }
        });
    }

  /*  private void tyersAutoCompleteTextView(ArrayList<TyerList> tyersLists) {
        tyersName = new ArrayList<>();
        for (TyerList data: tyersLists){
            tyersName.add(data.getTyers_no());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_custom_text, tyersName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tyres.setThreshold(1);
        // attaching data adapter to spinner
        spinner_tyres.setAdapter(dataAdapter);
    }*/

    private void tyersAutoCompleteTextView(ArrayList<TyerList> loadLists) {
        tyersName = new ArrayList<>();
        for (TyerList data : loadLists) {
            tyersName.add(data.getTyers_no());
            if (data.getTyers_id().equals(tyersName)) {
                spinner_tyres.setText(data.getTyers_no());
                //getState(country);
                Toast.makeText(this, "#tyersName " + tyersName, Toast.LENGTH_SHORT).show();
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, tyersName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_2);
        spinner_tyres.setThreshold(1);
        // attaching data adapter to spinner
        spinner_tyres.setAdapter(dataAdapter);
    }

    private void getLoadLists() {
        //defining a progress dialog to show while signing up
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        loadLists = new ArrayList<>();

        apiService.callLoadLists(APIUrl.KEY).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                //hiding progress dialog
                progressDialog.dismiss();
                System.out.println(TAG+" Response "+ response.body().getResponse());
                if(Integer.parseInt(response.body().getResponse()) == 101) {
                    loadLists = response.body().getLoadList();
                    loadAutoCompleteTextView(loadLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 102){
                    loadAutoCompleteTextView(loadLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 103){
                    loadAutoCompleteTextView(loadLists);
                    System.out.println(TAG+ " Required Parameter Missing");
                }else if(Integer.parseInt(response.body().getResponse()) == 104){
                    loadAutoCompleteTextView(loadLists);
                    System.out.println(TAG+ " Invalid Key");
                }else if(Integer.parseInt(response.body().getResponse()) == 105){
                    loadAutoCompleteTextView(loadLists);
                    System.out.println(TAG+ " Login failed");
                }else if(Integer.parseInt(response.body().getResponse()) == 106){
                    loadAutoCompleteTextView(loadLists);
                    System.out.println(TAG+ " Page Not Found");
                }else {
                    loadAutoCompleteTextView(loadLists);
                    System.out.println(TAG+ " Else Close");
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                loadAutoCompleteTextView(loadLists);
            }
        });
    }

/*    private void loadAutoCompleteTextView(ArrayList<LoadList> loadLists) {
        loadName = new ArrayList<>();
        for (LoadList data: loadLists){
            loadName.add(data.getLoadrang_name());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_custom_text, loadName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_load.setThreshold(1);
        // attaching data adapter to spinner
        spinner_load.setAdapter(dataAdapter);
    }*/

    private void loadAutoCompleteTextView(ArrayList<LoadList> loadLists) {
        loadName = new ArrayList<>();
        for (LoadList data : loadLists) {
            loadName.add(data.getLoadrang_name());
            if (data.getLoadrang_id().equals(loadName)) {
                spinner_load.setText(data.getLoadrang_name());
                //getState(country);
                Toast.makeText(this, "#loadName " + loadName, Toast.LENGTH_SHORT).show();
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, loadName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_2);
        spinner_load.setThreshold(1);
        // attaching data adapter to spinner
        spinner_load.setAdapter(dataAdapter);
    }
   /* private void catTypeAutoCompleteTextView(ArrayList<VehicleCatList> catTypeLists) {
        catTypeName = new ArrayList<>();
        for (VehicleCatList data:catTypeLists){
            catTypeName.add(data.getVehicle_cat_name());0
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_custom_text,catTypeName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setThreshold(1);
        // attaching data adapter to spinner
        spinner_type.setAdapter(dataAdapter);
    }*/

    private void catTypeAutoCompleteTextView(ArrayList<VehicleCatList> loadLists) {
        catTypeName = new ArrayList<>();
        for (VehicleCatList data : loadLists) {
            catTypeName.add(data.getVehicle_cat_name());
            if (data.getVehicle_cat_id().equals(catTypeName)) {
                spinner_type.setText(data.getVehicle_cat_name());
                //getState(country);
                Toast.makeText(this, "#load Id "+catTypeName, Toast.LENGTH_SHORT).show();
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, catTypeName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_2);
        spinner_type.setThreshold(1);
        // attaching data adapter to spinner
        spinner_type.setAdapter(dataAdapter);
    }
    private void getMolarYearLists() {
        //defining a progress dialog to show while signing up
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        molarYearLists = new ArrayList<>();

        apiService.callMolarYearLists(APIUrl.KEY).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                //hiding progress dialog
                progressDialog.dismiss();
                System.out.println(TAG+" Response "+ response.body().getResponse());
                if(Integer.parseInt(response.body().getResponse()) == 101) {
                    molarYearLists = response.body().getModalYearList();
                    molarYearAutoCompleteTextView(molarYearLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 102){
                    molarYearAutoCompleteTextView(molarYearLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 103){
                    molarYearAutoCompleteTextView(molarYearLists);
                    System.out.println(TAG+ " Required Parameter Missing");
                }else if(Integer.parseInt(response.body().getResponse()) == 104){
                    molarYearAutoCompleteTextView(molarYearLists);
                    System.out.println(TAG+ " Invalid Key");
                }else if(Integer.parseInt(response.body().getResponse()) == 105){
                    molarYearAutoCompleteTextView(molarYearLists);
                    System.out.println(TAG+ " Login failed");
                }else if(Integer.parseInt(response.body().getResponse()) == 106){
                    molarYearAutoCompleteTextView(molarYearLists);
                    System.out.println(TAG+ " Page Not Found");
                }else {
                    molarYearAutoCompleteTextView(molarYearLists);
                    System.out.println(TAG+ " Else Close");
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                molarYearAutoCompleteTextView(molarYearLists);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

   /* private void molarYearAutoCompleteTextView(ArrayList<ModalYearList> molarYearLists) {
        molarYearName = new ArrayList<>();
        for (ModalYearList data: molarYearLists){
            molarYearName.add(data.getVehicle_mody_name());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_custom_text, molarYearName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_modal_years.setThreshold(1);
        // attaching data adapter to spinner
        spinner_modal_years.setAdapter(dataAdapter);
    }*/

    private void molarYearAutoCompleteTextView(ArrayList<ModalYearList> loadLists) {
        molarYearName = new ArrayList<>();
        for (ModalYearList data : loadLists) {
            molarYearName.add(data.getVehicle_mody_name());
            if (data.getVehicle_mody_id().equals(molarYearName)) {
                spinner_modal_years.setText(data.getVehicle_mody_name());
                //getState(country);
                Toast.makeText(this, "#molarYearName "+molarYearName, Toast.LENGTH_SHORT).show();
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, molarYearName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_2);
        spinner_modal_years.setThreshold(1);
        // attaching data adapter to spinner
        spinner_modal_years.setAdapter(dataAdapter);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AddVehicleActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
            }
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Error occurred while creating the File
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile);

                    destination = photoFile;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_CAMERA);

                }
            }
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSubmitVehicle:
                vehicle_number = edt_vehicle_no.getText().toString();
                vehicle_reading = edt_vehicle_reading.getText().toString();
                vehicleInsurance = edt_vehicle_insurance.getText().toString();
                vehicleRC = edt_vehicle_rc.getText().toString();
                MultipartBody.Part doc_vehicle = null;
                MultipartBody.Part doc_insurance = null;
                MultipartBody.Part doc_rc = null;
                try{
                    vehicle_file.isFile();
                    RequestBody requestFileAadhar =RequestBody.create(MediaType.parse("*/*"), vehicle_file);
                    doc_vehicle = MultipartBody.Part.createFormData("doc_vehicle", vehicle_file.getName(), requestFileAadhar);
                }catch (NullPointerException e){
                }
                try{
                    insurance_file.isFile();
                    RequestBody requestFilePan =RequestBody.create(MediaType.parse("*/*"), insurance_file);
                    doc_insurance = MultipartBody.Part.createFormData("doc_ins", insurance_file.getName(), requestFilePan);
                }catch (NullPointerException e){
                }
                try{
                    rc_file.isFile();
                    RequestBody requestFile =RequestBody.create(MediaType.parse("*/*"), rc_file);
                    doc_rc = MultipartBody.Part.createFormData("doc_rc", rc_file.getName(), requestFile);
                }catch (NullPointerException e){
                }

                if(!isNetworkAvailable()){
                    Toasty.error(this, "No internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(vehicleId)){
                    Toasty.error(this,"Select Company",Toasty.LENGTH_SHORT).show();
                    return;
                } if(TextUtils.isEmpty(vehicleCatId)){
                Toasty.error(this,"Select Type",Toasty.LENGTH_SHORT).show();
                return;
                } if(TextUtils.isEmpty(loadId)){
                    Toasty.error(this,"Select Load",Toasty.LENGTH_SHORT).show();
                    return;
                } if(TextUtils.isEmpty(tyersId)){
                    Toasty.error(this,"Select Tyres",Toasty.LENGTH_SHORT).show();
                    return;
                }if(TextUtils.isEmpty(yearId)){
                    Toasty.error(this,"Select Modal Years",Toasty.LENGTH_SHORT).show();
                    return;
                }if(TextUtils.isEmpty(vehicle_number)){
                    Toasty.error(this,"Enter Vehicle number",Toasty.LENGTH_SHORT).show();
                    return;
                }if(TextUtils.isEmpty(vehicle_reading)){
                    Toasty.error(this,"Enter Vehicle Reading",Toasty.LENGTH_SHORT).show();
                    return;
                }if(TextUtils.isEmpty(vehicleInsurance)){
                    Toasty.error(this,"Enter Vehicle Insurance Expired Date",Toasty.LENGTH_SHORT).show();
                    return;
                }if(TextUtils.isEmpty(vehicleRC)){
                    Toasty.error(this,"Enter Vehicle RC Expired Date",Toasty.LENGTH_SHORT).show();
                    return;
                }
                if(vehicle_file==null){
                    Toasty.error(this,"Add Vehical Image",Toasty.LENGTH_SHORT).show();
                    return;
                }if(vehicle_file != null && insurance_file==null){
                     Toasty.error(this,"Add Insurance Image",Toasty.LENGTH_SHORT).show();
                    return;
                }if(insurance_file != null && rc_file==null){
                    Toasty.error(this,"Add RC Image",Toasty.LENGTH_SHORT).show();
                    return;
                }
                System.out.println("key "+ APIUrl.KEY+
                        "cus_id "+cus_id+
                        "vehicleId "+vehicleId+
                        "vehicleCatId "+vehicleCatId+
                        "loadId "+loadId+
                        "tyersId "+tyersId+
                        "yearId "+yearId+
                        "vehicle_number "+vehicle_number+
                        "vehicle_reading "+vehicle_reading+
                        "vehicleInsurance "+vehicleInsurance+
                        "vehicleRC "+vehicleRC+
                        "vehicle_file "+vehicle_file.getName()+
                        "insurance_file "+insurance_file.getName()+
                        "rc_file "+rc_file.getName());
                addVehicle(RequestBody.create(MediaType.parse("text/plain"), APIUrl.KEY),
                        RequestBody.create(MediaType.parse("text/plain"),cus_id),
                        RequestBody.create(MediaType.parse("text/plain"),vehicleId),
                        RequestBody.create(MediaType.parse("text/plain"),vehicleCatId),
                        RequestBody.create(MediaType.parse("text/plain"),loadId),
                        RequestBody.create(MediaType.parse("text/plain"),tyersId),
                        RequestBody.create(MediaType.parse("text/plain"),yearId),
                        RequestBody.create(MediaType.parse("text/plain"),vehicle_number),
                        RequestBody.create(MediaType.parse("text/plain"),vehicle_reading),
                        RequestBody.create(MediaType.parse("text/plain"),vehicleInsurance),
                        RequestBody.create(MediaType.parse("text/plain"),vehicleRC),
                        RequestBody.create(MediaType.parse("text/plain"),vehicle_file.getName()),
                        RequestBody.create(MediaType.parse("text/plain"),insurance_file.getName()),
                        RequestBody.create(MediaType.parse("text/plain"),rc_file.getName()),
                        doc_vehicle,doc_insurance,doc_rc);

                break;
            case R.id.edt_vehicle_rc:
                dateDialog = new DatePickerDialog(this,
                        date_rc,
                        dateCalendar.get(Calendar.YEAR),
                        dateCalendar.get(Calendar.MONTH),
                        dateCalendar.get(Calendar.DAY_OF_MONTH));
                dateDialog.show();
                break;
            case R.id.edt_vehicle_insurance:
                dateDialog = new DatePickerDialog(this,
                        date_insurance,
                        dateCalendar.get(Calendar.YEAR),
                        dateCalendar.get(Calendar.MONTH),
                        dateCalendar.get(Calendar.DAY_OF_MONTH));
                dateDialog.show();
                break;
            case R.id.profile_vehicle:
                vehicle = true;
                pan = false;
                rc = false;
                selectImage();
                break;
            case R.id.profile_insurance:
                vehicle = false;
                pan = true;
                rc = false;
                selectImage();
                break;
            case R.id.profile_rc:
                vehicle = false;
                pan = false;
                rc = true;
                selectImage();
                break;
        }
    }

    private void addVehicle(RequestBody key, RequestBody cus_id, RequestBody vehicle_id, RequestBody vehicle_cat_id,
                            RequestBody loadrang_id, RequestBody vehicle_no_tyres, RequestBody vehicle_mody_id,
                            RequestBody vehicled_regno, RequestBody vehicled_kmrd, RequestBody vehicled_ins_expdate, RequestBody vehicle_fc_expdate,
                            RequestBody vehicle_file_name, RequestBody insurance_file_name, RequestBody rc_file_name,
                            MultipartBody.Part doc_vehicle, MultipartBody.Part doc_insurance, MultipartBody.Part doc_rc) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        apiService.callAddVehicle( key,  cus_id,  vehicle_id,  vehicle_cat_id,
                loadrang_id,  vehicle_no_tyres,  vehicle_mody_id,
                vehicled_regno,  vehicled_kmrd,  vehicled_ins_expdate,  vehicle_fc_expdate,
                vehicle_file_name,insurance_file_name,rc_file_name,
                doc_vehicle,  doc_insurance,  doc_rc).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                progressDialog.dismiss();
                System.out.println(TAG +"Add Vehicle response "+response.body().getResponse());
                if (Integer.parseInt(response.body().getResponse()) == 101) {
                    Toasty.success(getApplicationContext(),"Vehicle Added Successfully", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                }else {
                    Toasty.success(getApplicationContext(),"Adding Failed, try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                System.out.println(TAG +"Add Vehicle Throwable "+t);
                errorOut(t);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            //Toasty.error(this, "Image pick cancel", Toasty.LENGTH_SHORT).show();
            return;
        } else {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SELECT_FILE) {
                    try {
                        destination = FileUtil.from(AddVehicleActivity.this,data.getData());
                        newFile = new Compressor(this)
                                .setMaxWidth(640)
                                .setMaxHeight(480)
                                .setQuality(50)
                                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                                .compressToFile(destination);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else {
                    try {
                        newFile = new Compressor(this)
                                .setMaxWidth(640)
                                .setMaxHeight(480)
                                .setQuality(50)
                                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                                .compressToFile(destination);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(pan){
                    Picasso.with(AddVehicleActivity.this)
                            .load(destination)
                            .placeholder(R.drawable.pan)
                            .error(R.drawable.pan)
                            .into(profile_insurance);
                    insurance_file = newFile;
                }if(vehicle){
                    Picasso.with(AddVehicleActivity.this)
                            .load(destination)
                            .placeholder(R.drawable.van)
                            .error(R.drawable.van)
                            .into(profile_vehicle);
                    vehicle_file = newFile;
                }if(rc){
                    Picasso.with(AddVehicleActivity.this)
                            .load(destination)
                            .placeholder(R.drawable.rc_book)
                            .error(R.drawable.rc_book)
                            .into(profile_rc);
                    rc_file = newFile;
                }
            }
        }
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
}
