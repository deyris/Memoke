package es.barcelona.dey.memoke.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by deyris.drake on 13/3/16.
 */
public class Board {

    public enum State {
        IN_PROCESS, SAVED, EMPTY, COMPLETED
    }

    private Map<Integer,Pair> pairs;
    private String title;
    private State state;
    private Date date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Map<Integer,Pair> getPairs() {
        return pairs;
    }

    public void setPairs(Map<Integer,Pair> pairs) {

        this.pairs = pairs;
    }



}
