package es.barcelona.dey.memoke;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by deyris.drake on 13/2/16.
 */
public class BoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new TabAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(BoardActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();





                GridView gridView = (GridView)parent;
                FrameLayout frame = (FrameLayout) parent.getSelectedView();
             //  String idSelected =  parent.getItemAtPosition(position).toString();
            //   FrameLayout idSelected =  (FrameLayout) gridView.getSelectedItem();



            //    setAnimationToFrame(idSelected);
                ;
             //   TabAdapter tabAdapter = (TabAdapter)gridView.getAdapter();
             //   tabAdapter.girar();


              //  final LinearLayout cardFront = (LinearLayout) v.findViewById(R.id.card_front_layout);
             //   final LinearLayout cardBack = (LinearLayout) v.findViewById(R.id.card_back_layout);




            }
        });
    }

    public void setAnimationToFrame(FrameLayout frame){


         AnimatorSet showFrontAnim = new AnimatorSet();
         AnimatorSet showBackAnim = new AnimatorSet();
         boolean isShowingBack = false;

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

        if (isShowingBack) {
            showFrontAnim.start();
            isShowingBack = false;
        } else {
            showBackAnim.start();
            isShowingBack = true;
        }
    }
}
