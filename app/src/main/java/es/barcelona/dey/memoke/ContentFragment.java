package es.barcelona.dey.memoke;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.barcelona.dey.memoke.beans.Pair;
import es.barcelona.dey.memoke.beans.Tab;

/**
 * Created by deyris.drake on 18/2/16.
 */
public class ContentFragment extends Fragment {

    LinearLayout mLayout;

    FrameLayout mFrameTab1;

    FrameLayout mFrameTab2;

    static final int PHOTO_FROM_CAMERA = 1;
    static final int PHOTO_FROM_GALLERY = 2;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_PICTURE = 2;

    String mCurrentPhotoPath;

    private Pair mCurrentPair;
    int mCurrentTab;
    int mCurrentFrameResultShow;



    public interface OnDataPass {
        public void onDataPass(int data);
        public void onDataPass(EditText data);

    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mCurrentPair == null) {
            mCurrentPair = new Pair(getActivity());
        }
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mLayout = (LinearLayout) inflater.inflate(R.layout.fragment_creation_content,
                container, false);

        mFrameTab1 = (FrameLayout) mLayout.findViewById(R.id.editContent1);
        mFrameTab2 = (FrameLayout) mLayout.findViewById(R.id.editContent2);
        setListenerFrame(mFrameTab1);
        setListenerFrame(mFrameTab2);

        return mLayout;
    }

    private Tab.Type getTypeById(int id){
        switch (id){
            case 0: return Tab.Type.TEXT;
            case 1: return Tab.Type.PHOTO;
            default:return Tab.Type.FIGURE;
        }
    }



    public  void setListenerFrame(FrameLayout frame) {

        frame.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Un texto", "Una foto", "Una figura"};

                //Determinamos en que ficha estamos basándonos Ficha 1/Ficha 2
                LinearLayout parent = (LinearLayout) v.getParent();
                TextView textTab = (TextView) parent.getChildAt(0);
                mCurrentTab = (textTab.getText().toString().indexOf("1")>0?1:2);

                //Determinamos donde mostrar el resultado
                FrameLayout thisFrame = (FrameLayout)mLayout.findViewById(v.getId());
                mCurrentFrameResultShow = thisFrame.getChildAt(0).getId();

                //Abrimos un dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("¿Cómo será esta ficha?");

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        mCurrentPair.getTabs()[mCurrentTab -1]=new Tab();
                        mCurrentPair.getTabs()[mCurrentTab -1].setType(getTypeById(item));
                        initChargeTab();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    public void receivingFromDialog(int data){
        if (data==PHOTO_FROM_CAMERA){
            openingCamera();
        }
        if (data==PHOTO_FROM_GALLERY){
            openingGallery();
        }
    }

    private void initChargeTab(){
        if (this.mCurrentPair.getTabs()[mCurrentTab -1].getType()==Tab.Type.TEXT) {
            DialogText textDialog = new DialogText((CreationActivity) getActivity());
            textDialog.show();
        }
        if (this.mCurrentPair.getTabs()[mCurrentTab -1].getType()==Tab.Type.PHOTO) {
            DialogPhoto cdd = new DialogPhoto((CreationActivity) getActivity());
            cdd.show();
        }
    }

    public void receivingFromDialog(EditText data){

        TextView textView = (TextView)mLayout.findViewById(mCurrentFrameResultShow);
        textView.setText(data.getText());
        textView.setTextSize(data.getTextSize() / 2);

        mCurrentPair.getTabs()[mCurrentTab -1].setText(data.getText().toString());
        mCurrentPair.getTabs()[mCurrentTab -1].setSize((int) data.getTextSize());
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

        TextView textResult = (TextView)mLayout.findViewById(mCurrentFrameResultShow);
        textResult.setText("");

        Picasso.with(getActivity()).load(mCurrentPhotoPath)
                .resize(textResult.getHeight(), textResult.getWidth())
                .centerCrop().into(new Target() {

            @Override
            public void onPrepareLoad(Drawable arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity().getApplicationContext(), "Start Loading", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                // TODO Auto-generated method stub
                TextView textResult = (TextView)mLayout.findViewById(mCurrentFrameResultShow);

                textResult.setBackground(new BitmapDrawable(getActivity().getApplicationContext().getResources(), bitmap));
                mCurrentPair.getTabs()[mCurrentTab-1].setUri(mCurrentPhotoPath);
            }

            @Override
            public void onBitmapFailed(Drawable arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity().getApplicationContext(), "Failed Loading", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
