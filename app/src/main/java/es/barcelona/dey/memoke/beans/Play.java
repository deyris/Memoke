package es.barcelona.dey.memoke.beans;

/**
 * Created by deyris.drake on 9/4/16.
 * A Play is formed by two moves
 */
public class Play {
    private TabForGame[] movedTabs = new TabForGame[2];
    private boolean finished;

    public TabForGame[] getMovedTabs() {
        return movedTabs;
    }

    public void setMovedTabs(TabForGame[] movedTabs) {
        this.movedTabs = movedTabs;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
