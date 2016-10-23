package es.barcelona.dey.memoke.presenters;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
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
        tabsForGame = boardInteractor.buildGameFromBoard(currentBoard);
        game.setTabForGames(tabsForGame);
        game.setTitle(currentBoard.getTitle());
        this.game = game;
        this.clickedPositions = new ArrayList();
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
            boardView.disappearsTabForGame(currentFrame.get(currentPlay.getMovedTabs()[0].getPositionInBoard()));
            boardView.disappearsTabForGame(currentFrame.get(currentPlay.getMovedTabs()[1].getPositionInBoard()));

            if (game.getTotalSuccess()==game.getTabForGames().length/2){
                boardView.warnYouWin();
            }

        }else{
            //Fail
            game.setTotalFailure(game.getTotalFailure() + 1);
            //Se giran las fichas
            boardView.setAnimationToFrame(currentFrame.get(currentPlay.getMovedTabs()[0].getPositionInBoard()),currentPlay.getMovedTabs()[0].getPositionInBoard());
            boardView.setAnimationToFrame(currentFrame.get(currentPlay.getMovedTabs()[1].getPositionInBoard()),currentPlay.getMovedTabs()[1].getPositionInBoard());
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

            if (null!=lastPlay.getMovedTabs()[0] && lastPlay.getMovedTabs()[0].getPositionInBoard() == position) {
                lastPlay.getMovedTabs()[0]=null;
                if (lastPlay.isEmpty()){
                    removeLastPlayFromGame();
                }
                return;
            }
            if (null!=lastPlay.getMovedTabs()[1] && lastPlay.getMovedTabs()[1].getPositionInBoard() == position) {
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


    public void manageClickOnFrame(View v, int position){
        final Handler handler = new Handler();
        final int pos = position;
        boolean itsTheSame = false;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                finaliceLastPlay();

            }
        };

        if (tabsForGame[pos].isShowingBack()) {
            handler.removeCallbacks(runnable);
            boardView.setAnimationToFrame((FrameLayout) v, position);
            removeTabFromPlay(position);
            return;
        }
        getClickedPositions().add(pos);

        FrameLayout frameLayout = (FrameLayout) v;

        tabsForGame[position].setPositionInBoard(position);

        currentFrame.put(position, frameLayout);
        boolean isImparAndNotOne = this.clickedPositions.size() % 2 != 0 && this.clickedPositions.size() > 1;
        if (isImparAndNotOne) { //Si es impar las analizo ya

            finaliceLastPlay();
            actualicePlays(pos);

            boardView.setAnimationToFrame(frameLayout, position);

        } else {
            actualicePlays(pos);

            boardView.setAnimationToFrame(frameLayout, position);
            handler.postDelayed(runnable, 3000); // after 3 sec

        }
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

    public TabForGame[] getTabsForGame() {
        return tabsForGame;
    }

    public void setTabsForGame(TabForGame[] tabsForGame) {
        this.tabsForGame = tabsForGame;
    }

    public HashMap<Integer, FrameLayout> getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(HashMap<Integer, FrameLayout> currentFrame) {
        this.currentFrame = currentFrame;
    }
}
