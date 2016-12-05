package es.barcelona.dey.memoke.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.test.RenamingDelegatingContext;

/**
 * Created by deyris.drake on 27/11/16.
 */
public class SpecializedMockContext extends RenamingDelegatingContext {
    public static final String PREFIX = "test.";
    public Context context;

    public SpecializedMockContext(Context context) {
        super(context, PREFIX);
        this.context = context;
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return super.getSharedPreferences(PREFIX + name, mode);
    }

    public Context getApplicationContext(){
        return context;
    }
}
