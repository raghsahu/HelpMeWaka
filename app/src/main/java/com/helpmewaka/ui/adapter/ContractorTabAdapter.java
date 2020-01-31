package com.helpmewaka.ui.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.helpmewaka.ui.contractor.fragment.ContractorSignupFragment;
import com.helpmewaka.ui.customer.fragment.CustomerSignupFragment;

/**
 * Created by Ravindra Birla on 10/09/2019.
 */
public class ContractorTabAdapter extends FragmentPagerAdapter {
    int totalTabs;
    private Context context;

    public ContractorTabAdapter(FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CustomerSignupFragment customer = new CustomerSignupFragment();
                return customer;
            case 1:
                ContractorSignupFragment contractor = new ContractorSignupFragment();
                return contractor;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
