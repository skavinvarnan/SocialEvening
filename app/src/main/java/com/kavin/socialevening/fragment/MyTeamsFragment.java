package com.kavin.socialevening.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kavin.socialevening.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTeamsFragment extends Fragment {


    public MyTeamsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_teams, container, false);
    }


}
