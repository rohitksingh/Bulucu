package com.freewifi.rohksin.freewifi.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.R;

import androidx.annotation.NonNull;

public class ConnectDialog extends Dialog {

    private Context context;
    private Button button;
    private ProgressBar progressBar;
    private TextView textView;

    public ConnectDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_connect_layout);
        button = findViewById(R.id.cancel);
        progressBar = findViewById(R.id.progressbar);
        textView = findViewById(R.id.text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
