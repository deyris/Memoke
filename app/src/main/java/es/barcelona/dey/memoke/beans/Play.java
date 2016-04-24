package es.barcelona.dey.memoke.beans;

/**
 * Created by deyris.drake on 9/4/16.
 * A Play is formed by two moves
 */
public class Play {
    private TabForGame[] movedTabs = new TabForGame[2];
    private boolean finished;
    private boolean completed;

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

    public boolean isCompleted() {

        return (null!=getMovedTabs()[0] && null!=getMovedTabs()[1]);
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
