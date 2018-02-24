package com.freewifi.rohksin.freewifi.Adapters;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.R;

import java.security.PublicKey;
import java.util.List;

/**
 * Created by Illuminati on 2/24/2018.
 */

public class CloseWifiListAdapter extends RecyclerView.Adapter<CloseWifiListAdapter.CloseWifiViewHolder> {


    private Context context;
    private List<ScanResult> scanResults;

    public CloseWifiListAdapter(Context context, List<ScanResult> scanResults)
    {
        this.context = context;
        this.scanResults = scanResults;
    }


    @Override
    public CloseWifiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.wifi_item, parent, false);
        return new CloseWifiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CloseWifiViewHolder holder, int position) {

        ScanResult scanResult = scanResults.get(position);
        holder.wifiName.setText(scanResult.SSID);
    }

    @Override
    public int getItemCount() {
        return scanResults.size();
    }

    public class CloseWifiViewHolder extends RecyclerView.ViewHolder{


        private TextView wifiName;

        public CloseWifiViewHolder(View itemView) {
            super(itemView);
            wifiName = (TextView)itemView.findViewById(R.id.wifiName);
        }
    }
}
