package es.barcelona.dey.memoke;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

/**
 * Created by deyris.drake on 3/2/16.
 */
public class DialogText extends Dialog implements android.view.View.OnClickListener {

    public CreationActivity c;
    public Button btnAceptDialogText, btnCancelDialogText, btnDelDialogText;
    public EditText mEditText;
    public String textFromFragment;
    public long textSizeFromFragment;

    public String getTextFromFragment() {
        return textFromFragment;
    }

    public void setTextFromFragment(String textFromFragment) {
        this.textFromFragment = textFromFragment;
    }

    public long getTextSizeFromFragment() {
        return textSizeFromFragment;
    }

    public void setTextSizeFromFragment(long textSizeFromFragment) {
        this.textSizeFromFragment = textSizeFromFragment;
    }



    public DialogText(CreationActivity a) {
        super(a);
        this.c = a;
    }

    @Override
    public void onClick(View v) {

    }

    private class seekListener implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {

            mEditText.setTextSize(progress);

        }

        public void onStartTrackingTouch(SeekBar seekBar) {}

        public void onStopTrackingTouch(SeekBar seekBar) {}

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_text);


        mEditText = (EditText) findViewById(R.id.txtDialogText);


        SeekBar seekBar = (SeekBar)findViewById(R.id.seekDialogText);
        seekBar.setOnSeekBarChangeListener(new seekListener());

        btnAceptDialogText =  (Button)findViewById(R.id.btnAceptDialogText);
        btnAceptDialogText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                c.onDataPass(mEditText);
                cancel();
            }
        });
        btnCancelDialogText =  (Button)findViewById(R.id.btnCancelDialogText);
        btnCancelDialogText.findViewById(R.id.btnCancelDialogText).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();

            }
        });
        btnDelDialogText = (Button)findViewById(R.id.btnDelDialogText);
        mEditText.setText("");
        if (null !=getTextFromFragment()){
            //Si tenemos texto dentro ya, entonces el btnDel, se verá
             mEditText.setText(getTextFromFragment());
             mEditText.setHint("");
             btnDelDialogText.setVisibility(View.VISIBLE);
             btnDelDialogText.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mEditText.setText("");

                }
            });
        }
        if (0 !=getTextSizeFromFragment()){
            //Si tenemos texto dentro ya, entonces el btnDel, se verá
            mEditText.setTextSize(getTextSizeFromFragment()/2);
        }
    }
}
