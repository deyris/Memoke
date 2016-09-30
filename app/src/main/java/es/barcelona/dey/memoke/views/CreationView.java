package es.barcelona.dey.memoke.views;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by deyris.drake on 18/9/16.
 */
public interface CreationView {

    Context getContext();

    Bundle actualizeBundle(Bundle bundle, String nameData, String data);


}
