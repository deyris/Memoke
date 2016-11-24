package es.barcelona.dey.memoke.presenters;

import android.content.Context;
import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import es.barcelona.dey.memoke.beans.Pair;
import es.barcelona.dey.memoke.interactors.ContentInteractor;
import es.barcelona.dey.memoke.interactors.MainInteractor;
import es.barcelona.dey.memoke.views.ContentView;
import es.barcelona.dey.memoke.views.MainView;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by deyris.drake on 27/10/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class ContentPresenterTest {


    @Mock
    ContentView contentViewContext;

    @InjectMocks
    ContentPresenter contentPresenter;

    @Mock
    ContentInteractor contentInteractor;


    @Mock
    Bundle savedInstanceState;
    Bundle arguments;
    Pair pair;

   // Context context = Mockito.mock(Context.class);

    @Before
    public void setUp(){
        contentInteractor = Mockito.mock(ContentInteractor.class);

       // this.context = Mockito.mock(Context.class);

        contentPresenter = Mockito.mock(ContentPresenter.class);
       // contentPresenter.setView(contentViewContext);

    }
    @Test(expected = IllegalArgumentException.class)
    public void setViewIfViewNull_IllegalArgumentException(){
        contentPresenter.setView(null);
    }

    @Test
    public void onViewCreatedIfJsonCurrentPairNull_setmCurrentPair(){
        arguments = Mockito.mock(Bundle.class);
        when(contentPresenter.getCurrentPairFromContext(savedInstanceState,arguments)).thenReturn("algo");
        contentPresenter.onViewCreated(savedInstanceState,arguments);
        pair = Mockito.mock(Pair.class);
        verify(contentPresenter).setmCurrentPair(pair);
    }

    @Test
    public void onViewCreatedIfJsonCurrentPairNull_fillPairOnView(){
        doNothing().when(contentPresenter).getBundleFromContext(any(Bundle.class),any(Bundle.class));
        when(contentPresenter.getCurrentPairFromContext(any(Bundle.class),any(Bundle.class))).thenReturn(anyString());
        verify(contentPresenter).fillPairOnView();
    }

    @Test
    public void onViewCreated_fillNumberInCurrentPairByArguments(){

    }

    @Test
    public void onViewCreated_controlButtonsAntSgte(){

    }
}