package com.example.homework_1022_windows_calc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Holder> {

    Context context;
    List<HistoryData> list;

    public HistoryAdapter(Context context, List<HistoryData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        int itemposition = i;
        holder.formulaText.setText(list.get(itemposition).formula);
        holder.resultText.setText(list.get(itemposition).result);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public TextView formulaText;
        public TextView resultText;

        public Holder(@NonNull View itemView) {
            super(itemView);
            formulaText = itemView.findViewById(R.id.text_formula);
            resultText = itemView.findViewById(R.id.text_result);
        }
    }
}
