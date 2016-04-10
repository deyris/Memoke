package es.barcelona.dey.memoke;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.beans.Game;
import es.barcelona.dey.memoke.beans.Play;
import es.barcelona.dey.memoke.beans.Tab;
import es.barcelona.dey.memoke.beans.TabForGame;
import es.barcelona.dey.memoke.services.PlayServices;

/**
 * Created by deyris.drake on 13/2/16.
 */
public class BoardActivity extends AppCompatActivity {


    Game game = new Game();
    static  TabForGame[] tabsForGame = new TabForGame[]{};
    PlayServices playServices = new PlayServices();


    private void doLock(boolean locked) {
        if (locked) {
            int o = getResources().getConfiguration().orientation;
            if (o == Configuration.ORIENTATION_LANDSCAPE)
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            else if (o == Configuration.ORIENTATION_PORTRAIT)
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        doLock(true);

        //Recuperamos tablero actual
        Board currentBoard = null;
        Bundle bundle = getIntent().getExtras();


        if (bundle!=null && null!=bundle.getString(CreationActivity.PARAM_CURRENT_BOARD)) {
            String jsonCurrentPair = bundle.getString(CreationActivity.PARAM_CURRENT_BOARD);
            final Gson gson = new Gson();
            currentBoard = gson.fromJson(jsonCurrentPair,Board.class);

        }
        if (null!=currentBoard){
           inicialiceGame(currentBoard);
        }

        GridView gridview = (GridView) findViewById(R.id.gridview);

        TabAdapter adapter = new TabAdapter(this,
                tabsForGame);

        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                FrameLayout frameLayout = (FrameLayout) v;
                LinearLayout linearLayout = (LinearLayout) frameLayout.getChildAt(0);
                TextView textView = (TextView) linearLayout.getChildAt(0);
                Toast.makeText(getApplicationContext(),
                        textView.getText(), Toast.LENGTH_SHORT).show();
                TabAdapter adapter = (TabAdapter) parent.getAdapter();
                setAnimationToFrame(frameLayout, position);
                tabsForGame[position].setIdFrame(frameLayout.getId());
                tabsForGame[position].setPositionInBoard(position);
                //Actualizar jugada
                actualicePlays(position);
            }
        });



    }

    public void setAnimationToFrame(FrameLayout frame, int position){


         AnimatorSet showFrontAnim = new AnimatorSet();
         AnimatorSet showBackAnim = new AnimatorSet();


        final LinearLayout cardFront = (LinearLayout) frame.findViewById(R.id.card_front_layout);
        final LinearLayout cardBack = (LinearLayout) frame.findViewById(R.id.card_back_layout);



        // Load the animator sets from XML and group them together

        AnimatorSet leftIn   = (AnimatorSet) AnimatorInflater
                .loadAnimator(this, R.animator.card_flip_left_in);
        AnimatorSet rightOut = (AnimatorSet) AnimatorInflater
                .loadAnimator(this, R.animator.card_flip_right_out);
        AnimatorSet leftOut  = (AnimatorSet) AnimatorInflater
                .loadAnimator(this, R.animator.card_flip_left_out);
        AnimatorSet rightIn  = (AnimatorSet) AnimatorInflater
                .loadAnimator(this, R.animator.card_flip_right_in);

        leftIn.setTarget(cardFront);
        rightOut.setTarget(cardBack);
        showFrontAnim.playTogether(leftIn, rightOut);

        leftOut.setTarget(cardFront);
        rightIn.setTarget(cardBack);
        showBackAnim.playTogether(leftOut, rightIn);

        if (tabsForGame[position].isShowingFront()) {
            TextView textView = (TextView) cardBack.getChildAt(0);
            ImageView imageView = (ImageView)cardBack.getChildAt(1);

            if (tabsForGame[position].getType().equals(Tab.Type.TEXT)){
                imageView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setText(tabsForGame[position].getText());
                textView.setTextSize(tabsForGame[position].getSize()/2);
            }
            if (tabsForGame[position].getType().equals(Tab.Type.PHOTO)){
                textView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                Picasso.with(this).load(tabsForGame[position].getUri())
                        .resize(180, 180)
                        .centerCrop()
                        .into(imageView);
            }
            //textView.setBackgroundResource(imgs[position]);
            showFrontAnim.start();
            tabsForGame[position].setShowingFront(false);

        } else {
            cardBack.setVisibility(View.VISIBLE);


            showBackAnim.start();
            tabsForGame[position].setShowingFront(true);
        }
    }

    public void inicialiceGame(Board currentBoard){
        this.game = new Game();
        tabsForGame = playServices.getTabsForPlay(currentBoard);
        game.setTabForGames(tabsForGame);
        game.setTitle(currentBoard.getTitle());
    }

    public void actualicePlays(int position){
        TabForGame tab = tabsForGame[position];
        int totalPlays = (null!=this.game.getPlays())?this.game.getPlays().size():0;
        Play lastPlay = null;
        if (totalPlays > 0) {
            lastPlay = (Play) this.game.getPlays().get(totalPlays - 1);
        }
        if (lastPlay==null || lastPlay.isFinished()){
            //Se abre una nueva jugada
            Play newPlay = new Play();
            newPlay.getMovedTabs()[0]=tab;
            if (lastPlay==null){
                this.game.setPlays(new ArrayList<Play>());
            }
            this.game.getPlays().add(newPlay);
        }else{
            //Guardo la ficha movida
            lastPlay.getMovedTabs()[1]=tab;
            //Finalizo la jugada
            finalicePlays(lastPlay, position);
        }
    }

    public void finalicePlays(Play currentPlay, int position){
        //Analizamos acierto o fail
        TabForGame tab1 = currentPlay.getMovedTabs()[0];
        TabForGame tab2 = currentPlay.getMovedTabs()[1];

        if (tab1.getNumberOfPair()==tab2.getNumberOfPair()){
            //Success
            this.game.setTotalSuccess(this.game.getTotalSuccess() + 1);
            //Se actualiza ok de TabForGame
            tab1.setOk(true);
            tab2.setOk(true);
            //Desaparecen la fichas
           // disappearsTabForGame(currentPlay, position);
            turnTabForGame(currentPlay);

        }else{
            //Fail
            this.game.setTotalFailure(this.game.getTotalFailure() + 1);
            //Se giran las fichas
            turnTabForGame(currentPlay);
        }

        //Se actualiza finished en Play
        currentPlay.setFinished(true);



    }

    public void turnTabForGame(Play currentPlay){

        for(TabForGame tab:currentPlay.getMovedTabs()) {
            FrameLayout frameLayout = (FrameLayout) findViewById(tab.getIdFrame());
            setAnimationToFrame(frameLayout, tab.getPositionInBoard());
        }
    }

}
