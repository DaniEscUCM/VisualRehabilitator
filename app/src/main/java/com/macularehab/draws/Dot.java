package com.macularehab.draws;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Pair;
import android.view.View;
import java.util.List;

public class Dot extends View {

    private Paint paint;
    private List<Pair<Integer,Integer>> coor_resul;
    private float x,y;
    private int size, metric;

    public Dot(Context context, float x, float y, List<Pair<Integer, Integer>> coor_resul, int width, int metric) {
        super(context);
        this.x=x;
        this.y=y;
        this.coor_resul=coor_resul;
        this.size=width;
        this.metric=metric;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);


    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i=0;i<this.coor_resul.size();i++) {
            paint.setAlpha(size);
            float local_x = x + (metric * coor_resul.get(i).first);
            float local_y = y + (metric * coor_resul.get(i).second);
            canvas.drawCircle(local_x, local_y, size, paint);
        }
    }
}
