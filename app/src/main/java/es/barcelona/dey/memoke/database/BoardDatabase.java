package es.barcelona.dey.memoke.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import es.barcelona.dey.memoke.ContentFragment;
import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.beans.Pair;

/**
 * Created by deyris.drake on 13/3/16.
 */
public class BoardDatabase {

    private static final Gson GSON = new Gson();

    private static final String PREF_BOARD_LIST = "boardList";
    private static final String PREF_BOARD_SELECTED = "boardSelected";

    private static List<Board> boards = null;

    private static Board selectedBoard = null;

    public static List<Board> getBoards(Context context) {
        if (boards == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String value = preferences.getString(PREF_BOARD_LIST, null);
            if (value != null) {
                boards = GSON.fromJson(value, BoardList.class);
            } else {
                boards = new BoardList();
            }
        }
        return boards;
    }

    public static void addBoard(Context context, Board board) {
        List<Board> boards = getBoards(context);
        boards.add(board);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREF_BOARD_LIST, GSON.toJson(boards)).commit();
    }

    public static void updateOrAddBoard(Context context, Board board){
        List<Board>boards = getBoards(context);
        boolean existBoard = false;

        for(Board b: boards){
            if (b.getTitle().equals(board.getTitle())){
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                preferences.edit().putString(PREF_BOARD_SELECTED, GSON.toJson(b)).commit();
                existBoard = true;
                break;
            }
        }
        if (!existBoard){
            //Añadimos el board nuevo
            addBoard(context, board);
        }


    }

    public static  Board getBoard(Context context, String title){
        Board board = null;

        List<Board>boards = getBoards(context);

        for(Board b: boards){
            if (b.getTitle().equals(title)){
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                preferences.edit().putString(PREF_BOARD_SELECTED, GSON.toJson(b)).commit();
                return b;
            }
        }

        return board;

    }


    public static void deleteBoard (Context context, String title){

        List<Board>boards = getBoards(context);

        for(Board b: boards){
            if (b.getTitle().equals(title)){
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                preferences.edit().remove(GSON.toJson(b)).commit();
                break;
            }
        }

        boards = getBoards(context);
    }

}
