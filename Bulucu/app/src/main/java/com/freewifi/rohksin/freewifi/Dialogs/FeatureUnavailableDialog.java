package com.freewifi.rohksin.freewifi.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.freewifi.rohksin.freewifi.R;

import androidx.annotation.NonNull;

public class FeatureUnavailableDialog extends Dialog {

    private Context context;
    private Button button;

    public FeatureUnavailableDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_feature_unvaileble_layout);
        button = findViewById(R.id.ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
