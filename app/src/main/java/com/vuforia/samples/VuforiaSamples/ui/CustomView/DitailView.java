package com.vuforia.samples.VuforiaSamples.ui.CustomView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vuforia.samples.VuforiaSamples.R;

/**
 * Created by aquat on 2018/01/09.
 */

public class DitailView extends LinearLayout {


    protected TextView titleTextView;
    protected TextView contentsTextView;

    /**
     * コンストラクタ
     * @param context
     * @param attrs
     */
    public DitailView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        View layout = LayoutInflater.from(context).inflate(R.layout.ditail_item_layout,this);

        titleTextView = (TextView)layout.findViewById(R.id.title_text);
        contentsTextView = (TextView)layout.findViewById(R.id.contents_text);

    }

    /**
     * テキストのセット
     * @param title
     * @param contents
     */
    public void setText(String title,String contents){

        try{

            titleTextView.setText(title);
            contentsTextView.setText(contents);

        }catch (Exception e){

        }
    }
}
