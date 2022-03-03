package com.macularehab.draws;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Pair;
import java.util.List;

/**
 * Class to draw different dots in relation to a centre and a metric unit.
 */
public class Dot {

    private Paint paint;
    private List<Pair<Integer,Integer>> coor_resul;
    private float x,y, radius, metric;

    /**
     * Constructor to create dots with draw method with a metric unit in relation to a centre dot
     * (x,y)
     *
     * @param x Reference x coordinate point , normally the centre of the screen
     * @param y Reference x coordinate point , normally the centre of the screen
     * @param coor_resul List of Pair of relation coordinates with (x,y) reference
     * @param radius dots radius
     * @param metric metric unit to distance between dots, if no specific unit, use 1
     */
    public Dot(float x, float y, List<Pair<Integer, Integer>> coor_resul, float radius, float metric) {
        this.x=x;
        this.y=y;
        this.coor_resul=coor_resul;
        this.radius =radius;
        this.metric=metric;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
    }

    public void draw(Canvas canvas) {
        for(int i=0;i<this.coor_resul.size();i++) {
            float local_x = x + (metric * coor_resul.get(i).first);
            float local_y = y + (metric * coor_resul.get(i).second);
            canvas.drawCircle(local_x,local_y, radius, paint);
        }
    }

    /* SIMPLIFIED WAY TO PRINT A CIRCLE
        ImageView out=findViewById(R.id.multiple_dots);
        Bitmap btm= Bitmap.createBitmap(out.getWidth(),out.getWidth(), Bitmap.Config.ARGB_8888);
        Canvas canvas= new Canvas(btm);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);

        metric_unit = grid.getWidth()/(float)14;

        find_dot.setVisibility(View.VISIBLE);
        canvas.drawCircle((out.getWidth()/(float)2),(out.getWidth()/(float)2), find_dot.getWidth()/(float)2, paint);
        canvas.drawCircle((out.getWidth()/(float)2)+(coor.get(1).first*metric_unit),(out.getWidth()/(float)2), find_dot.getWidth()/(float)2, paint);
        canvas.drawCircle((out.getWidth()/(float)2)+(coor.get(0).first*metric_unit),(out.getWidth()/(float)2), find_dot.getWidth()/(float)2, paint);
        out.setImageBitmap(btm);*/
}
