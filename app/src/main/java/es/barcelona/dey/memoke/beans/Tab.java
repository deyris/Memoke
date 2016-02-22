package es.barcelona.dey.memoke.beans;

/**
 * Created by deyris.drake on 16/2/16.
 */
public class Tab {

    public enum Type {
        TEXT, PHOTO, FIGURE
    }

    public enum State {
        IN_PROCESS, SAVED
    }

    private int number;
    private Type type;
    private State state;
    private String uri;
    private String text;
    private int size;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


}
