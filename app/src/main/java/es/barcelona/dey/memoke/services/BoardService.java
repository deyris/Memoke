package es.barcelona.dey.memoke.services;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.beans.Pair;
import es.barcelona.dey.memoke.beans.Tab;
import es.barcelona.dey.memoke.database.BoardDatabase;

/**
 * Created by deyris.drake on 13/3/16.
 */
public class BoardService {


    public Context context;

    public BoardService(Context context) {

        this.context = context.getApplicationContext();
        this.boardDatabase = new BoardDatabase();
    }

    private BoardDatabase boardDatabase;

    public void savePairInBoard(Board mBoard, Pair pair){

        if (null!=mBoard.getPairs() && mBoard.getPairs().containsKey(pair.getNumber())) {
            mBoard.getPairs().remove(pair.getNumber());
        }
        pair.setState(Pair.State.SAVED);

        mBoard.getPairs().put(pair.getNumber(), pair);

        //Persistimos lo que hay en el fragment

        boardDatabase.updateOrAddBoard(context, mBoard);
    }


    public boolean existsBoards(){
        List<Board> testBoard = boardDatabase.getBoards(context);

        return testBoard!=null && testBoard.size()>0;
    }

    public boolean existsThisBoard(String title){

        List<Board> testBoard = boardDatabase.getBoards(context);

        if (testBoard!=null) {
            for (Board board : testBoard) {
                if (null != board.getTitle() && board.getTitle().equals(title)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void deleteBoard(String title){
        boardDatabase.deleteBoard(context,title.trim());

    }

    public Board getBoard(String title){
        return boardDatabase.getBoard(context, title);
    }

    public void updateOrAddBoard(Board board){
        boardDatabase.updateOrAddBoard(context,board);
    }


}
