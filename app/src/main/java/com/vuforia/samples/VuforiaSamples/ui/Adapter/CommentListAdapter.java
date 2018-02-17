package com.vuforia.samples.VuforiaSamples.ui.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vuforia.samples.VuforiaSamples.R;
import com.vuforia.samples.VuforiaSamples.ui.Common.CommentInfo;
import com.vuforia.samples.VuforiaSamples.ui.Common.UserInfo;
import com.vuforia.samples.VuforiaSamples.ui.CustomView.EvaluationView;

/**
 * Created by aquat on 2017/12/28.
 */

public class CommentListAdapter extends ArrayAdapter<CommentInfo>{

    LayoutInflater mInflater;

    public CommentListAdapter(@NonNull Context context) {
        super(context,0);
        mInflater = LayoutInflater.from(context);

    }

    //タップアクションを無効にする
    @Override
    public boolean isEnabled(int position){
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //コメント情報を取得
        CommentInfo info = getItem(position);

        //Viewが再利用可能かどうか
        if(convertView != null){

           // int id = (int)convertView.getTag();


        }

        //レイアウトを取得
        //if(convertView == null){
        //自分のコメントかどうか
        int gravity = Gravity.RIGHT;
        try{
            if(!info.userId.equals(UserInfo.getInstance().getUserId())){
                convertView = mInflater.inflate(R.layout.cmt_other,parent,false);
                gravity = Gravity.LEFT;
            }else {
                convertView = mInflater.inflate(R.layout.cmt_self,parent,false);

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        if(info != null){

            //コメント作成
            TextView tv = (TextView)convertView.findViewById(R.id.cmt);
            tv.setText(info.userCmt);
            //評価を設定
            EvaluationView evaluationView = (EvaluationView)convertView.findViewById(R.id.stars_mini);
            evaluationView.setImageSize(15);
            evaluationView.setGravity(gravity);
            evaluationView.changeStarState((int)info.star - 1);
            evaluationView.setCallBackState(EvaluationView.CallBackState.OFF);
            evaluationView.setMargin(0,0,0,0);

                   /* ViewGroup vg = (ViewGroup)convertView.findViewById(R.id.stars_mini);

                    //LinearLayout linearLayout = (LinearLayout)convertView.findViewById(R.id.starsmini_line2);
                    //linearLayout.setGravity(Gravity.RIGHT);
                    for(int i = 0;i< 5;i++){

                        ImageButton imageButton = (ImageButton)vg.getChildAt(i);

                        if(i < info.star) {
                            imageButton.setImageResource(R.drawable.star_on);
                        }else{
                            imageButton.setImageResource(R.drawable.star_off);
                        }
                    }*/
            //ユーザー名
            TextView userName = (TextView)convertView.findViewById(R.id.user_name);
            userName.setText(info.userName);

            //登録日
            TextView insertDate = (TextView)convertView.findViewById(R.id.insert_date);
            insertDate.setText(info.insertDate);

            //タグにIDを登録する
            convertView.setTag(info.userId);

        }



        //; }


        return convertView;
    }

}
