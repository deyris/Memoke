package es.barcelona.dey.memoke;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by deyris.drake on 1/4/16.
 */
public class TabAdapter extends BaseAdapter {
    private Context mContext;
    private AnimatorSet showFrontAnim = new AnimatorSet();
    private AnimatorSet showBackAnim = new AnimatorSet();
    private boolean isShowingBack = false;

    public TabAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        LinearLayout linearLayout;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes


            //LayoutInflater inflater = LayoutInflater.from();
            linearLayout = (LinearLayout)  LayoutInflater.from(mContext).inflate(R.layout.tab_inside, parent, false);


            final LinearLayout cardFront = (LinearLayout) linearLayout.findViewById(R.id.card_front_layout);
            final LinearLayout cardBack = (LinearLayout) linearLayout.findViewById(R.id.card_back_layout);

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

            LinearLayout cardContainer = (LinearLayout)
                    linearLayout.findViewById(R.id.card_container_layout);
            // Set the flip animation to be triggered on container clicking
            cardContainer.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (isShowingBack) {
                        cardFront.setVisibility(View.GONE);
                        cardBack.setVisibility(View.VISIBLE);
                        showFrontAnim.start();
                        isShowingBack = false;

                    }
                    else {
                        cardFront.setVisibility(View.VISIBLE);
                        cardBack.setVisibility(View.GONE);
                        showBackAnim.start();
                        isShowingBack = true;
                    }
                }
            });

        } else {
           // imageView = (ImageView) convertView;
            linearLayout = (LinearLayout)convertView;
        }

       // imageView.setImageResource(mThumbIds[position]);
        return linearLayout;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.ic_action_name, R.drawable.ic_action_name,
            R.drawable.ic_action_name, R.drawable.ic_action_name,
            R.drawable.ic_action_name, R.drawable.ic_action_name,
            R.drawable.ic_action_name, R.drawable.ic_action_name,
            R.drawable.ic_action_name, R.drawable.ic_action_name,
            R.drawable.ic_action_name, R.drawable.ic_action_name,
            R.drawable.ic_action_name, R.drawable.ic_action_name,
            R.drawable.ic_action_name, R.drawable.ic_action_name,
            R.drawable.ic_action_name, R.drawable.ic_action_name,
            R.drawable.ic_action_name, R.drawable.ic_action_name,
            R.drawable.ic_action_name, R.drawable.ic_action_name
    };



}
