package es.barcelona.dey.memoke;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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

import java.util.List;

import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.database.BoardDatabase;
import es.barcelona.dey.memoke.services.BoardServices;

/**
 * Created by deyris.drake on 23/1/16.
 */
public class MainFragment extends Fragment {

    public static String PARAM_TITLE = "TITLE";
    public static String PARAM_NUMBER = "NUMERO";

    EditText mTxtTitle= null;
    EditText mTxtNumber = null;
    Button  mBtnCreate = null;

    BoardServices boardServices = new BoardServices();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout mRelativeLayout = (LinearLayout) inflater.inflate(R.layout.fragment_main,
                container, false);


        mBtnCreate = (Button)mRelativeLayout.findViewById(R.id.btnCrear);

        mBtnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Verificar si existe ya el tablero y lanzar un popup
                if (boardServices.existThisBoard(mTxtTitle.getText().toString().trim(),v.getContext())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainFragment.this.getActivity());
                    builder.setTitle("Ya tienes un tablero con este nombre. ¿Deseas reemplazarlo?");

                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                           //Borramos el tablero
                            boardServices.deleteBoard(mTxtTitle.getText().toString(),MainFragment.this.getActivity());
                            List<Board> testBoard = BoardDatabase.getBoards(MainFragment.this.getActivity());
                            openToCreateBoard(true);
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO
                           //
                            openToCreateBoard(false);
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }




            }

        }

        );

        Button mBtnLoad = (Button)mRelativeLayout.findViewById(R.id.btnLoad);
        if (boardServices.existsBoards(this.getActivity())){
            mBtnLoad.setVisibility(View.VISIBLE);
        }else{
            mBtnLoad.setVisibility(View.GONE);
        }

        return mRelativeLayout;


    }


    public void openToCreateBoard(boolean createFromZero){
        Intent i = new Intent(getActivity(),CreationActivity.class);
        i.putExtra(PARAM_TITLE,mTxtTitle.getText().toString().trim());
        startActivity(i);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTxtTitle = (EditText)getActivity().findViewById(R.id.txtTitle);


    }
}
