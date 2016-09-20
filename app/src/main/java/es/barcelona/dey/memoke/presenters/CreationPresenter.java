package es.barcelona.dey.memoke.presenters;

import android.os.Bundle;

import com.google.gson.Gson;

import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.beans.Pair;
import es.barcelona.dey.memoke.database.BoardDatabase;
import es.barcelona.dey.memoke.interactors.CreationInteractor;
import es.barcelona.dey.memoke.interactors.MainInteractor;
import es.barcelona.dey.memoke.ui.MainFragment;
import es.barcelona.dey.memoke.views.CreationView;

/**
 * Created by deyris.drake on 18/9/16.
 */
public class CreationPresenter implements Presenter<CreationView>{

    CreationView creationView;
    CreationInteractor creationInteractor;
    final Gson gson = new Gson();
    int mCurrentPair;
    Board mBoard;

    @Override
    public void setView(CreationView view) {
        if (view == null) throw new IllegalArgumentException("You can't set a null view");

        creationView = view;
        creationInteractor = new CreationInteractor(creationView.getContext());
    }

    @Override
    public void detachView() {
        creationView = null;
    }

    public void updateOrAddBoard(Board board){
        BoardDatabase.updateOrAddBoard(creationView.getContext(),board);
    }

    public void verifyIfExistPairInBoard(Board board, Pair pair){
        creationInteractor.savePairInBoard(board, pair);
    }

    public int getmCurrentPair() {
        return mCurrentPair;
    }

    public void setmCurrentPair(int mCurrentPair) {
        this.mCurrentPair = mCurrentPair;
    }

    private Pair loadPairFromExistingBoard(String jsonBoard){
        Board mBoard = gson.fromJson(jsonBoard, Board.class);
        //Buscamos currentPair
        Pair currentPair = new Pair();
        if (null!=mBoard.getPairs()) {
            this.mCurrentPair = (null != mBoard.getPairs()) ? mBoard.getPairs().size() : 1;
            //Actualizamos currentPair
            currentPair = mBoard.getPairs().get(this.mCurrentPair);
            this.mBoard = mBoard;
        }else{
            //Es un tablero vacío, que existe pero no tiene ninguna pareja aún
            this.mCurrentPair = 1;

        }

        return currentPair;
    }

    private Pair generateNewPair(){
        //Creamos tablero
        mBoard = new Board();
        mCurrentPair = 1;
        Pair newPair = new Pair();

        return newPair;
    }

    public Pair generateNextPair(Bundle bundleFromMain){
        Pair currentPair;
        if (bundleFromMain.getString(MainFragment.PARAM_SELECTED_BOARD) != null) {  //Hay un board que ya existe que viene del view anterior

            currentPair =  loadPairFromExistingBoard(bundleFromMain.getString(MainFragment.PARAM_SELECTED_BOARD));

        } else {
            //Creamos tablero
            currentPair = generateNewPair();

        }
        return currentPair;
    }

    private void updateTitleFromBundle(Bundle bundleFromMain){
        //Actualizamos title tablero
        if (null==mBoard){
            mBoard = new Board();
        }
        if (bundleFromMain.getString(MainFragment.PARAM_TITLE) != null) {
            String title = bundleFromMain.getString(MainFragment.PARAM_TITLE).toString();
            mBoard.setTitle(title);
        }

    }
    public Board getBoardWithTitleFromMain(Bundle bundleFromMain){
        updateTitleFromBundle(bundleFromMain);
        return mBoard;
    }
    public Board getBoardWithTitleFromMain() {
        return mBoard;
    }

    public void setmBoard(Board mBoard) {
        this.mBoard = mBoard;
    }
}
