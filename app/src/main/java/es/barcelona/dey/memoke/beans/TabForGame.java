package es.barcelona.dey.memoke.beans;

/**
 * Created by deyris.drake on 7/4/16.
 */
public class TabForGame extends Tab {

    private int numberOfPair;
    private boolean showingBack;
    private boolean ok;
    private long idFrame;
    private int positionInBoard = -1;

    public TabForGame(Tab tab){
       this.setText(tab.getText());
        this.setSize(tab.getSize());
        this.setUri(tab.getUri());
        this.setType(tab.getType());

    }

    public TabForGame(){

    }
    public int getNumberOfPair() {
        return numberOfPair;
    }

    public void setNumberOfPair(int numberOfPair) {
        this.numberOfPair = numberOfPair;
    }

    public boolean isShowingBack() {
        return showingBack;
    }

    public void setShowingBack(boolean showingBack) {
        this.showingBack = showingBack;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public long getIdFrame() {
        return idFrame;
    }

    public void setIdFrame(long idFrame) {
        this.idFrame = idFrame;
    }

    public int getPositionInBoard() {
        return positionInBoard;
    }

    public void setPositionInBoard(int positionInBoard) {
        this.positionInBoard = positionInBoard;
    }
}
