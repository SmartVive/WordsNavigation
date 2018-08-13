package com.example.administrator.azlist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordsNavigation extends View {
    private List<String> words = new ArrayList<>();
    //private String words[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
    //        "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private int itemWidth;
    private int itemHeight;
    private int paddingTop = 0;
    private int touchIndex = 0;
    private Paint bgPaint = new Paint();
    private Paint wordsPaint = new Paint();
    public OnWordsChangeListener listener;

    public WordsNavigation(Context context) {
        super(context);
    }

    public WordsNavigation(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WordsNavigation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setTouchIndex(String word) {
        touchIndex = words.indexOf(word);
        invalidate();
    }

    public void setWords(List<String> words) {
        this.words = words;
        measure(0, 0);
        invalidate();
    }

    private void init() {
        bgPaint.setColor(getResources().getColor(R.color.colorPrimary));
        wordsPaint.setColor(Color.BLACK);
        wordsPaint.setTextAlign(Paint.Align.CENTER);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (words.size() == 0){
            return;
        }
        itemWidth = getMeasuredWidth();
        itemHeight = getMeasuredHeight() / words.size();
        if (itemHeight > itemWidth * 3 / 2) {//当字母少的时候,设置最大高度为宽度1.5倍
            itemHeight = itemWidth * 3 / 2;
            paddingTop = (getMeasuredHeight() - itemHeight * words.size()) / 2;
        }
        if (itemWidth < itemHeight) {
            wordsPaint.setTextSize(itemWidth / 7 * 5);
        } else {
            wordsPaint.setTextSize(itemHeight / 7 * 5);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < words.size(); i++) {
            if (touchIndex == i) {
                int x = itemWidth / 2;
                int y = itemHeight / 2 + itemHeight * i + paddingTop;
                if (itemWidth < itemHeight) {
                    canvas.drawCircle(x, y, itemWidth / 2, bgPaint);
                } else {
                    canvas.drawCircle(x, y, itemHeight / 2, bgPaint);
                }
                wordsPaint.setColor(Color.WHITE);
            } else {
                wordsPaint.setColor(Color.BLACK);
            }

            Rect rect = new Rect();
            wordsPaint.getTextBounds(words.get(i), 0, 1, rect);
            int wordsHeight = rect.height();

            canvas.drawText(words.get(i), itemWidth / 2, itemHeight / 2 + wordsHeight / 2 + itemHeight * i + paddingTop, wordsPaint);

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            float y = event.getY() - paddingTop;
            if (y > itemHeight * words.size()) {
                y = words.size();
            } else if (y < 0) {
                y = 0;
            }
            touchIndex = (int) (y / itemHeight);
            invalidate();
            if (listener != null) {
                listener.wordsChange(words.get(touchIndex));
            }
        }

        return true;
    }

    public interface OnWordsChangeListener {
        void wordsChange(String word);
    }

    public void setOnWordsChangeListener(OnWordsChangeListener listener) {
        this.listener = listener;
    }

}
