package com.freewifi.rohksin.freewifi.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.freewifi.rohksin.freewifi.Dialogs.FeatureUnavailableDialog;
import com.freewifi.rohksin.freewifi.Interfaces.WifiScanInterface;
import com.freewifi.rohksin.freewifi.R;
import com.freewifi.rohksin.freewifi.Utilities.AppUtility;
import com.freewifi.rohksin.freewifi.Utilities.WifiUtility;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.List;

/**
 * Created by RohitKSingh on 2/17/2018.
 */

/***************************************************************************************************
 *                                          TODO
 *    Do we need AsyncLoader for loading Drawable
 *    Move Inner common BroadcastReceiver to a seperate class
 *
 **************************************************************************************************/

public class HomePageActivity extends AppCompatActivity implements WifiScanInterface{


    private RelativeLayout mainLayout;
    private TextView openNetwork;
    private TextView closedNetwork;
    private TextView openNum;
    private TextView closeNum;
    private TextView scanNow;
    private FrameLayout scan;
    private LinearLayout openTouch,closeTouch;
    private ImageView privacyPolicy;
    private ImageView notifyMe;

    //Banner
    private RelativeLayout banner;
    private TextView bannerText;
    private Button bannerButton;

    private WifiScanReceiver wifiScanReceiver;

    private WifiManager manager;
    private List<ScanResult> allScanResults;
    private List<ScanResult> openScanResults;
    private List<ScanResult> closeScanResults;

    private Drawable openWifiLogo;
    private Drawable closeWifiLogo;
    private Drawable scanNowLogo;
    private int tapCounter=0;

    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    /***********************************************************************************************
     *                     Activity Life cycle methods   && Runtime Permission                     *
     /*********************************************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_layout);
        setupGoogleSignInClient();
        setUpRemoteConfig();
        setUpUI();
        if(!AppUtility.hasCompletedIntro)
        {
            new DrawableLoader().execute();
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
        startScan();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 87);
            }
        }
    }


    /***********************************************************************************************
     *                     Menu Related                                                            *
     /*********************************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signout:
                signOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onPause()
    {
        super.onPause();
        stopScan();
    }


    /***********************************************************************************************
     *                                  Interface methods                                          *
     **********************************************************************************************/

    @Override
    public void startScan()
    {
        manager = WifiUtility.getSingletonWifiManager(this);
        wifiScanReceiver = new WifiScanReceiver();
        registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        manager.startScan();
    }


    @Override
    public void stopScan()
    {
        unregisterReceiver(wifiScanReceiver);
    }



    @Override
    public void updateScanUI() {

    }



    /***********************************************************************************************
    *                                    BroadcastReceiver                                         *
    ***********************************************************************************************/

    private class WifiScanReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {

                manager.startScan();  // Contineous scan
                setUpAllList();
            }

        }
    }

    /***********************************************************************************************
    *                                   Private Helper Methods                                     *
    ***********************************************************************************************/


    private void openPrivacyPolicyDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.terms)
                .setCancelable(true)
                .setPositiveButton(R.string.read, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String url = AppUtility.getPrivacyPolicyUrl();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }


    private void openNotifyMe()
    {


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.P){
            new FeatureUnavailableDialog(this).show();
        }else{
            Intent i = new Intent(this, NotifyMeActivity.class);
            startActivity(i);
        }

    }


    private void addIntroView(int targetId, String msg, String desc, int color, Drawable drawable)
    {
        TapTargetView.showFor(this,                 // `this` is an Activity
                TapTarget.forView(findViewById(targetId), msg, desc)
                        // All options below are optional
                        .outerCircleColor(color)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        .targetCircleColor(android.R.color.white)   // Specify a color for the target circle
                        .titleTextSize(20)// Specify the size (in sp) of the title text
                        .titleTextColor(android.R.color.white)      // Specify the color of the title text
                        .descriptionTextSize(15)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(android.R.color.white)  // Specify the color of the description text
                        .textColor(android.R.color.white)            // Specify a color for both the title and description text
                        .dimColor(android.R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                        .icon(drawable)                     // Specify a custom drawable to draw as the target
                        .targetRadius(60),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        // doSomething();
                        setUpIntroView();
                    }
                });

    }


    private void setUpIntroView()
    {

        switch (tapCounter) {

            case 0:
                addIntroView(R.id.openContainer, AppUtility.getString(R.string.open_networks), AppUtility.getString(R.string.click_open_networks), android.R.color.holo_green_dark,openWifiLogo);
                tapCounter++;
                break;

            case 1:
                addIntroView(R.id.closeContainer, AppUtility.getString(R.string.close_networks), AppUtility.getString(R.string.click_closed_networks), android.R.color.holo_orange_dark, closeWifiLogo);
                tapCounter++;
                break;

            case 2:
                addIntroView(R.id.scan, AppUtility.getString(R.string.scan_now), AppUtility.getString(R.string.scan_your_surrounding), android.R.color.holo_blue_dark, closeWifiLogo);
                tapCounter++;
                AppUtility.saveIntoComplete();
                break;

            default:
                break;

        }

    }


    private void setUpAllList()
    {
        allScanResults = manager.getScanResults();
        WifiUtility.updateWifiResult(allScanResults);
        openScanResults = WifiUtility.getOpenScanResult();
        closeScanResults = WifiUtility.getCloseScanResult();

        if(allScanResults!=null)
            scanNow.setText(allScanResults.size()+"");

        if(openScanResults.size()!=0)
        {
            ScanResult scan = openScanResults.get(0);
            openNetwork.setText(scan.SSID+"");
            openNum.setText(openScanResults.size()+"");

        }else {
            openNetwork.setText(R.string.no_network);
            openNum.setText(openScanResults.size()+"");
        }

        if(closeScanResults.size()!=0)
        {
            ScanResult scan = closeScanResults.get(0);
            closedNetwork.setText(scan.SSID+"");
            closeNum.setText(closeScanResults.size()+"");
        }
        else {
            closedNetwork.setText(R.string.no_network);
            closeNum.setText(closeScanResults.size()+"");
        }

    }


    private void setUpUI()
    {

        mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        openNetwork = (TextView)findViewById(R.id.open);
        closedNetwork = (TextView)findViewById(R.id.close);
        openNum = (TextView)findViewById(R.id.openNum);
        closeNum = (TextView)findViewById(R.id.closeNum);
        openTouch = (LinearLayout) findViewById(R.id.openTouch);
        closeTouch = (LinearLayout) findViewById(R.id.closeTouch);
        scanNow = (TextView)findViewById(R.id.scanNow);
        mainLayout.setPadding(0, AppUtility.getStatusBarHeight(),0,0);
        privacyPolicy = (ImageView)findViewById(R.id.privacyPolicy);
        notifyMe = (ImageView)findViewById(R.id.notifyMe);
        scan = (FrameLayout)findViewById(R.id.scan);

        banner = findViewById(R.id.banner);
        bannerText = (TextView)findViewById(R.id.banner_Text);
        bannerButton = findViewById(R.id.banner_button);

        notifyMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNotifyMe();
            }
        });

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPrivacyPolicyDialog();
            }
        });


        openTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomePageActivity.this, WifiListActivity.class);
                intent.setAction("OPEN_NETWORK");
                intent.putExtra("BG_COLOR",android.R.color.holo_green_light);
                startActivity(intent);

            }
        });


        closeTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomePageActivity.this, WifiListActivity.class);
                intent.setAction("CLOSE_NETWORK");
                intent.putExtra("BG_COLOR",android.R.color.holo_orange_light);
                startActivity(intent);

            }
        });


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomePageActivity.this, ScanSurroundingActivity.class));
            }
        });


    }


    /***********************************************************************************************
     *                                    AsyncTask for loading Drawable                           *
     /*********************************************************************************************/

    private class DrawableLoader extends AsyncTask<Void, Void, Void>{

        @Override
        public Void doInBackground(Void... params)
        {
            openWifiLogo = getResources().getDrawable( R.drawable.open_network);
            closeWifiLogo = getResources().getDrawable( R.drawable.closed_network);
            scanNowLogo = getResources().getDrawable( R.drawable.scan_now);
            return null;
        }

        @Override
        public void onPostExecute(Void result)
        {
            setUpIntroView();
        }

    }

    public void signOut(){

        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(HomePageActivity.this, "Sign out", Toast.LENGTH_SHORT).show();
                        revokeAccess();
                    }
                });

    }


    private void revokeAccess() {

        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(HomePageActivity.this, "Sign out successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(HomePageActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }});
    }


    private void setupGoogleSignInClient(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void setUpRemoteConfig(){
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(10)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);


        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();

                            Toast.makeText(HomePageActivity.this, "Fetch and activate succeeded",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(HomePageActivity.this, "Fetch failed",
                                    Toast.LENGTH_SHORT).show();
                        }


                        boolean show_ad = mFirebaseRemoteConfig.getBoolean("show_ad");
                        String banner_text = mFirebaseRemoteConfig.getString("banner_text");
                        String button_text = mFirebaseRemoteConfig.getString("banner_button_text");
                        final String banner_url = mFirebaseRemoteConfig.getString("banner_url");


                        banner.setVisibility((show_ad) ? View.VISIBLE : View.GONE);
                        bannerText.setText(banner_text);
                        bannerButton.setText(button_text);
                        bannerButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(banner_url));
                                startActivity(i);
                            }
                        });

                    }
                });



    }

}