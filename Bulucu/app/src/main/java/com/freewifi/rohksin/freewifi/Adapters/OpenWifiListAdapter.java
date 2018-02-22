package com.freewifi.rohksin.freewifi.Adapters;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.CallbackListeners.ListItemListener;
import com.freewifi.rohksin.freewifi.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Illuminati on 2/22/2018.
 */

public class OpenWifiListAdapter extends RecyclerView.Adapter<OpenWifiListAdapter.OpenWifiViewHolder> {


    private Context context;
    private List<ScanResult> list;

    ListItemListener listItemListener;

    public OpenWifiListAdapter(Context context, List<ScanResult> list)
    {
        this.context = context;
        this.list = list;
        listItemListener = (ListItemListener)context;
    }

    @Override
    public OpenWifiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wifi_item,parent,false);
        return new OpenWifiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OpenWifiViewHolder holder, int position) {

        final ScanResult scanResult = list.get(position);
        holder.wifiName.setText(scanResult.SSID);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItemListener.itemClick(scanResult);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OpenWifiViewHolder extends RecyclerView.ViewHolder{

        public TextView wifiName;


        public OpenWifiViewHolder(View itemView) {
            super(itemView);
            wifiName = (TextView)itemView.findViewById(R.id.wifiName);
        }
    }
}
