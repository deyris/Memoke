package es.barcelona.dey.memoke.views;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by deyris.drake on 11/10/16.
 */
public interface HeaderView {

    Context getContext();

    void fillHeaderData(Bundle bundle);
}
