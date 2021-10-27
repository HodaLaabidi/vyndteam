package com.vyndsolutions.vyndteam.adapters;

/**
 * Created by Hoda on 10/03/2018.
 */


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vyndsolutions.vyndteam.fragments.BusinessFragment;
import com.vyndsolutions.vyndteam.fragments.ContractFragment;
import com.vyndsolutions.vyndteam.fragments.ManagerFragment;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {

    public MainViewPagerAdapter(FragmentManager fm ) {
        super(fm);

    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a fragment .
        int realPosition = position;
        if ( realPosition == position+1) {
            return  BusinessFragment.newInstance(realPosition);
        } else if( realPosition == 2){
            return ManagerFragment.newInstance(realPosition);
        }else if ( realPosition == 3){
            return ContractFragment.newInstance(realPosition);
        }else {
            return ContractFragment.newInstance(realPosition);
//            return MenuFragment.newInstance(realPosition);
        }
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return 4;
    }





}
