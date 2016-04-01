package es.barcelona.dey.memoke.services;

import android.content.Context;

import java.util.List;

import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.beans.Pair;
import es.barcelona.dey.memoke.database.BoardDatabase;

/**
 * Created by deyris.drake on 13/3/16.
 */
public class BoardServices {


    public void savePairInBoard(Context context, Board mBoard, Pair pair){
        if (mBoard.getPairs().containsKey(pair.getNumber())) {
            mBoard.getPairs().remove(pair.getNumber());
        }
        pair.setState(Pair.State.SAVED);
        mBoard.getPairs().put(pair.getNumber(), pair);

        //Persistimos lo que hay en el fragment

        BoardDatabase.updateOrAddBoard(context, mBoard);
    }


    public boolean existsBoards(Context context){
        List<Board> testBoard = BoardDatabase.getBoards(context);

        return testBoard.size()>0;
    }

    public boolean existThisBoard(String title, Context context){
        List<Board> testBoard = BoardDatabase.getBoards(context);

        for (Board board: testBoard){
            if (null!=board.getTitle() && board.getTitle().equals(title)){
                return true;
            }
        }

        return false;
    }

    public void deleteBoard(String title, Context context){
        BoardDatabase.deleteBoard(context,title.trim());

    }

    public Board getBoard(String title, Context context){
        return BoardDatabase.getBoard(context, title);
    }
}
