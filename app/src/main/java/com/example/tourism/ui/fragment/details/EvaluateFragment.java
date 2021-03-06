package com.example.tourism.ui.fragment.details;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.tourism.R;
import com.example.tourism.widget.ChildAutoViewPager;
import com.example.tourism.widget.ViewBundle;

/**
 * 预定须知
 */
public class EvaluateFragment extends Fragment {
    private static final String KEY = "viewBundle";
    private ChildAutoViewPager viewPager;

    public static EvaluateFragment newInstance(ViewBundle viewBundle) {
        Bundle args = new Bundle();
        args.putParcelable(KEY, viewBundle);
        EvaluateFragment fragment = new EvaluateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果getArguments 为空，则不执行
        if (getArguments() == null) return;
        ViewBundle viewBundle = getArguments().getParcelable(KEY);
        viewPager = viewBundle != null ? viewBundle.getViewPager() : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evaluate, container, false);
        if (viewPager != null) viewPager.setObjectForPosition(2, view);
        return view;
    }

}
