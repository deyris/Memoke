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


    private List<Board> getBoardsFromCache(Context context){
        String boardsJson = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(BoardDatabase.PREF_BOARD_LIST,"empty");
        List<Board> boards = null;
        if (!"empty".equals(boardsJson)){
            boards =  GSON.fromJson(boardsJson, BoardList.class);
        }
        return boards;
    }
    public  List<Board> getBoards(Context context) {

        if (boards == null) {
            boards = getBoardsFromCache(context);
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
        if (boards!=null) {
            for (Board b : boards) {
                loc++;
                if (null != b.getTitle() && b.getTitle().equals(board.getTitle())) {
                    existBoard = true;
                    break;
                }

            }
            if (existBoard) {
                //Borramos
                boards.remove(loc);
            }
        }else{
            boards = new BoardList();
        }
        boards.add(board);

        this.boards = boards;
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREF_BOARD_SELECTED, GSON.toJson(board)).commit();
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREF_BOARD_LIST, GSON.toJson(boards)).commit();



    }

    private Board getSelectedBoardFromCache(Context context){
        String boardJson =  PreferenceManager.getDefaultSharedPreferences(context).getString(BoardDatabase.PREF_BOARD_SELECTED,"empty");
        Board board = null;

        if(!"empty".equals(boardJson)){
            board = GSON.fromJson(boardJson,Board.class);
        }

        return board;
    }

    public void resetSelectedBoard(String title){
        Board board = getSelectedBoard();
        if (null!=board && board.getTitle().equals(title)) {
            setSelectedBoard(null);
        }
    }

    public   Board getBoard(Context context, String title){
        Board board = getSelectedBoard();
        if (null!=board && !board.getTitle().equals(title)){
            board = null;
        }
        if (null==board){
            board = getSelectedBoardFromCache(context);
            if (null!=board) {
                boolean itsBoardSelected = board.getTitle().equals(title);
                if (itsBoardSelected) {
                    return board;
                } else {
                    board = null;
                }
            }

            if (null==board){
                List<Board>boards = this.getBoards(context);

                if (boards!=null) {
                    for (Board b : boards) {
                        if (b.getTitle().equals(title)) {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                            String r = GSON.toJson(b);
                            preferences.edit().putString(PREF_BOARD_SELECTED, r).commit();
                            selectedBoard = b;
                            board = b;
                            break;
                        }
                    }

                }
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
