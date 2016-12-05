package es.barcelona.dey.memoke.presenters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceManager;


import java.util.HashMap;

import es.barcelona.dey.memoke.R;
import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.beans.Pair;
import es.barcelona.dey.memoke.database.BoardDatabase;
import es.barcelona.dey.memoke.interactors.CreationInteractor;
import es.barcelona.dey.memoke.ui.ContentFragment;
import es.barcelona.dey.memoke.views.CreationView;

/**
 * Created by deyris.drake on 18/9/16.
 */
public class CreationPresenter extends ComunPresenter implements Presenter<CreationView>{

    public static String PARAM_CURRENT_PAIR = "PARAM_CURRENT_PAIR";
    public static String PARAM_CURRENT_PAIR_NUMBER = "PARAM_CURRENT_PAIR_NUMBER";
    public static String PARAM_CURRENT_BOARD ="PARAM_CURRENT_BOARD";

    CreationView creationView;
    CreationInteractor creationInteractor;

    int idCurrentPair;
    Board mBoard;

    @Override
    public void setView(CreationView view) {
        if (view == null) throw new IllegalArgumentException("You can't set a null view");

        creationView = view;
        creationInteractor = new CreationInteractor(creationView.getContext().getApplicationContext());
    }

    @Override
    public void detachView() {
        creationView = null;
    }

    public void updateOrAddBoard(Board board){
        creationInteractor.updateOrAddBoard(board);
        this.mBoard = creationInteractor.getBoard(this.mBoard.getTitle());

    }

    public void savePairInBoard(Pair pair){

        creationInteractor.savePairInBoard(this.mBoard.getTitle(), pair);
        this.mBoard = creationInteractor.getBoard(this.mBoard.getTitle());
    }

    public int getIdCurrentPair() {

        return idCurrentPair;
    }

    public void setIdCurrentPair(int idCurrentPair) {
        this.idCurrentPair = idCurrentPair;
    }


    private Pair loadPairFromExistingBoard(String jsonmBoard){
        //TODO: al llamar a este método ya tengo en el Bundle Title y BOard... quitar board, demora innecesaria. Revisar q siempre estén los dos
        Board mBoard = this.getCurrentBoard(jsonmBoard);
        //Actualizamos mBoard con valor del context
        mBoard = creationInteractor.getBoard(mBoard.getTitle());
        //Buscamos currentPair
        Pair currentPair = new Pair();
        if (null!=mBoard.getPairs() && mBoard.getPairs().size()>0) {
            this.idCurrentPair = mBoard.getPairs().size();
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
        if (bundleFromMain.getString(MainPresenter.PARAM_SELECTED_BOARD) != null) {  //Hay un board que ya existe que viene del view anterior

            currentPair =  loadPairFromExistingBoard(bundleFromMain.getString(MainPresenter.PARAM_SELECTED_BOARD));
        } else {
            //Creamos tablero
            currentPair = generateNewPair();

        }
        return currentPair;
    }



    public Bundle prepareForContentFragmentFirstLoad(boolean existeContentFragment,Bundle bundleFromMainActivity, Bundle savedInstanceState){
        if(!existeContentFragment) { //Primera vez que se carga el fragment
            Pair currentPair;
            currentPair = generateNextPair(bundleFromMainActivity);

            //Actualizamos bundle
            savedInstanceState = creationView.actualizeBundle(savedInstanceState,PARAM_CURRENT_PAIR,getJsonCurrentPair(currentPair));

            //Actualizamos mBoard
            getBoardWithTitleFromMain(bundleFromMainActivity);

        }

        return savedInstanceState;
    }

    public void createCreationActivity(boolean existeContentFragment,Bundle savedInstanceState, Bundle bundleFromMain){


        updateIdCurrentPairIfExistInContext(savedInstanceState);
        updateBoardIfExistInContent(savedInstanceState);

        savedInstanceState = prepareForContentFragmentFirstLoad(existeContentFragment,bundleFromMain,savedInstanceState);

        creationView.prepareForContentFragmentForRotate(savedInstanceState);


        //Inicializamos botones

        //Boton siguiente
        creationView.inicializeButtonNext();

        //Boton anterior
        creationView.inicializeButtonPast();
    }


    public void updateIdCurrentPairIfExistInContext(Bundle savedInstanceState){
        if(null!= savedInstanceState){
            this.setIdCurrentPair(savedInstanceState.getInt(PARAM_CURRENT_PAIR_NUMBER));
        }
    }
    public void updateBoardIfExistInContent(Bundle savedInstanceState){
        if(null!= savedInstanceState){
            this.setmBoard(this.getCurrentBoard(savedInstanceState.getString(PARAM_CURRENT_BOARD)));
        }
    }

    private void updateTitleFromBundle(Bundle bundleFromMain){
        //Actualizamos title tablero
        if (bundleFromMain.getString(MainPresenter.PARAM_TITLE) != null) {
            String title = bundleFromMain.getString(MainPresenter.PARAM_TITLE).toString();
            mBoard = creationInteractor.getBoard(title);
            if (null==mBoard){
                mBoard = new Board();

            }
            mBoard.setTitle(title);
        }

    }
    public Board getBoardWithTitleFromMain(Bundle bundleFromMain){
        updateTitleFromBundle(bundleFromMain);
        return mBoard;
    }

    public void setmBoard(Board mBoard) {
        this.mBoard = mBoard;
    }


    public boolean pairNotSavedYet(Pair pair){
        return null!=pair && (pair.getState().equals(Pair.State.COMPLETED) || pair.getState().equals(Pair.State.IN_PROCESS));
    }

    public String getNextPairOnBoard(int mCurrentPair){
        Pair pairSgte = new Pair();
        String jsonPairAnt = null;
        if (mBoard.getPairs().size() > mCurrentPair) {
            pairSgte = mBoard.getPairs().get(mCurrentPair + 1);

        }else{
            pairSgte.setNumber(mCurrentPair+1);

        }
        jsonPairAnt = gson.toJson(pairSgte);
        return jsonPairAnt;

    }

    public void clickOnNextButton(boolean existsCreationFragment, Pair pair){

        inicializeBoardIfPairsAreNull();

        Bundle bundleSgte = new Bundle();
        if (pairNotSavedYet(pair)) {

            pair.setNumber(getIdCurrentPair());

            //Verificamos si ya pair existe para agregarlo o modificarlo
            savePairInBoard(pair);
            setIdCurrentPair(pair.getNumber());////////////////////////////

            //Rescatamos la pareja
            String jsonNextPair = getNextPairOnBoard(getIdCurrentPair());

            //Rellenamos Bundle con la pareja siguiente
            bundleSgte.putSerializable(CreationPresenter.PARAM_CURRENT_PAIR, jsonNextPair);

            //Vaciamos fragment y nos vamos al sgte
            putFragmentEmptyAndGoNext(bundleSgte);

            //Ponemos el boton Siguiente invisible de nuevo
            creationView.hideNextButton();

        } else {



            //Rescatamos la pareja
            String jsonNextPair = getNextPairOnBoard(getIdCurrentPair());

            //Rellenamos Bundle con la pareja siguiente
            bundleSgte.putSerializable(CreationPresenter.PARAM_CURRENT_PAIR, jsonNextPair);

            putFragmentEmptyAndGoNext(bundleSgte);

        }
        //Actualizamos creationFragment con el numero de la pareja
        actualicePairNumber(existsCreationFragment);

    }

    public void actualicePairNumber(boolean existsCreationFragment){
        //  Bundle bundleFromMain = getIntent().getExtras();

        if (existsCreationFragment) {
           creationView.actualicePairNumberInContentFragment();
        }
    }

    public void putFragmentEmptyAndGoNext(Bundle bundleSgte){
        FragmentTransaction ft = creationView.getFragmentManager().beginTransaction();

        //Incrementamos la pareja y pasamos el bundle
        incrementIdCurrentPair();
        bundleSgte.putInt(CreationPresenter.PARAM_CURRENT_PAIR_NUMBER, getIdCurrentPair());

        ft.setCustomAnimations(R.animator.slide_in_up, R.animator.slide_out_up).replace(R.id.content_frame,
                ContentFragment.newInstance(bundleSgte),
                ContentFragment.TAG).addToBackStack(null).commit();


    }


    public void clickOnPastButton(){
        //En caso de que se haya completado el estado de la pareja, guardamos
        ContentFragment f = (ContentFragment)creationView.getFragmentManager().findFragmentByTag(ContentFragment.TAG);
        Pair pairForSave = f.getContentPresenter().getmCurrentPair();
        if (pairForSave.getState().equals(Pair.State.COMPLETED)) {
            savePairInBoard(pairForSave);
        }

        putFragmentOnPast();

        creationView.setListenerBtnSgte();


        creationView.actualicePairNumberInContentFragment();

    }

    public void savingInstanceState(Bundle outState){
        if (null != this.getmBoard())   {
            //Salvamos lo que hay en mBoard
            updateOrAddBoard(getmBoard());
           //Guardamos el id VISUALIZADO en el momento de irnos
            outState.putInt(CreationPresenter.PARAM_CURRENT_PAIR_NUMBER,getIdCurrentPair());
            outState.putString(CreationPresenter.PARAM_CURRENT_BOARD, getJsonCurrentBoard(getmBoard()));

        }
    }

    public void putFragmentOnPast(){
        decrementIdCurrentPair();
        Pair pairAnt = getmBoard().getPairs().get(getIdCurrentPair());

        //Actualizamos fragment
        Bundle bundleAnt = new Bundle();
        String jsonPairAnt = getJsonCurrentPair(pairAnt);

        bundleAnt.putSerializable(CreationPresenter.PARAM_CURRENT_PAIR,jsonPairAnt);
        FragmentManager fragmentManager = creationView.getFragmentManager();
        FragmentTransaction ft =fragmentManager.beginTransaction();

        ft.setCustomAnimations(R.animator.slide_out_up_ant, R.animator.slide_in_up_ant).replace(R.id.content_frame,
                ContentFragment.newInstance(bundleAnt),
                ContentFragment.TAG).addToBackStack(null).commit();
    }

    public void inicializeBoardIfPairsAreNull(){
        if (null == mBoard.getPairs()) {
            mBoard.setPairs(new HashMap<Integer, Pair>());
        }
    }

    public Board getmBoard() {
        return mBoard;
    }

    public Board getmBoard(String title) {
        return creationInteractor.getBoard(title);
    }

    public void onPauseActivity() {
        creationInteractor.updateOrAddBoard(mBoard);
    }
}
