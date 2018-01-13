package com.vuforia.samples.VuforiaSamples.ui.Common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by K.Oda on 2017/12/16.
 */

public class PrefarenceRequest extends Activity{

    //region 定義
    //プリファレンス名
    public static String PRENAME_USERINFO = "UserInfo";

    //key
    public static String PREKEY_USERNAME = "userName";

    public static String PREKEY_USERID = "userID";

    public static String PREKEY_SEX = "sex";
    //endregion

    //region フィールド
    private static SharedPreferences preferences;
    //endregion

    public static boolean getUserPrefarence(String key, Context context){

        //SharedPreferences preferences;
        try{
            //プリファレンス取得
            preferences = context.getSharedPreferences(key,MODE_PRIVATE);

            String uName = preferences.getString(PREKEY_USERNAME,"");
            String uId = (preferences.getString(PREKEY_USERID,""));
            int usex = (preferences.getInt(PREKEY_SEX,0));

            UserInfo.getInstance().setUserName(uName);
            UserInfo.getInstance().setUserId(uId);
            UserInfo.getInstance().setSex(usex);

            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean setUserPrefarence(String key,Context context){

        try {
            preferences = context.getSharedPreferences(key,MODE_PRIVATE);
            SharedPreferences.Editor editer = preferences.edit();

            editer.putString(PREKEY_USERNAME,UserInfo.getInstance().getUserName());
            editer.putString(PREKEY_USERID,UserInfo.getInstance().getUserId());
            editer.putInt(PREKEY_SEX,UserInfo.getInstance().getSex());
            editer.commit();
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}
