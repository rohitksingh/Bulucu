package com.freewifi.rohksin.freewifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Illuminati on 5/5/2017.
 */
public class WifiListAdapter extends RecyclerView.Adapter<WifiListAdapter.WifiViewHolder> {

    private List<ScanResult> wifiList;
    private Context context;

    private List<String> testData;

    public WifiListAdapter(Context context,List<ScanResult> wifiList)
    {
        this.context = context;
        //this.wifiList = wifiList;
        Log.d("Rohit", "Inside list Adapter checklist"+(wifiList==null));
        //testData = wifiList;
        this.wifiList = wifiList;
        //Log.d("Rohit", "wifilist null"+(wifiList==null));
/*        if(wifiList.size()>0)
        {
            Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(1000);
        }
     */
    }

    @Override
    public WifiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_wifi_layout,parent,false);
        return new WifiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WifiViewHolder holder, int position) {
       final ScanResult wifi = wifiList.get(position);
        //final String data = testData.get(position);
        holder.wifiName.setText(wifi.SSID);
       // holder.wifiSecurity.setText(wifi.capabilities);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,wifi.SSID,Toast.LENGTH_LONG).show();

                new Connect(context,wifi);

            }
        });

    }

    @Override
    public int getItemCount() {
        return wifiList.size();
    }

    public class WifiViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView wifiName;
        private TextView wifiSecurity;
        private View view;

        public WifiViewHolder(View view)
        {
            super(view);
            this.view = view;
            image = (ImageView)view.findViewById(R.id.image);
            wifiName = (TextView)view.findViewById(R.id.wifiName);
            wifiSecurity = (TextView)view.findViewById(R.id.wifiType);

        }



    }
}
