package com.common.view.simple;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import com.common.view.BaseDialog;
import com.common.view.R;


/**
 * @author Vincent QQ:1032006226
 * @version v1.0
 * @name StartKangMedical_Android
 * @page com.toncentsoft.starkangmedical_android.view
 * @class describe
 * @date 2018/4/23 10:10
 */
public class RecordStyleDialog extends BaseDialog {

    private ImageView iv;
    private double db;
    private Context mContext;
    private static final String TAG = "录音dialog";

    public RecordStyleDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
        setContentView(R.layout.library_dialog_layout_record);
        setDimAmount(0);
        setCanceledOnTouchOutside(false);
        iv = findViewById(R.id.library_dialog_layout_record);
        iv.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.dialog_icon_record_1));
    }

    public boolean isShowDialog;

    public boolean isShowDialog() {
        return isShowDialog;
    }

    public void showDialog(){
        isShowDialog = true;
        show();
    }

    public void dismissDialog(){
        isShowDialog = false;
        dismiss();
        hide();
    }

    /**
     * 0 dB 到90.3 dB。
     * @param db
     */
    public void upDbSize(double db){
        if(iv == null || mContext == null){
            return;
        }
        /*if(db != 0.0) {
            Log.d(TAG, "upDbSize: " + String.valueOf(db));
        }*/
       if(db <30){
            iv.setImageDrawable(ContextCompat.getDrawable(mContext,R.mipmap.dialog_icon_record_1));
       }else if(db >30 && db <44){
           iv.setImageDrawable(ContextCompat.getDrawable(mContext,R.mipmap.dialog_icon_record_2));
       }else if(db >44 && db <58){
           iv.setImageDrawable(ContextCompat.getDrawable(mContext,R.mipmap.dialog_icon_record_3));
       }else if(db >58 && db <72){
           iv.setImageDrawable(ContextCompat.getDrawable(mContext,R.mipmap.dialog_icon_record_4));
       }else if(db >72){
           iv.setImageDrawable(ContextCompat.getDrawable(mContext,R.mipmap.dialog_icon_record_5));
       }
    }

}
