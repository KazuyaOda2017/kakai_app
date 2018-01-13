package com.vuforia.samples.VuforiaSamples.ui.Common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by aquat on 2017/12/26.
 */

public class  ConvertJson {

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * オブジェクトをJsonにシリアライズ
     * @param obj
     * @return
     */
    public static String SerializeJson(Object obj){
        String json = null;
        try {
            json = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * jsonをProductInfoにデシリアライズ
      * @param json
     * @return
     */
    public static ProductInfo DeserializeJsonToProductInfo(String json){

        ProductInfo productInfo = new ProductInfo();
        try{
            productInfo = objectMapper.readValue(json, ProductInfo.class);
        }catch (IOException e){
            e.printStackTrace();
        }

        return productInfo;

    }

    /**
     * jsonをコメントデータオブジェクトにデシリアライズ
     * @param json
     * @return
     */
    public static CmtDataList DeserializeJsonToCmtDataList(String json){

        CmtDataList cmtDataList = new CmtDataList();
        try{
            cmtDataList = objectMapper.readValue(json, CmtDataList.class);
        }catch (IOException e){
            e.printStackTrace();
        }

        return cmtDataList;

    }


}
