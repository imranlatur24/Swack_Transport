package com.swack.transport.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.swack.transport.R;
import com.swack.transport.activities.AddVehicleActivity;
import com.swack.transport.activities.HistoryActivity;
import com.swack.transport.activities.LoginActivity;
import com.swack.transport.activities.MainActivity;
import com.swack.transport.activities.ProfileActivity;
import com.swack.transport.activities.SupportActivity;


import java.util.StringTokenizer;



public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.MyViewHolder> {

    private MainActivity context;
    public String gar_id,gar_list_id,f,s;
    //private TextToSpeech mTTS;

    StringTokenizer tokens ;
    String data;
    String[] items;
    private String TITLE []= {
            "Request History",
            "Call Center",
            "Add Vehical",
            "Logout"};
    private int IMAGES []={
            R.drawable.ic_info,
            R.drawable.ic_support,
            R.drawable.ic_vehical,
            R.drawable.ic_logout};

    private int COlOR []={
            R.color.colorWhite,
            R.color.colorWhite,
            R.color.colorWhite,
            R.color.colorWhite,
            R.color.colorWhite,
            R.color.colorWhite};

    private Dialog dialog1;

    public GridViewAdapter(MainActivity context, String gar_id) {
        this.context = context;
        this.gar_id = gar_id;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_menu_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        int w = holder.cvCategory.getResources().getDisplayMetrics().widthPixels;
        int h = holder.cvCategory.getResources().getDisplayMetrics().heightPixels;
        holder.images.getLayoutParams().width = w/8;
        holder.images.getLayoutParams().height = w/8;
        holder.ltyCategory.getLayoutParams().height = h/5;

        holder.title.setText(TITLE[position]);
        holder.title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.title.setSelected(true);
        holder.images.setImageResource(IMAGES[position]);
        holder.ltyCategory.setBackgroundColor(context.getResources().getColor(COlOR[position]));
        holder.ltyCategory.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NewApi")
                @Override
            public void onClick(View v) {
                switch (position)
                {
                        case 0:
                            context.startActivity(new Intent(context, HistoryActivity.class));
                        break;
                        case 1:
                            context.startActivity(new Intent(context, SupportActivity.class));
                        break;
                        case 2:
                            context.startActivity(new Intent(context, AddVehicleActivity.class));
                        break;
                        case 3:
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                            alertDialogBuilder.setTitle(context.getString(R.string.app_name));
                            alertDialogBuilder.setIcon(R.drawable.logo);
                            alertDialogBuilder.setMessage(context.getString(R.string.logout_now));
                            alertDialogBuilder.setCancelable(false);

                            alertDialogBuilder.setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

                                @SuppressLint("NewApi")
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    context.prefManager.connectDB();
                                    context.prefManager.setBoolean("isLogin", false);
                                    context.prefManager.closeDB();
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(intent);
                                    context.finish();
                                }
                            });

                            alertDialogBuilder.setNegativeButton(context.getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                            break;
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return IMAGES.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView images;
        public LinearLayout ltyCategory;
        public CardView cvCategory;
        public MyViewHolder(View view) {
            super(view);
            images = (ImageView) view.findViewById(R.id.imageView_image);
            title = (TextView) view.findViewById(R.id.textView_title);
            ltyCategory = (LinearLayout) view.findViewById(R.id.ltyCategory);
            cvCategory = (CardView) view.findViewById(R.id.cvCategory);

        }
    }

}
