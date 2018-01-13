/*===============================================================================
Copyright (c) 2016 PTC Inc. All Rights Reserved.

Copyright (c) 2012-2015 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/


package com.vuforia.samples.VuforiaSamples.ui.ActivityList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vuforia.samples.VuforiaSamples.R;
import com.vuforia.samples.VuforiaSamples.ui.Common.MenuIconInfo;


// This activity starts activities which demonstrate the Vuforia features
public class ActivityMenu extends Activity
{
    private String mClassToLaunch;
    private String mClassToLaunchPackage;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_menu);

        //パッケージ名の取得
        mClassToLaunchPackage = getPackageName();


        //上部TableLayoutを設定
        ViewGroup vg = (ViewGroup) findViewById(R.id.table_menu_top);

        MenuIconInfo menuIconInfo = new MenuIconInfo();

        //行をicon分追加する(上限８)
        int row = 0;
        for (int i = 0, column = 0; i < menuIconInfo.getCountIcon(); i++, column++) {
            try {
                //4列追加したら次の行へ
                if (column == 4) {
                    row++;
                    column = 0;
                }
                //TableRowを取得
                getLayoutInflater().inflate(R.layout.menu_tablerow_line_layout, vg);
                //row行目を取得
                //TableRow trindex = (TableRow)vg.getChildAt(row);
                LinearLayout trindex = (LinearLayout) vg.getChildAt(row);

                //Viewを取得
                LinearLayout layout = (LinearLayout) trindex.getChildAt(column);
                //TextView(アイコン名)を設定
                TextView textView = (TextView) (layout.findViewById(R.id.text_icon_name));
                textView.setText(menuIconInfo.getIconName(i));
                //ImageButtonを設定
                ImageButton imageButton = (ImageButton) (layout.findViewById(R.id.icon_image));
                imageButton.setImageResource((Integer) menuIconInfo.getItem(i));
                //遷移先クラスを設定
               /* Intent intent = new Intent(getApplication(), (Class<?>) menuIconInfo.getNextActivity(i));
                imageButton.setTag(intent);*/
                Intent intent = new Intent();
                mClassToLaunch = mClassToLaunchPackage + "."
                        + menuIconInfo.getNextActivity(i);

                intent.setClassName(mClassToLaunchPackage, mClassToLaunch);
                imageButton.setTag(intent);

                //ボタンを可視化する
                trindex.getChildAt(column).setVisibility(View.VISIBLE);

                //リスナーに登録
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Tagに設定された遷移先を取得して画面遷移をする
                        Intent intent_new = (Intent) v.getTag();
                        startActivity(intent_new);
                    }
                });
            } catch (Exception e) {
                continue;
            }
            //8個追加でbreak
            if (i == 7) {
                break;
            }
        }
    }

   /*
        mClassToLaunchPackage = getPackageName();
        mClassToLaunch = mClassToLaunchPackage + "."
                + "app.VuMark.VuMark";

        // ボタンのオブジェクトを取得
        Button btn = (Button)findViewById(R.id.vu_mark);

        // クリックイベントを受け取れるようにする
        btn.setOnClickListener(new View.OnClickListener() {
            // このメソッドがクリック毎に呼び出される
            public void onClick(View v) {
                click();
            }
        });*/

    private void click() {
        Intent i = new Intent();
        i.setClassName(mClassToLaunchPackage, mClassToLaunch);
        startActivity(i);

    }



}
