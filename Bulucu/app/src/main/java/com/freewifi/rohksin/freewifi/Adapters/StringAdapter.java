package com.freewifi.rohksin.freewifi.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by RohitKsingh on 2/19/2018.
 */

public class StringAdapter extends RecyclerView.Adapter<StringAdapter.StingViewHolder>{

    private Context context;
    private List<String> scanList;

    public StringAdapter(Context context, List<String> scanList)
    {
        this.context = context;
        this.scanList = scanList;
    }


    @Override
    public StingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.scan_item, parent, false);
        return new StingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StingViewHolder holder, int position) {
        String scanName = scanList.get(position);
        holder.name.setText(scanName);
    }


    @Override
    public int getItemCount() {
        return scanList.size();
    }

    public class StingViewHolder extends RecyclerView.ViewHolder{

        public TextView name;

        public StingViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.string);
        }
    }

}
