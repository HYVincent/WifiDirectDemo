package com.common.view.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.common.view.R;
import com.common.view.util.DpUtil;

/**
 * @author Vincent QQ:1032006226
 * @version v1.0
 * @name EasyApp
 * @page com.common.view.view
 * @class describe
 * @date 2018/5/17 11:26
 */

@SuppressLint("AppCompatCustomView")
public class LeftTextEditText extends EditText {
    private Context mContext;
    private int leftTextColor;
    private float leftTextSize;
    private String leftTextStr;
    private View.OnClickListener mListener;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 文本外层的矩形
     */
    private Rect mBound;
    public LeftTextEditText(Context context) {
        super(context);
    }

    public LeftTextEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public LeftTextEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public void setDrawableClk(View.OnClickListener listener) {
        mListener = listener;
    }

    /**
     * 初始化参数
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LeftTextEditText);
        try {
            leftTextColor = typedArray.getColor(R.styleable.LeftTextEditText_left_text_color, ContextCompat.getColor(mContext,R.color.color_black_333333));
            leftTextSize  = typedArray.getInteger(R.styleable.LeftTextEditText_left_text_size,  DpUtil.dp2px(mContext,12));
            leftTextStr   = typedArray.getString(R.styleable.LeftTextEditText_left_text);
        }finally {
            //释放资源
            typedArray.recycle();
        }

        /**
         * 获得绘制文本的宽和高
         */
        mPaint = new Paint();
        mPaint.setTextSize(leftTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mBound = new Rect();
        Log.i("Tag", "TextLength:" + leftTextStr.length());
        mPaint.getTextBounds(leftTextStr, 0, leftTextStr.length(), mBound);

        int left = (int) getPaint().measureText(leftTextStr)+ getPaddingLeft();
        setPadding(left, getPaddingTop(), getPaddingBottom(), getPaddingRight());
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(leftTextStr)) {
            canvas.drawText(leftTextStr, 0, (getMeasuredHeight() - getTextSize()) / 2 + getTextSize(), getPaint());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mListener != null && getCompoundDrawables()[2] != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    int i = getMeasuredWidth() - getCompoundDrawables()[2].getIntrinsicWidth();
                    if (event.getX() > i) {
                        mListener.onClick(this);
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }

        }
        return super.onTouchEvent(event);
    }

}

