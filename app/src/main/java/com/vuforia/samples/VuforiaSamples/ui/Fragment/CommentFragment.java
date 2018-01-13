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

import java.util.HashMap;

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

    protected int selectStar;

    private SwipeRefreshLayout mSwipeRefresh;

    //コメントデータ
    public CmtDataList _commentData;


    private static ObjectMapper objectMapper = new ObjectMapper();

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
        CmtInputView cmtInputView = (CmtInputView)layout.findViewById(R.id.input_area);
        cmtInputView.setStar(2);//評価ボタン初期化
        cmtInputView.setOnCallBack(new CmtInputView.CallBackTask() {
            @Override
            public void CallBack(int result) {

            }

            @Override
            public void SabmitCallBack(int result, CommentInfo cInfo) {
                //コメントを追加する
                adapter.add(cInfo);

                //スクロール制御
                //スクロールを一番下へ
                int itemCount = listView.getCount();
                listView.setSelection(itemCount -1);

                //テキストボックスからフォーカスを外す
                layout.requestFocus();
            }
        });


        //コメントデータの取得
        getCommentData(0);
     /*   //取得コメントを表示する
        setCmtDataTimeLine(_commentData);*/
        layout.requestFocus();

        return layout;

    }

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

        //コメントの数だけアダプターに追加
      /*  for(CommentInfo info:commentData.dispList){
            adapter.add(info);
        }
*/
    }
    //endregion

    //region コメントデータの追加読み込み
    private boolean ReloadCommentData() {

        try {

            //追加取得のデータ
            String commentjsonStr = "{\"dispList\":[{\"insertDate\":\"2017.11.25\",\"sex\":0,\"star\":5,\"userCmt\":\"最高\",\"userId\":6,\"userName\":\"小田\"},{\"insertDate\":\"2017.12.1\",\"sex\":0,\"star\":3,\"userCmt\":\"苦かった\",\"userId\":2,\"userName\":\"田中\"},{\"insertDate\":\"2017.12.5\",\"sex\":0,\"star\":2,\"userCmt\":\"いまいち\",\"userId\":3,\"userName\":\"中橋\"},{\"insertDate\":\"2017.12.4\",\"sex\":0,\"star\":4,\"userCmt\":\"また来ます\",\"userId\":4,\"userName\":\"中野\"},{\"insertDate\":\"2017.12.5\",\"sex\":1,\"star\":1,\"userCmt\":\"まぁまぁ\",\"userId\":5,\"userName\":\"溝辺\"}],\"offset\":0,\"totalNumber\":5}";

            CmtDataList cData = new CmtDataList();
            cData = ConvertJson.DeserializeJsonToCmtDataList(commentjsonStr);


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
