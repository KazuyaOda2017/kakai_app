package com.vuforia.samples.VuforiaSamples.ui.Common;

import com.vuforia.samples.VuforiaSamples.R;

/**
 * Created by K.Oda on 2017/10/14.
 */

public class MenuIconInfo {

    //region icon名
    private String[] iconName = {
            "ARCamera",

    };
    //endregion

    //region icon画像
    private Integer[] iconImage ={
            R.drawable.icon_ar_camera,
            R.drawable.icon_ar_camera,


    };
    //endregion

    //region 画面遷移先クラス名（パス）
    private  String[] nextActivityClass ={
            //カメラ画面
            "app.VuMark.VuMark",

    };
    //endregion

    //region コンストラクター
    public MenuIconInfo(){

    }
    //endregion


    //region メソッド
    public int getCountIcon() {
        return iconImage.length;
    }

    //アイコン画像取得
    public Object getItem(int position) {
        return iconImage[position];
    }

    //アイコン名取得
    public String getIconName(int position){
        return iconName[position];
    }

    //遷移先を取得
    public String  getNextActivity(int position){
        return nextActivityClass[position];
    }

    //endregion

}