package es.barcelona.dey.memoke.presenters;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.test.RenamingDelegatingContext;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.database.BoardDatabase;
import es.barcelona.dey.memoke.interactors.MainInteractor;
import es.barcelona.dey.memoke.ui.CreationActivity;
import es.barcelona.dey.memoke.views.MainView;

/**
 * Created by deyris.drake on 18/9/16.
 */
public class MainPresenter implements Presenter<MainView>{

    public static String PARAM_TITLE = "TITLE";
    public static String PARAM_SELECTED_BOARD = "PARAM_SELECTED_BOARD";

    MainView mainView;
    public String mTitle="";


    MainInteractor mainInteractor;

    Board mBoard;

    public MainPresenter(MainInteractor interactor) {
        mainInteractor = interactor;
    }

    @Override
    public void setView(MainView view) {

        if (view == null) throw new IllegalArgumentException("You can't set a null view");
        mainView = view;
    }



    @Override
    public void detachView() {
        mainView = null;
    }


    public void clickPositiveButtonOnDialog(String titleBoard){

        deleteBoard(titleBoard);
        // List<Board> testBoard = BoardDatabase.getBoards(MainFragment.this.getActivity());
        mainView.openToCreateBoardFromZero(titleBoard);

    }

    public void clickNegativeButtonOnDialog(String title){

        //Restablecer el board
        String jsonSelectedBoard =getBoardForRestore(title);

        mainView.openToCreateBoardFromOther(jsonSelectedBoard,title);
    }


    public boolean isButtonMoreBoardsVisible(){

        return mainInteractor.existsMoreBoards();
    }

    public void clickOnCreateButton(String title){
      //  Board board = mainInteractor.getBoardService().getBoard(title);
        this.mTitle = title;

        if ((mBoard!=null && mBoard.getTitle().equals(title))|| mainInteractor.existsThisBoard(title)){
            mainView.launchAlertExistsThisBoard();

        }else{
            mainView.openToCreateBoardFromZero(title);
        }

    }

    public void updateBoard(Board mBoard){
        this.mBoard = mBoard;
    }


    public void manageVisibiltyForLoadButton(){
        if (isButtonMoreBoardsVisible()) {
            mainView.showButtonMoreBoards();
        } else {
            mainView.hideButtonMoreBoards();
        }
    }

    public String getBoardForRestore(String title){
        return mainInteractor.restoreBoard(title);
    }

    public void deleteBoard(String title){
        mainInteractor.deleteBoard(title);
    }


    public void onResumeFragment() {
        if (!"".equals(mTitle)) {
            mainInteractor.resetBoard(mTitle);
            this.mBoard = mainInteractor.getBoardService().getBoard(mTitle);
        }
    }
}
