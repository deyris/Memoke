package es.barcelona.dey.memoke.ui;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import es.barcelona.dey.memoke.R;
import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.beans.Game;
import es.barcelona.dey.memoke.beans.Play;
import es.barcelona.dey.memoke.beans.Tab;
import es.barcelona.dey.memoke.beans.TabForGame;
import es.barcelona.dey.memoke.presenters.BoardPresenter;
import es.barcelona.dey.memoke.services.PlayService;
import es.barcelona.dey.memoke.views.BoardView;

/**
 * Created by deyris.drake on 13/2/16.
 */
public class BoardActivity extends AppCompatActivity implements BoardView{


    //static Game game = new Game();
    //static  TabForGame[] tabsForGame = new TabForGame[]{};
    //static HashMap<Integer,FrameLayout> currentFrame = new HashMap<Integer,FrameLayout>();
    //static  ArrayList clickedPositions;
    BoardPresenter boardPresenter;

    @Override
    public Context getContext(){
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        boardPresenter = new BoardPresenter();
        boardPresenter.setView(this);

        boardPresenter.doLock(true,this);

        //Recuperamos tablero actual
        Board currentBoard = null;
        Bundle bundle = getIntent().getExtras();


        if (bundle!=null && null!=bundle.getString(CreationActivity.PARAM_CURRENT_BOARD)) {
            String jsonCurrenBoard = bundle.getString(CreationActivity.PARAM_CURRENT_BOARD);

            currentBoard = boardPresenter.getCurrentBoard(jsonCurrenBoard);

        }
        if (null!=currentBoard){
           boardPresenter.inicialiceGame(currentBoard);
          // tabsForGame = boardPresenter.getGame().getTabForGames();
            //clickedPositions = boardPresenter.inicialiceClickedPositions(clickedPositions);
        }

        GridView gridview = (GridView) findViewById(R.id.gridview);

        TabAdapter adapter = new TabAdapter(this,
                boardPresenter.getTabsForGame());

        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

               boardPresenter.manageClickOnFrame(v,position);

            }


        });

    }

    @Override
    public void warnYouWin(){
        Toast.makeText(getApplicationContext(),
                "You won! But..what have you learned?", Toast.LENGTH_SHORT).show();
    }

    @Override
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

        TabForGame tab = boardPresenter.getTabsForGame()[position];
        if (tab.isShowingBack()) {
            TextView textView = (TextView) cardFront.getChildAt(0);
            ImageView imageView = (ImageView)cardFront.getChildAt(1);

            if (tab.getType().equals(Tab.Type.TEXT)){
                imageView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setText(tab.getText());
                textView.setTextSize(tab.getSize()/2);
            }
            if (tab.getType().equals(Tab.Type.PHOTO)){
                textView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                Picasso.with(this).load(tab.getUri())
                        .resize(180, 180)
                        .centerCrop()
                        .into(imageView);
            }
            showFrontAnim.start();
            
            Toast.makeText(getApplicationContext(),
                    new Integer(position).toString(), Toast.LENGTH_SHORT).show();
            tab.setShowingBack(false);

        } else {
            cardFront.setVisibility(View.VISIBLE);
            showBackAnim.start();
            tab.setShowingBack(true);
        }


    }



    @Override
    public void turnTabForGame(Play currentPlay, FrameLayout frameLayout, int positionInBoard){

        //for(TabForGame tab:currentPlay.getMovedTabs()) {
            //FrameLayout frameLayout = boardPresenter.getCurrentFrame().get(currentPlay.getMovedTabs()[play].getPositionInBoard());
            setAnimationToFrame(frameLayout, positionInBoard);
        //FrameLayout frameLayout1 = currentFrame.get(currentPlay.getMovedTabs()[1].getPositionInBoard());
       // setAnimationToFrame(frameLayout1, currentPlay.getMovedTabs()[1].getPositionInBoard());
       // }


    }

    @Override
    public void disappearsTabForGame(Play currentPlay, FrameLayout frameLayout){

       // FrameLayout frameLayout = currentFrame.get(currentPlay.getMovedTabs()[play].getPositionInBoard());
        frameLayout.setVisibility(View.GONE);
       // FrameLayout frameLayout1 = currentFrame.get(currentPlay.getMovedTabs()[1].getPositionInBoard());
        //frameLayout1.setVisibility(View.GONE);
    }

}
