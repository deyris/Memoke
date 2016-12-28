package es.barcelona.dey.memoke.interactors;

import android.content.Context;

import com.google.gson.Gson;

import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.clients.MemokeApp;
import es.barcelona.dey.memoke.services.BoardService;

/**
 * Created by deyris.drake on 18/9/16.
 */
public class MainInteractor {

    BoardService boardService;
    Context context;

    public MainInteractor(Context context) {

        this.boardService = new BoardService(context);
        this.context = context.getApplicationContext();
    }

    public boolean existsMoreBoards(){
        return boardService.existsBoards();
    }

    public boolean existsThisBoard(String title){
        return boardService.existsThisBoard(title);
    }

    public String restoreBoard(String title){
        Board board = boardService.getBoard(title);
        final Gson gson = new Gson();
        String jsonSelectedBoard = gson.toJson(board).toString();
        return jsonSelectedBoard;
    }

    public void resetBoard(String title){
        boardService.resetSelectedBoard(title);
    }

    public void deleteBoard(String title){
        boardService.deleteBoard(title);
    }

    public BoardService getBoardService() {
        return boardService;
    }

    public void setBoardService(BoardService boardService) {
        this.boardService = boardService;
    }


}
