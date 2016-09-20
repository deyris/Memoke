package es.barcelona.dey.memoke.clients;

import android.app.Application;
import android.content.Context;

/**
 * Created by deyris.drake on 18/9/16.
 */
public class MemokeApp extends Application {

    public static MemokeApp get(Context context) {
        return (MemokeApp) context.getApplicationContext();
    }
}
