package es.barcelona.dey.memoke.presenters;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import es.barcelona.dey.memoke.beans.Pair;
import es.barcelona.dey.memoke.beans.Tab;
import es.barcelona.dey.memoke.interactors.CreationInteractor;
import es.barcelona.dey.memoke.views.ContentView;
import es.barcelona.dey.memoke.views.CreationView;

/**
 * Created by deyris.drake on 26/9/16.
 */
public class ContentPresenter implements Presenter<ContentView>{

    ContentView contentView;
    final Gson gson = new Gson();


    public Pair getCurrentPair(String jsonCurrentPair) {
        return gson.fromJson(jsonCurrentPair, Pair.class);
    }

    public String getJsonCurrentPair(Pair currentPair){
        return gson.toJson(currentPair).toString();
    }

    @Override
    public void setView(ContentView view) {
        if (view == null) throw new IllegalArgumentException("You can't set a null view");

        contentView = view;
    }

    @Override
    public void detachView() {
        contentView = null;
    }


    public void fillPairOnView(String jsonCurrentPair){
        if(null!=jsonCurrentPair) {

          contentView.fillFirstTab();
          contentView.fillSecondTab();
          contentView.fillImgsWithCurrent();

        }
    }


    public Tab.Type getTypeById(int id){
        switch (id){
            case 0: return Tab.Type.TEXT;
            case 1: return Tab.Type.PHOTO;
            default:return Tab.Type.FIGURE;
        }
    }

    public void controlButtonsAntSgte(){
        contentView.showAntButton();
        contentView.showContinueButton();
    }

    public boolean existTextToShowInView(Pair currentPair, int tab){
        return null!= currentPair.getTabs()[tab - 1].getText() && !currentPair.getTabs()[tab - 1].getText().isEmpty();
    }

    public void fillTextInTab(Pair currentPair, int tab, int idText){
        String val = currentPair.getTabs()[tab - 1].getText();
        int size = currentPair.getTabs()[tab - 1].getSize() / 2;
        contentView.fillTextInTab(idText,val,size) ;
    }

    public void hideImageInTab(int  idText, int idImg){

        contentView.hideImageInTab(idText, idImg);
    }

    public void putTabIN_PROCESS(Pair mCurrentPair){
        if (mCurrentPair==null){
            mCurrentPair = new Pair();
        }
        mCurrentPair.setState(Pair.State.IN_PROCESS);
    }

    public void markCurrentView(View view, int tab){
       view.setTag(tab);
    }

    public int getMarkOfCurrentView(View view){
       return (int)view.getTag();
    }
}
