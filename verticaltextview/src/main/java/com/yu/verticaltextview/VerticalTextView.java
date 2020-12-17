package com.yu.verticaltextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class VerticalTextView extends View {
    private Paint mPaint;
    private String text;
    private int textSize;
    private int textColor;
    private boolean isRtl;          //是否从右到左，默认false
    private int textAlign;          //文字对齐方式
    private int rowSpan;           //列间距
    private Typeface typeface;
    private float maxTextWidth;     //最宽的值
    private char[] arraysOfText;
    private int measureHeight;

    public VerticalTextView(Context context) {
        this(context, null);
    }

    public VerticalTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VerticalTextView);
        text = array.getString(R.styleable.VerticalTextView_text);
        textSize = array.getDimensionPixelSize(R.styleable.VerticalTextView_textSize, 10);
        textColor = array.getColor(R.styleable.VerticalTextView_textColor, Color.BLACK);
        isRtl = array.getBoolean(R.styleable.VerticalTextView_isRtl, false);
        textAlign = array.getInt(R.styleable.VerticalTextView_textAlign, 0);
        rowSpan = array.getDimensionPixelSize(R.styleable.VerticalTextView_rowSpan, 0);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        init();
        findMaxTextWidth();

        array.recycle();
    }

    private void init() {
        mPaint.setTextSize(textSize);
        mPaint.setColor(textColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(textAlign == TextAlignType.left ?
                Paint.Align.LEFT : textAlign == TextAlignType.center ?
                Paint.Align.CENTER : Paint.Align.RIGHT);
        if (typeface != null) {
            mPaint.setTypeface(typeface);
        }
    }

    //遍历找到最宽的文字宽度
    private void findMaxTextWidth() {
        if (text != null && text.length() != 0) {
            arraysOfText = text.toCharArray();
            for (char c : arraysOfText) {
                String str = String.valueOf(c);
                float measureTextWidth = mPaint.measureText(str);
                if (measureTextWidth > maxTextWidth) {
                    maxTextWidth = measureTextWidth;
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    //测量文字宽度
    private int getMeasureWidth(int measureSpec) {
        measureHeight = 0;
        int rowNum = 1;  //列数
        float width = 0;
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);
        float textHeight = mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top;
        float startY = getPaddingTop();

        if ("".equals(text) || text == null) {
            return 0;
        }
        for (char c : arraysOfText) {
            String str = String.valueOf(c);
            if ("\n".equals(str)) {       //另起一列
                rowNum++;
                startY = getPaddingTop();
                continue;
            }
            startY = startY + textHeight;
            if (startY + getPaddingBottom() > getMeasuredHeight()) {        //如果加上paddingBottom超过了高度，换行
                measureHeight = Math.max((int) Math.ceil(startY - textHeight), measureHeight);
                startY = getPaddingTop() + textHeight;
                rowNum = rowNum + 1;
            }
            measureHeight = Math.max((int) Math.ceil(startY), measureHeight);
        }
        measureHeight = measureHeight + getPaddingBottom();             //控件高度
        if (mode == MeasureSpec.EXACTLY) {
            width = size;
        } else {
            width = maxTextWidth * rowNum + getPaddingLeft() + getPaddingRight() + rowSpan * (rowNum - 1);
        }
        return (int) width;
    }

    public int measureHeight(int measureSpec) {
        int height = 0;
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        } else {
            height = measureHeight;
        }
        return height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float baseLine = getPaddingTop() - mPaint.getFontMetrics().top;
        float textH = mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top;
        float startX;

        //根据对齐方式，确定起始位置的x坐标
        switch (textAlign) {
            case TextAlignType.left:
                startX = isRtl ? getMeasuredWidth() - getPaddingRight() - maxTextWidth : getPaddingLeft();
                break;
            case TextAlignType.center:
                startX = isRtl ? getMeasuredWidth() - getPaddingRight() - maxTextWidth + maxTextWidth / 2 : getPaddingLeft() + maxTextWidth / 2;
                break;
            default:
                startX = isRtl ? getMeasuredWidth() - getPaddingRight() - maxTextWidth + maxTextWidth : getPaddingLeft() + maxTextWidth;
                break;
        }

        if (text != null && text.length() != 0) {
            for (char c : arraysOfText) {
                String str = String.valueOf(c);
                if ("\n".equals(str)) {
                    baseLine = getPaddingTop() - mPaint.getFontMetrics().top;
                    startX = isRtl ? startX - maxTextWidth - rowSpan : startX + maxTextWidth + rowSpan;
                    continue;
                }
                if (baseLine + mPaint.getFontMetrics().bottom + getPaddingBottom() > getMeasuredHeight()) {
                    baseLine = getPaddingTop() - mPaint.getFontMetrics().top;
                    startX = isRtl ? startX - maxTextWidth - rowSpan : startX + maxTextWidth + rowSpan;
                }
                canvas.drawText(str, startX, baseLine, mPaint);
                baseLine += textH;
            }
        }
    }

    public void setText(String text) {
        this.text = text;
        findMaxTextWidth();
        requestLayout();
        postInvalidate();
    }

    public void setTextSize(int textSize) {
        int size = sp2px(getContext(), textSize);
        if (mPaint.getTextSize() != size) {
            this.textSize = size;
            mPaint.setTextSize(size);
            findMaxTextWidth();
            requestLayout();
            postInvalidate();
        }
    }

    public void setTextColor(int textColor) {
        if (mPaint.getColor() != textColor) {
            this.textColor = textColor;
            mPaint.setColor(textColor);
            postInvalidate();
        }
    }

    //设置文字显示顺序（true:从右到左， false:从左到右）
    public void setRtl(boolean rtl) {
        this.isRtl = rtl;
        postInvalidate();
    }

    public boolean isRtl() {
        return isRtl;
    }

    //设置字体
    public void setTypeface(Typeface typeface) {
        if (mPaint.getTypeface() != typeface) {
            this.typeface = typeface;
            mPaint.setTypeface(typeface);
            postInvalidate();
        }
    }

    //设置对齐方式
    public void setTextAlign(int textAlign) {
        this.textAlign = textAlign;
        Paint.Align align = textAlign == TextAlignType.left ?
                Paint.Align.LEFT : textAlign == TextAlignType.center ?
                Paint.Align.CENTER : Paint.Align.RIGHT;
        mPaint.setTextAlign(align);
        requestLayout();
        postInvalidate();
    }

    //设置每列间隔
    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
        requestLayout();
        postInvalidate();
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    //文字对齐模式
    public interface TextAlignType {
        int left = 0;
        int center = 1;
        int right = 2;
    }
}
