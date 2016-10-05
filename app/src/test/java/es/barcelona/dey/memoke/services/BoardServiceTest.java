package es.barcelona.dey.memoke.services;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.database.BoardDatabase;

import static org.junit.Assert.*;

/**
 * Created by deyris.drake on 4/10/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class BoardServiceTest {

    @Mock
    Context context;
    @Mock
    BoardDatabase boardDatabase;



    @Test
    public void validExistsBoardReturnFalseIfEmptyArray(){

        Mockito.when(BoardDatabase.getBoards(context)).thenReturn(new ArrayList<Board>());
        BoardService boardService = new BoardService(context);
        assertEquals(boardService.existsBoards(),false);

    }

    @Test
    public void validExistsBoardReturnFalseIfNull(){

        Mockito.when(BoardDatabase.getBoards(context)).thenReturn(null);
        BoardService boardService = new BoardService(context);
        assertEquals(boardService.existsBoards(),false);

    }

}