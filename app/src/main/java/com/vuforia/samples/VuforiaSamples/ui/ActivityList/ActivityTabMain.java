package com.vuforia.samples.VuforiaSamples.ui.ActivityList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuforia.samples.VuforiaSamples.R;
import com.vuforia.samples.VuforiaSamples.ui.Adapter.TabFragmentAdapter;
import com.vuforia.samples.VuforiaSamples.ui.Common.CmtDataList;
import com.vuforia.samples.VuforiaSamples.ui.Common.ProductInfo;

import java.util.List;

/**
 * Created by aquat on 2017/12/28.
 */

public class ActivityTabMain extends FragmentActivity {

    //region 定義
    public enum Page {
        Comment(0),
        Ditail(1),;

        private final int id;

        private Page(final int id) {
            this.id = id;
        }

        public int getInt() {
            return  this.id;
        }


    }
    //endregion

    ViewPager viewPager;
    // キーボード表示を制御するためのオブジェクト
    InputMethodManager inputMethodManager;
    private LinearLayout mainlayout;

   // private UserInfo userInfo;

    private List<ProductInfo> sampleDataList;
    private CmtDataList commentData;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_main);

        //ユーザー情報を受け取る
       // userInfo = (UserInfo)getIntent().getSerializableExtra("userInfo");

        //アダプター作成
        TabFragmentAdapter adapter = new TabFragmentAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mainlayout = (LinearLayout)findViewById(R.id.main_layout);

        //タブボタン設定
        ViewGroup vg = (ViewGroup)findViewById(R.id.tab);
        //Layout取得
        getLayoutInflater().inflate(R.layout.tab_layout,vg);
        Button commentBtn = (Button)(vg.findViewById(R.id.tab_comment));
        Button ditailBtn = (Button)(vg.findViewById(R.id.tab_ditail));
       /* Button commentBtn = (Button)(findViewById(R.id.tab_comment));
        Button ditailBtn = (Button)(findViewById(R.id.tab_ditail));*/

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(Page.Comment.getInt());
                inputMethodManager.hideSoftInputFromWindow(mainlayout.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                mainlayout.requestFocus();
            }
        });

        ditailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(Page.Ditail.getInt());
                //キーボードを隠す
                inputMethodManager.hideSoftInputFromWindow(mainlayout.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                mainlayout.requestFocus();
            }
        });


        viewPager.setOnTouchListener(new ViewPager.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //キーボードを隠す
                inputMethodManager.hideSoftInputFromWindow(mainlayout.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                mainlayout.requestFocus();
                return false;
            }
        });

    }

    @Override
    protected void onPause(){
        super.onPause();

    }

    @Override
    protected void onStop(){
        super.onStop();

        this.finish();
    }
}
