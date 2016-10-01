package es.barcelona.dey.memoke.beans;

import android.content.Context;

import es.barcelona.dey.memoke.beans.Tab;

/**
 * Created by deyris.drake on 16/2/16.
 */
public class Pair {
    public enum State {
        IN_PROCESS, SAVED, EMPTY, COMPLETED
    }

    private Tab[] tabs = new Tab[2];
    private int number;
    private State state;
    private boolean simetric;


    public Pair(){
        this.state = State.EMPTY;
    }
    public Tab[] getTabs() {
        return tabs;
    }

    public void setTabs(Tab[] tabs) {
        this.tabs = tabs;

    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public State getState() {

        if(null==tabs[0] || null==tabs[1]){
            this.state=State.IN_PROCESS;
        }
        if (null==tabs[0] && null==tabs[1]){
            this.state=State.EMPTY;
        }
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isSimetric() {
        return simetric;
    }

    public void setSimetric(boolean simetric) {
        this.simetric = simetric;
    }

    public boolean isReadyToBePassed(){
        return this.getState().equals(Pair.State.COMPLETED) || this.getState().equals(Pair.State.SAVED);
    }


}
