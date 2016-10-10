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
import es.barcelona.dey.memoke.presenters.HeaderPresenter;
import es.barcelona.dey.memoke.views.HeaderView;

/**
 * Created by deyris.drake on 25/1/16.
 */
public class CreationFragment extends Fragment implements HeaderView{

    public static final String TAG = "MMKCreationFragment";

    TextView mTxtTitle;
    TextView mTxtNumber;

    HeaderPresenter headerPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout mLinearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_creation,
                container, false);

        headerPresenter = new HeaderPresenter();
        headerPresenter.setView(this);

        mTxtTitle = (TextView)mLinearLayout.findViewById(R.id.creationTitle);
        mTxtNumber = (TextView)mLinearLayout.findViewById(R.id.creationNumber);
        Bundle bundle = getActivity().getIntent().getExtras();

        headerPresenter.creationFragmentOnCreate(bundle);


        return mLinearLayout;


    }

    @Override
    public void fillHeaderData(Bundle bundle){
        Resources res = getResources();
        mTxtTitle.setText(String.format(res.getString(R.string.creation_title), bundle.getString(HeaderPresenter.PARAM_TITLE)));
        Bundle bundle1 = getArguments();
        mTxtNumber.setText(String.format(res.getString(R.string.creation_number), bundle1.getInt(CreationPresenter.PARAM_CURRENT_PAIR)));

    }
}
