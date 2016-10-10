package es.barcelona.dey.memoke.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import es.barcelona.dey.memoke.R;
import es.barcelona.dey.memoke.beans.Pair;
import es.barcelona.dey.memoke.presenters.ContentPresenter;
import es.barcelona.dey.memoke.presenters.CreationPresenter;
import es.barcelona.dey.memoke.views.CreationView;

/**
 * Created by deyris.drake on 24/1/16.
 */
public class CreationActivity extends AppCompatActivity implements CreationView, ContentFragment.OnDataPass, ContentFragment.FragmentIterationListener{

    private CreationFragment mCreationFragment;
 //   private   ContentFragment mContentFragment;
//    public static Board mBoard;

    CreationPresenter creationPresenter;

    Bundle contentBundle;

    @Override
    public Context getContext(){
        return this;
    }

    @Override
    public Bundle actualizeBundle(Bundle bundle, String nameData, String data) {

        if (null == bundle) {
            bundle = new Bundle();
        }
        bundle.putString(nameData, data);
        return bundle;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    public void onFragmentIteration(Bundle arguments){
        ContentFragment f = (ContentFragment)getFragmentManager().findFragmentByTag(ContentFragment.TAG);

        if (f!=null && arguments!=null && arguments.get(CreationPresenter.PARAM_CURRENT_PAIR)!=null){
            contentBundle = arguments;
        }

        if (arguments!=null && arguments.get("TEXT")!=null){
            contentBundle = arguments;
        }
    }

      @Override
    public void onSaveInstanceState(Bundle outState) {
          super.onSaveInstanceState(outState);

          if (null != creationPresenter.getmBoard())   {
              //Salvamos lo que hay en mBoard
              creationPresenter.updateOrAddBoard(creationPresenter.getmBoard());
              //Guardamos el id VISUALIZADO en el momento de irnos
              outState.putInt(CreationPresenter.PARAM_CURRENT_PAIR_NUMBER,creationPresenter.getIdCurrentPair());
              outState.putString(CreationPresenter.PARAM_CURRENT_BOARD, creationPresenter.getJsonCurrentBoard(creationPresenter.getmBoard()));
          }

      }
    private boolean fragmentAlreadyRestoredFromSavedState(String tag) {
        return (getFragmentManager().findFragmentByTag(tag) != null ? true : false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_creation);

            creationPresenter = new CreationPresenter();
            creationPresenter.setView(this);


            // Get a reference to the FragmentManager
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            FragmentManager fragmentManager = getFragmentManager();

            creationPresenter.createCreationActivity(savedInstanceState,getIntent().getExtras(),fragmentManager,fragmentTransaction);

        }

    public void inicializeButtonNext(){
        Button btnSgte = (Button) findViewById(R.id.btnSgte);
        btnSgte.setVisibility(View.GONE);
        setListenerBtnSgte();
    }

    public void inicializeButtonPast(){
        Button btnAnt = (Button)findViewById(R.id.btnAnt);
        btnAnt.setVisibility(View.GONE);
        setListenerBtnAnterior();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.creation_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.showBoardTab:
                Intent i = new Intent(this, BoardActivity.class);
                i.putExtra(CreationPresenter.PARAM_CURRENT_BOARD,creationPresenter.getJsonCurrentBoard(creationPresenter.getmBoard()));
                startActivity(i);
                return true;
            default:
                return false;
        }
    }


    @Override
    public void onDataPass(int data) {
        ContentFragment f = (ContentFragment)getFragmentManager().findFragmentByTag(ContentFragment.TAG);
        f.receivingFromDialog(data);

    }

    @Override
    public void onDataPass(EditText data) {
        ContentFragment f = (ContentFragment)getFragmentManager().findFragmentByTag(ContentFragment.TAG);
        f .receivingFromDialog(data);

    }
    @Override
    public void hideNextButton(){
        Button button = (Button) findViewById(R.id.btnSgte);
        button.setVisibility(View.GONE);
    }

    @Override
    public void hidePastButton(){
        Button button = (Button) findViewById(R.id.btnAnt);
        button.setVisibility(View.GONE);
    }

    @Override
    public void setListenerBtnSgte(){
        Button btnSgte = (Button)findViewById(R.id.btnSgte);
        btnSgte.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               creationPresenter.clickOnNextButton();


            }
        });
    }

    public void putFragmentEmptyAndGoNext(Bundle bundleSgte){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        //Incrementamos la pareja y pasamos el bundle
        creationPresenter.incrementIdCurrentPair();
        bundleSgte.putInt(CreationPresenter.PARAM_CURRENT_PAIR_NUMBER, creationPresenter.getIdCurrentPair());

        ft.setCustomAnimations(R.animator.slide_in_up, R.animator.slide_out_up).replace(R.id.content_frame,
                ContentFragment.newInstance(bundleSgte),
                ContentFragment.TAG).addToBackStack(null).commit();


    }



    public void actualicePairNumber(){
        //Actualizamos creationFragment con el numero de la pareja
        CreationFragment cf = (CreationFragment) getFragmentManager().findFragmentByTag(CreationFragment.TAG);
        Bundle bundleFromMain = getIntent().getExtras();

        if (null != cf) {
            cf.mTxtNumber.setText(String.format(getResources().getString(R.string.creation_number), creationPresenter.getIdCurrentPair()));

        }
    }



    public void setListenerBtnAnterior(){
        Button btnAnt = (Button)findViewById(R.id.btnAnt);

        btnAnt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                creationPresenter.clickOnPastButton();
            }
        });
    }
}
