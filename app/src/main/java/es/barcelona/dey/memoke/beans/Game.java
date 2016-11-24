package es.barcelona.dey.memoke.beans;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by deyris.drake on 8/4/16.
 */
public class Game {
    private String title;
    private TabForGame[] tabForGames;
    private int maxMoveNumber;
    private Time time;
    private int totalSuccess;
    private int totalFailure;
    private ArrayList<Play> plays;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TabForGame[] getTabForGames() {
        return tabForGames;
    }

    public void setTabForGames(TabForGame[] tabForGames) {
        this.tabForGames = tabForGames;
    }

    public int getMaxMoveNumber() {
        return maxMoveNumber;
    }

    public void setMaxMoveNumber(int maxMoveNumber) {
        this.maxMoveNumber = maxMoveNumber;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public int getTotalSuccess() {
        return totalSuccess;
    }

    public void setTotalSuccess(int totalSuccess) {
        this.totalSuccess = totalSuccess;
    }

    public int getTotalFailure() {
        return totalFailure;
    }

    public void setTotalFailure(int totalFailure) {
        this.totalFailure = totalFailure;
    }

    public ArrayList getPlays() {
        return plays;
    }

    public void setPlays(ArrayList plays) {
        this.plays = plays;
    }
}
