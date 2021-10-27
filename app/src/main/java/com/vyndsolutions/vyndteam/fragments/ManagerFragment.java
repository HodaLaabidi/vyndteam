package com.vyndsolutions.vyndteam.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vyndsolutions.vyndteam.R;

import java.util.List;


public  class ManagerFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    View rootView;
    RecyclerView list;
    FloatingActionButton fab;
    Context context;
    List<String> string_list;

    public ManagerFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ManagerFragment newInstance(int sectionNumber) {
        ManagerFragment fragment = new ManagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_manager, container, false);

        return rootView;
    }
}