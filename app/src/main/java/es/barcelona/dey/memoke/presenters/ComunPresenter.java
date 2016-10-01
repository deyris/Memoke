package es.barcelona.dey.memoke.presenters;

import com.google.gson.Gson;

import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.beans.Pair;

/**
 * Created by deyris.drake on 1/10/16.
 */
public class ComunPresenter {

    final Gson gson = new Gson();


    public Pair getCurrentPair(String jsonCurrentPair) {
        return gson.fromJson(jsonCurrentPair, Pair.class);
    }

    public String getJsonCurrentPair(Pair currentPair){
        return gson.toJson(currentPair).toString();
    }

    public Board getCurrentBoard(String jsonCurrentBoard) {
        return gson.fromJson(jsonCurrentBoard, Board.class);
    }

    public String getJsonCurrentBoard(Board currentBoard){
        return gson.toJson(currentBoard).toString();
    }
}
