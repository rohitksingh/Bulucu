package com.freewifi.rohksin.freewifi.Testing;

import android.animation.Animator;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.freewifi.rohksin.freewifi.R;

/**
 * Created by Illuminati on 2/23/2018.
 */

/*
       TODO
       How to get exact points of touch
       Hoe to reverse Animation
       Another Way around

 */

public class MyFragment extends Fragment implements GenericFragment{


   public int centerX;
   public int centerY;

    private View view;


    @Override
    public void doSomething() {
        changeTextColor();
    }

    private TextView textView ;


    public MyFragment getInstanceState(int centerX, int centery)
    {

        Bundle bundle = new Bundle();
        bundle.putInt("XX",centerX);
        bundle.putInt("YY",centery);
        MyFragment fragment = new MyFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        view =inflater.inflate(R.layout.test,parent, false);
        textView = (TextView)view.findViewById(R.id.testText);

        getBundleArgument(getArguments());

        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                setRevealAnimation(v);
            }
        });

        return view;
    }


    @Override
    public void onDestroyView()
    {
        setReverseAnimation(view);
        super.onDestroyView();
    }

    public void changeTextColor()
    {
        textView.setText("wow");
    }




    //********************************************************************************************************//
    //                                      HELPER METHODS                                                    //
    //********************************************************************************************************//

    private void setRevealAnimation(View view )
    {

        int startRadius = 0;
        int  endRadius = (int)Math.hypot(view.getWidth(), view.getHeight());

        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);
            anim.setDuration(500);
            anim.start();
        }
    }

    private void setReverseAnimation(View view)
    {

        int startRadius = 0;
        int  endRadius = (int)Math.hypot(view.getWidth(), view.getHeight());

        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, endRadius, startRadius);
            anim.setDuration(500);
            anim.start();
        }
    }

    private void getBundleArgument(Bundle bundle)
    {
        centerX = bundle.getInt("XX");
        centerY = bundle.getInt("YY");
    }


}
