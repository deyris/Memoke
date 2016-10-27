package es.barcelona.dey.memoke.presenters;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import es.barcelona.dey.memoke.interactors.ContentInteractor;
import es.barcelona.dey.memoke.interactors.MainInteractor;
import es.barcelona.dey.memoke.views.ContentView;
import es.barcelona.dey.memoke.views.MainView;

import static org.junit.Assert.*;

/**
 * Created by deyris.drake on 27/10/16.
 */
public class ContentPresenterTest {


    @Mock
    ContentView contentViewContext;


    @InjectMocks
    ContentPresenter contentPresenter;

    @Mock
    ContentInteractor contentInteractor;

    Context context = Mockito.mock(Context.class);

    @Before
    public void setUp(){
        contentInteractor = Mockito.mock(ContentInteractor.class);

        this.context = Mockito.mock(Context.class);

        contentPresenter = new ContentPresenter(contentInteractor);
        contentPresenter.setView(contentViewContext);

    }
    @Test
    public void testSetView() throws Exception {

    }
}