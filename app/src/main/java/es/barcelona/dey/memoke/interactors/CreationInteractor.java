package es.barcelona.dey.memoke.interactors;

import android.content.Context;

import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.beans.Pair;
import es.barcelona.dey.memoke.services.BoardService;

/**
 * Created by deyris.drake on 18/9/16.
 */
public class CreationInteractor {

    BoardService boardService;
    Context context;

    public CreationInteractor(Context context) {

        this.boardService = new BoardService(context);
        this.context = context;
    }

    public void savePairInBoard(Board board, Pair pair){
        boardService.savePairInBoard(context, board, pair);
    }

}
