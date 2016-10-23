package es.barcelona.dey.memoke.ui;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import es.barcelona.dey.memoke.R;
import es.barcelona.dey.memoke.presenters.MainPresenter;
import es.barcelona.dey.memoke.views.MainView;

/**
 * Created by deyris.drake on 23/1/16.
 */
public class MainFragment extends Fragment  implements MainView {

    EditText mTxtTitle = null;
    Button mBtnCreate = null;
    Button mBtnMoreBoard = null;

    MainPresenter mainPresenter;

    @Override
    public Context getContext(){
        return getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout mRelativeLayout = (LinearLayout) inflater.inflate(R.layout.fragment_main,
                container, false);

        mainPresenter = new MainPresenter();
        mainPresenter.setView(MainFragment.this);

        mBtnCreate = (Button) mRelativeLayout.findViewById(R.id.btnCrear);

        mBtnMoreBoard = (Button) mRelativeLayout.findViewById(R.id.btnLoad);

        mBtnCreate.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                            mainPresenter.clickOnCreateButton(mTxtTitle.getText().toString().trim());

                                          }

                                      }

        );


        mainPresenter.manageVisibiltyForLoadButton();

        return mRelativeLayout;


    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTxtTitle = (EditText) getActivity().findViewById(R.id.txtTitle);
    }


    @Override
    public void launchAlertExistsThisBoard() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainFragment.this.getActivity());
        builder.setTitle("Ya tienes un tablero con este nombre. ¿Deseas reemplazarlo?");

        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mainPresenter.clickPositiveButtonOnDialog(mTxtTitle.getText().toString());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mainPresenter.clickNegativeButtonOnDialog(mTxtTitle.getText().toString().trim());
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void openToCreateBoardFromZero(String title) {
        Intent i = new Intent(getActivity(), CreationActivity.class);
        i.putExtra(MainPresenter.PARAM_TITLE, title);

        startActivity(i);
    }

    @Override
    public void openToCreateBoardFromOther(String jsonSelectedBoard, String title){
        Intent i = new Intent(getActivity(), CreationActivity.class);
        i.putExtra(MainPresenter.PARAM_TITLE, title);
        i.putExtra(MainPresenter.PARAM_SELECTED_BOARD, jsonSelectedBoard);

        startActivity(i);
    }

    @Override
    public void hideButtonMoreBoards(){
       // Button mBtnLoad = (Button) getActivity().findViewById(R.id.btnLoad);
        mBtnMoreBoard.setVisibility(View.GONE);

    }

    @Override
    public void showButtonMoreBoards(){
      //  Button mBtnLoad = (Button) getActivity().findViewById(R.id.btnLoad);
        mBtnMoreBoard.setVisibility(View.VISIBLE);

    }


}
