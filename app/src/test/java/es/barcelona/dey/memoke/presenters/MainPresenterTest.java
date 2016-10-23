package es.barcelona.dey.memoke.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import es.barcelona.dey.memoke.interactors.MainInteractor;
import es.barcelona.dey.memoke.views.MainView;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by deyris.drake on 3/10/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest extends InstrumentationTestCase{

    @Mock
    MainView mainViewContext;
    @Mock
    PreferenceManager preferenceManager;

    @InjectMocks
    MainPresenter mainPresenter;

    @Mock
    MainInteractor mainInteractor;
     SharedPreferences sharedPrefs = Mockito.mock(SharedPreferences.class);
     Context context = Mockito.mock(Context.class);

    @Before
    public void setUp(){
        mainInteractor = Mockito.mock(MainInteractor.class);

        this.sharedPrefs = Mockito.mock(SharedPreferences.class);
        this.context = Mockito.mock(Context.class);
        Mockito.when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPrefs);

        mainPresenter = new MainPresenter();
        mainPresenter.setView(mainViewContext);

    }

    @Test
    public void isButtonMoreBoardVisibleWithMoreBoard_return_true(){

       // Mockito.when(PreferenceManager.getDefaultSharedPreferences(context)).thenReturn(sharedPrefs);

       // when(mainInteractor.existsMoreBoards()).thenReturn(true);
        mainPresenter.isButtonMoreBoardsVisible();
        verify(mainInteractor).existsMoreBoards();

       // assertEquals (mainPresenter.isButtonMoreBoardsVisible(),true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setViewIsIllegalArgumentExceptionIfViewNull(){
        MainPresenter mainPresenter = new MainPresenter();
        mainPresenter.setView(null);
    }
}