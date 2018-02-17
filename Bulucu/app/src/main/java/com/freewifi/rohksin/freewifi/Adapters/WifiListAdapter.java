package com.freewifi.rohksin.freewifi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.Activities.WifiDetailActivity;
import com.freewifi.rohksin.freewifi.WifiUtility;
import com.freewifi.rohksin.freewifi.R;

import java.util.List;

/**
 * Created by Illuminati on 5/5/2017.
 */
public class WifiListAdapter extends RecyclerView.Adapter<WifiListAdapter.WifiViewHolder> {

    private List<ScanResult> wifiList;
    private Context context;

    public WifiListAdapter(Context context,List<ScanResult> wifiList)
    {
        this.context = context;
        this.wifiList = wifiList;
    }

    @Override
    public WifiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wifi_item,parent,false);
        return new WifiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WifiViewHolder holder, int position) {

        final ScanResult wifi = wifiList.get(position);
        holder.wifiName.setText(wifi.SSID);

        holder.connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WifiUtility.connect(context, wifi);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openWifiDetailIntent = new Intent(context, WifiDetailActivity.class);
                WifiUtility.setInspectWifi(wifi);
                context.startActivity(openWifiDetailIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return wifiList.size();
    }

    public class WifiViewHolder extends RecyclerView.ViewHolder{

        private TextView wifiName;
        private Button connect;

        public WifiViewHolder(View view)
        {
            super(view);

            wifiName = (TextView)view.findViewById(R.id.wifiName);
            connect = (Button) view.findViewById(R.id.connect);
        }
    }
}
