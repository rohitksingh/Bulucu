package com.freewifi.rohksin.freewifi.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.freewifi.rohksin.freewifi.R;

public class NotifyMeDialog extends Dialog {

    private Context context;
    private Button button;

    public NotifyMeDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_notify_layout);
        button = (Button) findViewById(R.id.gotIt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
