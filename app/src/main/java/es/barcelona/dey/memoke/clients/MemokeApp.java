package es.barcelona.dey.memoke.clients;

import android.app.Application;
import android.content.Context;
import android.os.Build;

/**
 * Created by deyris.drake on 18/9/16.
 */
public class MemokeApp extends Application {

    public static MemokeApp get(Context context) {
        return (MemokeApp) context.getApplicationContext();
    }

    public static boolean canMakeSmores(){

        return(Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1);

    }
}
