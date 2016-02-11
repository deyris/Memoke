package es.barcelona.dey.memoke;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import es.barcelona.dey.memoke.R;

/**
 * Created by deyris.drake on 2/2/16.
 */
public class DialogPhoto extends Dialog implements android.view.View.OnClickListener {

    public CreationActivity c;
    public Button camera, gallery;

    public DialogPhoto(CreationActivity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCamera:
                c.onDataPass(ContentFragment.PHOTO_FROM_CAMERA);
                break;
            case R.id.btnGallery:
                c.onDataPass(ContentFragment.PHOTO_FROM_GALLERY);
                break;
            default:
                break;
        }
        dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_photo);
        camera = (Button) findViewById(R.id.btnCamera);
        gallery = (Button) findViewById(R.id.btnGallery);
        camera.setOnClickListener(this);
        gallery.setOnClickListener(this);
    }



}
