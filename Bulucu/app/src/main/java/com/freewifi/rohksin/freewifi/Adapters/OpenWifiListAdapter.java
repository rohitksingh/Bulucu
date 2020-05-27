package com.freewifi.rohksin.freewifi.Adapters;

import android.content.Context;
import android.net.wifi.ScanResult;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Utilities.AppUtility;
import com.freewifi.rohksin.freewifi.Utilities.WifiUtility;

import java.util.List;

/**
 * Created by RohitKsingh on 2/22/2018.
 */

public class OpenWifiListAdapter extends RecyclerView.Adapter<OpenWifiListAdapter.OpenWifiViewHolder> {

    private Context context;
    private List<ScanResult> list;

    public OpenWifiListAdapter(Context context, List<ScanResult> list)
    {
        this.context = context;
        this.list = list;
    }

    @Override
    public OpenWifiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_wifilist_layout,parent,false);
        return new OpenWifiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OpenWifiViewHolder holder, int position) {

        final ScanResult scanResult = list.get(position);
        holder.wifiName.setText(scanResult.SSID);
        holder.wifiImage.setImageResource(R.drawable.open_network);
        holder.connect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, AppUtility.getString(R.string.connecting)+" "+scanResult.SSID+" ...", Toast.LENGTH_LONG).show();
                WifiUtility.connect(context, scanResult);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   IntentUtility.startTrackWifiActivity(context, scanResult);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OpenWifiViewHolder extends RecyclerView.ViewHolder{

        public TextView wifiName;
        public ImageView wifiImage;
        public TextView connect;

        public OpenWifiViewHolder(View itemView) {
            super(itemView);
            wifiName = itemView.findViewById(R.id.wifiName);
            wifiImage = itemView.findViewById(R.id.wifiImage);
            connect = itemView.findViewById(R.id.connect);
        }
    }
}
