package com.swack.transport.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swack.transport.R;
import com.swack.transport.activities.HistoryActivity;
import com.swack.transport.adapter.TransportAdapter;
import com.swack.transport.data.APIService;
import com.swack.transport.data.APIUrl;
import com.swack.transport.model.ResponseResult;
import com.swack.transport.model.TransportList;

import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.swack.transport.activities.BaseActivity.prefManager;

public class NewOrderFragment extends Fragment {
    private HistoryActivity historyActivity;
    private RecyclerView frag_recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout error_layout;
    private Button btnRetry;
    private ProgressBar progressBar;
    private LinearLayout errorLayout;
    private TextView txtError;
    private TransportAdapter homeAdapter;
    private String cus_id,cus_name;
     private APIService apiService;
     private String transid;
     public NewOrderFragment () {
     }

     @SuppressLint("ValidFragment")
     public NewOrderFragment(HistoryActivity historyActivity,String transid) {
         this.historyActivity = historyActivity;
         this.transid = transid;
     }

     @Override
     public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater. inflate(R.layout.list_fragment, container, false);

         apiService = APIUrl.getClient().create(APIService.class);

         prefManager.connectDB();
         cus_name = prefManager.getString("transport_name");
         prefManager.closeDB();

         progressBar = (ProgressBar) view.findViewById(R.id.main_progress);
         errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
         btnRetry = (Button) view.findViewById(R.id.error_btn_retry);
         txtError = (TextView) view.findViewById(R.id.error_txt_cause);
         frag_recyclerView = view.findViewById(R.id.frag_recyclerView);
        mLayoutManager = new LinearLayoutManager(historyActivity, RecyclerView.VERTICAL,false);
        frag_recyclerView.setLayoutManager(mLayoutManager);

        finalbill();

        return view;
    }


    private void finalbill() {
//        swipeContainer.setRefreshing(false);
        progressBar.setVisibility(View.VISIBLE);
        frag_recyclerView.setVisibility(View.GONE);

        callCategory().enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                progressBar.setVisibility(View.GONE);
                try {
                    System.out.println("Transport Response : "+response.body().getResponse());
                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        if(!response.body().getTransport_detail_list().isEmpty()) {
                            errorLayout.setVisibility(View.GONE);
                            frag_recyclerView.setVisibility(View.VISIBLE);
                            homeAdapter = new TransportAdapter(historyActivity,transid);
                            frag_recyclerView.setAdapter(homeAdapter);
                            homeAdapter.setListArray(response.body().getTransport_detail_list());
                        }else {
                            showErrorView("Hi "+ cus_name+"\n"+getResources().getString(R.string.create_new));
                        }
                    } else if (Integer.parseInt(response.body().getResponse()) == 102) {
                        showErrorView("Hi "+ cus_name+"\n"+getResources().getString(R.string.create_new));
                    }else {
                        showErrorView("Hi "+ cus_name+"\n"+getResources().getString(R.string.create_new));
                    }
                } catch (NullPointerException | NumberFormatException e) {
                    showErrorView("Hi "+ cus_name+"\n"+getResources().getString(R.string.create_new));
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showErrorView(t);
            }
        });
    }

    //categories
    private Call<ResponseResult> callCategory() {
        prefManager.connectDB();
        String latitude = prefManager.getString("latitude");
        String longitude = prefManager.getString("longitude");
        prefManager.closeDB();
        System.out.println("New Order list API KEY " + APIUrl.KEY+" trans id : "+transid+"latitude " +latitude+" longitude "+longitude);
        return apiService.transportList(
                APIUrl.KEY,
                transid,
                latitude,
                longitude
        );
    }

    private void showErrorView(Throwable throwable) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    private void showErrorView(String error) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            txtError.setText(error);
        }
    }

    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = "Hi "+ cus_name+"\n"+getResources().getString(R.string.create_new);

        if (!historyActivity.isNetworkAvailable()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = "Hi "+ cus_name+"\n"+getResources().getString(R.string.create_new);
        }

        return errorMsg;
    }

}

