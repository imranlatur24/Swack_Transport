package com.swack.transport.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.swack.transport.activities.HistoryActivity;
import com.swack.transport.fragment.CompletedOrderFragment;
import com.swack.transport.fragment.NewOrderFragment;
import com.swack.transport.fragment.OngoingOrderFragment;


public class ViewPagerOrderAdapter extends FragmentStatePagerAdapter {
    private HistoryActivity historyActivity;
    private int mNumOfTabs;
    private String transid;

    public ViewPagerOrderAdapter(FragmentManager fm, int NumOfTabs, HistoryActivity historyActivity,String transid)
    {
        super(fm);
        this.historyActivity = historyActivity;
        this.mNumOfTabs = NumOfTabs;
        this.transid = transid;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                NewOrderFragment tab3 = new NewOrderFragment(historyActivity,transid);
                return tab3;

            case 1:
                OngoingOrderFragment tab1 = new OngoingOrderFragment(historyActivity,transid);
                return tab1;

            case 2:
                CompletedOrderFragment tab2 = new CompletedOrderFragment(historyActivity,transid);
                return tab2;

            default:
                return null;
        }
    }


    @Override
    public int getCount()
    {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Breakdown";
            case 1:
                return "Transport";
            default:
                return null;
        }
    }
}
