package es.barcelona.dey.memoke.ui;


import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import es.barcelona.dey.memoke.R;
import es.barcelona.dey.memoke.beans.Pair;
import es.barcelona.dey.memoke.presenters.CreationPresenter;
import es.barcelona.dey.memoke.views.CreationView;

/**
 * Created by deyris.drake on 24/1/16.
 */
public class CreationActivity extends AppCompatActivity implements CreationView, ContentFragment.OnDataPass, ContentFragment.FragmentIterationListener{

    CreationPresenter creationPresenter;

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

    @Override
    public void onFragmentIteration(Bundle arguments){

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
          super.onSaveInstanceState(outState);

         creationPresenter.savingInstanceState(outState);

      }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_creation);

            creationPresenter = new CreationPresenter();
            creationPresenter.setView(this);
            boolean existeContentFragment = fragmentAlreadyRestoredFromSavedState(ContentFragment.TAG);
            creationPresenter.createCreationActivity(existeContentFragment,savedInstanceState,getIntent().getExtras());
        }
    @Override
    public void inicializeButtonNext(){
        Button btnSgte = (Button) findViewById(R.id.btnSgte);
        btnSgte.setVisibility(View.GONE);
        setListenerBtnSgte();
    }
    @Override
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
    public void setListenerBtnSgte(){
        Button btnSgte = (Button)findViewById(R.id.btnSgte);
        btnSgte.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ContentFragment f = (ContentFragment) getFragmentManager().findFragmentByTag(ContentFragment.TAG);
                Pair pair = f.getContentPresenter().getmCurrentPair();
                HeaderFragment cf = (HeaderFragment) getFragmentManager().findFragmentByTag(HeaderFragment.TAG);
                boolean existsCreationFragment = cf!=null;
                creationPresenter.clickOnNextButton(existsCreationFragment,pair);

            }
        });
    }

    @Override
    public void actualicePairNumberInContentFragment(){
        HeaderFragment cf = (HeaderFragment) getFragmentManager().findFragmentByTag(HeaderFragment.TAG);

        String text = String.format(getResources().getString(R.string.creation_number), creationPresenter.getIdCurrentPair());
        cf.mTxtNumber.setText(text);
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

    public boolean fragmentAlreadyRestoredFromSavedState(String tag) {
        return (getFragmentManager().findFragmentByTag(tag) != null ? true : false);
    }



    public void prepareForContentFragmentForRotate(Bundle savedInstanceState){
        Bundle bundle = new Bundle();
        bundle.putInt(CreationPresenter.PARAM_CURRENT_PAIR, creationPresenter.getIdCurrentPair());

        HeaderFragment mCreationFragment = new HeaderFragment();
        mCreationFragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.header_frame, mCreationFragment, HeaderFragment.TAG);
        fragmentTransaction.commit();

        if (!fragmentAlreadyRestoredFromSavedState(ContentFragment.TAG)) {
            fragmentManager.beginTransaction().add(R.id.content_frame,
                    ContentFragment.newInstance(savedInstanceState),
                    ContentFragment.TAG).addToBackStack(ContentFragment.TAG).commit();
        }

    }


}
