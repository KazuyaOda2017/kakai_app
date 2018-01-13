package com.vuforia.samples.VuforiaSamples.ui.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.vuforia.samples.VuforiaSamples.R;
import com.vuforia.samples.VuforiaSamples.ui.Common.ProductInfo;

import java.util.HashMap;

/**
 * Created by aquat on 2017/12/25.
 */

public class ContentsCardView extends LinearLayout {

    //region 変数
    private TableLayout tableLayout;
    private ImageButton imageButton;
    private ViewGroup vg;

    private Context _context;

    //endregion

    /**
     * コンストラクタ
     * @param context
     * @param attrs
     */
    public ContentsCardView(Context context,  AttributeSet attrs) {
        super(context, attrs);

        _context = context;

        //レイアウトを取得する
        View layout = LayoutInflater.from(context).inflate(R.layout.contents_card,this);

        //TableLayoutの取得
        tableLayout = (TableLayout)layout.findViewById(R.id.product_table);

        //ImageButtonを取得
        imageButton = (ImageButton)layout.findViewById(R.id.icon_comment);



    }

    /**
     * コメントアイコンの表示設定
     * @param visibility
     */
    public void setCmtIconVisibility(int visibility){
        imageButton.setVisibility(visibility);
    }

    /**
     * 商品情報をテーブルにセットする
     * @param productInfo
     */
    public void setContentsInfo(HashMap<String,String> productInfo){
        try {
            //TableLayoutをViewGropに設定
             vg = (ViewGroup)tableLayout;

            //商品名　キャッチコピー　販売価格　ドリップコーヒー価格を表示する
            int row = 0;
            for(String index: ProductInfo.cardIndexNames) {
                LayoutInflater.from(_context).inflate(R.layout.tablerow_layout, vg);
                TableRow tr = (TableRow) vg.getChildAt(row);

                TextView indexView = (TextView) tr.getChildAt(0);
                TextView valueView = (TextView)tr.getChildAt(1);

                indexView.setText(index);
                valueView.setText(productInfo.get(index));
                //一行目はマージンを調整
                if(row == 0){
                    ViewGroup.LayoutParams lp = indexView.getLayoutParams();
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)lp;
                    mlp.setMargins(1,1,1,1);
                    indexView.setLayoutParams(mlp);

                    lp = valueView.getLayoutParams();
                    mlp = (ViewGroup.MarginLayoutParams)lp;
                    mlp.setMargins(0,1,1,1);
                    valueView.setLayoutParams(mlp);
                }
                row ++ ;

            }

        }catch (Exception e){

        }
    }

    /**
     * テーブルをクリアする
     */
    public void clearTable(){
        try{

            if(vg != null){
                vg.removeAllViews();
            }

        }catch (Exception e){

        }
    }


}
