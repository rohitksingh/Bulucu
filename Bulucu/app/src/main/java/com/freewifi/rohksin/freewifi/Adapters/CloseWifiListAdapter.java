package com.freewifi.rohksin.freewifi.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.Activities.TrackWifiActivity;
import com.freewifi.rohksin.freewifi.CallbackListeners.ListItemListener;
import com.freewifi.rohksin.freewifi.Dialogs.WifiStatDialog;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Testing.TestService;
import com.freewifi.rohksin.freewifi.Utilities.IntentUtility;

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

        final ScanResult scanResult = scanResults.get(position);
        holder.wifiName.setText(scanResult.SSID);
        holder.wifiImage.setImageResource(R.drawable.closed_network);
        holder.wifidetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new WifiStatDialog(context , scanResult);
                dialog.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentUtility.startTrackWifiActivity(context, scanResult);

            }
        });
    }

    @Override
    public int getItemCount() {
        return scanResults.size();
    }

    public class CloseWifiViewHolder extends RecyclerView.ViewHolder{


        private TextView wifiName;
        private ImageView wifiImage;
        private TextView wifidetail;

        public CloseWifiViewHolder(View itemView) {
            super(itemView);
            wifiName = (TextView)itemView.findViewById(R.id.wifiName);
            wifiImage = (ImageView)itemView.findViewById(R.id.wifiImage);
            wifidetail = (TextView)itemView.findViewById(R.id.connect);
            wifidetail.setText("Detail");
        }
    }
}
