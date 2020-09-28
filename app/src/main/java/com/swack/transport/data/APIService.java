package com.swack.transport.data;


import com.swack.transport.model.CityListModel;
import com.swack.transport.model.DistrictModel;
import com.swack.transport.model.MyVehicleList;
import com.swack.transport.model.MyVehicleListDetails;
import com.swack.transport.model.ResponseResult;
import com.swack.transport.model.TalukaListModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService
{
    @Multipart
    @POST("vehicle_details.php")
    Call<ResponseResult> callAddVehicle(
            @Part("key") RequestBody key,
            @Part("transport_id") RequestBody cus_id,
            @Part("vehicle_id") RequestBody vehicle_id,
            @Part("vehicle_cat_id") RequestBody vehicle_cat_id,
            @Part("loadrang_id") RequestBody loadrang_id,
            @Part("vehicle_no_tyres") RequestBody vehicle_no_tyres,
            @Part("vehicle_mody_id") RequestBody vehicle_mody_id,
            @Part("vehicled_regno") RequestBody vehicled_regno,
            @Part("vehicled_kmrd") RequestBody vehicled_kmrd,
            @Part("vehicled_ins_expdate") RequestBody vehicled_ins_expdate,
            @Part("vehicle_fc_expdate") RequestBody vehicle_fc_expdate,
            @Part("CusVehiclePhoto") RequestBody doc_vehicle,
            @Part("doc_insPhoto") RequestBody doc_ins,
            @Part("doc_rcPhoto") RequestBody doc_rc,
            @Part MultipartBody.Part vehicle,
            @Part MultipartBody.Part doc_insurance,
            @Part MultipartBody.Part rc);

    @FormUrlEncoded
    @POST("MyVehicle.php")
    Call<MyVehicleListDetails> getVehical(
            @Field("key") String key,
            @Field("transport_id") String cus_id,
            @Field("vehicle_id") String vehicle_id
    );

    @FormUrlEncoded
    @POST("SelectLoad.php")
    Call<ResponseResult> callLoadLists(
            @Field("key") String key);
    @FormUrlEncoded
    @POST("SelectVehicleModalyear.php")
    Call<ResponseResult> callMolarYearLists(
            @Field("key") String key);
    @FormUrlEncoded
    @POST("Selecttyers.php")
    Call<ResponseResult> callTyersLists(
            @Field("key") String key);
    @FormUrlEncoded
    @POST("SelectVehicleCategory.php")
    Call<ResponseResult> callCatTypeLists(
            @Field("key") String key,
            @Field("vehicle_id") String vehicleId);
    @FormUrlEncoded
    @POST("SelectVehicleDetails.php")
    Call<MyVehicleList> callVehicleLists(
            @Field("key") String key,
            @Field("transport_id")String cus_id);
    @FormUrlEncoded
    @POST("SelectVehicleDetails.php")
    Call<ResponseResult> callVehicleListsBreak(
            @Field("key") String key,
            @Field("transport_id")String cus_id);

    @FormUrlEncoded
    @POST("SelectVehicleType.php")
    Call<ResponseResult> callVehicleLists(
            @Field("key") String key);
    @FormUrlEncoded
    @POST("transprocess.php")
    Call<ResponseResult> transprocess(
            @Field("key") String key,
            @Field("trnreqcusid") String mobile_no);

    @FormUrlEncoded
    @POST("breakdownprocess.php")
    Call<ResponseResult> breakdownprocess(
            @Field("key") String key,
            @Field("cus_id") String mobile_no);

    //The Submit Order call
    @Multipart
    @POST("submitOrder.php")
    Call<ResponseResult> callSubmitBill(
            @Part("key") RequestBody key,
            @Part("cusage_id") RequestBody garage_id,
            @Part("cus_id") RequestBody customer_id,
            @Part("ordreq_id") RequestBody order_id,
            @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("showjobitem.php")
    Call<ResponseResult> showJobItemDetails(
            @Field("key") String key,
            @Field("job_ord_id") String job_ord_id
    );

    @FormUrlEncoded
    @POST("pendingbreack.php")
    Call<ResponseResult> pending(
            @Field("key") String key,
            @Field("cus_id") String mobile_no);

    @FormUrlEncoded
    @POST("pendingtrans.php")
    Call<ResponseResult> pendingTrans(
            @Field("key") String key,
            @Field("trnreqcusid") String mobile_no);

    //The all State call
    @FormUrlEncoded
    @POST("state.php")
    Call<DistrictModel> getState(@Field("key") String key);

    //The city call
    @FormUrlEncoded
    @POST("city.php")
    Call<CityListModel> getCity(@Field("key") String key,
                                @Field("state_id") String state_id);

    //The taluka call
    @FormUrlEncoded
    @POST("taluka.php")
    Call<TalukaListModel> getTaluka(@Field("key") String key,
                                    @Field("city_id") String city_id);


    @FormUrlEncoded
    @POST("otp_verification.php")
    Call<ResponseResult> otpVerificationPassword(
            @Field("key") String key,
            @Field("transport_id") String id,
            @Field("transport_mob") String mobile,
            @Field("new_password") String password,
            @Field("OTP") String otp);


    //The forgot password call
    @FormUrlEncoded
    @POST("forgot_pass.php")
    Call<ResponseResult> forgotPassword(
            @Field("key") String key,
            @Field("transport_mob") String mobile_no);

    //version update
    @FormUrlEncoded
    @POST("version_update.php")
    Call<ResponseResult> card_Versioncode(
            @Field("key") String key,
            @Field("version_code") String version_code
    );

    //show slider
    @FormUrlEncoded
    @POST("show_slider.php")
    Call<ResponseResult> callSlider(
            @Field("key") String key);

    @FormUrlEncoded
    @POST("MobileVerify.php")
    Call<ResponseResult> mobileVerification(
            @Field("key") String key,
            @Field("transport_mob") String gar_mobi);

    @FormUrlEncoded
    @POST("TransReg.php")
    Call<ResponseResult> callRegister(
            @Field("key") String key,
            @Field("transport_name") String transport_name,
            @Field("transport_email") String transport_email,
            @Field("transport_mob") String transport_mobi,
            @Field("transport_password") String transport_password,
            @Field("transport_address") String cus_address,
            @Field("transport_lat") String transport_lat,
            @Field("transport_long") String transport_long,
            @Field("districtid") String districtid,
            @Field("talukaid") String talukaid,
            @Field("stateid") String stateid
    );

    //The login call
    @FormUrlEncoded
    @POST("Login.php")
    Call<ResponseResult> callLoginApi(
            @Field("key") String key,
            @Field("fcmcode") String fcmcode,
            @Field("transport_mob") String transport_mobi,
            @Field("transport_password") String transport_password);


    //support
    @FormUrlEncoded
    @POST("support.php")
    Call<ResponseResult> getSupport(
            @Field("key") String key);
    
    //The change password call
    @FormUrlEncoded
    @POST("ChangePass.php")
    Call<ResponseResult> callChangePassword(
            @Field("key") String key,
            @Field("transport_id") String transport_id,
            @Field("transport_password") String transport_password);

    //The new order call
    @FormUrlEncoded
    @POST("neworder.php")
    Call<ResponseResult> transportList(
            @Field("key") String key,
            @Field("transport_id") String transport_id,
            @Field("from_lat") String from_lat,
            @Field("from_long") String from_long);

    //The on process  call
    @FormUrlEncoded
    @POST("onprocess.php")
    Call<ResponseResult> transportListOngoing(
            @Field("key") String key,
            @Field("transport_id") String transport_id,
            @Field("from_lat") String from_lat,
            @Field("from_long") String from_long);


    //The complete list call
    @FormUrlEncoded
    @POST("complete.php")
    Call<ResponseResult> transportListCompleted(
            @Field("key") String key,
            @Field("transport_id") String transport_id,
            @Field("from_lat") String from_lat,
            @Field("from_long") String from_long);

    //The confirm order  call
    @FormUrlEncoded
    @POST("confirmorder.php")
    Call<ResponseResult>  transportConfirm(
            @Field("key") String key,
            @Field("transport_id") String transport_id,
            @Field("trnreqid") String trnreqid,
            @Field("cus_id") String cus_id
            );

    //The confirm order  call
    @Multipart
    @POST("submitbill.php")
    Call<ResponseResult> transportCompleted(
            @Part("key") RequestBody key,
            @Part("transport_id") RequestBody transport_id,
            @Part("cus_id") RequestBody cus_id,
            @Part("trareqid") RequestBody trareqid,
            @Part MultipartBody.Part doc_rc);

    //The update profile call
    @Multipart
    @POST("TraProfile.php")
    Call<ResponseResult> updateProfile(
            @Part("key") RequestBody key,
            @Part("transport_id") RequestBody transport_id,
            @Part("transport_name") RequestBody transport_name,
            @Part("transport_email") RequestBody transport_email,
            @Part("transport_mob") RequestBody transport_mobi,
            @Part("transport_address") RequestBody transport_address,
            @Part("ProfilePhoto") RequestBody profilePhoto,
            @Part("AadharPhoto") RequestBody aadharPhoto,
            @Part("PanPhoto") RequestBody panPhoto,
            @Part("ShopactPhoto") RequestBody shopactPhoto,
            @Part MultipartBody.Part profile_file,
            @Part MultipartBody.Part aadhar_file,
            @Part MultipartBody.Part pan_file,
            @Part MultipartBody.Part logo_file);
}
