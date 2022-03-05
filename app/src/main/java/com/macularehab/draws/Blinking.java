package com.macularehab.draws;

import android.os.CountDownTimer;
import android.view.View;

/**
 *  Class to set a View Object to blink three times in three seconds
 */
public abstract class Blinking {
    private boolean blinking=false;
    protected View obj;
    private final CountDownTimer timer;
    private final boolean end_visible;

    /**
     * Set a View Object to blink three times in three seconds
     * @param _obj View object desired to blink
     * @param _end_visible Indicates either end visible or not
     */
    public Blinking(View _obj, boolean _end_visible){
        obj=_obj;
        this.end_visible=_end_visible;
        timer = new CountDownTimer(3000, 500) {
            boolean vi = false;

            public void onTick(long millisUntilFinished) {
                if (vi) {
                    obj.setVisibility(View.VISIBLE);
                    blinking = true;
                } else obj.setVisibility(View.INVISIBLE);
                vi = !vi;
            }

            public void onFinish() {
                finish();
            }

        };
    }

    private void finish(){
        if(end_visible) obj.setVisibility(View.VISIBLE);
        else obj.setVisibility(View.INVISIBLE);
        blinking=false;
        end_blinking();
    }

    /**
     * Method that indicates action when ending blinking.
     */
    protected abstract void end_blinking();

    /**
     * Starts blinking process
     */
    public void do_blink(){
        timer.start();
    }

    /**
     * Stop blinking process doing the end_blinking action.
     */
    public void cancel_blink(){
        timer.cancel();
        finish();
    }

    /**
     * Indicates if view is blinking or not.
     * @return True if blinking.
     */
    public boolean is_blinking() {
        return blinking;
    }
}
