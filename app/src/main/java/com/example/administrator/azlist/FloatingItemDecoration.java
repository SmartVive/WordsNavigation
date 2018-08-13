package com.example.administrator.azlist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.List;

public class FloatingItemDecoration extends RecyclerView.ItemDecoration {
    private List<CityBean> cityBeans;
    private Context context;
    private Paint bgPaint;
    private Paint textPaint;
    private Drawable drawable;
    private int titleHeight;
    private int decorationHeight = 1;

    public FloatingItemDecoration(Context context, List<CityBean> cityBeans) {
        this.cityBeans = cityBeans;
        this.context = context;
        //DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        titleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());
        init();
    }

    private void init() {
        //分割线颜色
        drawable = new ColorDrawable(Color.LTGRAY);
        bgPaint = new Paint();
        bgPaint.setColor(Color.LTGRAY);
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(titleHeight * 4 / 5);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (parent.getPaddingTop()<=child.getTop()-titleHeight){
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                int position = params.getViewLayoutPosition();
                if (!cityBeans.get(position).getIndex().isEmpty()) {
                    int left = parent.getPaddingLeft();
                    int right = parent.getWidth() - parent.getPaddingRight();
                    int top = child.getTop() - titleHeight;
                    int bottom = top + titleHeight;
                    c.drawRect(left, top, right, bottom, bgPaint);
                    Rect rect = new Rect();
                    textPaint.getTextBounds(cityBeans.get(position).getIndex(), 0, 1, rect);
                    //向左移动8dp
                    int textX = parent.getPaddingLeft() + (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, context.getResources().getDisplayMetrics());
                    int textY = child.getTop() - titleHeight / 2 + rect.height() / 2;
                    c.drawText(cityBeans.get(position).getIndex(), textX, textY, textPaint);
                } else {
                    int left = parent.getPaddingLeft();
                    int right = parent.getWidth() - parent.getPaddingRight();
                    int top = child.getTop();
                    int bottom = child.getTop() + 1;
                    drawable.setBounds(left, top, right, bottom);
                    drawable.draw(c);
                }
            }

        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int firstVisibleItemPosition = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
        if (cityBeans.size() > 0 && firstVisibleItemPosition>=0) {//加载完才执行
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            int top = 0;
            int bottom = 0;
            int textX = 0;
            int textY = 0;
            Rect rect = new Rect();
            textPaint.getTextBounds(cityBeans.get(firstVisibleItemPosition).getFirstWord(), 0, 1, rect);
            //文字向左移动8dp
            textX = parent.getPaddingLeft() + (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, context.getResources().getDisplayMetrics());
            View chlid = parent.findViewHolderForAdapterPosition(firstVisibleItemPosition).itemView;
            int height = chlid.getHeight();
            if (!cityBeans.get(firstVisibleItemPosition + 1).getIndex().isEmpty() && height + chlid.getTop() - parent.getPaddingTop() < titleHeight) {//1.下个列表是标题,2.两个标题碰撞
                //碰撞后被压缩的高度
                int minusHeight = titleHeight - height - chlid.getTop() + parent.getPaddingTop();
                //顶部减去被压缩的高度
                top = parent.getTop() - minusHeight + parent.getPaddingTop();
                bottom = top + titleHeight;
                textY = parent.getTop() + titleHeight / 2 + rect.height() / 2 - minusHeight + parent.getPaddingTop();
            } else {
                top = parent.getTop() + parent.getPaddingTop();
                bottom = top + titleHeight;
                textY = parent.getTop() + titleHeight / 2 + rect.height() / 2 + parent.getPaddingTop();
            }

            c.drawRect(left, top, right, bottom, bgPaint);
            c.drawText(cityBeans.get(firstVisibleItemPosition).getFirstWord(), textX, textY, textPaint);
        }

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildViewHolder(view).getAdapterPosition();
        if (!cityBeans.get(position).getIndex().isEmpty()) {
            //标题
            outRect.set(0, titleHeight, 0, 0);
        } else {
            //分割线
            outRect.set(0, decorationHeight, 0, 0);
        }
    }
}
