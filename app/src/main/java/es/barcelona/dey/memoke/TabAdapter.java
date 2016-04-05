package es.barcelona.dey.memoke;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by deyris.drake on 1/4/16.
 */
public class TabAdapter extends BaseAdapter {
    private Context mContext;
    private AnimatorSet showFrontAnim = new AnimatorSet();
    private AnimatorSet showBackAnim = new AnimatorSet();
    private boolean isShowingBack = false;
    String[] textos;

    public TabAdapter(Context mContext, String[] numbers) {
        this.mContext = mContext;
        this.textos = numbers;
    }

    public int getCount() {
        return textos.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public FrameLayout[] frames;

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        FrameLayout linearLayout;
        if (convertView == null) {

            //LayoutInflater inflater = LayoutInflater.from();
            linearLayout = (FrameLayout)  LayoutInflater.from(mContext).inflate(R.layout.tab_inside, parent, false);


            final LinearLayout cardFront = (LinearLayout) linearLayout.findViewById(R.id.card_front_layout);

            final LinearLayout cardBack = (LinearLayout) linearLayout.findViewById(R.id.card_back_layout);
            TextView textView = (TextView) cardBack.getChildAt(0);
            textView.setText(textos[position]);
            cardBack.setVisibility(View.GONE);

            // Load the animator sets from XML and group them together

            AnimatorSet leftIn   = (AnimatorSet) AnimatorInflater
                    .loadAnimator(mContext, R.animator.card_flip_left_in);
            AnimatorSet rightOut = (AnimatorSet) AnimatorInflater
                    .loadAnimator(mContext, R.animator.card_flip_right_out);
            AnimatorSet leftOut  = (AnimatorSet) AnimatorInflater
                    .loadAnimator(mContext, R.animator.card_flip_left_out);
            AnimatorSet rightIn  = (AnimatorSet) AnimatorInflater
                    .loadAnimator(mContext, R.animator.card_flip_right_in);

            leftIn.setTarget(cardFront);
            rightOut.setTarget(cardBack);
            showFrontAnim.playTogether(leftIn, rightOut);

            leftOut.setTarget(cardFront);
            rightIn.setTarget(cardBack);
            showBackAnim.playTogether(leftOut, rightIn);

           /* final FrameLayout cardContainer = (FrameLayout) getItem(position);


        //   FrameLayout cardContainer = (FrameLayout)
           //         linearLayout.findViewById(R.id.card_container_layout);
            // Set the flip animation to be triggered on container clicking
            cardContainer.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    if (isShowingBack) {
                        showFrontAnim.start();
                        isShowingBack = false;
                    } else {
                        showBackAnim.start();
                        isShowingBack = true;
                    }
                }
            });*/

        } else {
            linearLayout = (FrameLayout)convertView;
        }





       // imageView.setImageResource(mThumbIds[position]);
        return linearLayout;
    }

    public void girar(){
        if (isShowingBack) {
            showFrontAnim.start();
            isShowingBack = false;
        } else {
            showBackAnim.start();
            isShowingBack = true;
        }
    }



    public AnimatorSet getShowFrontAnim() {
        return showFrontAnim;
    }

    public void setShowFrontAnim(AnimatorSet showFrontAnim) {
        this.showFrontAnim = showFrontAnim;
    }

    public AnimatorSet getShowBackAnim() {
        return showBackAnim;
    }

    public void setShowBackAnim(AnimatorSet showBackAnim) {
        this.showBackAnim = showBackAnim;
    }

    public boolean isShowingBack() {
        return isShowingBack;
    }

    public void setIsShowingBack(boolean isShowingBack) {
        this.isShowingBack = isShowingBack;
    }
}
