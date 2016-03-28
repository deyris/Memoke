package es.barcelona.dey.memoke.services;

import android.content.Context;

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
}
