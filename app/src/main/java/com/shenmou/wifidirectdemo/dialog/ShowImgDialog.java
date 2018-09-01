package com.shenmou.wifidirectdemo.dialog;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.common.view.BaseDialog;
import com.shenmou.wifidirectdemo.R;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name WifiDirectDemo
 * @page com.shenmou.wifidirectdemo.dialog
 * @class describe
 * @date 2018/9/1 17:50
 */
public class ShowImgDialog extends BaseDialog {

    public ShowImgDialog(@NonNull Context context,String imgPath) {
        super(context);
        this.imgPath = imgPath;
    }
    private ImageView imageView;
    private String imgPath;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout_show_img);
        imageView = findViewById(R.id.imageview);
        imageView.setImageBitmap(BitmapFactory.decodeFile(imgPath));
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        dismiss();
        return super.onTouchEvent(event);
    }
}
