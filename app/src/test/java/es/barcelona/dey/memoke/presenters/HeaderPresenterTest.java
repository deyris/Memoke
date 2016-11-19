package es.barcelona.dey.memoke.presenters;

import android.os.Bundle;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import es.barcelona.dey.memoke.views.HeaderView;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;

/**
 * Created by osboxes on 19/11/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class HeaderPresenterTest {

    @InjectMocks
    HeaderPresenter headerPresenter;

    @Mock
    HeaderView headerView;

    @Mock
    Bundle bundle;

    @Mock
    HeaderView view;

    @Test
    public void testSetView_ifViewNull() {
        try {
            headerPresenter.setView(null);
            fail( "My method didn't throw when I expected it to" );
        } catch (IllegalArgumentException illegalArgumentException) {
        }
    }

    @Test
    public void testSetView_ifViewNotNull() {
        headerPresenter.setView(view);
        assertEquals(headerPresenter.headerView, view);
    }

    @Test
    public void testDetachView() {

    }

    @Test
    public void testCreationFragmentOnCreate_ifBundleNotNull() {
        headerPresenter.creationFragmentOnCreate(bundle);
        verify(headerView).fillHeaderData(bundle);
    }

    @Test
    public void testCreationFragmentOnCreate_ifBundleNull() {

        try {
            headerPresenter.creationFragmentOnCreate(null);
            fail( "My method didn't throw when I expected it to" );
        } catch (IllegalArgumentException illegalArgumentException) {
        }
    }
}