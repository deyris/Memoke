package es.barcelona.dey.memoke.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import es.barcelona.dey.memoke.R;
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

            creationPresenter.createCreationActivity(savedInstanceState,getIntent().getExtras());
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

               creationPresenter.clickOnNextButton();

            }
        });
    }

    @Override
    public void actualicePairNumberInContentFragment(CreationFragment cf){
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
}
