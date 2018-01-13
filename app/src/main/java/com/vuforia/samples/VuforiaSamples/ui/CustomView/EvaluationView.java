package com.vuforia.samples.VuforiaSamples.ui.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.vuforia.samples.VuforiaSamples.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aquat on 2017/12/25.
 */

public class EvaluationView extends LinearLayout{

    //region 定義
    /**
     * コールバック状態
     */
    public enum CallBackState{
        ON,
        OFF,
    }
    //endregion

    //region 変数
    protected List<StarView> starButtons;
    private CallBackTask callBackTask;
    private LinearLayout linearLayout;
    //評価数保持用
    private int _index = 0;
    //endregion

    //region プロパティ
    private CallBackState callBackState;
    public void setCallBackState(CallBackState state){
        this.callBackState = state;
    }
    //endregion

    /**
     * コンストラクタ
     * @param context
     * @param attrs
     */
    public EvaluationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //コールバックの初期設定
        callBackState = CallBackState.ON;

        View layout = LayoutInflater.from(context).inflate(R.layout.star_layout,this);

        linearLayout = (LinearLayout)layout.findViewById(R.id.root_LinearLayout);

        starButtons = new ArrayList<StarView>();
        for(int i = 0;i < StarInfo.getStarCount();i++){
            StarView starButton = (StarView) layout.findViewById((Integer) StarInfo.getStarItem(i));
            starButton.setIndex(i);

            starButton.setOnCallBack(new StarView.CallBackTask() {
                @Override
                public void CallBack(int result) {

                    if(callBackState == CallBackState.OFF){
                        //タップ時のコールバックをOしない
                        return;
                    }
                    //スターボタンのコールバック
                    changeStarState(result);

                }
            });

            starButtons.add(starButton);
        }
    }

    /**
     * 画像サイズを設定する
     * @param size
     */
    public void setImageSize(int size){
        try{

            for(StarView starView:starButtons){
                starView.setImageSize(size);
            }

        }catch (Exception e){

        }
    }

    /**
     * Gravityを設定する
     * @param gravity
     */
    public void setGravity(int gravity){

        linearLayout.setGravity(gravity);
    }

    /**
     * マージンを設定する
     * @param marginLeft
     * @param marginTop
     * @param marginRight
     * @param marginBottom
     */
    public void setMargin(int marginLeft,int marginTop,int marginRight,int marginBottom){
        LayoutParams lp = (LayoutParams) linearLayout.getLayoutParams();
        MarginLayoutParams mlp = (MarginLayoutParams)lp;
        mlp.setMargins(marginLeft,marginTop,marginRight,marginBottom);
        linearLayout.setLayoutParams(mlp);
    }

    /**
     * 有効/無効の設定
     * @param value
     */
    public void setEnabled(boolean value){
        for(StarView starView:starButtons){
            starView.setEnabled(value);
            if(value){
                starView.setState(StarView.StarState.OFF);
            }else{
                starView.setState(StarView.StarState.DISABLE);
            }
        }

        if(value){
            //初期位置に戻す
            changeStarState(2);
            //コールバックする
            callBackState = CallBackState.ON;
        }else{
            //コールバックしない
            callBackState = CallBackState.OFF;
        }
    }

    /**
     * コールバックを設定する
     * @param _cbj
     */
    public void setOnCallBack(CallBackTask _cbj){
        callBackTask = _cbj;
    }

    /**
     * コールバックタスクの抽象クラス
     */
    public static abstract class CallBackTask{
        abstract public void CallBack(int result);
    }

    /**
     * タップ位置まで画像を入れ替える
     * @param index
     */
    public void changeStarState(int index)
    {
        try{
            //index位置までONに、以降はOFFにする
            for(int i = 0;i < starButtons.size();i++){
                if(i<=index){
                    starButtons.get(i).setState(StarView.StarState.ON);
                }else{
                    starButtons.get(i).setState(StarView.StarState.OFF);
                }
            }
            //indexを保持する
            _index = index;

            //タップ位置をコールバックする
            callBackTask.CallBack(index);
        }catch (Exception e){

        }

    }

    public static class StarInfo extends BaseAdapter {

        private static Integer[] starIdArray ={
                R.id.star_1,
                R.id.star_2,
                R.id.star_3,
                R.id.star_4,
                R.id.star_5,
        };

        @Override
        public int getCount() {
            return starIdArray.length;
        }

        @Override
        public Object getItem(int i) {
            return starIdArray[i];
        }

        public static int getStarCount() {
            return starIdArray.length;
        }

        public static Object getStarItem(int i) {
            return starIdArray[i];
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    }
}
