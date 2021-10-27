package com.vyndsolutions.vyndteam.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vyndsolutions.vyndteam.R;



    /**
     * A placeholder fragment containing a simple view.
     */
    public  class BusinessFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public BusinessFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static BusinessFragment newInstance(int sectionNumber) {


            BusinessFragment fragment = new BusinessFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER , sectionNumber);
        fragment.setArguments(args);
        return fragment ;
        }

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_business, container, false);
        return rootView;
        }
        }

