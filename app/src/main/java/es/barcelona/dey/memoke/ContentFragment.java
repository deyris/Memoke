package es.barcelona.dey.memoke;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * Created by deyris.drake on 26/1/16.
 */
public class ContentFragment extends Fragment {

    LinearLayout mLayout;

    Button mBtnPhoto1;
    Button mBtnText1 = null;
    Button mBtnFig1 = null;
    ImageView mImg1 = null;

    Button mBtnPhoto2 = null;
    Button mBtnText2 = null;
    Button mBtnFig2 = null;
    ImageView mImg2 = null;

    LinearLayout mLinearLayout1 = null;
    LinearLayout mLinearLayout2 = null;

    String mCurrentPhotoPath;
    /*Para mostrar la foto, se refiere a la ficha 1 o la 2*/
    int mCurrentLinearPhoto;
    /*Para mostra el texto, se refiere dentro de una ficha, al frame a ocultar (el de los botones)*/
    int mCurrentFrameTextHide;
    /*Para mostra el texto, se refiere dentro de una ficha, al frame a mostrar */
    int mCurrentFrameTextShow;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final String ID_CURRENT_TAB = "ID_CURRENT_TAB";
    static final int PHOTO_FROM_CAMERA = 1;
    static final int PHOTO_FROM_GALLERY = 2;

    public static int createdPair = 0;


    static final int REQUEST_SELECT_PICTURE = 2;


    public interface OnDataPass {
        public void onDataPass(int data);
        public void onDataPass(EditText data);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mLayout = (LinearLayout) inflater.inflate(R.layout.fragment_content,
                container, false);

        mBtnPhoto1 = (Button)mLayout.findViewById(R.id.btnPhoto1);


        mBtnText1 = (Button) mLayout.findViewById(R.id.btnText1);
        mBtnFig1 = (Button) mLayout.findViewById(R.id.btnFig1);

        mBtnPhoto2 = (Button)mLayout.findViewById(R.id.btnPhoto2);
        mBtnText2 = (Button) mLayout.findViewById(R.id.btnText2);

        mLinearLayout1 = (LinearLayout)mLayout.findViewById(R.id.editContent1);
        mLinearLayout2 = (LinearLayout)mLayout.findViewById(R.id.editContent2);

        genPhotoButton(mBtnPhoto1);
        genPhotoButton(mBtnPhoto2);

        genTextButton(mBtnText1);
        genTextButton(mBtnText2);

        //pair = new Pair();
       // pair.setIdPair(createdPair++);

        return  mLayout;

    }

    public void receivingFromDialog(int data){
        if (data==PHOTO_FROM_CAMERA){
            openingCamera();
        }
        if (data==PHOTO_FROM_GALLERY){
            openingGallery();
        }
    }

    public void receivingFromDialog(EditText data){


        FrameLayout frameLayout = (FrameLayout)mLayout.findViewById(mCurrentFrameTextHide);
        frameLayout.setVisibility(View.GONE);


        FrameLayout frameLayout1 = (FrameLayout)mLayout.findViewById(mCurrentFrameTextShow);
        frameLayout1.setVisibility(View.VISIBLE);

        int a = frameLayout1.getChildCount();

        TextView textView = (TextView)frameLayout1.getChildAt(0);
        textView.setText(data.getText());
        textView.setTextSize(data.getTextSize()/2);


    }



    private void openingCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                              // Ensure that there's a camera activity to handle the intent
                                              if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                                  // Create the File where the photo should go
                                                  File photoFile = null;
                                                  try {
                                                      photoFile = createImageFile();
                                                  } catch (IOException ex) {
                                                      // Error occurred while creating the File

                                                  }
                                                  // Continue only if the File was successfully created
                                                  if (photoFile != null) {
                                                      takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                                              Uri.fromFile(photoFile));

                                                      startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

                                                  }
                                              }
    }

    private void openingGallery(){
       Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        startActivityForResult(intent, REQUEST_SELECT_PICTURE);
    }

    public void genPhotoButton(Button button){


        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          LinearLayout t = (LinearLayout) v.getParent();
                                          mCurrentLinearPhoto = t.getId();
                                          DialogPhoto cdd = new DialogPhoto((CreationActivity) getActivity());
                                          cdd.show();

                                      }

                                  }

        );

    }

    private void genTextButton(Button button){


        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          LinearLayout t = (LinearLayout)v.getParent();
                                          FrameLayout f = (FrameLayout) t.getParent();
                                          t = (LinearLayout)f.getParent();

                                          int a = t.getChildCount();

                                          FrameLayout fHide = (FrameLayout)t.getChildAt(1); //A esconder
                                          FrameLayout fShow = (FrameLayout)t.getChildAt(2); //A mostrar
                                          mCurrentFrameTextHide = fHide.getId();
                                          mCurrentFrameTextShow = fShow.getId();

                                          DialogText textDialog = new DialogText((CreationActivity)getActivity());
                                          textDialog.show();
                                         }

                                  }

        );

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );



        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_IMAGE_CAPTURE){
            setPicToBackground();
        }else if (requestCode==REQUEST_SELECT_PICTURE){
            Uri selectedImage = data.getData();
            mCurrentPhotoPath = selectedImage.toString();
            setPicToBackground();
        }


    }

    public void setPicToBackground(){

        LinearLayout LinearLayout = (LinearLayout)mLayout.findViewById(mCurrentLinearPhoto);

        int a = LinearLayout.getChildCount();

        for (int i = 0; i < LinearLayout.getChildCount(); i++) {

            Button button = (Button ) LinearLayout.getChildAt(i);
            if(button instanceof Button){
                button.setVisibility(View.GONE);
            }

        }

        Picasso.with(getActivity()).load(mCurrentPhotoPath)
                .resize(LinearLayout.getHeight(), LinearLayout.getWidth())
                .centerCrop().into(new Target() {

            @Override
            public void onPrepareLoad(Drawable arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity().getApplicationContext(), "Start Loading", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                // TODO Auto-generated method stub
                LinearLayout LinearLayout = (LinearLayout)mLayout.findViewById(mCurrentLinearPhoto);

                LinearLayout.setBackground(new BitmapDrawable(getActivity().getApplicationContext().getResources(), bitmap));
                saveData();
            }

            @Override
            public void onBitmapFailed(Drawable arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity().getApplicationContext(), "Failed Loading", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void saveData(){

    }

}
