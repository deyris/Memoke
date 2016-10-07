package es.barcelona.dey.memoke.presenters;

import android.os.Bundle;

import java.util.HashMap;

import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.beans.Pair;
import es.barcelona.dey.memoke.database.BoardDatabase;
import es.barcelona.dey.memoke.interactors.CreationInteractor;
import es.barcelona.dey.memoke.ui.CreationActivity;
import es.barcelona.dey.memoke.ui.MainFragment;
import es.barcelona.dey.memoke.views.CreationView;

/**
 * Created by deyris.drake on 18/9/16.
 */
public class CreationPresenter extends ComunPresenter implements Presenter<CreationView>{

    CreationView creationView;
    CreationInteractor creationInteractor;

    int idCurrentPair;
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

    public void savePairInBoard(Pair pair){
        creationInteractor.savePairInBoard(this.mBoard, pair);
    }

    public int getIdCurrentPair() {

        return idCurrentPair;
    }

    public void setIdCurrentPair(int idCurrentPair) {
        this.idCurrentPair = idCurrentPair;
    }

    private Pair loadPairFromExistingBoard(String jsonBoard){
        Board mBoard = gson.fromJson(jsonBoard, Board.class);
        //Buscamos currentPair
        Pair currentPair = new Pair();
        if (null!=mBoard.getPairs()) {
            this.idCurrentPair = (null != mBoard.getPairs()) ? mBoard.getPairs().size() : 1;
            //Actualizamos currentPair
            currentPair = mBoard.getPairs().get(this.idCurrentPair);
            this.mBoard = mBoard;
        }else{
            //Es un tablero vacío, que existe pero no tiene ninguna pareja aún
            this.idCurrentPair = 1;

        }

        return currentPair;
    }



    private Pair generateNewPair(){
        //Creamos tablero
        mBoard = new Board();
        idCurrentPair = 1;
        Pair newPair = new Pair();

        return newPair;
    }

    public void incrementIdCurrentPair(){
        this.idCurrentPair++;
    }

    public void decrementIdCurrentPair(){
        this.idCurrentPair--;
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

    public void updateIdCurrentPairIfExistInContext(Bundle savedInstanceState){
        if(null!= savedInstanceState){
            this.setIdCurrentPair(savedInstanceState.getInt(CreationActivity.PARAM_CURRENT_PAIR_NUMBER));
        }
    }
    public void updateBoardIfExistIncontent(Bundle savedInstanceState){
        if(null!= savedInstanceState){
            this.setmBoard(this.getCurrentBoard(savedInstanceState.getString(CreationActivity.PARAM_CURRENT_BOARD)));
        }
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


    public boolean pairNotSavedYet(Pair pair){
        return pair.getState().equals(Pair.State.COMPLETED) || pair.getState().equals(Pair.State.IN_PROCESS);
    }

    public String getNextPairOnBoard(int mCurrentPair){
        Pair pairSgte = new Pair();
        String jsonPairAnt = null;
        if (mBoard.getPairs().size() >= mCurrentPair) {
            pairSgte = mBoard.getPairs().get(mCurrentPair);
            jsonPairAnt = gson.toJson(pairSgte);
        }

        return jsonPairAnt;

    }

    public void inicializeBoardIfPairsAreNull(){
        if (null == mBoard.getPairs()) {
            mBoard.setPairs(new HashMap<Integer, Pair>());
        }
    }

    public Board getmBoard() {
        return mBoard;
    }
}
