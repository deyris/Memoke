package es.barcelona.dey.memoke.views;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

/**
 * Created by deyris.drake on 26/9/16.
 */
public interface ContentView {

    Context getContext();


    void fillFirstTab();

    void fillSecondTab();

    void fillImgsWithCurrent();

    void showContinueButton();

    void showAntButton();

    void fillTextInTab(int idText, String val, int size);

    void hideImageInTab(int idText, int idImg);


}
