package es.barcelona.dey.memoke.presenters;

import es.barcelona.dey.memoke.interactors.MainInteractor;
import es.barcelona.dey.memoke.views.MainView;

/**
 * Created by deyris.drake on 18/9/16.
 */
public class MainPresenter implements Presenter<MainView>{

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

    public boolean isButtonLoadVisible(){
        return mainInteractor.existsMoreBoards();
    }

    public void verifyIfExistBoardAndLauncPopup(String title){
        if (mainInteractor.existThisBoard(title)){
            mainView.launchAlertExistsThisBoard();

        }else{
            mainView.openToCreateBoard(true);
        }

    }

    public String getBoardForRestore(String title){
        return mainInteractor.restoreBoard(title);
    }

    public void deleteBoard(String title){
        mainInteractor.deleteBoard(title);
    }
}
