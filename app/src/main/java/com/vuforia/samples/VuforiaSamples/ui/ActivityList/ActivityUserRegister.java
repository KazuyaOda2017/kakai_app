package com.vuforia.samples.VuforiaSamples.ui.ActivityList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuforia.samples.VuforiaSamples.R;
import com.vuforia.samples.VuforiaSamples.app.VuMark.VuMark;
import com.vuforia.samples.VuforiaSamples.ui.Common.ConvertJson;
import com.vuforia.samples.VuforiaSamples.ui.Common.HttpRequest;
import com.vuforia.samples.VuforiaSamples.ui.Common.JsonUserInfo;
import com.vuforia.samples.VuforiaSamples.ui.Common.PrefarenceRequest;
import com.vuforia.samples.VuforiaSamples.ui.Common.UserInfo;


/**
 * Created by K.Oda on 2017/12/16.
 */

public class ActivityUserRegister extends Activity{


    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private UserInfo userInfo;

    private HttpRequest httpRequest;

    private EditText editText;
    private RadioGroup radioGroup;
    private Button submitBtn;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_regist_layout);

        userInfo = UserInfo.getInstance();

        //エディットテキストの設定
        editText = (EditText)findViewById(R.id.edittext_username);
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

            }
        });

        //ラジオボタンの設定
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup_sex);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                try {
                    RadioButton rb = (RadioButton)findViewById(i);
                    int sex = Integer.parseInt(String.valueOf(rb.getTag()));

                    //ユーザー情報更新
                    userInfo.setSex(sex);

                }catch (Exception e){

                }
            }
        });

        //初期-男にチェックを入れる
        RadioButton rb = (RadioButton)findViewById(R.id.radiobtn_man);
        rb.setChecked(true);


        //送信ボタンの設定
        submitBtn = (Button)findViewById(R.id.submit_userInfoBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                //入力テキストをユーザー情報に登録
                String userName = editText.getText().toString();

                //テキスト未入力でとりあえずリターン
                if (userName.trim().equals("")) {
                    return;
                }
                userInfo.setUserName(userName);

                //Httpリクエスト送信
                excuteHttpRequest();

            }
        });
    }

    private void setPreferences(){
        PrefarenceRequest.setUserPrefarence(PrefarenceRequest.PRENAME_USERINFO, this.getApplicationContext());
    }

    private void excuteHttpRequest(){
        //ユーザー名と性別をサーバーに送信
        String http_res = "";
        try {
            //Jsonオブジェクト作成
            JsonUserInfo j_user = new JsonUserInfo();
            j_user.userName = userInfo.getUserName();
            j_user.sex = userInfo.getSex();

            String json = ConvertJson.SerializeJson(j_user);

            //String url = "http://192.168.0.150:8080/kakai2017-restapi/insert/user";
            //http_res = HttpRequest.excutePost(url,json);

            httpRequest = new HttpRequest(HttpRequest.INSERT_USERINFO, json,this);
            Uri.Builder builder = new Uri.Builder();
            httpRequest.execute(builder);
            httpRequest.setOnCallBack(new HttpRequest.CallBackTask() {
                @Override
                public void CallBack(String result) {
                    //処理終了で呼び出すコールバック

                    //UserIdを取得できたらプリファレンスに登録して画面遷移
                    //ユーザーIDを取得
                    //userInfo.setUserId(result);
                    //とりあえず１を登録
                    userInfo.setUserId(result);

                    try {
                        //プリファレンスに書き込み
                        setPreferences();

                    } catch (Exception e) {
                    }

                    //ユーザー情報を渡して画面遷移する
                    Intent intent = new Intent(getApplication(), VuMark.class);
                    intent.putExtra("userInfo", userInfo);
                    startActivity(intent);
                    ActivityUserRegister.this.finish();//この画面を終了する
                }
            });
        }catch (Exception e) {

        }
    }

}
