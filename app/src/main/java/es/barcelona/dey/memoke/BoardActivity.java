package es.barcelona.dey.memoke;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.beans.Tab;
import es.barcelona.dey.memoke.beans.TabForPlay;
import es.barcelona.dey.memoke.services.PlayServices;

/**
 * Created by deyris.drake on 13/2/16.
 */
public class BoardActivity extends AppCompatActivity {

    static final String[] numbers = new String[] {


            "R", "B", "C", "D"

    };

    static  TabForPlay[] tabsForPlay = new TabForPlay[]{};
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
            tabsForPlay = playServices.getTabsForPlay(currentBoard);
        }

        GridView gridview = (GridView) findViewById(R.id.gridview);

        TabAdapter adapter = new TabAdapter(this,
                 tabsForPlay);

        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                FrameLayout frameLayout = (FrameLayout) v;
                LinearLayout linearLayout = (LinearLayout)frameLayout.getChildAt(0);
                TextView textView = (TextView)linearLayout.getChildAt(0);
                Toast.makeText(getApplicationContext(),
                        textView.getText()  , Toast.LENGTH_SHORT).show();
                TabAdapter adapter = (TabAdapter) parent.getAdapter();
                setAnimationToFrame(frameLayout, position,adapter.isShowingBack());
                adapter.setIsShowingBack(!adapter.isShowingBack());
            }
        });



    }

    public void setAnimationToFrame(FrameLayout frame, int position, boolean isShowingBackAdapter){


         AnimatorSet showFrontAnim = new AnimatorSet();
         AnimatorSet showBackAnim = new AnimatorSet();
         boolean isShowingBack = isShowingBackAdapter;

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

        if (tabsForPlay[position].isShowingFront()) {
            TextView textView = (TextView) cardBack.getChildAt(0);
            ImageView imageView = (ImageView)cardBack.getChildAt(1);

            if (tabsForPlay[position].getType().equals(Tab.Type.TEXT)){
                imageView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setText(tabsForPlay[position].getText());
                textView.setTextSize(tabsForPlay[position].getSize());
            }
            if (tabsForPlay[position].getType().equals(Tab.Type.PHOTO)){
                textView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                Picasso.with(this).load(tabsForPlay[position].getUri())
                        .resize(180, 180)
                        .centerCrop()
                        .into(imageView);
            }
            //textView.setBackgroundResource(imgs[position]);
            showFrontAnim.start();
            tabsForPlay[position].setShowingFront(false);

        } else {
            cardBack.setVisibility(View.VISIBLE);


            showBackAnim.start();
            tabsForPlay[position].setShowingFront(true);
        }
    }
}
