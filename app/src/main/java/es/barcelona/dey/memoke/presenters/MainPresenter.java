package es.barcelona.dey.memoke.presenters;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

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
    MainInteractor mainInteractor;

    public MainPresenter() {

    }

    @Override
    public void setView(MainView view) {
        if (view == null) throw new IllegalArgumentException("You can't set a null view");

        mainView = view;
        mainInteractor = new MainInteractor(mainView.getContext());
    }

    @Override
    public void detachView() {
        mainView = null;
    }


    public void clickPositiveButtonOnDialog(Activity activity,DialogInterface dialog, String titleBoard){
        //Borramos el tablero
        deleteBoard(titleBoard);
        // List<Board> testBoard = BoardDatabase.getBoards(MainFragment.this.getActivity());
        openToCreateBoardFromZero(activity,titleBoard);
        dialog.dismiss();
    }

    public void clickNegativeButtonOnDialog(Activity activity, String title, DialogInterface dialog){
        //mainView.openToCreateBoardFromOther();
        Intent i = new Intent(activity, CreationActivity.class);
        i.putExtra(MainPresenter.PARAM_TITLE, title);
        //Restablecer el board
        String jsonSelectedBoard =getBoardForRestore(title);
        i.putExtra(MainPresenter.PARAM_SELECTED_BOARD, jsonSelectedBoard);


        activity.startActivity(i);
        dialog.dismiss();
    }


    public boolean isButtonMoreBoardsVisible(){
        return mainInteractor.existsMoreBoards();
    }

    public void clickOnCreateButton(Activity activity, String title){
        if (mainInteractor.existsThisBoard(title)){
            mainView.launchAlertExistsThisBoard();

        }else{
            openToCreateBoardFromZero(activity, title);
        }

    }

    public void openToCreateBoardFromZero(Activity activity, String title) {
        Intent i = new Intent(activity, CreationActivity.class);
        i.putExtra(MainPresenter.PARAM_TITLE, title);

        activity.startActivity(i);
    }

    public void visibiltyForLoadButton(Button mBtnLoad){
        if (isButtonMoreBoardsVisible()) {
            mBtnLoad.setVisibility(View.VISIBLE);
        } else {
            mBtnLoad.setVisibility(View.GONE);
        }
    }

    public String getBoardForRestore(String title){
        return mainInteractor.restoreBoard(title);
    }

    public void deleteBoard(String title){
        mainInteractor.deleteBoard(title);
    }
}
