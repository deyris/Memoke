package es.barcelona.dey.memoke.interactors;

import android.content.Context;

import es.barcelona.dey.memoke.clients.MemokeApp;
import es.barcelona.dey.memoke.services.BoardService;

/**
 * Created by deyris.drake on 18/9/16.
 */
public class ContentInteractor {

    BoardService boardService;


    public ContentInteractor(Context context) {
        this.boardService = new BoardService(context);
    }
}
