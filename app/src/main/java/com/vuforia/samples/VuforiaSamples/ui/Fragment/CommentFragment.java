package com.vuforia.samples.VuforiaSamples.ui.Fragment;


import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuforia.samples.VuforiaSamples.R;
import com.vuforia.samples.VuforiaSamples.ui.Adapter.CommentListAdapter;
import com.vuforia.samples.VuforiaSamples.ui.Common.CmtDataList;
import com.vuforia.samples.VuforiaSamples.ui.Common.CommentInfo;
import com.vuforia.samples.VuforiaSamples.ui.Common.ConvertJson;
import com.vuforia.samples.VuforiaSamples.ui.Common.HttpRequest;
import com.vuforia.samples.VuforiaSamples.ui.Common.ProductInfo;
import com.vuforia.samples.VuforiaSamples.ui.Common.UserInfo;
import com.vuforia.samples.VuforiaSamples.ui.CustomView.CmtInputView;
import com.vuforia.samples.VuforiaSamples.ui.CustomView.ContentsCardView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by aquat on 2017/12/28.
 */


public class CommentFragment extends Fragment{

    protected ListView listView;
    protected EditText input_text;
    protected ImageButton submitBtn;
    protected CommentListAdapter adapter;
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    protected ViewGroup vg_productCard;

    // protected List<ImageButton> starBtnList;

    View layout;

    private SwipeRefreshLayout mSwipeRefresh;
    //コメントデータ
    public CmtDataList _commentData;
    private  CmtInputView cmtInputView;
    private static ObjectMapper objectMapper = new ObjectMapper();

    //初期化フラグ
    private boolean isInitialize = true;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        //レイアウトを取得
        layout = inflater.inflate(R.layout.cmt_fragment, null);

        //ユーザー情報の作成
       /* userInfo = new UserInfo();
        userInfo.setUserId(1);
        userInfo.setUserName("K.Oda");*/

        //アダプターの作成
        adapter = new CommentListAdapter(this.getActivity().getApplicationContext());



        //region 商品情報カード作成
        ContentsCardView contentsCardView = (ContentsCardView)layout.findViewById(R.id.productCard) ;
        contentsCardView.setContentsInfo(UserInfo.getInstance().getProductInfoMap());
        //アイコンを非表示にする
        contentsCardView.setCmtIconVisibility(View.INVISIBLE);

        //endregion

        //region ListViewの設定
        //ListView生成
        listView = (ListView)layout.findViewById(R.id.comment_list);
        //境界線をなくす
        listView.setDivider(null);
        listView.setPadding(10,10,10,10);
        //アダプターをセット
        listView.setAdapter(adapter);
        //endregion

        //region SwipeRefreshの設定
        mSwipeRefresh = (SwipeRefreshLayout)layout.findViewById(R.id.swipe_refresh) ;
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 引っ張って離した時に呼ばれます。
                //loadData();
                ReloadCommentData();
            }
        });
        //endregion



        //コメント入力エリアの設定
        cmtInputView = (CmtInputView)layout.findViewById(R.id.input_area);
        cmtInputView.setStar(2);//評価ボタン初期化
        cmtInputView.setOnCallBack(new CmtInputView.CallBackTask() {
            @Override
            public void CallBack(int result) {

            }

            @Override
            public void SabmitCallBack(int result, CommentInfo cInfo) {
                //コメントを追加する
                //コメントデータをシリアライズ
                 final String requestStr = ConvertJson.SerializeJson(cInfo);
                //HttpRequest
                HttpRequest httpRequest = new HttpRequest(HttpRequest.EDIT_COMMENT,requestStr,null);
                Uri.Builder builder = new Uri.Builder();
                httpRequest.progressType = HttpRequest.ProgressType.NOPROGRESS;
                httpRequest.execute(builder);
                httpRequest.setOnCallBack(new HttpRequest.CallBackTask() {
                    @Override
                    public void CallBack(String result) {

                        //String jsonStr = result;//"{\"dispList\":[{\"insertDate\":\"2017.11.25\",\"sex\":0,\"star\":5,\"userCmt\":\"美味しかった\",\"userId\":1,\"userName\":\"小田\"},{\"insertDate\":\"2017.12.1\",\"sex\":0,\"star\":3,\"userCmt\":\"苦かった\",\"userId\":2,\"userName\":\"田中\"},{\"insertDate\":\"2017.12.5\",\"sex\":0,\"star\":2,\"userCmt\":\"いまいち\",\"userId\":3,\"userName\":\"中橋\"},{\"insertDate\":\"2017.12.4\",\"sex\":0,\"star\":4,\"userCmt\":\"また来ます\",\"userId\":4,\"userName\":\"中野\"},{\"insertDate\":\"2017.12.5\",\"sex\":1,\"star\":1,\"userCmt\":\"まぁまぁ\",\"userId\":5,\"userName\":\"溝辺\"}],\"offset\":0,\"totalNumber\":5}";

                        if(result.equals("0")){
                            //書き込み成功で画面を初期化する
                            Initialize();

                        }else {
                            //失敗時の処理
                        }
                        //_commentData = ConvertJson.DeserializeJsonToCmtDataList(commentjsonStr);

                        //オフセット値を取得
                        //取得コメントを表示する
                        //setCmtDataTimeLine(_commentData);
                    }
                });
                //adapter.add(cInfo);



                //テキストボックスからフォーカスを外す
                layout.requestFocus();
            }
        });


      /*  //コメントデータの取得
        _commentData =  new CmtDataList();
        getCommentData(0);
     *//*   //取得コメントを表示する
        setCmtDataTimeLine(_commentData);*/
        Initialize();
        layout.requestFocus();

        return layout;

    }

    //region 画面初期化
    private void Initialize() {
        try{

            isInitialize = true;

            //アダプタークリア
            adapter.clear();

            //コメントデータ再取得
            _commentData = new CmtDataList();

            getCommentData(0);


        }catch (Exception e){

        }

    }
    //endregion

    //region コメントデータ取得処理
    private boolean getCommentData(int offset){

        try{

            //コメントデータを取得してフィールドに投げる

            CmtDataList cmtdata = new CmtDataList();
            cmtdata.userId = UserInfo.getInstance().getUserId();
            cmtdata.markerId = Integer.parseInt(UserInfo.getInstance().getProductInfoMap().get("MarkerID"));
            cmtdata.offset = offset;

            final String requestStr = ConvertJson.SerializeJson(cmtdata);

            //HttpRequest
            HttpRequest httpRequest = new HttpRequest(HttpRequest.SELECT_COMMENT,requestStr,null);
            Uri.Builder builder = new Uri.Builder();
            httpRequest.progressType = HttpRequest.ProgressType.NOPROGRESS;
            httpRequest.execute(builder);
            httpRequest.setOnCallBack(new HttpRequest.CallBackTask() {
                @Override
                public void CallBack(String result) {

                    String commentjsonStr = result;//"{\"dispList\":[{\"insertDate\":\"2017.11.25\",\"sex\":0,\"star\":5,\"userCmt\":\"美味しかった\",\"userId\":1,\"userName\":\"小田\"},{\"insertDate\":\"2017.12.1\",\"sex\":0,\"star\":3,\"userCmt\":\"苦かった\",\"userId\":2,\"userName\":\"田中\"},{\"insertDate\":\"2017.12.5\",\"sex\":0,\"star\":2,\"userCmt\":\"いまいち\",\"userId\":3,\"userName\":\"中橋\"},{\"insertDate\":\"2017.12.4\",\"sex\":0,\"star\":4,\"userCmt\":\"また来ます\",\"userId\":4,\"userName\":\"中野\"},{\"insertDate\":\"2017.12.5\",\"sex\":1,\"star\":1,\"userCmt\":\"まぁまぁ\",\"userId\":5,\"userName\":\"溝辺\"}],\"offset\":0,\"totalNumber\":5}";
                    //Json文字列をデシリアライズ

                    _commentData = ConvertJson.DeserializeJsonToCmtDataList(commentjsonStr);
                    List<CommentInfo> newList = new ArrayList<>();

                    try{
                        //取得したdispListの順序を逆にする
                        for(int i = _commentData.dispList.size()-1; i >= 0 ; i --){
                            newList.add(_commentData.dispList.get(i));
                        }
                    }catch (Exception e){

                    }
                    //リスト入れ替え
                    _commentData.dispList = newList;

                    //自分のコメントがあったらテキストボックスに文字を入れる
                    if( _commentData.userComment.userId != null){

                        //コメント
                        cmtInputView.setText(_commentData.userComment.userCmt);
                        //評価
                        cmtInputView.setStar(((int)_commentData.userComment.star) - 1);
                    }

                    //オフセット値を取得
                    //取得コメントを表示する
                    setCmtDataTimeLine(_commentData);
                }
            });

            return true;
        }catch (Exception e){
            return false;
        }


    }
    //endregion

    //region コメント表示処理
    private void setCmtDataTimeLine(CmtDataList commentData){

       /* //コメントの数だけアダプターに追加
        for(CommentInfo info:commentData.dispList){
            adapter.add(info);
        }*/

          //上にインサート
            int index = commentData.dispList.size() - 1;
            for (int i = index; i >= 0; i--) {
                adapter.insert(commentData.dispList.get(i), 0);
            }

            if(isInitialize){
                //スクロール制御
                //スクロールを一番下へ
                int itemCount = listView.getCount();
                listView.setSelection(itemCount -1);
            }
    }
    //endregion

    //region コメント追加処理
    private void insertCmtDataTimeLine(CmtDataList commentData){

        //コメントの数だけ上にインサートする

    }
    //endregion

    //region コメントデータの追加読み込み
    private boolean ReloadCommentData() {

        try {

            isInitialize = false;
            //現在取得中のコメントデータのオフセット値を取得
            int offset = 0;

            if(_commentData != null){
                offset = _commentData.offset;
            }

            //オフセット値を指定して追加取得のデータ
            getCommentData(offset);
            /*String commentjsonStr = "{\"dispList\":[{\"insertDate\":\"2017.11.25\",\"sex\":0,\"star\":5,\"userCmt\":\"最高\",\"userId\":6,\"userName\":\"小田\"},{\"insertDate\":\"2017.12.1\",\"sex\":0,\"star\":3,\"userCmt\":\"苦かった\",\"userId\":2,\"userName\":\"田中\"},{\"insertDate\":\"2017.12.5\",\"sex\":0,\"star\":2,\"userCmt\":\"いまいち\",\"userId\":3,\"userName\":\"中橋\"},{\"insertDate\":\"2017.12.4\",\"sex\":0,\"star\":4,\"userCmt\":\"また来ます\",\"userId\":4,\"userName\":\"中野\"},{\"insertDate\":\"2017.12.5\",\"sex\":1,\"star\":1,\"userCmt\":\"まぁまぁ\",\"userId\":5,\"userName\":\"溝辺\"}],\"offset\":0,\"totalNumber\":5}";

            CmtDataList cData = new CmtDataList();
            cData = ConvertJson.DeserializeJsonToCmtDataList(commentjsonStr);*/


            //adapter.addAll(commentData.dispList);
          /*  //上にインサート
            int index = cData.dispList.size() - 1;
            for (int i = index; i >= 0; i--) {
                adapter.insert(cData.dispList.get(i), 0);
            }
*/
            //setCmtDataTimeLine(cData);

            if (mSwipeRefresh.isRefreshing()) {
                mSwipeRefresh.setRefreshing(false);
            }

            return true;
        } catch (Exception e) {
            if (mSwipeRefresh.isRefreshing()) {
                mSwipeRefresh.setRefreshing(false);
            }
            return false;
        }


    }
    //endregion


}
