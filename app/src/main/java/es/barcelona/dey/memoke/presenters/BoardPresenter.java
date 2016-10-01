package es.barcelona.dey.memoke.presenters;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.beans.Game;
import es.barcelona.dey.memoke.beans.Play;
import es.barcelona.dey.memoke.beans.TabForGame;
import es.barcelona.dey.memoke.interactors.BoardInteractor;
import es.barcelona.dey.memoke.views.BoardView;

/**
 * Created by deyris.drake on 1/10/16.
 */
public class BoardPresenter extends ComunPresenter implements Presenter<BoardView>{

    BoardView boardView;
    BoardInteractor boardInteractor;
    Game game;
    TabForGame[] tabsForGame = new TabForGame[]{};
    HashMap<Integer,FrameLayout> currentFrame = new HashMap<Integer,FrameLayout>();
    ArrayList clickedPositions;

    @Override
    public void setView(BoardView view) {
        if (view == null) throw new IllegalArgumentException("You can't set a null view");
        boardInteractor = new BoardInteractor();
        boardView = view;
    }

    @Override
    public void detachView() {
        boardView = null;
    }

    public void doLock(boolean locked, Activity activity) {
        if (locked) {
            int o = boardView.getContext().getResources().getConfiguration().orientation;
            if (o == Configuration.ORIENTATION_LANDSCAPE)
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            else if (o == Configuration.ORIENTATION_PORTRAIT)
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
    }

    public void inicialiceGame(Board currentBoard){
        Game game = new Game();
        TabForGame[] tabsForGame = boardInteractor.buildGameFromBoard(currentBoard);
        game.setTabForGames(tabsForGame);
        game.setTitle(currentBoard.getTitle());
        this.game = game;
        this.clickedPositions = new ArrayList();
    }

    public ArrayList inicialiceClickedPositions(ArrayList clickedPositions){

        this.clickedPositions = new ArrayList();
        return new ArrayList();
    }

    public void finaliceLastPlay(){
        Play currentPlay = getLastPlayNotFinished(true);
        if (null==currentPlay || currentPlay.isFinished() || !currentPlay.isCompleted())    {
            return;
        }

        //Analizamos acierto o fail
        TabForGame tab1 = currentPlay.getMovedTabs()[0];
        TabForGame tab2 = currentPlay.getMovedTabs()[1];

        if (tab1.getNumberOfPair()==tab2.getNumberOfPair()){
            //Success
            game.setTotalSuccess(game.getTotalSuccess() + 1);
            //Se actualiza ok de TabForGame
            tab1.setOk(true);
            tab2.setOk(true);
            //Desaparecen la fichas
            boardView.disappearsTabForGame(currentPlay);
            if (game.getTotalSuccess()==game.getTabForGames().length/2){
                boardView.warnYouWin();
            }

        }else{
            //Fail
            game.setTotalFailure(game.getTotalFailure() + 1);
            //Se giran las fichas
            boardView.turnTabForGame(currentPlay);
        }

        //Se actualiza finished en Play
        currentPlay.setFinished(true);

    }

    public Play getLastPlayNotFinished(boolean needCompleted){
        int totalPlays = (null!=game.getPlays())?game.getPlays().size():0;
        Play lastPlay = null;
        if (totalPlays > 0) {
            for (int i=totalPlays-1;i>=0;i--){
                lastPlay = (Play) game.getPlays().get(i);
                if (lastPlay.isFinished()){
                    return null;
                }else{
                    if (needCompleted) {
                        if (lastPlay.isCompleted() && !lastPlay.isFinished()) {
                            return lastPlay;
                        } else {
                            lastPlay = null;
                        }
                    }else{
                        return  lastPlay;
                    }
                }
            }

        }

        return lastPlay;
    }

    public void actualicePlays(int position){
        TabForGame tab = game.getTabForGames()[position]; //tabsForGame[position];
        int totalPlays = (null!=game.getPlays())?game.getPlays().size():0;
        Play lastPlay = null;
        if (totalPlays > 0) {
            lastPlay = (Play) game.getPlays().get(totalPlays - 1);
        }
        if (lastPlay==null || lastPlay.isFinished()){
            //Se abre una nueva jugada
            Play newPlay = new Play();
            newPlay.getMovedTabs()[0]=tab;
            if (lastPlay==null){
                game.setPlays(new ArrayList<Play>());
            }
            game.getPlays().add(newPlay);
        }else{
            //Guardo la ficha movida
            lastPlay.getMovedTabs()[1]=tab;

        }
    }



    public void removeTabFromPlay(int position){
        Play lastPlay = getLastPlayNotFinished(false);
        if (position < clickedPositions.size()) {
            clickedPositions.remove(position);
        }

        if (null!=lastPlay) {

            if (lastPlay.getMovedTabs()[0].getPositionInBoard() == position) {
                lastPlay.getMovedTabs()[0]=null;
                if (lastPlay.isEmpty()){
                    removeLastPlayFromGame();
                }
                return;
            }
            if (lastPlay.getMovedTabs()[1].getPositionInBoard() == position) {
                lastPlay.getMovedTabs()[1] = null;
                if (lastPlay.isEmpty()){
                    removeLastPlayFromGame();                }
                return;
            }

        }

    }

    private void removeLastPlayFromGame(){
        this.game.getPlays().remove(this.game.getPlays().size()-1);
    }




    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public ArrayList getClickedPositions() {
        return clickedPositions;
    }

    public void setClickedPositions(ArrayList clickedPositions) {
        this.clickedPositions = clickedPositions;
    }
}
