package com.freewifi.rohksin.freewifi.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.Models.WifiResult;
import com.freewifi.rohksin.freewifi.R;

import java.util.List;

public class NotifyMeListAdapter extends RecyclerView.Adapter<NotifyMeListAdapter.NotifyMeItemViewHolder> {


    private List<WifiResult> wifiResults;
    private Context context;

    public NotifyMeListAdapter(Context context, List<WifiResult> wifiResults)
    {
        this.context = context;
        this.wifiResults = wifiResults;
    }

    @NonNull
    @Override
    public NotifyMeItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_simplestring_layout, viewGroup, false);
        return new NotifyMeItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyMeItemViewHolder notifyMeItemViewHolder, int i) {

        WifiResult wifiResult = wifiResults.get(i);
        notifyMeItemViewHolder.wifiDetail.setText(wifiResult.toString());

    }

    @Override
    public int getItemCount() {
        return wifiResults.size();
    }

    public class NotifyMeItemViewHolder extends RecyclerView.ViewHolder
    {

        public TextView wifiDetail;

        public NotifyMeItemViewHolder(@NonNull View itemView) {
            super(itemView);
            wifiDetail = itemView.findViewById(R.id.string);
        }
    }

}
