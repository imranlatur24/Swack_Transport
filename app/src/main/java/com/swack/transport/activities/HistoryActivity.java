package com.swack.transport.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.swack.transport.BuildConfig;
import com.swack.transport.R;
import com.swack.transport.adapter.CompleteTransportAdapter;
import com.swack.transport.adapter.ProcessTransportAdapter;
import com.swack.transport.adapter.TransportAdapter;
import com.swack.transport.adapter.ViewPagerOrderAdapter;
import com.swack.transport.data.APIUrl;
import com.swack.transport.data.FileUtil;
import com.swack.transport.model.ResponseResult;
import com.swack.transport.model.TransportList;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends BaseActivity implements
        TransportAdapter.CallbackInterface,
        ProcessTransportAdapter.CallbackInterface,
        CompleteTransportAdapter.CallbackInterface {

    private static final int REQUEST_CAMERA = 101;
    private TransportList transportList;
    private File destination,newFile;
    private ViewPager viewPagerCart;
    private ViewPagerOrderAdapter adapterCart;
    private TabLayout tabLayoutCart;
    private String transid;
    private static SwipeRefreshLayout swipeContainer;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setTitle("Request History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefManager.connectDB();
        transid = prefManager.getString("transport_id");
        prefManager.closeDB();

        tabLayoutCart = (TabLayout) findViewById(R.id.tabLayoutCart);
        tabLayoutCart.addTab(tabLayoutCart.newTab().setText("New\nRequest"));
        tabLayoutCart.addTab(tabLayoutCart.newTab().setText("Ongoing Request"));
        tabLayoutCart.addTab(tabLayoutCart.newTab().setText("Completed Request"));
        tabLayoutCart.setTabMode(TabLayout.GRAVITY_CENTER);
        viewPagerCart = (ViewPager) findViewById(R.id.viewPagerCart);
        viewPagerCart.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutCart));
        adapterCart = new ViewPagerOrderAdapter(getSupportFragmentManager(), tabLayoutCart.getTabCount(),
                HistoryActivity.this,transid);
        viewPagerCart.setAdapter(adapterCart);
        tabLayoutCart.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerCart.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeMainActivity);
        swipeContainer.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (!isNetworkAvailable()) {
                    swipeContainer.setRefreshing(false);
                    Toasty.error(HistoryActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    swipeContainer.setRefreshing(false);

                    adapterCart = new ViewPagerOrderAdapter(getSupportFragmentManager(), tabLayoutCart.getTabCount(),
                            HistoryActivity.this,transid);
                    viewPagerCart.setAdapter(adapterCart);
                }
            }
        });

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

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_dashboard, menu);
        ImageView locButton = (ImageView) menu.findItem(R.id.action_refresh).getActionView();
        if (locButton != null) {
            locButton.setImageResource(R.drawable.ic_refresh);
            locButton.setPadding(8,8,8,8);
            // need some resize
            locButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation rotation = AnimationUtils.loadAnimation(HistoryActivity.this, R.anim.rotate);
                    view.startAnimation(rotation);
                    // create and use new data set
                    adapterCart = new ViewPagerOrderAdapter(getSupportFragmentManager(), tabLayoutCart.getTabCount(),
                            HistoryActivity.this,transid);
                    viewPagerCart.setAdapter(adapterCart);
                }
            });
        }
        return true;
    }
*/
    private void completeOrder(final TransportList mOrder) {
//        swipeContainer.setRefreshing(false);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();


        RequestBody requestFile =RequestBody.create(MediaType.parse("*/*"), newFile);
        //Toast.makeText(this, "tra", Toast.LENGTH_SHORT).show();
        MultipartBody.Part doc_rc = MultipartBody.Part.createFormData("bill_selfi_img", newFile.getName(), requestFile);
        apiService.transportCompleted(RequestBody.create(MediaType.parse("text/plain"), APIUrl.KEY),
                RequestBody.create(MediaType.parse("text/plain"),transid),
                RequestBody.create(MediaType.parse("text/plain"),mOrder.getTrnreqcusid()),
                RequestBody.create(MediaType.parse("text/plain"),mOrder.getTrnreqid()),
                doc_rc
        ).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                System.out.println("Transport Confirm response "+response.body().getResponse());
                progressDialog.dismiss();
                if (Integer.parseInt(response.body().getResponse()) == 101) {
                    Toasty.success(HistoryActivity.this, "Transport Order Completed", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(new Intent(HistoryActivity.this,HistoryActivity.class));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else if (Integer.parseInt(response.body().getResponse()) == 102) {
                    Toasty.error(HistoryActivity.this, getResources().getString(R.string.account_block), Toast.LENGTH_LONG).show();
                }  else {
                    Toasty.error(HistoryActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                System.out.println("Transport Confirm Throwable "+t);
                errorOut(t);
            }
        });
    }

    @Override
    public void onHandleSelection() {
        startActivity(new Intent(HistoryActivity.this,HistoryActivity.class));
    }

    @Override
    public void onHandleSelection(TransportList transportList) {
        this.transportList = transportList;
        cameraIntent();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            //Toasty.error(this, "Image pick cancel", Toasty.LENGTH_SHORT).show();
            return;
        } else {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CAMERA) {
                    try {
                        newFile = new Compressor(this)
                                .setMaxWidth(640)
                                .setMaxHeight(480)
                                .setQuality(50)
                                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                                .compressToFile(destination);

                        completeOrder(transportList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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