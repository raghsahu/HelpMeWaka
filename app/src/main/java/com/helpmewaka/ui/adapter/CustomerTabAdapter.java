package com.helpmewaka.ui.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.helpmewaka.ui.contractor.fragment.ContractorLoginFragment;
import com.helpmewaka.ui.customer.fragment.CustomerLoginFragment;

/**
 * Created by Ravindra Birla on 10/09/2019.
 */
public class CustomerTabAdapter extends FragmentPagerAdapter {
    int totalTabs;
    private Context context;

    public CustomerTabAdapter(FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CustomerLoginFragment customer = new CustomerLoginFragment();
                return customer;
            case 1:
                ContractorLoginFragment contractor = new ContractorLoginFragment();
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
