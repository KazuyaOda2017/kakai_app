package com.vuforia.samples.VuforiaSamples.ui.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aquat on 2017/12/28.
 */

public class CmtDataList {

    public int cmtCount;

    public int offset;

    public List<CommentInfo> dispList;

    public int markerId;

    public String userId;

   public TComment userComment;

    public CmtDataList() {
        dispList = new ArrayList<CommentInfo>();
        userComment = new TComment();
    }

    public class TComment{

       // public int markerId;

        public  String userName;

        public String userId;

        public String userCmt;

        public double star;

        public Integer sex;

        //public  String commentId;

      //  public boolean displayFlg;

        //public boolean deleteFlg;

        public String insertDate;

      //  public String updateDate;
/*
        "commentId":null
                "markerId":null
                "userId":null
                "userCmt":null
                "star":null
                "displayFlg":null
                "deleteFlg":null
                "insertDate":null
                "updateDate":null}}*/
    }
}


