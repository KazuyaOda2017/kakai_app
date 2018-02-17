package com.vuforia.samples.VuforiaSamples.ui.CustomView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.vuforia.samples.VuforiaSamples.R;
import com.vuforia.samples.VuforiaSamples.ui.Common.CommentInfo;
import com.vuforia.samples.VuforiaSamples.ui.Common.UserInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aquat on 2017/12/25.
 */

public class CmtInputView extends LinearLayout{

    protected EditText editText;
    protected ImageButton submitBtn;
    protected EvaluationView evaluationView;
    protected LinearLayout rootView;

    private CallBackTask callBackTask;

    protected int selectStar;

    public CmtInputView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View layout = LayoutInflater.from(context).inflate(R.layout.comment_input_area,this);

        rootView = (LinearLayout)layout.findViewById(R.id.root_LinearLayout);

        //region 入力エリア
        editText = (EditText)layout.findViewById(R.id.edittext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //変更されたテキストを表示
                String inputStr = editable.toString();

                if(inputStr.trim().equals("")){
                    submitBtn.setEnabled(false);
                }else{
                    submitBtn.setEnabled(true);
                }
            }
        });
        //endregion

        //region 送信ボタン
        submitBtn = (ImageButton)layout.findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //テキストボックスの入力をコメントに反映する
                String inputStr = editText.getText().toString();

                //入力をチェックする
                if(!checkInputText(inputStr)) return;

                //コメント情報を作成
                CommentInfo cInfo = new CommentInfo();
                cInfo.userCmt = (inputStr);
                cInfo.star = (selectStar);
                cInfo.userName = UserInfo.getInstance().getUserName();

                cInfo.userId = UserInfo.getInstance().getUserId();
                cInfo.sex = UserInfo.getInstance().getSex();

                //マーカーID
                cInfo.markerId = Integer.parseInt(UserInfo.getInstance().getProductInfoMap().get("MarkerID"));
                //日付を取得
                Date date = new Date();
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
               // String date1 = sdf1.format(date);
                //cInfo.insertDate = (date1);

                //入力を空にする
                editText.setText("");

                //コメント情報をサーバーに送信する

                //結果をコールバックする
                //callBackTask.CallBack(0);
                //callBackTask.CallBackTest(0,cInfo);
                callBackTask.SabmitCallBack(0,cInfo);
            }
        });

        //送信ボタンを非表示
        submitBtn.setEnabled(false);
        //endregion

        //region 評価ボタン
        try{
            evaluationView = (EvaluationView)layout.findViewById(R.id.stars);

            //Gravityの設定
            evaluationView.setGravity(Gravity.LEFT);
            evaluationView.setImageSize(25);

            //コールバックの登録
            evaluationView.setOnCallBack(new EvaluationView.CallBackTask() {
                @Override
                public void CallBack(int result) {
                    //resultをフィールドにセット
                    selectStar = result +1;//評価値はresultの+1
                }
            });
            //初期化
           // evaluationView.changeStarState(2);

        }catch (Exception e){

        }
        //endregion
    }

    /**
     * 評価ボタンの設定
     * @param num
     */
    public void setStar(int num){

        try{
            evaluationView.changeStarState(num);
        }catch (Exception e){

        }
    }

    public void setText(String text){
        try{
            editText.setText(text);
        }catch (Exception e){

        }
    }

    /**
     * 有効/無効の設定
     * @param value
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint({"ResourceAsColor", "WrongConstant"})
    public void setEnabled(boolean value){

        //入力エリア
        //初期化
        evaluationView.changeStarState(2);
        //入力内容をクリア
        editText.setText("");

        editText.setEnabled(value);
        //背景色
        GradientDrawable drawable = new GradientDrawable();
        drawable.mutate();
        drawable.setShape(0);// 0がRECTANGLE
        drawable.setCornerRadius(5);
        if(value){
            drawable.setColor(Color.parseColor("#339900"));
        }else {
            drawable.setColor(Color.parseColor("#336400"));
        }
        rootView.setBackground(drawable);

        //評価ボタン
        evaluationView.setEnabled(value);

    }

    public void setOnCallBack(CallBackTask _cbj){
        callBackTask = _cbj;
    }

    //region テキスト入力チェック
    private boolean checkInputText(String str) {
        boolean result = true;
        if(str.trim().equals("")){
            result = false;
        }
        return result;
    }
    //endregion

    //コールバックの抽象クラス
    public static abstract class CallBackTask{
        abstract public void CallBack(int result);
        abstract public void SabmitCallBack(int result, CommentInfo cInfo);
        //abstract public void CallBackTest(int result,CommentInfo commentInfo);
    }
}