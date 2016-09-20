package es.barcelona.dey.memoke.presenters;

/**
 * Created by deyris.drake on 18/9/16.
 */
public interface Presenter<V> {

    void setView(V view);

    void detachView();
}
