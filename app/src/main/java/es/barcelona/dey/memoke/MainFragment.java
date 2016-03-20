package es.barcelona.dey.memoke;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

/**
 * Created by deyris.drake on 23/1/16.
 */
public class MainFragment extends Fragment {

    public static String PARAM_TITLE = "TITLE";
    public static String PARAM_NUMBER = "NUMERO";

    EditText mTxtTitle= null;
    EditText mTxtNumber = null;
    Button  mBtnCreate = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout mRelativeLayout = (LinearLayout) inflater.inflate(R.layout.fragment_main,
                container, false);


        mBtnCreate = (Button)mRelativeLayout.findViewById(R.id.btnCrear);

        mBtnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(getActivity(),CreationActivity.class);

                i.putExtra(PARAM_TITLE,mTxtTitle.getText().toString().trim());
                startActivity(i);
            }

        }

        );

        return mRelativeLayout;


    }





    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTxtTitle = (EditText)getActivity().findViewById(R.id.txtTitle);


    }
}
