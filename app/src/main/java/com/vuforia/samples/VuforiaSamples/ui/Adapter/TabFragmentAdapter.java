package com.vuforia.samples.VuforiaSamples.ui.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.vuforia.samples.VuforiaSamples.ui.Fragment.CommentFragment;
import com.vuforia.samples.VuforiaSamples.ui.Fragment.ContentsDitalFragment;

/**
 * Created by aquat on 2017/12/28.
 */

public class TabFragmentAdapter extends FragmentStatePagerAdapter {


    public TabFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                CommentFragment fragment1 = new CommentFragment();
                return fragment1;

            case 1:
                return new ContentsDitalFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }



    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
}
