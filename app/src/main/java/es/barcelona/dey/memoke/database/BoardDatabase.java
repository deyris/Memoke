package es.barcelona.dey.memoke.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.List;

import es.barcelona.dey.memoke.beans.Board;

/**
 * Created by deyris.drake on 13/3/16.
 */
public class BoardDatabase {

    private static final Gson GSON = new Gson();

    private static final String PREF_BOARD_LIST = "boardList";
    private static final String PREF_BOARD_SELECTED = "boardSelected";

    private  List<Board> boards = null;

    private  Board selectedBoard = null;

    public  List<Board> getBoards(Context context) {
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

    public  void addBoard(Context context, Board board) {
        List<Board> boards = getBoards(context);
        boards.add(board);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREF_BOARD_LIST, GSON.toJson(boards)).commit();
    }

    public  void updateOrAddBoard(Context context, Board board){
        List<Board>boards = getBoards(context);
        boolean existBoard = false;
        int loc = -1;
        for(Board b: boards){
            loc++;
            if (null!=b.getTitle() && b.getTitle().equals(board.getTitle())){
                existBoard = true;
                break;
            }

        }
        if (existBoard){
            //Borramos
            boards.remove(loc);
        }
        boards.add(board);

        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREF_BOARD_LIST, GSON.toJson(boards)).commit();



    }

    public   Board getBoard(Context context, String title){
        Board board = null;

        List<Board>boards = this.getBoards(context);

        for(Board b: boards){
            if (b.getTitle().equals(title)){
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                preferences.edit().putString(PREF_BOARD_SELECTED, GSON.toJson(b)).commit();
                return b;
            }
        }

        return board;

    }


    public  void deleteBoard (Context context, String title){
        List<Board> boards = getBoards(context);
        Board board = getBoard(context, title);
        if (null!=board) {
            boards.remove(board);
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREF_BOARD_LIST, GSON.toJson(boards)).commit();
            clearCache();
        }
    }

    private  void clearCache(){
        this.boards = null;
    }


    public List<Board> getBoards() {
        return boards;
    }

    public void setBoards(List<Board> boards) {
        this.boards = boards;
    }

    public Board getSelectedBoard() {
        return selectedBoard;
    }

    public void setSelectedBoard(Board selectedBoard) {
        this.selectedBoard = selectedBoard;
    }
}
