package es.barcelona.dey.memoke.beans;

import java.sql.Time;

/**
 * Created by deyris.drake on 8/4/16.
 */
public class Play {
    private String title;
    private TabForPlay[] tabForPlays;
    private int moveNumber;
    private long score;
    private Time time;
    private int totalSuccess;
    private int totalFailure;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TabForPlay[] getTabForPlays() {
        return tabForPlays;
    }

    public void setTabForPlays(TabForPlay[] tabForPlays) {
        this.tabForPlays = tabForPlays;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
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
}
