package es.barcelona.dey.memoke;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import es.barcelona.dey.memoke.beans.Tab;
import es.barcelona.dey.memoke.beans.TabForGame;

/**
 * Created by deyris.drake on 1/4/16.
 */
public class TabAdapter extends BaseAdapter {
    private Context mContext;
    private AnimatorSet showFrontAnim = new AnimatorSet();
    private AnimatorSet showBackAnim = new AnimatorSet();
    TabForGame[] tabsForPlay;


    public TabAdapter(Context mContext, TabForGame[] tabs) {
        this.mContext = mContext;
        this.tabsForPlay = tabs;
    }

    public int getCount() {
        return tabsForPlay.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }



    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        FrameLayout linearLayout;
        if (convertView == null) {

            //LayoutInflater inflater = LayoutInflater.from();
            linearLayout = (FrameLayout)  LayoutInflater.from(mContext).inflate(R.layout.tab_inside, parent, false);


            final LinearLayout cardFront = (LinearLayout) linearLayout.findViewById(R.id.card_front_layout);

            final LinearLayout cardBack = (LinearLayout) linearLayout.findViewById(R.id.card_back_layout);

          //  textView.setBackgroundResource(imgs[position]);
          //  textView.setText(textos[position]);
            Tab tabForPlay = tabsForPlay[position];
            TextView textView = (TextView) cardBack.getChildAt(0);
            ImageView imageView = (ImageView)cardBack.getChildAt(1);

            if (tabForPlay.getType().equals(Tab.Type.TEXT)){
                imageView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setText(tabForPlay.getText());
                textView.setTextSize(tabForPlay.getSize()/2);
            }
            if (tabForPlay.getType().equals(Tab.Type.PHOTO)){
                textView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(tabForPlay.getUri())
                        .resize(180, 180)
                        .centerCrop()
                        .into(imageView);
            }
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

        return linearLayout;
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


}
