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

    public CreationInteractor(Context context) {

        this.boardService = new BoardService(context.getApplicationContext());
    }

    public void savePairInBoard(String title, Pair pair){
        Board board = getBoard(title);
        if (board==null){
            board = new Board();
            board.setTitle(title);
        }
        boardService.savePairInBoard(board, pair);
    }

    public void updateOrAddBoard(Board board){
        boardService.updateOrAddBoard(board);
    }

    public Board getBoard(String title){
        return boardService.getBoard(title);
    }

}
