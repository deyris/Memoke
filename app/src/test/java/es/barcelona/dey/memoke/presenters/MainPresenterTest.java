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
public class MainPresenterTest{

    @Mock
    MainView mainViewContext;


    @InjectMocks
    MainPresenter mainPresenter;

    @Mock
    MainInteractor mainInteractor;

    Context context = Mockito.mock(Context.class);

    @Before
    public void setUp(){
        mainInteractor = Mockito.mock(MainInteractor.class);

        this.context = Mockito.mock(Context.class);

        mainPresenter = new MainPresenter(mainInteractor);
        mainPresenter.setView(mainViewContext);

    }

    @Test(expected = IllegalArgumentException.class)
    public void setViewIfViewNull_IllegalArgumentException(){
        mainPresenter.setView(null);
    }

    @Test
    public void isButtonMoreBoardVisibleIfMoreBoard_returnTrue(){

        when(mainInteractor.existsMoreBoards()).thenReturn(true);
        assertEquals (mainPresenter.isButtonMoreBoardsVisible(),true);
    }

    @Test
    public void ifExistBoard_launchAlertExistsThisBoard(){

        when(mainInteractor.existsThisBoard(anyString())).thenReturn(true);
        mainPresenter.clickOnCreateButton(anyString());
        verify(mainViewContext).launchAlertExistsThisBoard();

    }

    @Test
    public void ifNotExistBoard_openToCreateBoardFromZero(){

        when(mainInteractor.existsThisBoard(anyString())).thenReturn(false);
        mainPresenter.clickOnCreateButton(anyString());
        verify(mainViewContext).openToCreateBoardFromZero(anyString());

    }

    @Test
    public void ifExistMoreBoard_buttonMoreBoardsIsVisible(){

        when(mainPresenter.isButtonMoreBoardsVisible()).thenReturn(true);
        mainPresenter.manageVisibiltyForLoadButton();
        verify(mainViewContext).showButtonMoreBoards();
    }

    @Test
    public void ifNotExistMoreBoard_buttonMoreBoardsIsNotVisible(){

        when(mainPresenter.isButtonMoreBoardsVisible()).thenReturn(false);
        mainPresenter.manageVisibiltyForLoadButton();
        verify(mainViewContext).hideButtonMoreBoards();
    }

    @Test
    public void isButtonMoreBoardsVisible_existsMoreBoards(){
        mainPresenter.isButtonMoreBoardsVisible();
        verify(mainInteractor).existsMoreBoards();
    }

}