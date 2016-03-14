package es.barcelona.dey.memoke;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;

import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.beans.Pair;
import es.barcelona.dey.memoke.database.BoardDatabase;
import es.barcelona.dey.memoke.database.TabDatabase;
import es.barcelona.dey.memoke.services.BoardServices;

/**
 * Created by deyris.drake on 24/1/16.
 */
public class CreationActivity extends AppCompatActivity implements ContentFragment.OnDataPass, ContentFragment.FragmentIterationListener{

    private CreationFragment mCreationFragment;
    private   ContentFragment mContentFragment;
    public static int mCurrentPair = 1;
    public static Board mBoard;
    final Gson gson = new Gson();
    BoardDatabase bd = new BoardDatabase();

    Bundle contentBundle;

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("DEY", "Estoy en CreationActivity.onDestroy");
    }

    public void onFragmentIteration(Bundle arguments){
        Log.d("DEY", "Estoy en CreationActivity.onFragmentIteration");
        if (mContentFragment!=null && arguments!=null && arguments.get("PARAM_CURRENT_PAIR")!=null){
            Log.d("DEY", "Estoy en CreationActivity.onFragmentIteration distinto de null");
            contentBundle = arguments;
        }else{
            Log.d("DEY", "Estoy en CreationActivity.onFragmentIteration, soy null");

        }

        if (arguments!=null && arguments.get("TEXT")!=null){
            contentBundle = arguments;
        }
    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_creation);
            Log.d("DEY", "Estoy en CreationActivity.onCreate");


            // Get a reference to the FragmentManager
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            FragmentManager fragmentManager = getFragmentManager();

            Bundle bundle = new Bundle();
            bundle.putInt("CURRENT_PAIR", mCurrentPair);

            mCreationFragment = new CreationFragment();
            mCreationFragment.setArguments(bundle);

            fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.header_frame, mCreationFragment);
            fragmentTransaction.commit();

            if (!FragmentAlreadyRestoredFromSavedState(ContentFragment.TAG)) {
                // mContentFragment = ContentFragment.newInstance(savedInstanceState);
                fragmentManager.beginTransaction().add(R.id.content_frame,
                        ContentFragment.newInstance(savedInstanceState),
                        ContentFragment.TAG).addToBackStack(ContentFragment.TAG).commit();
            }
            //Creamos tablero
            mBoard = new Board();
            String title = "";
            Bundle bundleFromMain = getIntent().getExtras();

            if(bundleFromMain.getString(MainFragment.PARAM_TITLE)!= null){
                title = bundleFromMain.getString(MainFragment.PARAM_TITLE).toString();
            }
            mBoard.setTitle(title);
           // bd.addBoard(getBaseContext(), mBoard);

            //Boton siguiente
            Button btnSgte = (Button) findViewById(R.id.btnSgte);
            btnSgte.setVisibility(View.INVISIBLE);
            btnSgte.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Comunicamos que nos vamos
                    ContentFragment f = (ContentFragment)getFragmentManager().findFragmentByTag(ContentFragment.TAG);
                    Pair pair = f.getmCurrentPair();
                    pair.setNumber(mCurrentPair);

                    if (null == mBoard.getPairs()){
                        mBoard.setPairs(new HashMap<Integer,Pair>());
                    }
                    //Verificamos si ya pair existe para agregarlo o modificarlo
                    if (mBoard.getPairs().containsKey(pair.getNumber())){
                        mBoard.getPairs().remove(pair.getNumber());
                    }
                    mBoard.getPairs().put(pair.getNumber(), pair);

                   //Persistimos lo que hay en el fragment

                    BoardDatabase.updateOrAddBoard(getBaseContext(), mBoard);
                    List<Board> testBoard = BoardDatabase.getBoards(getBaseContext());

                    //Vaciamos fragment y nos vamos al sgte
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame,
                            ContentFragment.newInstance(null),
                            ContentFragment.TAG).addToBackStack(ContentFragment.TAG).commit();

                    //Actualizamos creationFragment con el numero de la pareja
                    mCreationFragment.mTxtNumber.setText();
                }
            });
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.creation_menu, menu);

        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.showBoardTab:
                Intent i = new Intent(this, BoardActivity.class);

                //i.putExtra(PARAM_TITLE,mTxtTitle.getText().toString());
                startActivity(i);

                return true;
            default:
                return false;
        }
    }

    private boolean FragmentAlreadyRestoredFromSavedState(String tag) {
        return (getFragmentManager().findFragmentByTag(tag) != null ? true : false);
    }

    @Override
    public void onDataPass(int data) {
      //  if (mContentFragment!=null) {
        ContentFragment f = (ContentFragment)getFragmentManager().findFragmentByTag(ContentFragment.TAG);
        f.receivingFromDialog(data);
       // }
    }

    @Override
    public void onDataPass(EditText data) {
        ContentFragment f = (ContentFragment)getFragmentManager().findFragmentByTag(ContentFragment.TAG);
        f .receivingFromDialog(data);

    }




}
