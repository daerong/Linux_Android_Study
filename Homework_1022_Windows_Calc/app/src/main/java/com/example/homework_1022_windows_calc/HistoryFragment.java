package com.example.homework_1022_windows_calc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<HistoryData> list = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View linearLayout = inflater.inflate(R.layout.fragment_history, container,false);

        recyclerView = linearLayout.findViewById( R.id.recycler_view );
        recyclerView.setHasFixedSize(true);
        
        adapter = new HistoryAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return linearLayout;
    }

    public void addHistory(String formulaStr, String resultStr){
        list.add(0, new HistoryData(formulaStr, resultStr));
//        adapter.notifyItemInserted(0);

        adapter = new HistoryAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }
}
