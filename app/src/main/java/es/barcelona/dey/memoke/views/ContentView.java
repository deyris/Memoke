package es.barcelona.dey.memoke.views;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

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


    void showNextButton();

    void hideNextButton();

    void showAntButton();

    void hideAntButton();

    void fillTextInTab(int idText, String val, int size);

    void hideImageInTab(int idText, int idImg);

    void setListenerFrame(FrameLayout frame, int tab);

    String getCurrentPairFromContext(Bundle savedInstanceState);

    void fillNumberInCurrentPair();

    void fillResultWithCurrent(int idText, int tab, ImageView imgToHide1);

    void inicializeFragment();

    void setPicToBackground();

    void setPicToImg(ImageView img, int height, int width);

}
