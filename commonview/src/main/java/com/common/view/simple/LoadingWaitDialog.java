package com.common.view.simple;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.common.view.BaseDialog;
import com.common.view.R;
import com.common.view.util.DpUtil;

/**
 * @author Vincent QQ:1032006226
 * @version v1.0
 * @name EasyApp
 * @page com.common.view.simple
 * @class describe
 * @date 2018/5/17 17:50
 */

public class LoadingWaitDialog extends BaseDialog implements View.OnClickListener{

    private Context mContext;
    private TextView cancel,loading;
    private LodingWaitListener lodingWaitListener;

    public LoadingWaitDialog setLoadingText(String loadingTextString){
        if(loading != null){
            loading.setText(loadingTextString);
        }
        return this;
    }


    public LoadingWaitDialog setLodingWaitListener(LodingWaitListener lodingWaitListener) {
        this.lodingWaitListener = lodingWaitListener;
        return this;
    }

    public LoadingWaitDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public LoadingWaitDialog(Context mContext,int styleId){
        super(mContext,styleId);
        this.mContext = mContext;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_layout_loadings);
        loading = findViewById(R.id.library_dialog_tv_loading);
        cancel = findViewById(R.id.library_dailog_tv_cancel);
        cancel.setOnClickListener(this);
        setOutCancel(false);
        setMargin(DpUtil.dp2px(mContext,5));
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() ==  R.id.library_dailog_tv_cancel){
            if(lodingWaitListener != null){
                lodingWaitListener.onCancel();
            }
            dismiss();
        }
    }

    public interface LodingWaitListener{
        void onCancel();
    }

}
