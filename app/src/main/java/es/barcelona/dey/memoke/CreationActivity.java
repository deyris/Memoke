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
    BoardServices boardServices = new BoardServices();

    public static String PARAM_CURRENT_PAIR = "PARAM_CURRENT_PAIR";
    public static String PARAM_CURRENT_PAIR_NUMBER = "PARAM_CURRENT_PAIR_NUMBER";
    public static String PARAM_CURRENT_BOARD ="PARAM_CURRENT_BOARD";

    Bundle contentBundle;

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("DEY", "Estoy en CreationActivity.onDestroy");
    }

    public void onFragmentIteration(Bundle arguments){
        Log.d("DEY", "Estoy en CreationActivity.onFragmentIteration");
        if (mContentFragment!=null && arguments!=null && arguments.get(PARAM_CURRENT_PAIR)!=null){
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
    public void onSaveInstanceState(Bundle outState) {
          super.onSaveInstanceState(outState);

          if (null != mBoard)   {
              //Salvamos lo que hay en mBoard
                BoardDatabase.updateOrAddBoard(getBaseContext(), mBoard);
          }     List<Board> testBoard = BoardDatabase.getBoards(getBaseContext());
      }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_creation);
            Log.d("DEY", "Estoy en CreationActivity.onCreate");


            // Get a reference to the FragmentManager
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            FragmentManager fragmentManager = getFragmentManager();




            //Verificamos si venimos o no de un fichero ya existente
            Bundle bundleFromMain = getIntent().getExtras();
            String title = "";
            boolean existeContentFragment = FragmentAlreadyRestoredFromSavedState(ContentFragment.TAG);
            if(!existeContentFragment) {
                if (bundleFromMain.getString(MainFragment.PARAM_SELECTED_BOARD) != null) {

                    final Gson gson = new Gson();

                    mBoard = gson.fromJson(bundleFromMain.getString(MainFragment.PARAM_SELECTED_BOARD), Board.class);
                    title = mBoard.getTitle();
                    //Buscamos currentPair
                    Pair currentPair = new Pair();
                    if (null!=mBoard.getPairs()) {
                        mCurrentPair = (null != mBoard.getPairs()) ? mBoard.getPairs().size() : 0;
                        //Actualizamos currentPair
                        currentPair = mBoard.getPairs().get(mCurrentPair);
                    }

                    String jsonCurrentPair = gson.toJson(currentPair).toString();
                    if (null == savedInstanceState) {
                        savedInstanceState = new Bundle();
                    }
                    savedInstanceState.putString(PARAM_CURRENT_PAIR, jsonCurrentPair);

                } else {

                    //Creamos tablero
                    mBoard = new Board();
                    if (bundleFromMain.getString(MainFragment.PARAM_TITLE) != null) {
                        title = bundleFromMain.getString(MainFragment.PARAM_TITLE).toString();
                        mBoard.setTitle(title);
                    }
                }
            }
            Bundle bundle = new Bundle();
            bundle.putInt("CURRENT_PAIR", mCurrentPair);

            mCreationFragment = new CreationFragment();
            mCreationFragment.setArguments(bundle);

            fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.header_frame, mCreationFragment, CreationFragment.TAG);
            fragmentTransaction.commit();

            if (!existeContentFragment) {
                // mContentFragment = ContentFragment.newInstance(savedInstanceState);
                fragmentManager.beginTransaction().add(R.id.content_frame,
                        ContentFragment.newInstance(savedInstanceState),
                        ContentFragment.TAG).addToBackStack(ContentFragment.TAG).commit();
            }
           // bd.addBoard(getBaseContext(), mBoard);

            //Cargamos los valores de board si es que ya existía
          //  fillBoard(title);
         //   mBoard.setTitle(title);

            //Boton siguiente
            Button btnSgte = (Button) findViewById(R.id.btnSgte);
            btnSgte.setVisibility(View.GONE);
            setListenerBtnSgte();

            //Boton anterior
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

                i.putExtra(PARAM_CURRENT_BOARD,gson.toJson(mBoard));
                startActivity(i);
                return true;
            default:
                return false;
        }
    }



    private void fillBoard(String title)       {
        mBoard =  BoardDatabase.getBoard(getBaseContext(), title);

     if (mBoard==null){
         mBoard = new Board();
     }
    }


    private boolean FragmentAlreadyRestoredFromSavedState(String tag) {
        return (getFragmentManager().findFragmentByTag(tag) != null ? true : false);
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

    public void setListenerBtnSgte(){
        Button btnSgte = (Button)findViewById(R.id.btnSgte);
        btnSgte.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ContentFragment f = (ContentFragment) getFragmentManager().findFragmentByTag(ContentFragment.TAG);
                Pair pair = f.getmCurrentPair();

                //Guardamos sólo si no está guardado ya
                if (!pair.getState().equals(Pair.State.SAVED)) {

                    pair.setNumber(mCurrentPair);

                    if (null == mBoard.getPairs()) {
                        mBoard.setPairs(new HashMap<Integer, Pair>());
                    }
                    //Verificamos si ya pair existe para agregarlo o modificarlo
                    boardServices.savePairInBoard(getBaseContext(),mBoard,pair);

                   //Vaciamos fragment y nos vamos al sgte
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();

                    //Incrementamos la pareja y pasamos el bundle
                    Bundle bundleSgte = new Bundle();
                    mCurrentPair++;
                    bundleSgte.putInt(PARAM_CURRENT_PAIR_NUMBER, mCurrentPair);

                    ft.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up).replace(R.id.content_frame,
                            ContentFragment.newInstance(bundleSgte),
                            ContentFragment.TAG).addToBackStack(null).commit();


                    //Actualizamos creationFragment con el numero de la pareja
                    CreationFragment cf = (CreationFragment) getFragmentManager().findFragmentByTag(CreationFragment.TAG);
                    Bundle bundleFromMain = getIntent().getExtras();

                    if (null != cf) {
                        cf.mTxtNumber.setText(String.format(getResources().getString(R.string.creation_number), mCurrentPair));

                    }
                    //Ponemos el boton Siguiente invisible de nuevo
                    Button button = (Button) findViewById(v.getId());
                    button.setVisibility(View.GONE);

                }else{
                    Log.d("DEY","currentPair no tiene estado Save y por eso no hago nada");
                    mCurrentPair++;

                    if (null == mBoard.getPairs()) {
                        mBoard.setPairs(new HashMap<Integer, Pair>());
                    }



                    //Vaciamos fragment y nos vamos al sgte
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();

                    //Incrementamos la pareja y pasamos el bundle
                    Bundle bundleSgte = new Bundle();
                    bundleSgte.putInt(PARAM_CURRENT_PAIR_NUMBER, mCurrentPair);

                    //Rescatamos la pareja
                    if (mBoard.getPairs().size()<= mCurrentPair) {
                        Pair pairSgte = mBoard.getPairs().get(mCurrentPair);
                        String jsonPairAnt = gson.toJson(pairSgte);
                        bundleSgte.putSerializable(PARAM_CURRENT_PAIR, jsonPairAnt);
                    }

                    ft.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up).replace(R.id.content_frame,
                            ContentFragment.newInstance(bundleSgte),
                            ContentFragment.TAG).addToBackStack(null).commit();

                    //Actualizamos creationFragment con el numero de la pareja
                    CreationFragment cf = (CreationFragment) getFragmentManager().findFragmentByTag(CreationFragment.TAG);
                    Bundle bundleFromMain = getIntent().getExtras();

                    if (null != cf) {
                        cf.mTxtNumber.setText(String.format(getResources().getString(R.string.creation_number), mCurrentPair));

                    }


                }


            }
        });
    }

    public void setListenerBtnAnterior(){
        Button btnAnt = (Button)findViewById(R.id.btnAnt);

        btnAnt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //En caso de que se haya completado el estado de la pareja, guardamos
                ContentFragment f = (ContentFragment)getFragmentManager().findFragmentByTag(ContentFragment.TAG);
                Pair pairForSave = f.getmCurrentPair();
                if (pairForSave.getState().equals(Pair.State.COMPLETED)) {
                    boardServices.savePairInBoard(getBaseContext(),mBoard,pairForSave);

                }
                mCurrentPair--;
                Pair pairAnt = mBoard.getPairs().get(mCurrentPair);

                //Actualizamos fragment
                Bundle bundleAnt = new Bundle();
                String jsonPairAnt = gson.toJson(pairAnt);
                bundleAnt.putSerializable(PARAM_CURRENT_PAIR,jsonPairAnt);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction ft =fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_out_up_ant, R.anim.slide_in_up_ant).replace(R.id.content_frame,
                        ContentFragment.newInstance(bundleAnt),
                        ContentFragment.TAG).addToBackStack(null).commit();

                setListenerBtnSgte();

                //Actualizamos creationFragment con el numero de la pareja
                CreationFragment cf = (CreationFragment) getFragmentManager().findFragmentByTag(CreationFragment.TAG);
                Bundle bundleFromMain = getIntent().getExtras();

                if (null != cf) {
                    cf.mTxtNumber.setText(String.format(getResources().getString(R.string.creation_number), mCurrentPair));

                }

            }
        });
    }
}
