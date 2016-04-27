package es.barcelona.dey.memoke;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.HashMap;

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
    static HashMap<Integer,FrameLayout> currentFrame = new HashMap<Integer,FrameLayout>();
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
                boolean itsTheSame = false;
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        finaliceLastPlay();

                    }
                };

                if (tabsForGame[pos].isShowingBack()) {
                    handler.removeCallbacks(runnable);
                     setAnimationToFrame((FrameLayout) v, position);
                    removeTabFromPlay(position);
                    return;
                }
                clickedPositions.add(pos);

                // int lastPlay = lastPlayIsFinished();
                FrameLayout frameLayout = (FrameLayout) v;

                tabsForGame[position].setPositionInBoard(position);

                currentFrame.put(position, frameLayout);

                if (clickedPositions.size() % 2 != 0 && clickedPositions.size() > 1) { //Si es impar las analizo ya

                    finaliceLastPlay();
                    actualicePlays(pos);

                    setAnimationToFrame(frameLayout, position);

                } else {
                    actualicePlays(pos);

                    setAnimationToFrame(frameLayout, position);
                    handler.postDelayed(runnable, 3000); // after 3 sec

                }

            }


        });

    }

    private void removeTabFromPlay(int position){
        Play lastPlay = getLastPlayNotFinished(false);
        if (position < clickedPositions.size()) {
            clickedPositions.remove(position);
        }

        if (null!=lastPlay) {

            if (lastPlay.getMovedTabs()[0].getPositionInBoard() == position) {
                lastPlay.getMovedTabs()[0]=null;
                if (lastPlay.isEmpty()){
                    removeLastPlayFromGame();
                }
                return;
            }
            if (lastPlay.getMovedTabs()[1].getPositionInBoard() == position) {
                lastPlay.getMovedTabs()[1] = null;
                if (lastPlay.isEmpty()){
                    removeLastPlayFromGame();                }
                return;
            }

        }

    }

    public Play getLastPlayNotFinished(boolean needCompleted){
        int totalPlays = (null!=this.game.getPlays())?this.game.getPlays().size():0;
        Play lastPlay = null;
        if (totalPlays > 0) {
            for (int i=totalPlays-1;i>=0;i--){
                lastPlay = (Play) this.game.getPlays().get(i);
                if (lastPlay.isFinished()){
                    return null;
                }else{
                    if (needCompleted) {
                        if (lastPlay.isCompleted() && !lastPlay.isFinished()) {
                            return lastPlay;
                        } else {
                            lastPlay = null;
                        }
                    }else{
                        return  lastPlay;
                    }
                }
            }

        }

        return lastPlay;
    }

   /* public Play getLastPlayCompleted(){
        int totalPlays = (null!=this.game.getPlays())?this.game.getPlays().size():0;
        Play lastPlay = null;
        if (totalPlays > 0) {
            for (int i=totalPlays-1;i>=0;i--){
                lastPlay = (Play) this.game.getPlays().get(i);
                if (lastPlay.isFinished()){
                    return null;
                }else{

                         return  lastPlay;

                }
            }

        }

        return lastPlay;
    }*/



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

        }
    }

    private void removeLastPlayFromGame(){
        this.game.getPlays().remove(this.game.getPlays().size()-1);
    }

    public void finaliceLastPlay(){
        Play currentPlay = getLastPlayNotFinished(true);
        if (null==currentPlay || currentPlay.isFinished() || !currentPlay.isCompleted())    {
            return;
        }

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
            FrameLayout frameLayout0 = currentFrame.get(currentPlay.getMovedTabs()[0].getPositionInBoard());
            setAnimationToFrame(frameLayout0, currentPlay.getMovedTabs()[0].getPositionInBoard());
        FrameLayout frameLayout1 = currentFrame.get(currentPlay.getMovedTabs()[1].getPositionInBoard());
        setAnimationToFrame(frameLayout1, currentPlay.getMovedTabs()[1].getPositionInBoard());
       // }


    }

    public void disappearsTabForGame(Play currentPlay){

        FrameLayout frameLayout0 = currentFrame.get(currentPlay.getMovedTabs()[0].getPositionInBoard());
        frameLayout0.setVisibility(View.GONE);
        FrameLayout frameLayout1 = currentFrame.get(currentPlay.getMovedTabs()[1].getPositionInBoard());
        frameLayout1.setVisibility(View.GONE);
    }

}
