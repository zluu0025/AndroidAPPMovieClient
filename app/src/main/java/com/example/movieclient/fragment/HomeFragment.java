package com.example.movieclient.fragment;

import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.movieclient.R;
import com.example.movieclient.activity.ChartActivity;
import com.example.movieclient.activity.MapsActivity;
import com.example.movieclient.activity.MovieInfoActivity;
import com.example.movieclient.activity.SearchActivity;
import com.example.movieclient.activity.WatchActivity;
import com.example.movieclient.utils.SP;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private View view;
    private FragmentActivity context;
    private TextView tvName;
    private String userid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        String name = SP.getInstance(getActivity()).getString("NAME");
        tvName.setText(name);
    }

    private void initView() {
        tvName = view.findViewById(R.id.tv_name);
        View ll_search = view.findViewById(R.id.llSearch);
        View ll_watch = view.findViewById(R.id.llWatch);
        View ll_report = view.findViewById(R.id.llChart);
        View movie_memoir = view.findViewById(R.id.m_memoir);
        View maps = view.findViewById(R.id.nv_map);

        ll_search.setOnClickListener(this);
        ll_watch.setOnClickListener(this);
        ll_report.setOnClickListener(this);
        movie_memoir.setOnClickListener(this);
        maps.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null ;
        switch (v.getId()){

            case R.id.llSearch:
                intent=    new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);

                break;
            case R.id.llWatch:
                startActivity(new Intent(getActivity(), WatchActivity.class));

                break;

            case R.id.llChart:

            case R.id.nv_map:
                intent =    new Intent(getActivity(), ChartActivity.class);
                startActivity(intent);
                break;

//            case R.id.m_memoir:
//                intent = new Intent(getActivity(), MovieInfoActivity.class);
//                startActivity(intent);
//                break;
        }
    }
}
