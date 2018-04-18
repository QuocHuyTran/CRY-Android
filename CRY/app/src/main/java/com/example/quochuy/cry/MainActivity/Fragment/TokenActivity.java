package com.example.quochuy.cry.MainActivity.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quochuy.cry.R;

/**
 * Created by Quoc Huy on 4/3/2018.
 */

public class TokenActivity extends Fragment {

    View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_token,container,false);
        return rootView;
    }
}
