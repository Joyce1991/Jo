package com.jalen.jo.fragments;

import android.support.v4.app.Fragment;

/**
 * Created by jh on 2015/3/27.
 */
public class BaseFragment extends Fragment {
    public String tag;

    public BaseFragment(){
        super();
        tag = this.getClass().getSimpleName();
    }


}

