package com.swack.transport.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.swack.transport.BuildConfig;
import com.swack.transport.R;
import com.swack.transport.data.APIUrl;
import com.swack.transport.model.FinalListModel;
import com.swack.transport.model.ItemList;
import com.swack.transport.model.ResponseResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class JobDetailActivity extends BaseActivity {

    private static final String TAG = "JobDetailActivity";
    private static final int REQUEST_CAMERA = 101;
    private TableLayout mTableLayout;
    private Button btn_submit_bill;
    private String order_id,gar;
    private Spinner edit_state;
    private Button btnAddBill;
    private EditText edtJobEstimate,edtjobfinalamt;
    private String jobFinalAmt,jobEstimate;
    private Dialog dialog;
    private ArrayList<ItemList> vehicalLists;
    private String vehical_type = "1",gar_id,customer_id;
    private ArrayList<String> typeName;
    private String vehical_name;
    private File image,newFile;
    private String ettype="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        getSupportActionBar().setTitle("Order id : "+getIntent().getStringExtra("order_id"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTableLayout = (TableLayout) findViewById(R.id.tableInvoices);
        btn_submit_bill = (Button) findViewById(R.id.btn_submit_bill);

        prefManager.connectDB();
        gar_id = prefManager.getString("gar_id");
        prefManager.closeDB();
        order_id = getIntent().getStringExtra("order_id");
        customer_id = getIntent().getStringExtra("customer_id");
        addHeaders();
        addData();

        if(!getIntent().getBooleanExtra("button",false)){
            btn_submit_bill.setVisibility(View.VISIBLE);
        }
        btn_submit_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkPermission()){
                    requestPermission();
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
                            Uri photoURI = FileProvider.getUriForFile(JobDetailActivity.this,
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    photoFile);

                            image = photoFile;
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, REQUEST_CAMERA);

                        }
                    }
                }

            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{CAMERA,READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE}, 1);
    }

    public boolean checkPermission() {

        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        final TextView tv = new TextView(this);
        tv.setId(id);
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(4, 4, 4, 4);
        tv.setTextSize(18);
        tv.setBackgroundColor(bgColor);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setLayoutParams(getLayoutParams());

        return tv;
    }

    @NonNull
    private TableRow.LayoutParams getLayoutParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 2, 2, 2);
        return params;
    }

    @NonNull
    private TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
    }
    /**
     * This function add the headers to the table
     **/
    public void addHeaders() {
        TableRow tr = new TableRow(this);
        tr.addView(getTextView(0, "Sr no.", Color.WHITE, Typeface.BOLD, ContextCompat.getColor(this, R.color.colorAccent)));
        tr.addView(getTextView(0, "Part Name", Color.WHITE, Typeface.BOLD, ContextCompat.getColor(this, R.color.colorAccent)));
        tr.addView(getTextView(0, "Qty", Color.WHITE, Typeface.BOLD, ContextCompat.getColor(this, R.color.colorAccent)));
        tr.addView(getTextView(0, "Price", Color.WHITE, Typeface.BOLD, ContextCompat.getColor(this, R.color.colorAccent)));
        tr.addView(getTextView(0, "Amount", Color.WHITE, Typeface.BOLD, ContextCompat.getColor(this, R.color.colorAccent)));
        mTableLayout.addView(tr, getTblLayoutParams());
    }

    public void addHeadersSum(int sum) {
        TableRow tr = new TableRow(this);
        tr.addView(getTextView(0, " ", Color.WHITE, Typeface.BOLD, ContextCompat.getColor(this, R.color.colorAccent)));
        tr.addView(getTextView(0, " ", Color.WHITE, Typeface.BOLD, ContextCompat.getColor(this, R.color.colorAccent)));
        tr.addView(getTextView(0, " ", Color.WHITE, Typeface.BOLD, ContextCompat.getColor(this, R.color.colorAccent)));
        tr.addView(getTextView(0, "Total", Color.WHITE, Typeface.BOLD, ContextCompat.getColor(this, R.color.colorAccent)));
        tr.addView(getTextView(0, " "+sum, Color.WHITE, Typeface.BOLD, ContextCompat.getColor(this, R.color.colorAccent)));
        mTableLayout.addView(tr, getTblLayoutParams());
    }

    private void addData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        System.out.println("order_id "+order_id);
        apiService.showJobItemDetails(APIUrl.KEY,order_id).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                progressDialog.dismiss();
                Log.d(TAG, "response login : " + response.body().getResponse());
                if (Integer.parseInt(response.body().getResponse()) == 101) {
                        if(!response.body().getJobItemFinalList().isEmpty()){
                            ArrayList<FinalListModel> jobItemFinalList = new ArrayList<>();
                            jobItemFinalList = response.body().getJobItemFinalList();
                            int i = 0;
                            int sum = 0;
                            for(FinalListModel data: jobItemFinalList){
                                TableRow tr = new TableRow(JobDetailActivity.this);
                                tr.setLayoutParams(getLayoutParams());
                                tr.addView(getTextView(i+2, data.getJob_id(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(JobDetailActivity.this, R.color.colorWhite)));
                                tr.addView(getTextView(i+4, data.getServicepart_id(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(JobDetailActivity.this, R.color.colorWhite)));
                                tr.addView(getTextView(i+5, data.getJob_estimate(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(JobDetailActivity.this, R.color.colorWhite)));
                                tr.addView(getTextView(i+6, String.valueOf(Integer.parseInt(data.getJob_finalaount())/ Integer.parseInt(data.getJob_estimate())), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(JobDetailActivity.this, R.color.colorWhite)));
                                tr.addView(getTextView(i+7, data.getJob_finalaount(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(JobDetailActivity.this, R.color.colorWhite)));
                                mTableLayout.addView(tr, getTblLayoutParams());
                                i++;
                                sum = sum + Integer.parseInt(data.getJob_finalaount());
                            }
                            addHeadersSum(sum);
                        }else {

                        }
                }else {

                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                errorOut(t);
            }
        });
    }

    private void submitBill(RequestBody key, RequestBody order_id, RequestBody customer_id, RequestBody garage_id, MultipartBody.Part file) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        System.out.println("order_id "+order_id);
        apiService.callSubmitBill(key,garage_id,customer_id,order_id,file).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                progressDialog.dismiss();
                if (Integer.parseInt(response.body().getResponse()) == 101) {
                    Toasty.success(JobDetailActivity.this,"Submitted Bill",Toasty.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toasty.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                System.out.println(TAG +"Response " +t);
                progressDialog.dismiss();
                errorOut(t);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_menu, menu);
        if(getIntent().getBooleanExtra("button",false)){
            menu.findItem(R.id.action_add).setVisible(false);
        }
        return true;
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
            case R.id.action_add:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
