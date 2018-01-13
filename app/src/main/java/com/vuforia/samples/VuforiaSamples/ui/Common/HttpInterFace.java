package com.vuforia.samples.VuforiaSamples.ui.Common;

import android.net.Uri;
import android.os.AsyncTask;

/**
 * Created by aquat on 2017/12/18.
 */

interface HttpInterFace {

    //region 定義
    public enum ProgressType{
        DIALOG,
        NOPROGRESS,
    }
    //endregion

    //region 定数
    //ユーザー情報登録
    public static final String INSERT_USERINFO =  "http://192.168.0.150:8080/kakai2017-restapi/insert/user";

    //リロード
    public static final String SELECT_COMMENT = "http://192.168.0.150:8080/kakai2017-restapi/select/comment";

    //コメント登録・編集
    public static final String EDIT_COMMENT = "http://192.168.0.150:8080/kakai2017-restapi/edit/comment";

    //コンテンツ情報取得
    public static final String SELECT_CONTENTS = "http://192.168.0.150:8080/kakai2017-restapi/select/contents";
    //endregion

}
