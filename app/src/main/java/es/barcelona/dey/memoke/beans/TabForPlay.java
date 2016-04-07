package es.barcelona.dey.memoke.beans;

/**
 * Created by deyris.drake on 7/4/16.
 */
public class TabForPlay extends Tab {

    private int numberOfPair;
    private boolean showingFront = false;
    private boolean ok;

    public TabForPlay(Tab tab){
       this.setText(tab.getText());
        this.setSize(tab.getSize());
        this.setUri(tab.getUri());
        this.setType(tab.getType());

    }

    public TabForPlay(){

    }
    public int getNumberOfPair() {
        return numberOfPair;
    }

    public void setNumberOfPair(int numberOfPair) {
        this.numberOfPair = numberOfPair;
    }

    public boolean isShowingFront() {
        return showingFront;
    }

    public void setShowingFront(boolean showingFront) {
        this.showingFront = showingFront;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}
