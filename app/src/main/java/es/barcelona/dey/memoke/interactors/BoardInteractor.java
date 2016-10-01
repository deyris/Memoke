package es.barcelona.dey.memoke.interactors;

import android.content.Context;

import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.beans.TabForGame;
import es.barcelona.dey.memoke.services.PlayService;

/**
 * Created by deyris.drake on 2/10/16.
 */
public class BoardInteractor {

    PlayService playService;

    public BoardInteractor() {
        this.playService = new PlayService();
    }

    public TabForGame[] buildGameFromBoard(Board board){
        return playService.getTabsForPlay(board);
    }
}
