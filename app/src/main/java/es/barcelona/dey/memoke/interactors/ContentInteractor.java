package es.barcelona.dey.memoke.interactors;

import android.content.Context;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.clients.MemokeApp;
import es.barcelona.dey.memoke.services.BoardService;
import es.barcelona.dey.memoke.services.ImageService;

/**
 * Created by deyris.drake on 18/9/16.
 */
public class ContentInteractor {

    BoardService boardService;
    ImageService imageService;

    public ContentInteractor(Context context) {
        this.boardService = new BoardService(context.getApplicationContext());
        this.imageService = new ImageService();
    }

    public File createImageFile() throws IOException {
        return imageService.createImageFile();
    }

    public void updateOrAddBoard(Board board){
        boardService.updateOrAddBoard(board);
    }

}
