package com.zhurui.bunnymall.viewutils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

import com.zhurui.bunnymall.R;

/**
 * Created by zhaopf on 2017/11/23.
 */

public class EditTextLine extends EditText{
    private Paint paint;

    public EditTextLine(Context context, AttributeSet attrs) {
        super(context, attrs);

        //设置画笔的属性
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        //可以自定义画笔的颜色，我这里设置成黑色
        paint.setColor(context.getResources().getColor(R.color.gray));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**canvas画直线，从左下角到右下角，this.getHeight()-2是获得父edittext的高度，但是必须要-2这样才能保证
         * 画的横线在edittext上面，那样才看得见，如果不-2，你可以试一试看一下是否能看得见。
         */
        canvas.drawLine(0, this.getHeight()-1, this.getWidth()-2, this.getHeight()-1, paint);
    }
}
