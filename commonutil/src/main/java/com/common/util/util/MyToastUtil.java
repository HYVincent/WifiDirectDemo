package com.common.util.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.common.util.CommonUtil;
import com.common.util.R;

/**
 * @author Administrator QQ:1032006226
 * @version v1.0
 * @name EasyApp
 * @page com.common.voice.util
 * @class describe
 * @date 2018/5/15 0:16
 */
public class MyToastUtil {

    private static final String TAG = "Toast控制类";
    private static MyToast toast;
    private static MyToastUtil myToastUtil;

    private MyToastUtil(String msg, int imgId){
        if(toast == null){
            toast = new MyToast(msg,imgId);
        }
    }


    /**
     * 显示文字
     * @param msg
     */
    public static void showMsg(String msg){
        if(myToastUtil != null){
            toast.hideTvMsg(false);
            toast.hideImg(true);
            toast.resetMsg(msg);
        }else {
            myToastUtil = new MyToastUtil(msg,0);
        }
    }

    /**
     * 只显示图片
     * @param imgId
     */
    public static void showMsg(int imgId){
        if(myToastUtil != null){
            toast.hideTvMsg(true);
            toast.resetImg(imgId);
        }else {
            myToastUtil = new MyToastUtil("",imgId);
        }
    }

    /**
     * 图文都有
     * @param msg
     * @param imgId
     */
    public static void showMsg(String msg,int imgId){
        if(myToastUtil != null){
            toast.hideImg(false);
            toast.hideTvMsg(false);
            toast.resetMsg(msg);
            toast.resetImg(imgId);
        }else {
            myToastUtil = new MyToastUtil(msg,imgId);
        }
    }


    class MyToast extends Toast{

        private  ImageView ivImg;
        private  TextView tvMsg;
        private View view;
        private Context mContext;

        /**
         * Construct an empty Toast object.  You must call {@link #setView} before you
         * can call {@link #show}.
         *
         * //@param context The context to use.  Usually your {@link Application}
         *                or {@link Activity} object.
         */
        public MyToast(String msg,int imgId) {
            super(CommonUtil.getInstance().getmContext());
            mContext =CommonUtil.getInstance().getmContext();
            view = LayoutInflater.from(CommonUtil.getInstance().getmContext()).inflate(R.layout.library_layout_toast, null);
            ivImg = view.findViewById(R.id.toast_iv_icon);
            tvMsg = view.findViewById(R.id.toast_tv_msg);
            tvMsg.setText(msg);
            if(imgId != 0){
                ivImg.setImageDrawable(ContextCompat.getDrawable(CommonUtil.getInstance().getmContext(),imgId));
                ivImg.setVisibility(View.VISIBLE);
            }else {
                ivImg.setVisibility(View.GONE);
            }
            setView(view);
            show();
        }

        private void resetMsg(String msg){
            if(tvMsg != null){
                tvMsg.setText(msg);
            }
            hideImg(true);
            show();
        }

        private void resetImg(int imgId){
            if(ivImg != null&&imgId != 0){
                ivImg.setImageDrawable(ContextCompat.getDrawable(mContext,imgId));
                hideImg(false);
            }
            show();
        }

        /**
         * 隐藏或者显示图片
         * @param isHide
         */
        private void hideImg(boolean isHide) {
            if(ivImg != null){
                if(isHide){
                    ivImg.setVisibility(View.GONE);
                }else {
                    ivImg.setVisibility(View.VISIBLE);
                }
            }
        }

        /**
         * 隐藏或者显示图片
         * @param isHide
         */
        private void hideTvMsg(boolean isHide) {
            if(tvMsg != null){
                if(isHide){
                    tvMsg.setVisibility(View.GONE);
                }else {
                    tvMsg.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
