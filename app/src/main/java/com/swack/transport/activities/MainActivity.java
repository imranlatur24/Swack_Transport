package com.swack.transport.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.swack.transport.R;
import com.swack.transport.adapter.GridViewAdapter;
import com.swack.transport.adapter.SlidingImageAdapter;
import com.swack.transport.data.APIService;
import com.swack.transport.data.APIUrl;
import com.swack.transport.data.GPSTracker;
import com.swack.transport.model.ResponseResult;
import com.viewpagerindicator.CirclePageIndicator;
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView_gridview;
    private GridLayoutManager gridLayoutManager;
    NavigationView navigationView;
    private LinearLayoutManager linearLayoutManager;
    private TextToSpeech mTTS;
    ViewPager viewPager;
    private APIService apiService;
    private CardFlipPageTransformer pageTransformer;
    private CirclePageIndicator indicator;
    MainActivity context;
    private static int currentPage = 0, NUM_PAGES = 4, w, position;
    private Toolbar toolbar;
    private LinearLayout linearFilter;


    //update location
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 1000; /* 2 sec */
    private LocationManager locationManager;
    private String state = "";
    public Geocoder geocoder;
    private ProgressDialog progressDialog;
    public String lati, longi,gar_id,gar_list_id,cus_pic,gar_name,gar_ids;
    public GPSTracker gps;
    public TextView result, latitudeLL, longitudeLL, textView_CustomerName, textView_CustomerEmail;
    public double latitude;
    public double longitude;
    String lat12, lon12;
    private static SwipeRefreshLayout swipeContainer;
    String city, area;
    private List<Address> addressList;
    private GridViewAdapter gridViewAdapter;
    private View headerView;
    private String LoginName,LoginMobileNo;
    private CircleImageView imageView;


    // private HomeAdapter homeAdapter;
    private Menu nav_Menu;
    private TextView txtLoginName,txtLoginMobile;
    private Boolean isLogin;
    String data;
    String[] items;
    private Intent intent;
    private TextView mytextview_name;

    public MainActivity() {

    }

    @SuppressLint("ValidFragment")
    public MainActivity(MainActivity context) {
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(MainActivity.this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        sliderImages();
        requestPermission();
        checkPermission();
        location();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)

                != PackageManager.PERMISSION_GRANTED

                && ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)

                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }


        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        geocoder = new Geocoder(this, Locale.getDefault());

        prefManager.connectDB();
        LoginName = prefManager.getString("transport_name");
        LoginMobileNo = prefManager.getString("transport_mob");
        prefManager.closeDB();

        mytextview_name = findViewById(R.id.mytextview_name);
        mytextview_name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mytextview_name.setText("Welcome "+LoginName+"!");
        mytextview_name.setSelected(true);
        mytextview_name.setSingleLine(true);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);

        apiService = APIUrl.getClient().create(APIService.class);

        prefManager.connectDB();
        isLogin = prefManager.getBoolean("isLogin");
        gar_id = prefManager.getString("gar_id");
        gar_name = prefManager.getString("gar_name");
        cus_pic = prefManager.getString("transport_pic");
        prefManager.closeDB();

        System.out.println("#GAR ID : "+gar_id);

        recyclerView_gridview = (RecyclerView) findViewById(R.id.recyclerView_gridview);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView_gridview.setLayoutManager(gridLayoutManager);
        gridViewAdapter = new GridViewAdapter(MainActivity.this,gar_id);
        recyclerView_gridview.setAdapter(gridViewAdapter);



        nav_Menu = navigationView.getMenu();

        txtLoginName = (TextView) headerView.findViewById(R.id.txtLoginName);
        txtLoginMobile = (TextView) headerView.findViewById(R.id.txtLoginMobile);
        imageView = (CircleImageView) headerView.findViewById(R.id.imageView);
        Picasso.with(MainActivity.this).load(cus_pic).error(R.drawable.pic).into(imageView);

        if (isLogin) {
            txtLoginName.setText(LoginName);
            txtLoginMobile.setText(LoginMobileNo);
            nav_Menu.findItem(R.id.nav_logout).setVisible(true);
            nav_Menu.findItem(R.id.nav_logout).setVisible(false);
        } else {
            txtLoginName.setText(LoginName);
            txtLoginMobile.setText(LoginMobileNo);
            nav_Menu.findItem(R.id.nav_logout).setVisible(false);
            nav_Menu.findItem(R.id.nav_logout).setVisible(true);
        }
        //for slider
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //for slider
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);


        mTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                mTTS.setLanguage(new Locale("en","IN"));
            }
        });
    }

    //slider api
    //sliders for homepage
    private void sliderImages() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        callSignIn().enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                progressDialog.dismiss();
                try {
                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        //Toasty.success(getApplicationContext(), "Slider "+response.body().getSlider_list().size(), Toast.LENGTH_LONG).show();
                        viewPager.setAdapter(new SlidingImageAdapter(MainActivity.this, response.body().getSlider_list()));
                        indicator.setViewPager(viewPager);
                        pageTransformer = new CardFlipPageTransformer();
                        pageTransformer.setFlipOrientation(CardFlipPageTransformer.VERTICAL);
                        pageTransformer.setScalable(true);
                        viewPager.setPageTransformer(true, pageTransformer);
                        setViewPager(response.body().getSlider_list().size());
                        viewPager.setSystemUiVisibility(View.VISIBLE);

                    } else {
                        progressDialog.dismiss();
                        viewPager.setSystemUiVisibility(View.GONE);
                        System.out.println(TAG + " Else Close");
                    }

                } catch (NullPointerException | NumberFormatException e) {
                    viewPager.setSystemUiVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                // dismissProgressDialog(progressDialog1);
                progressDialog.dismiss();
                viewPager.setSystemUiVisibility(View.GONE);
            }
        });
    }

    private Call<ResponseResult> callSignIn() {
        return apiService.callSlider(
                APIUrl.KEY
        );
    }

    private void setViewPager(final int num_pages) {
        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(3 * density);


        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == num_pages) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 7000, 6000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("NewApi")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_changeassword) {
            startActivity(new Intent(MainActivity.this,ChangePasswordActivity.class));
        }else if (id == R.id.nav_vehicle) {
           startActivity(new Intent(MainActivity.this,VehicleActivity.class));
        } else if (id == R.id.nav_history_request) {
           startActivity(new Intent(MainActivity.this,HistoryActivity.class));
        } else if (id == R.id.nav_profile) {
           startActivity(new Intent(MainActivity.this,ProfileActivity.class));
        } else if (id == R.id.nav_callcenter) {
           startActivity(new Intent(MainActivity.this,SupportActivity.class));
        } else if (id == R.id.nav_privacypolicy) {
            startActivity(new Intent(MainActivity.this,PrivacyActivity.class));
        }  else if (id == R.id.nav_logout) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getString(R.string.app_name));
            alertDialogBuilder.setIcon(R.drawable.logo);
            alertDialogBuilder.setMessage(getString(R.string.logout_now));
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    prefManager.connectDB();
                    prefManager.setBoolean("isLogin", false);
                    prefManager.closeDB();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });

            alertDialogBuilder.setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Swack Workshop App\nClick here https://play.google.com/store/apps/details?id=com.swack.workshop to download the app");
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getResources().getString(R.string.app_name));
            alertDialogBuilder.setIcon(R.mipmap.ic_launcher_round);
            alertDialogBuilder.setMessage("Are you sure to exit?");
   /*         mTTS.speak("Are you sure to exit?", TextToSpeech.QUEUE_ADD, null, null);
            mTTS.setPitch((float)0.7);
            mTTS.setSpeechRate((float)0.5);*/
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    MainActivity.super.onBackPressed();
                   /* mTTS.speak("Have a Good Day..", TextToSpeech.QUEUE_ADD, null, null);
                    mTTS.setPitch((float)0.7);
                    mTTS.setSpeechRate((float)0.5);*/
                }
            });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    public void location() {
//        swipeContainer.setRefreshing(false);
        //swipeContainer.setRefreshing(false);
        //for get current employee address
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)

                != PackageManager.PERMISSION_GRANTED

                && ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)

                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

        } else {
            gps = new GPSTracker(MainActivity.this,MainActivity.this);

            // Check if GPS enabled
            if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                lat12 = String.valueOf(gps.getLatitude());
                lon12 = String.valueOf(gps.getLongitude());
                prefManager.connectDB();
                prefManager.setString("latitude",lat12);
                prefManager.setString("longitude",lon12);
                prefManager.closeDB();
                Log.d("#latitude :", String.valueOf(latitude));
                Log.d("#latitude :", String.valueOf(longitude));

                // \n is for new line
                lati = Double.toString(latitude);
                longi = Double.toString(longitude);

                address();

            } else {
                if (area == null && city == null) {
                    //  Toast.makeText(getApplicationContext(),"null value solved..",Toast.LENGTH_LONG).show();
                    if (gps.canGetLocation()) {

                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();

                        // \n is for new line
                        lati = Double.toString(latitude);
                        longi = Double.toString(longitude);


                        latitudeLL.setText(lati);
                        longitudeLL.setText(longi);

                        address();

                    } else {
                        gps.showSettingsAlert();
                    }
                }
            }
        }
    }

    public void address() {

        try {

            addressList = geocoder.getFromLocation(latitude, longitude, 1);

            city = addressList.get(0).getSubAdminArea();
            area = addressList.get(0).getSubLocality();
            //pincode = addressList.get(0).getPostalCode();

            String fullAddress = area + ", " + city + "";


            prefManager.connectDB();
            prefManager.setString("area", area);
            prefManager.setString("city", city);
            //prefManager.setString("pincode", pincode);
            prefManager.setString("lati", lati);
            prefManager.setString("longi", longi);
            prefManager.closeDB();


            System.out.println("Location " + fullAddress);
            //System.out.println("Location " + templistviewNew.isEmpty());



        } catch (IOException e) {
            //Toast.makeText(this, "IOException Null", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        } catch (IndexOutOfBoundsException e) {
            // Toast.makeText(this, "IndexOutOfBoundsException Null", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
                location();
        }
    }

    public boolean checkPermission() {

        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE);
        int result5 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_SMS);
        int result10 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result11 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return result1 == PackageManager.PERMISSION_GRANTED
                && result5 == PackageManager.PERMISSION_GRANTED
                && result10 == PackageManager.PERMISSION_GRANTED
                && result11 == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{CALL_PHONE, READ_SMS,
                WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
    }
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
           // Toast.makeText(this, "Location is detedcting..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        System.out.println("#Updated Location in each second"+msg);
        // You can now create a LatLng Object for use with maps

        prefManager.connectDB();
        prefManager.setString("latitude",Double.toString(location.getLatitude()));
        prefManager.setString("longitude",Double.toString(location.getLongitude()));
        prefManager.closeDB();


        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //Toast.makeText(getApplicationContext(), ""+latLng, Toast.LENGTH_SHORT).show();
        LatLng latlang = latLng;


    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults){
        switch (requestCode){
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
