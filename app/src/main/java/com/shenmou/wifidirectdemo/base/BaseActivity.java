package com.shenmou.wifidirectdemo.base;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.common.util.util.MyToastUtil;
import com.common.view.simple.LoadingDialog;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name WifiDirectDemo
 * @page com.shenmou.wifidirectdemo
 * @class describe
 * @date 2018/8/24 10:16
 */
public class BaseActivity extends AppCompatActivity {

    private LoadingDialog loadingDialog;

    protected void showLoadingDialog(String msg){
        if(loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
            loadingDialog = null;
        }
        loadingDialog = new LoadingDialog(BaseActivity.this);
        if(TextUtils.isEmpty(msg)){
            loadingDialog.setMsg("加载中..");
        }else {
            loadingDialog.setMsg(msg);
        }
        loadingDialog.show();
    }

    protected void dismissionDialog(){
        if(loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    public void showMsg(String msg){
        MyToastUtil.showMsg(msg);
    }

}
