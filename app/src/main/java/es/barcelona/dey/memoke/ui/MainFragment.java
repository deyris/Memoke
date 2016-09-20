package es.barcelona.dey.memoke.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.util.List;

import es.barcelona.dey.memoke.R;
import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.database.BoardDatabase;
import es.barcelona.dey.memoke.presenters.MainPresenter;
import es.barcelona.dey.memoke.services.BoardService;
import es.barcelona.dey.memoke.views.MainView;

/**
 * Created by deyris.drake on 23/1/16.
 */
public class MainFragment extends Fragment  implements MainView {

    public static String PARAM_TITLE = "TITLE";
    public static String PARAM_SELECTED_BOARD = "PARAM_SELECTED_BOARD";

    EditText mTxtTitle = null;
    EditText mTxtNumber = null;
    Button mBtnCreate = null;

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

        mBtnCreate.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                              //Verificar si existe ya el tablero y lanzar un popup
                                              mainPresenter.verifyIfExistBoardAndLauncPopup(mTxtTitle.getText().toString().trim());


                                          }

                                      }

        );

        Button mBtnLoad = (Button) mRelativeLayout.findViewById(R.id.btnLoad);
        if (mainPresenter.isButtonLoadVisible()) {
            mBtnLoad.setVisibility(View.VISIBLE);
        } else {
            mBtnLoad.setVisibility(View.GONE);
        }

        return mRelativeLayout;


    }


    @Override
    public void openToCreateBoard(boolean createFromZero) {
        Intent i = new Intent(getActivity(), CreationActivity.class);
        i.putExtra(PARAM_TITLE, mTxtTitle.getText().toString().trim());
        if (!createFromZero) {
            //Restablecer el board
            String jsonSelectedBoard = mainPresenter.getBoardForRestore(mTxtTitle.getText().toString().trim());
            i.putExtra(PARAM_SELECTED_BOARD, jsonSelectedBoard);

        }
        startActivity(i);
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

                //Borramos el tablero
                mainPresenter.deleteBoard(mTxtTitle.getText().toString());
                // List<Board> testBoard = BoardDatabase.getBoards(MainFragment.this.getActivity());
                openToCreateBoard(true);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                openToCreateBoard(false);
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


}
