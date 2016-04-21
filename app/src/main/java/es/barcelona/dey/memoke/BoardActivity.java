package es.barcelona.dey.memoke;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.util.Date;

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
    static FrameLayout[] currentFrame = new FrameLayout[2];
    static int countFrame=0;
    static  ArrayList clickedPositions;

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

                    final Handler handler = new Handler();
                    final int pos = position;
                    clickedPositions.add(pos);
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            //Actualizar jugada
                            actualicePlays(pos);
                            Log.i("DEY", "acabando: " + countFrame);

                        }
                    };
                   // int lastPlay = lastPlayIsFinished();
                    FrameLayout frameLayout = (FrameLayout) v;


                    LinearLayout linearLayout = (LinearLayout) frameLayout.getChildAt(0);

                    tabsForGame[position].setPositionInBoard(position);
                    if (countFrame == 2) {
                        countFrame = 0;
                    }
                    currentFrame[countFrame++] = frameLayout;

                    setAnimationToFrame(frameLayout, position);
                    handler.postDelayed(runnable, 1000); // after 3 sec

                }


        });



    }

    public int lastPlayIsFinished(){
        if (null!=game && null!=game.getPlays()) {
            int totalJugadas = game.getPlays().size();
            Play lastPlay = (Play) game.getPlays().get(totalJugadas -1);
            if (!lastPlay.isFinished()) {
                return 0;
            } else {
                return (int) clickedPositions.get(clickedPositions.size() - 3);
            }
        }else if (clickedPositions.size()==3){//For first play
            return (int)clickedPositions.get(1);
        }else{
            return 0;
        }
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

        leftIn.setTarget(cardBack);
        rightOut.setTarget(cardFront);
        showFrontAnim.playTogether(leftIn, rightOut);

        leftOut.setTarget(cardBack);
        rightIn.setTarget(cardFront);
        showBackAnim.playTogether(leftOut, rightIn);

        if (tabsForGame[position].isShowingBack()) {
            TextView textView = (TextView) cardFront.getChildAt(0);
            ImageView imageView = (ImageView)cardFront.getChildAt(1);

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
            showFrontAnim.start();
            
            Toast.makeText(getApplicationContext(),
                    new Integer(position).toString(), Toast.LENGTH_SHORT).show();
            tabsForGame[position].setShowingBack(false);

        } else {
            cardFront.setVisibility(View.VISIBLE);


            showBackAnim.start();
            tabsForGame[position].setShowingBack(true);
        }


    }

    public void inicialiceGame(Board currentBoard){
        this.game = new Game();
        tabsForGame = playServices.getTabsForPlay(currentBoard);
        game.setTabForGames(tabsForGame);
        game.setTitle(currentBoard.getTitle());
        clickedPositions = new ArrayList();
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
            finalicePlays(lastPlay);
        }


    }

    public void finalicePlays(Play currentPlay){
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
            disappearsTabForGame(currentPlay);
            if (this.game.getTotalSuccess()==this.game.getTabForGames().length/2){
                Toast.makeText(getApplicationContext(),
                        "You won! But..what have you learned?", Toast.LENGTH_SHORT).show();
            }

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

        //for(TabForGame tab:currentPlay.getMovedTabs()) {
            FrameLayout frameLayout0 = currentFrame[0];
            setAnimationToFrame(frameLayout0, currentPlay.getMovedTabs()[0].getPositionInBoard());
        FrameLayout frameLayout1 = currentFrame[1];
        setAnimationToFrame(frameLayout1, currentPlay.getMovedTabs()[1].getPositionInBoard());
       // }


    }

    public void disappearsTabForGame(Play currentPlay){

        FrameLayout frameLayout0 = currentFrame[0];
        frameLayout0.setVisibility(View.GONE);
        FrameLayout frameLayout1 = currentFrame[1];
        frameLayout1.setVisibility(View.GONE);
    }

}
