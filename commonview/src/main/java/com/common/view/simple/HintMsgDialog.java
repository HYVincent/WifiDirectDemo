package com.common.view.simple;

import android.content.Context;
import android.support.annotation.NonNull;

import com.common.view.BaseDialog;
import com.common.view.R;


/**
 * @author Administrator QQ:1032006226
 * @version v1.0
 * @name Julie
 * @page com.vincent.mylibrary.dialog
 * @class describe
 * @date 2018/3/18 18:25
 */

public class HintMsgDialog extends BaseDialog {

    private String cancelText = "取消";
    private String okText = "确认";
    private String hintMsg = "";

    public HintMsgDialog setCancelText(String cancelText) {
        this.cancelText = cancelText;
        return this;
    }

    public HintMsgDialog setOkText(String okText) {
        this.okText = okText;
        return this;
    }

    public HintMsgDialog setHintMsg(String hintMsg) {
        this.hintMsg = hintMsg;
        return this;
    }

    public HintMsgDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.library_layout_dialog_hint_msg);
    }
}
