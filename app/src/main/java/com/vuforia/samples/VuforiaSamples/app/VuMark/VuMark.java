/*===============================================================================
Copyright (c) 2016 PTC Inc. All Rights Reserved.


Copyright (c) 2012-2014 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/

package com.vuforia.samples.VuforiaSamples.app.VuMark;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuforia.CameraDevice;
import com.vuforia.CustomViewerParameters;
import com.vuforia.DataSet;
import com.vuforia.HINT;
import com.vuforia.ObjectTracker;
import com.vuforia.STORAGE_TYPE;
import com.vuforia.State;
import com.vuforia.Trackable;
import com.vuforia.Tracker;
import com.vuforia.TrackerManager;
import com.vuforia.Vuforia;
import com.vuforia.samples.SampleApplication.SampleApplicationControl;
import com.vuforia.samples.SampleApplication.SampleApplicationException;
import com.vuforia.samples.SampleApplication.SampleApplicationSession;
import com.vuforia.samples.SampleApplication.utils.LoadingDialogHandler;
import com.vuforia.samples.SampleApplication.utils.SampleApplicationGLView;
import com.vuforia.samples.SampleApplication.utils.Texture;
import com.vuforia.samples.VuforiaSamples.R;
import com.vuforia.samples.VuforiaSamples.ui.ActivityList.ActivitySplashScreen;
import com.vuforia.samples.VuforiaSamples.ui.ActivityList.ActivityTabMain;
import com.vuforia.samples.VuforiaSamples.ui.ActivityList.ActivityUserRegister;
import com.vuforia.samples.VuforiaSamples.ui.Common.CommentInfo;
import com.vuforia.samples.VuforiaSamples.ui.Common.ConvertJson;
import com.vuforia.samples.VuforiaSamples.ui.Common.HttpRequest;
import com.vuforia.samples.VuforiaSamples.ui.Common.ProductInfo;
import com.vuforia.samples.VuforiaSamples.ui.Common.UserInfo;
import com.vuforia.samples.VuforiaSamples.ui.CustomView.CmtInputView;
import com.vuforia.samples.VuforiaSamples.ui.CustomView.ContentsCardView;
import com.vuforia.samples.VuforiaSamples.ui.SampleAppMenu.SampleAppMenu;
import com.vuforia.samples.VuforiaSamples.ui.SampleAppMenu.SampleAppMenuGroup;
import com.vuforia.samples.VuforiaSamples.ui.SampleAppMenu.SampleAppMenuInterface;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


public class VuMark extends Activity implements SampleApplicationControl,
        SampleAppMenuInterface
{
    private static final String LOGTAG = "VuMark";
    
    SampleApplicationSession vuforiaAppSession;
    
    private DataSet mCurrentDataset;

    // Our OpenGL view:
    private SampleApplicationGLView mGlView;
    
    // Our renderer:
    private VuMarkRenderer mRenderer;
    
    private GestureDetector mGestureDetector;
    
    // The textures we will use for rendering:
    private Vector<Texture> mTextures;
    
    private boolean mContAutofocus = false;
    private boolean mExtendedTracking = false;

    private RelativeLayout mUILayout;
    
    private SampleAppMenu mSampleAppMenu;
    
    LoadingDialogHandler loadingDialogHandler = new LoadingDialogHandler(this);

    //region Views
    private View _viewCard;
    private String readId;
    private TextView _textType;
    private TextView _textValue;
    private ImageView _instanceImageView;
    private ContentsCardView _contentCard;
    private CmtInputView _cmtInputView;
    //endregion

    // Alert Dialog used to display SDK errors
    private AlertDialog mErrorDialog;
    
    boolean mIsDroidDevice = false;


    private static ObjectMapper objectMapper = new ObjectMapper();

    // Called when the activity first starts or the user navigates back to an
    // activity.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(LOGTAG, "onCreate");
        super.onCreate(savedInstanceState);
        
        vuforiaAppSession = new SampleApplicationSession(this);
        
        startLoadingAnimation();

        vuforiaAppSession
            .initAR(this, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        mGestureDetector = new GestureDetector(this, new GestureListener());
        
        // Load any sample specific textures:
        mTextures = new Vector<Texture>();
        loadTextures();
        
        mIsDroidDevice = Build.MODEL.toLowerCase().startsWith(
            "droid");

        LayoutParams layoutParamsControl = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        LayoutInflater inflater = getLayoutInflater();
        //_viewCard = inflater.inflate(R.layout.card, null);
        _viewCard =inflater.inflate(R.layout.card, null);
        _viewCard.setVisibility(View.INVISIBLE);
        //LinearLayout cardLayout = (LinearLayout) _viewCard.findViewById(R.id.TableLayout1);
        //LinearLayout cardLayout = (LinearLayout) _viewCard.findViewById(R.id.ContentTable);
        _contentCard = (ContentsCardView)_viewCard.findViewById(R.id.ContentTable);
        LinearLayout linearLayout = (LinearLayout)_contentCard.findViewById(R.id.other_review);
        linearLayout.setVisibility(View.VISIBLE);
        ImageButton imageButton = (ImageButton)_contentCard.findViewById(R.id.icon_comment);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //コメント画面に遷移する
                //Intent
                Intent intent = null;

                intent = new Intent(VuMark.this, ActivityTabMain.class);
                startActivity(intent);

                //商品カードを削除
                hideCard();

            }
        });

        _contentCard.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                   // hideCard();
                    return true;
                }
                return false;
            }
        });
        addContentView(_viewCard, layoutParamsControl);

        //_textType = (TextView) _viewCard.findViewById(R.id.text_type);
        //_textValue = (TextView) _viewCard.findViewById(R.id.text_value);
        //_instanceImageView = (ImageView) _viewCard.findViewById(R.id.instance_image);
    }
    
    // Process Single Tap event to trigger autofocus
    private class GestureListener extends
        GestureDetector.SimpleOnGestureListener
    {
        // Used to set autofocus one second after a manual focus is triggered
        private final Handler autofocusHandler = new Handler();
        
        
        @Override
        public boolean onDown(MotionEvent e)
        {
            return true;
        }
        
        
        @Override
        public boolean onSingleTapUp(MotionEvent e)
        {
            // Generates a Handler to trigger autofocus
            // after 1 second
            autofocusHandler.postDelayed(new Runnable()
            {
                public void run()
                {
                    boolean result = CameraDevice.getInstance().setFocusMode(
                        CameraDevice.FOCUS_MODE.FOCUS_MODE_TRIGGERAUTO);
                    
                    if (!result)
                        Log.e("SingleTapUp", "Unable to trigger focus");
                }
            }, 1000L);
            
            return true;
        }
    }
    
    
    // We want to load specific textures from the APK, which we will later use
    // for rendering.
    
    private void loadTextures()
    {
        mTextures.add(Texture.loadTextureFromApk("vumark_texture.png",
                getAssets()));
    }
    
    
    // Called when the activity will start interacting with the user.
    @Override
    protected void onResume()
    {
        Log.d(LOGTAG, "onResume");
        super.onResume();
        
        // This is needed for some Droid devices to force portrait
        if (mIsDroidDevice)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        
        try
        {
            vuforiaAppSession.resumeAR();
        } catch (SampleApplicationException e)
        {
            Log.e(LOGTAG, e.getString());
        }
        
        // Resume the GL view:
        if (mGlView != null)
        {
            mGlView.setVisibility(View.VISIBLE);
            mGlView.onResume();
        }
    }
    
    
    // Callback for configuration changes the activity handles itself
    @Override
    public void onConfigurationChanged(Configuration config)
    {
        Log.d(LOGTAG, "onConfigurationChanged");
        super.onConfigurationChanged(config);
        
        vuforiaAppSession.onConfigurationChanged();
    }
    
    
    // Called when the system is about to start resuming a previous activity.
    @Override
    protected void onPause()
    {
        Log.d(LOGTAG, "onPause");
        super.onPause();
        
        if (mGlView != null)
        {
            mGlView.setVisibility(View.INVISIBLE);
            mGlView.onPause();
        }

        try
        {
            vuforiaAppSession.pauseAR();
        } catch (SampleApplicationException e)
        {
            Log.e(LOGTAG, e.getString());
        }
    }
    
    
    // The final call you receive before your activity is destroyed.
    @Override
    protected void onDestroy()
    {
        Log.d(LOGTAG, "onDestroy");
        super.onDestroy();
        
        try
        {
            vuforiaAppSession.stopAR();
        } catch (SampleApplicationException e)
        {
            Log.e(LOGTAG, e.getString());
        }
        
        // Unload texture:
        mTextures.clear();
        mTextures = null;
        
        System.gc();
    }
    
    
    // Initializes AR application components.
    private void initApplicationAR()
    {
        // Create OpenGL ES view:
        int depthSize = 16;
        int stencilSize = 0;
        boolean translucent = Vuforia.requiresAlpha();
        
        mGlView = new SampleApplicationGLView(this);
        mGlView.init(translucent, depthSize, stencilSize);
        
        mRenderer = new VuMarkRenderer(this, vuforiaAppSession);
        mRenderer.setTextures(mTextures);
        mGlView.setRenderer(mRenderer);
        
    }


    private void startLoadingAnimation()
    {
        mUILayout = (RelativeLayout) View.inflate(this, R.layout.camera_overlay_reticle,
            null);
        
        mUILayout.setVisibility(View.VISIBLE);
        mUILayout.setBackgroundColor(Color.BLACK);
        
        // Gets a reference to the loading dialog
        loadingDialogHandler.mLoadingDialogContainer = mUILayout
            .findViewById(R.id.loading_indicator);
        
        // Shows the loading indicator at start
        loadingDialogHandler
            .sendEmptyMessage(LoadingDialogHandler.SHOW_LOADING_DIALOG);
        
        // Adds the inflated layout to the view
        addContentView(mUILayout, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        //コメント入力エリアを設定
        _cmtInputView = (CmtInputView)mUILayout.findViewById(R.id.cmtInputArea);
        //コメント入力を無効化
        _cmtInputView.setEnabled(false);
        //コールバックを設定
        _cmtInputView.setOnCallBack(new CmtInputView.CallBackTask() {
            @Override
            public void CallBack(int result) {

            }

            @Override
            public void SabmitCallBack(int result, CommentInfo cInfo) {

            }
        });

        //テスト用ボタンを設置
        Button testbutton = (Button)mUILayout.findViewById(R.id.test_btn);
        testbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_viewCard.getVisibility() == View.VISIBLE) {
                    hideCard();
                    return ;
                }

                showCard("","test",null);

            }
        });
        
    }

    /**
     * 読み込み情報を設定
     * @param type
     * @param value
     * @param bitmap
     */
    void showCard(final String type, final String value, final Bitmap bitmap) {
        final Context context = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // if scard is already visible with same VuMark, do nothing
                if ( readId != null  && readId.equals(value)) {
                    return;
                }

                readId = value;
                final Animation bottomUp = AnimationUtils.loadAnimation(context,
                        R.anim.bottom_up);

                //コメント入力を有効化
                _cmtInputView.setEnabled(true);

                Resources res = getResources();

                try {
                   /* // 読み取り結果取得
                    int valueId = res.getIdentifier("read_text_" + value, "array", getPackageName());
                    List<String> valueList = Arrays.asList(res.getStringArray(valueId));

                    // タイトル取得
                    int titleId = res.getIdentifier("read_text_title", "array", getPackageName());
                    List<String> titleList = Arrays.asList(res.getStringArray(titleId));

                    // テーブル設定
                    setTableRow(titleList, valueList);*/
                    //テスト用　商品データの受け取り
                    //マーカーIDをjsonにシリアライズ
                    ProductInfo marker = new ProductInfo();
                    marker.markerId = Integer.parseInt(readId);
                    String markerId_json = ConvertJson.SerializeJson(marker);
                    //HttpRequest
                    Uri.Builder builder = new Uri.Builder();
                    HttpRequest httpRequest = new HttpRequest(HttpRequest.SELECT_CONTENTS,markerId_json,null);
                    httpRequest.progressType = HttpRequest.ProgressType.NOPROGRESS;
                    httpRequest.execute(builder);
                    httpRequest.setOnCallBack(new HttpRequest.CallBackTask() {
                        @Override
                        public void CallBack(String result) {

                            //Json →　デシリアライズ
                            ProductInfo productInfo = new ProductInfo();

                            String jsonStr = result;
                           /* jsonStr = "{\"description\":\"MAHOT COFFEEの想いが詰まったブレンド\"," +
                                    "\"delInfo\":\"しっかりとしたビターなコクがあり、赤ワインのような上品なボディー。アクセントに完熟したチェリーのような風味がアフターテイストで楽しめます。,●●●●○,●●○○○,●●○○○,700円\\/100ｇ　1300円\\/200ｇ,370円,370円\"," +
                                    "\"contentsName\":\"MAHOT ブレンド　SONE\"," +
                                    "\"image\":\"kakai4/privateWebDau/Root/image/sample1.png\"," +
                                    "\"indexInfo\":\"商品名,キャッチコピー,詳細,コク,甘味,酸味,販売価格,ドリップコーヒー価格,カフェオレ価格\"}";*/

                            productInfo = ConvertJson.DeserializeJsonToProductInfo(jsonStr);

                            productInfo.indexInfo = "商品名,キャッチコピー,詳細,コク,甘味,酸味,販売価格,ドリップコーヒー価格,カフェオレ価格";

                            UserInfo.getInstance().ConvertProductInfo(productInfo);

                            //商品情報でカードを作成
                            _contentCard.setContentsInfo(UserInfo.getInstance().getProductInfoMap());

                            _viewCard.bringToFront();
                            _viewCard.setVisibility(View.VISIBLE);
                            _viewCard.startAnimation(bottomUp);
                        }
                    });


                    // mUILayout.invalidate();
                }catch (Exception e){

                }

            }
        });
    }

    /**
     * 読み取り結果を設定
     * @param titleList　タイトルリスト
     * @param valueList　読み取り結果理リスト
     */
    public void setTableRow(List<String> titleList, List<String> valueList){
        /*ViewGroup vg = (ViewGroup)findViewById(R.id.TableLayout1);
        for (int i = 0 ; i < valueList.size() ; i++) {
            getLayoutInflater().inflate(R.layout.tablerow_layout, vg);
            TableRow tr = (TableRow) vg.getChildAt(i);
            ((TextView) (tr.getChildAt(0))).setText(titleList.get(i));
            ((TextView) (tr.getChildAt(1))).setText(valueList.get(i));
        }*/
    }

    /**
     * TableLayoutを初期化
     */
    public void clearTableRow() {

        _contentCard.clearTable();
       /* ViewGroup vg = (ViewGroup)findViewById(R.id.TableLayout1);
        vg.removeAllViews();*/
    }

    void hideCard() {
        final Context context = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            // if card not visible, do nothing
            if (_viewCard.getVisibility() != View.VISIBLE) {
                return;
            }

            //コメント入力を無効化
                _cmtInputView.setEnabled(false);

            readId = null;

            //_textType.setText("");
            //_textValue.setText("");
            clearTableRow();
            Animation bottomDown = AnimationUtils.loadAnimation(context,
                    R.anim.bottom_down);

            _viewCard.startAnimation(bottomDown);
            _viewCard.setVisibility(View.INVISIBLE);
            // mUILayout.invalidate();
            }
        });
    }

    // Methods to load and destroy tracking data.
    @Override
    public boolean doLoadTrackersData()
    {
        TrackerManager tManager = TrackerManager.getInstance();
        ObjectTracker objectTracker = (ObjectTracker) tManager
            .getTracker(ObjectTracker.getClassType());
        if (objectTracker == null)
            return false;
        
        if (mCurrentDataset == null)
            mCurrentDataset = objectTracker.createDataSet();
        
        if (mCurrentDataset == null)
            return false;
        
        if (!mCurrentDataset.load(
                "testT.xml",
            STORAGE_TYPE.STORAGE_APPRESOURCE))
            return false;
        
        if (!objectTracker.activateDataSet(mCurrentDataset))
            return false;
        
        return true;
    }
    
    
    @Override
    public boolean doUnloadTrackersData()
    {
        // Indicate if the trackers were unloaded correctly
        boolean result = true;
        
        TrackerManager tManager = TrackerManager.getInstance();
        ObjectTracker objectTracker = (ObjectTracker) tManager
            .getTracker(ObjectTracker.getClassType());
        if (objectTracker == null)
            return false;
        
        if (mCurrentDataset != null && mCurrentDataset.isActive())
        {
            if (objectTracker.getActiveDataSet(0).equals(mCurrentDataset)
                && !objectTracker.deactivateDataSet(mCurrentDataset))
            {
                result = false;
            } else if (!objectTracker.destroyDataSet(mCurrentDataset))
            {
                result = false;
            }
            
            mCurrentDataset = null;
        }
        
        return result;
    }
    
    
    @Override
    public void onInitARDone(SampleApplicationException exception)
    {
        
        if (exception == null)
        {
            initApplicationAR();

            mRenderer.setActive(true);

            // Now add the GL surface view. It is important
            // that the OpenGL ES surface view gets added
            // BEFORE the camera is started and video
            // background is configured.
            addContentView(mGlView, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
            
            // Sets the UILayout to be drawn in front of the camera
            mUILayout.bringToFront();
            
            // Sets the layout background to transparent
            mUILayout.setBackgroundColor(Color.TRANSPARENT);
            
            try
            {
                vuforiaAppSession.startAR(CameraDevice.CAMERA_DIRECTION.CAMERA_DIRECTION_DEFAULT);
            } catch (SampleApplicationException e)
            {
                Log.e(LOGTAG, e.getString());
            }
            
            boolean result = CameraDevice.getInstance().setFocusMode(
                CameraDevice.FOCUS_MODE.FOCUS_MODE_CONTINUOUSAUTO);
            
            if (result)
                mContAutofocus = true;
            else
                Log.e(LOGTAG, "Unable to enable continuous autofocus");
            
            mSampleAppMenu = new SampleAppMenu(this, this, "VuMark",
                mGlView, mUILayout, null);
            setSampleAppMenuSettings();
            
        } else
        {
            Log.e(LOGTAG, exception.getString());
            showInitializationErrorMessage(exception.getString());
        }
    }
    
    
    // Shows initialization error messages as System dialogs
    public void showInitializationErrorMessage(String message)
    {
        final String errorMessage = message;
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                if (mErrorDialog != null)
                {
                    mErrorDialog.dismiss();
                }
                
                // Generates an Alert Dialog to show the error message
                AlertDialog.Builder builder = new AlertDialog.Builder(
                    VuMark.this);
                builder
                    .setMessage(errorMessage)
                    .setTitle(getString(R.string.INIT_ERROR))
                    .setCancelable(false)
                    .setIcon(0)
                    .setPositiveButton("OK",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                finish();
                            }
                        });
                
                mErrorDialog = builder.create();
                mErrorDialog.show();
            }
        });
    }
    
    
    @Override
    public void onVuforiaUpdate(State state)
    {
    }
    
    
    @Override
    public boolean doInitTrackers()
    {
        // Indicate if the trackers were initialized correctly
        boolean result = true;
        
        TrackerManager tManager = TrackerManager.getInstance();
        Tracker tracker;
        
        // Trying to initialize the image tracker
        tracker = tManager.initTracker(ObjectTracker.getClassType());
        if (tracker == null)
        {
            Log.e(
                LOGTAG,
                "Tracker not initialized. Tracker already initialized or the camera is already started");
            result = false;
        } else
        {
            Log.i(LOGTAG, "Tracker successfully initialized");
        }
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 10);
        return result;
    }
    
    
    @Override
    public boolean doStartTrackers()
    {
        // Indicate if the trackers were started correctly
        boolean result = true;
        
        Tracker objectTracker = TrackerManager.getInstance().getTracker(
            ObjectTracker.getClassType());
        if (objectTracker != null)
            objectTracker.start();
        
        return result;
    }
    
    
    @Override
    public boolean doStopTrackers()
    {
        // Indicate if the trackers were stopped correctly
        boolean result = true;
        
        Tracker objectTracker = TrackerManager.getInstance().getTracker(
            ObjectTracker.getClassType());
        if (objectTracker != null)
            objectTracker.stop();
        
        return result;
    }
    
    
    @Override
    public boolean doDeinitTrackers()
    {
        // Indicate if the trackers were deinitialized correctly
        boolean result = true;
        
        TrackerManager tManager = TrackerManager.getInstance();
        tManager.deinitTracker(ObjectTracker.getClassType());
        
        return result;
    }
    
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        // Process the Gestures
        /*if (mSampleAppMenu != null && mSampleAppMenu.processEvent(event))
            return true;
        
        return mGestureDetector.onTouchEvent(event);
        */
        return false;
    }
    
    
    boolean isExtendedTrackingActive()
    {
        return mExtendedTracking;
    }
    
    final public static int CMD_BACK = -1;
    final public static int CMD_EXTENDED_TRACKING = 1;

    // This method sets the menu's settings
    private void setSampleAppMenuSettings()
    {
        SampleAppMenuGroup group;
        
        group = mSampleAppMenu.addGroup("", false);
        group.addTextItem(getString(R.string.menu_back), -1);
        
        group = mSampleAppMenu.addGroup("", true);
        group.addSelectionItem(getString(R.string.menu_extended_tracking),
            CMD_EXTENDED_TRACKING, false);

        mSampleAppMenu.attachMenu();
    }
    
    
    @Override
    public boolean menuProcess(int command)
    {
        
        boolean result = true;
        
        switch (command)
        {
            case CMD_BACK:
                finish();
                break;

            case CMD_EXTENDED_TRACKING:
                for (int tIdx = 0; tIdx < mCurrentDataset.getNumTrackables(); tIdx++)
                {
                    Trackable trackable = mCurrentDataset.getTrackable(tIdx);
                    
                    if (!mExtendedTracking)
                    {
                        if (!trackable.startExtendedTracking())
                        {
                            Log.e(LOGTAG,
                                "Failed to start extended tracking target");
                            result = false;
                        } else
                        {
                            Log.d(LOGTAG,
                                "Successfully started extended tracking target");
                        }
                    } else
                    {
                        if (!trackable.stopExtendedTracking())
                        {
                            Log.e(LOGTAG,
                                "Failed to stop extended tracking target");
                            result = false;
                        } else
                        {
                            Log.d(LOGTAG,
                                "Successfully started extended tracking target");
                        }
                    }
                }
                
                if (result)
                    mExtendedTracking = !mExtendedTracking;
                
                break;
            
            default:
                break;
        }
        
        return result;
    }
}
