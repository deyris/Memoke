package es.barcelona.dey.memoke.ui;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import es.barcelona.dey.memoke.R;
import es.barcelona.dey.memoke.presenters.CreationPresenter;

/**
 * Created by deyris.drake on 25/1/16.
 */
public class CreationFragment extends Fragment {

    public static final String TAG = "MMKCreationFragment";

    public static String PARAM_TITLE = "TITLE";

    TextView mTxtTitle;
    TextView mTxtNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout mLinearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_creation,
                container, false);


        mTxtTitle = (TextView)mLinearLayout.findViewById(R.id.creationTitle);
        mTxtNumber = (TextView)mLinearLayout.findViewById(R.id.creationNumber);
        Bundle bundle = getActivity().getIntent().getExtras();

        if (bundle!=null) {
            Resources res = getResources();
            mTxtTitle.setText(String.format(res.getString(R.string.creation_title), bundle.getString(PARAM_TITLE)));
            Bundle bundle1 = getArguments();
            mTxtNumber.setText(String.format(res.getString(R.string.creation_number), bundle1.getInt(CreationPresenter.PARAM_CURRENT_PAIR)));
        }



        return mLinearLayout;


    }
}
