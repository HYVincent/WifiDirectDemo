package com.common.view.simple;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.common.view.BaseDialog;
import com.common.view.R;
import com.common.view.view.NumberPickerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator QQ:1032006226
 * @version v1.0
 * @name EasyApp
 * @page com.common.dialog.simple
 * @class describe
 * @date 2018/5/15 23:57
 */
public class SlideSingleListDialog extends BaseDialog {

    private TextView tvCancel,tvTitle,tvOk;
    private LinearLayout llCancel,llOk;
    private NumberPickerView numberPickerView;
    private String cancelText = "取消";
    private String titleText;
    private String okText= "确定";
    private int resultPosition;
    private SlideSingleListDialogResultListener slideListDialogResultListener;
    //是否循环滚动
    private boolean circulation = true;


    private List<String> data = new ArrayList<>();

    public SlideSingleListDialog(@NonNull Context context) {
        super(context);
    }

    public SlideSingleListDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_layout_slide_list);
        tvCancel = findViewById(R.id.slide_list_select_cancel);
        tvCancel.setText(cancelText);
        tvOk = findViewById(R.id.slide_list_select_ok);
        tvOk.setText(okText);
        tvTitle = findViewById(R.id.slide_list_select_title);
        tvTitle.setText(titleText);
        llCancel = findViewById(R.id.slide_list_select_cancel_ll);
        llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        llOk = findViewById(R.id.slide_list_select_ok_ll);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultPosition = numberPickerView.getValue();
                slideListDialogResultListener.result(resultPosition);
                dismiss();
            }
        });
        numberPickerView = findViewById(R.id.slide_list_select_npv);
        int size = data.size();
        String[] arr = data.toArray(new String[size]);//使用了第二种接口，返回值和参数均为结果
        numberPickerView.setScrollBarSize(5);
        numberPickerView.setDisplayedValues(arr);
        numberPickerView.setMinValue(0);
        numberPickerView.setMaxValue(arr.length - 1);
        numberPickerView.setValue(0);
        numberPickerView.setWrapSelectorWheel(circulation);
    }

    public SlideSingleListDialog setCancelText(String cancelText) {
        this.cancelText = cancelText;
        return this;
    }

    public SlideSingleListDialog setTitleText(String titleText) {
        this.titleText = titleText;
        return this;
    }

    public SlideSingleListDialog setOkText(String okText) {
        this.okText = okText;
        return this;
    }

    public SlideSingleListDialog setData(List<String> data) {
        this.data = data;
        return this;
    }

    public SlideSingleListDialog setSlideSingleListDialogResultListener(SlideSingleListDialogResultListener slideListDialogResultListener) {
        this.slideListDialogResultListener = slideListDialogResultListener;
        return this;
    }

    public SlideSingleListDialog setCirculation(boolean circulation) {
        this.circulation = circulation;
        return this;
    }

    public interface SlideSingleListDialogResultListener{
        void result(int position);
    }
}
