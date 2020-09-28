package com.swack.transport.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;
import com.swack.transport.R;
import com.swack.transport.data.APIUrl;
import com.swack.transport.model.SliderList;

import java.util.ArrayList;

public class SlidingImageAdapter extends PagerAdapter {

    private ArrayList<SliderList> sliderLists;
    private LayoutInflater inflater;
    private Context context;

    public SlidingImageAdapter(Context context, ArrayList<SliderList> sliderLists) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.sliderLists = sliderLists;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return sliderLists.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
        final TextView txtTitle = (TextView) imageLayout.findViewById(R.id.txtTitle);
        final TextView txtMobile = (TextView) imageLayout.findViewById(R.id.txtMobile);

        Picasso.with(context).load(sliderLists.get(position).getSliderimg_image()).error(R.mipmap.ic_launcher).into(imageView);
        txtTitle.setVisibility(View.GONE);
        txtMobile.setVisibility(View.GONE);
        view.addView(imageLayout, 0);
        System.out.println("pic : "+sliderLists.get(position).getSliderimg_image());

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}