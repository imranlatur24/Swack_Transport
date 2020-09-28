package com.swack.transport.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.swack.transport.BuildConfig;
import com.swack.transport.R;
import com.swack.transport.data.APIUrl;
import com.swack.transport.data.FileUtil;
import com.swack.transport.model.ResponseResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ProfileActivity";
    private static final int REQUEST_CAMERA = 101;
    private static final int SELECT_FILE = 102;
    private String cus_address,cus_name,cus_email,cus_mob,cus_id,cus_pic,cus_aadhar,cus_pan,cus_log;
    private boolean profile,aadhar,pan,logo;
    private ImageView imageView, add_aadhar, add_pan,add_image,profile_aadhar,profile_pan,profile_logo;
    private EditText profile_name,profile_email,profile_mobile,profile_address;
    private File destination,newFile,profile_file = null,aadhar_file = null,pan_file = null,logo_file = null;
    private CircleImageView profile_image;
    private Button fabProfileEdit;
    private FrameLayout frame,frame2,frame3,frame_logo,frame_pan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("My Profile");

         init();
         onClickListener();
     }

     private void init()
     {
         prefManager.connectDB();
         cus_id = prefManager.getString("transport_id");
         cus_name = prefManager.getString("transport_name");
         cus_address = prefManager.getString("transport_address");
         cus_email = prefManager.getString("transport_email");
         cus_mob = prefManager.getString("transport_mob");
         cus_pic = prefManager.getString("transport_pic");
         cus_aadhar = prefManager.getString("transport_aadhar");
         cus_pan = prefManager.getString("transport_pan");
         cus_log = prefManager.getString("transport_shopact");
         prefManager.closeDB();

         System.out.println("PROFILE CUS PIC PATH"+cus_pic);
         System.out.println("PROFILE ADHAAR PIC PATH"+cus_aadhar);
         System.out.println("PROFILE PAN PIC PATH"+cus_pan);

         frame = findViewById(R.id.frame);
         frame2 = findViewById(R.id.frame2);
         frame_pan = findViewById(R.id.frame_pan);
         frame_logo = findViewById(R.id.frame_logo);
         profile_name = findViewById(R.id.profile_name);
         profile_email = findViewById(R.id.profile_email);
         profile_mobile = findViewById(R.id.profile_mobile);
         profile_address = findViewById(R.id.profile_address);
        imageView = findViewById(R.id.imageView);
        add_aadhar = findViewById(R.id.add_aadhar);
        add_pan = findViewById(R.id.add_pan);
        add_image = findViewById(R.id.add_image);
        profile_image = findViewById(R.id.profile_image);
        profile_aadhar = findViewById(R.id.profile_aadhar);
        profile_pan = findViewById(R.id.profile_pan);
        profile_logo = findViewById(R.id.profile_logo);
        fabProfileEdit = findViewById(R.id.fabProfileEdit);

        profile_name.setText(cus_name);
        profile_email.setText(cus_email);
        profile_address.setText(cus_address);
        profile_mobile.setText(cus_mob);

        Picasso.with(ProfileActivity.this)
                .load(cus_pic)
                .placeholder(R.drawable.pic)
                .error(R.drawable.pic)
                .into(profile_image);
        Picasso.with(ProfileActivity.this)
                .load(cus_aadhar)
                .placeholder(R.drawable.adhar)
                .error(R.drawable.adhar)
                .into(profile_aadhar);
        Picasso.with(ProfileActivity.this)
                .load(cus_pan)
                .placeholder(R.drawable.pan)
                .error(R.drawable.pan)
                .into(profile_pan);
        Picasso.with(ProfileActivity.this)
                .load(cus_log)
                .placeholder(R.drawable.pic)
                .error(R.drawable.pic)
                .into(profile_logo);
    }

    private void onClickListener() {
        imageView.setOnClickListener(this);
        frame.setOnClickListener(this);
        frame_pan.setOnClickListener(this);
        frame2.setOnClickListener(this);
        frame_logo.setOnClickListener(this);
        add_aadhar.setOnClickListener(this);
        add_pan.setOnClickListener(this);
        add_image.setOnClickListener(this);
        fabProfileEdit.setOnClickListener(this);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
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


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabProfileEdit:
                cus_name = profile_name.getText().toString();
                cus_address = profile_address.getText().toString();
                cus_email = profile_email.getText().toString();
                cus_mob = profile_mobile.getText().toString();
                MultipartBody.Part transportprofilepic = null;
                MultipartBody.Part transportdoc_aadhar = null;
                MultipartBody.Part transportdoc_pan = null;
                MultipartBody.Part transportshopact = null;
                if (!isNetworkAvailable()) {
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.error_msg_no_internet), Toasty.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(cus_name)){
                    profile_name.setError("Enter full name");
                    return;
                }if(TextUtils.isEmpty(cus_address)){
                profile_address.setError("Enter full address");
                return;
            }if(TextUtils.isEmpty(cus_email)){
                profile_email.setError("Enter email");
                return;
            }if(TextUtils.isEmpty(cus_mob)){
                profile_mobile.setError("Enter Mobile number");
                return;
            }
                try{
                    profile_file.isFile();
                    RequestBody requestFileProfile =RequestBody.create(MediaType.parse("*/*"), profile_file);
                    transportprofilepic = MultipartBody.Part.createFormData("transport_profile", profile_file.getName(), requestFileProfile);
                }catch (NullPointerException e){
                    cus_pic = cus_pic.replace("http://swack.in/swack/UploadDocs/","");
                }
                try{
                    aadhar_file.isFile();
                    RequestBody requestFileAadhar =RequestBody.create(MediaType.parse("*/*"), aadhar_file);
                    transportdoc_aadhar = MultipartBody.Part.createFormData("transport_aadhar", aadhar_file.getName(), requestFileAadhar);
                }catch (NullPointerException e){
                    cus_aadhar = cus_aadhar.replace("http://swack.in/swack/UploadDocs/","");
                }
                try{
                    pan_file.isFile();
                    RequestBody requestFilePan =RequestBody.create(MediaType.parse("*/*"), pan_file);
                    transportdoc_pan = MultipartBody.Part.createFormData("transport_pan", pan_file.getName(), requestFilePan);
                }catch (NullPointerException e){
                    cus_pan = cus_pan.replace("http://swack.in/swack/UploadDocs/","");
                }
                try{
                    logo_file.isFile();
                    RequestBody requestFile =RequestBody.create(MediaType.parse("*/*"), logo_file);
                    transportshopact = MultipartBody.Part.createFormData("transport_shopact", logo_file.getName(), requestFile);
                }catch (NullPointerException e){
                    cus_log = cus_log.replace("http://swack.in/swack/UploadDocs/","");
                }

                updateProfile(RequestBody.create(MediaType.parse("text/plain"), APIUrl.KEY),
                        RequestBody.create(MediaType.parse("text/plain"),cus_id),
                        RequestBody.create(MediaType.parse("text/plain"),cus_name),
                        RequestBody.create(MediaType.parse("text/plain"),cus_email),
                        RequestBody.create(MediaType.parse("text/plain"),cus_mob),
                        RequestBody.create(MediaType.parse("text/plain"),cus_address),
                        RequestBody.create(MediaType.parse("text/plain"),cus_pic),
                        RequestBody.create(MediaType.parse("text/plain"),cus_aadhar),
                        RequestBody.create(MediaType.parse("text/plain"),cus_pan),
                        RequestBody.create(MediaType.parse("text/plain"),cus_log),
                        transportprofilepic,transportdoc_aadhar,transportdoc_pan,transportshopact);


                break;
            case R.id.frame2:
                profile = false;
                aadhar = true;
                pan = false;
                logo = false;
                selectImage();
                break;
            case R.id.frame_pan:
                profile = false;
                aadhar = false;
                pan = true;
                logo = false;
                selectImage();
                break;
            case R.id.frame_logo:
                profile = false;
                aadhar = false;
                pan = false;
                logo = true;
                selectImage();
                break;
            case R.id.frame:
                profile = true;
                aadhar = false;
                pan = false;
                logo = false;
                selectImage();
                break;
        }
    }

    private void updateProfile(RequestBody key, RequestBody transport_id, RequestBody transport_name, RequestBody transport_email, RequestBody transport_mobi, RequestBody transport_address, RequestBody profilePhoto, RequestBody aadharPhoto, RequestBody panPhoto, RequestBody shopactPhoto, MultipartBody.Part profile_file, MultipartBody.Part aadhar_file, MultipartBody.Part pan_file, MultipartBody.Part logo_file) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();
        apiService.updateProfile(key,transport_id,transport_name,transport_email,transport_mobi,transport_address,profilePhoto,aadharPhoto,panPhoto,shopactPhoto, profile_file,aadhar_file,pan_file,logo_file).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                progressDialog.dismiss();
                Log.d(TAG, "response login : " + response.body().getResponse());
                if (Integer.parseInt(response.body().getResponse()) == 101) {

                    Toasty.success(getApplicationContext(),"Profile updated", Toast.LENGTH_SHORT).show();
                    prefManager.connectDB();
                    prefManager.setBoolean("isLogin", true);
                    prefManager.setString("transport_id", response.body().getTraProfileList().get(0).getTransport_id());
                    prefManager.setString("transport_name", response.body().getTraProfileList().get(0).getTransport_name());
                    prefManager.setString("transport_address", response.body().getTraProfileList().get(0).getTransport_address());
                    prefManager.setString("transport_email", response.body().getTraProfileList().get(0).getTransport_email());
                    prefManager.setString("transport_mob", response.body().getTraProfileList().get(0).getTransport_mob());
                    prefManager.setString("transport_pic", response.body().getTraProfileList().get(0).getTransport_profile());
                    prefManager.setString("transport_aadhar", response.body().getTraProfileList().get(0).getTransport_aadhar());
                    prefManager.setString("transport_pan", response.body().getTraProfileList().get(0).getTransport_pan());
                    prefManager.setString("transport_shopact", response.body().getTraProfileList().get(0).getTransport_shopact());
                    prefManager.closeDB();
                    Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                    //intent.putExtra("transport_name","transport_name");
                    startActivity(intent);
                    finish();

                } else if (Integer.parseInt(response.body().getResponse()) == 102) {
                    progressDialog.dismiss();
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.account_block), Toast.LENGTH_LONG).show();
                }  else {
                    progressDialog.dismiss();
                    System.out.println(TAG + " Else Close");
                    Toasty.error(getApplicationContext(), "Profile updated failed", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
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
                        destination = FileUtil.from(ProfileActivity.this,data.getData());
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

                if(profile){
                    Picasso.with(ProfileActivity.this)
                            .load(destination)
                            .placeholder(R.drawable.pic)
                            .error(R.drawable.pic)
                            .into(profile_image);
                    profile_file = newFile;
                }if(pan){
                    Picasso.with(ProfileActivity.this)
                            .load(destination)
                            .placeholder(R.drawable.pan)
                            .error(R.drawable.pan)
                            .into(profile_pan);
                    pan_file = newFile;
                }if(aadhar){
                    Picasso.with(ProfileActivity.this)
                            .load(destination)
                            .placeholder(R.drawable.adhar)
                            .error(R.drawable.adhar)
                            .into(profile_aadhar);
                    aadhar_file = newFile;
                }if(logo){
                    Picasso.with(ProfileActivity.this)
                            .load(destination)
                            .placeholder(R.drawable.pic)
                            .error(R.drawable.pic)
                            .into(profile_logo);
                    logo_file = newFile;
                }
            }
        }
    }
}
