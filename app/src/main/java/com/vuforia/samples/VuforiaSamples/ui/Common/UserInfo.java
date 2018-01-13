package com.vuforia.samples.VuforiaSamples.ui.Common;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by K.Oda on 2017/12/16.
 */

public class UserInfo implements Serializable {

    //シングルトンインスタンス
    private static UserInfo instance = new UserInfo();

    //region プロパティ
    private String userName;
    public String getUserName(){return  userName;}
    public void setUserName(String userName){this.userName = userName;}

    private String userId;
    public String getUserId(){return userId;}
    public void setUserId(String userId){this.userId = userId;}

    private int sex;
    public int getSex(){return sex;}
    public void setSex(int i){this.sex = i;}


    private HashMap<String,String> productInfoMap;
    public HashMap<String,String> getProductInfoMap(){return this.productInfoMap;}
    public void addProductInfo(String key,String value){
        this.productInfoMap.put(key,value);
    }
    //endregion


    //region コンストラクタ
    private UserInfo(){
        this.productInfoMap = new HashMap<String,String>();
    }

    public static synchronized UserInfo getInstance(){
        if(instance==null){
            instance = new UserInfo();
        }
        return instance;
    }
    //endregion

    //region コンバーター
    public boolean ConvertProductInfo(ProductInfo productInfo){

        try{

            String[] indexlist = productInfo.indexInfo.split(",",0);

            //商品名
            addProductInfo(indexlist[0],productInfo.contentsName);

            //商品画像
            addProductInfo("写真",String.format("%s%s","http://192.168.0.150/privateWebDav/Root/image/",productInfo.image));

            //マーカーID
            addProductInfo("MarkerID",String.valueOf(productInfo.markerId));

            //キャッチコピー
            addProductInfo(indexlist[1],productInfo.description);

            //リストの３列目からは詳細データ
            //詳細
            String[] values = productInfo.dtlInfo.split(",",-1);
            //項目分ループする
            for(int i = 2,j = 0; i < indexlist.length;i++,j++){
                addProductInfo(indexlist[i],values[j]);
            }


            return true;
        }catch (Exception e){
            return false;
        }
    }
    //endregion
}
