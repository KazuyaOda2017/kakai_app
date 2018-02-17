/*===============================================================================
Copyright (c) 2016 PTC Inc. All Rights Reserved.

Copyright (c) 2012-2014 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/

package com.vuforia.samples.VuforiaSamples.ui.ActivityList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vuforia.samples.VuforiaSamples.R;
import com.vuforia.samples.VuforiaSamples.app.VuMark.VuMark;
import com.vuforia.samples.VuforiaSamples.ui.Common.PrefarenceRequest;
import com.vuforia.samples.VuforiaSamples.ui.Common.UserInfo;


public class ActivitySplashScreen extends Activity
{
    
    private static long SPLASH_MILLIS = 1000;

    private static final int RESULTCODE = 1;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(
            R.layout.splash_screen, null, false);
        
        addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT));


        
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            
            @Override
            public void run()
            {

                //プリファレンス
                PrefarenceRequest.getUserPrefarence(PrefarenceRequest.PRENAME_USERINFO,getApplicationContext());

                //Intent
                Intent intent = null;

/*
                if(UserInfo.getInstance().getUserId().equals("")) {
                    //登録画面に遷移する
                    intent = new Intent(ActivitySplashScreen.this,ActivityUserRegister.class);
                }else {
                    //カメラ画面に遷移する
                    String id = UserInfo.getInstance().getUserId();
                    intent = new Intent(ActivitySplashScreen.this, VuMark.class);
                            //VuMark.class);
                }
*/

                //登録画面に遷移する
                intent = new Intent(ActivitySplashScreen.this,ActivityUserRegister.class);

                //startActivity(intent);


                startActivityForResult(intent, RESULTCODE);

                overridePendingTransition(R.anim.in_right,R.anim.out_left);


            }
            
        }, SPLASH_MILLIS);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULTCODE) {
            if (resultCode == RESULT_OK) {
            }
        }
    }

}
