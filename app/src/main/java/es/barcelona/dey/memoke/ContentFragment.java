package es.barcelona.dey.memoke;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.barcelona.dey.memoke.beans.Pair;
import es.barcelona.dey.memoke.beans.Tab;
import es.barcelona.dey.memoke.database.TabDatabase;

/**
 * Created by deyris.drake on 18/2/16.
 */
public class ContentFragment extends Fragment {

    public static final String TAG = "MMKContentFragment";

    LinearLayout mLayout;

    FrameLayout mFrameTab1;

    FrameLayout mFrameTab2;

    Bundle savedState;


    static final int PHOTO_FROM_CAMERA = 1;
    static final int PHOTO_FROM_GALLERY = 2;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_PICTURE = 2;

    static String mCurrentPhotoPath;


    static private Pair mCurrentPair = new Pair();
    static int mCurrentTab;
    static int mCurrentTextResultShow;
    static int mCurrentImgResultShow;
    static int mCurrentFrame;

    TextView  mTextView1;
    TextView  mTextView2;

    ImageView  mImageView1;
    ImageView  mImageView2;

    static int finalHeight, finalWidth;
    private FragmentIterationListener mCallback = null;


    public interface OnDataPass {
        public void onDataPass(int data);
        public void onDataPass(EditText data);

    }

    public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }

    public static ContentFragment newInstance(Bundle arguments){
        ContentFragment f = new ContentFragment();
       // f.setRetainInstance(true);
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    public ContentFragment(){

    }

    /*Sobrecarga de los metodos del ciclo de vida de un Fragment*/

    //El Activity que contiene el Fragment ha terminado su creación
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("DEY", "Estoy en ContentFragment.onCreated ");

        try{
            mCallback = (FragmentIterationListener) getActivity();
        }catch(Exception ex){
            Log.e("ContentFragment", "El Activity debe implementar la interfaz FragmentIterationListener");
        }
    }


    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }

        Log.d("DEY", "Estoy en ContentFragment.onCreateView");
        mLayout = (LinearLayout) inflater.inflate(R.layout.fragment_creation_content,
                container, false);

        if (mLayout!=null) {
            mFrameTab1 = (FrameLayout) mLayout.findViewById(R.id.editContent1);
            mFrameTab2 = (FrameLayout) mLayout.findViewById(R.id.editContent2);


            mTextView1 = (TextView) mLayout.findViewById(R.id.txtContent1);
            mTextView2 = (TextView) mLayout.findViewById(R.id.txtContent2);

            mImageView1 = (ImageView) mLayout.findViewById(R.id.imgContent1);
            mImageView2 = (ImageView) mLayout.findViewById(R.id.imgContent2);
        }

        return mLayout;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListenerFrame(mFrameTab1);
        setListenerFrame(mFrameTab2);

        if (savedInstanceState!=null) {
            Log.d("DEY", "Estoy en ContentFragment.onCreate con bundle not null");
            String jsonCurrentPair = savedInstanceState.getString("PARAM_CURRENT_PAIR");
            if(null!=jsonCurrentPair) {
                Log.d("DEY", "jsonCurrentPair not null: " + jsonCurrentPair);
                final Gson gson = new Gson();
                mCurrentPair = gson.fromJson(jsonCurrentPair, Pair.class);

                Thread t =new Thread() {
                    public void run() {
                        fillResultWithCurrent(mTextView1.getId(), 1);

                    }};
                t.start();

                t =new Thread() {
                    public void run() {
                        fillResultWithCurrent(mTextView2.getId(), 2);

                    }};
                t.start();

                fillImgsWithCurrent();
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Serializamos nuestro currentPair
        if (null!=mCurrentPair && mCurrentPair.getState()!=Pair.State.EMPTY) {
            Log.d("DEY", "Estoy en ContentFragment.onSaveInstanceState con mCurrentPair not null");

            final Gson gson = new Gson();
            String jsonCurrentPair = gson.toJson(mCurrentPair).toString();

            outState.putString("PARAM_CURRENT_PAIR", jsonCurrentPair);

        }

    }



    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("DEY", "Estoy en ContentFragment.onDestroy");
        //Salvamos en fichero
       // TabDatabase.addPair(this.getActivity(), mCurrentPair);
    }

    /*Metodos internos para interaccion entre los objetos del fragment*/

    private void fillResultWithCurrent(int idText, int tab){
        TextView mText = (TextView) mLayout.findViewById(idText);
        if (mCurrentPair!=null && mCurrentPair.getTabs()[tab - 1] != null) {

            if (mCurrentPair.getTabs()[tab -1].getType()==Tab.Type.TEXT) {
                //Ocultar foto de ese frame
                FrameLayout parent = (FrameLayout)mText.getParent();
                ImageView imageView = (ImageView)parent.getChildAt(1);
                imageView.setVisibility(View.GONE);
                imageView.setBackground(null);

                mText.setVisibility(View.VISIBLE);
                if (!mCurrentPair.getTabs()[tab - 1].getText().isEmpty()) {
                    String val = mCurrentPair.getTabs()[tab - 1].getText();

                    mText.setText("");
                    mText.setText(val);
                    mText.setTextSize(mCurrentPair.getTabs()[tab - 1].getSize() / 2);
                }

            }

        }
    }

    private void preDrawPhoto(ImageView imageView, int currentTabTemp, String uri){
        final ImageView imageViewTmp = imageView;
        final int tabTmp = currentTabTemp;
        final String uriTemp = uri;
        ViewTreeObserver vto = imageViewTmp.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                mImageView1.getViewTreeObserver().removeOnPreDrawListener(this);
                finalHeight = imageViewTmp.getMeasuredHeight();
                finalWidth = imageViewTmp.getMeasuredWidth();

                int tempImg = mCurrentImgResultShow;
                String tempPhoto = mCurrentPhotoPath;
                int tempTab = mCurrentTab;

                mCurrentImgResultShow = imageViewTmp.getId();
                mCurrentPhotoPath = uriTemp;
                mCurrentTab = tabTmp;
                setPicToImg(imageViewTmp, finalHeight, finalWidth);

                mCurrentImgResultShow = tempImg;
                mCurrentPhotoPath = tempPhoto;
                mCurrentTab = tempTab;

                return true;
            }
        });
    }


    private void fillImgsWithCurrent(){

        if (mCurrentPair!=null && mCurrentPair.getTabs()[0]!=null) {

            if (mCurrentPair.getTabs()[0].getType() == Tab.Type.PHOTO) {
                mTextView1.setVisibility(View.GONE);
                preDrawPhoto(mImageView1,1,mCurrentPair.getTabs()[0].getUri());
             }
        }

        if (mCurrentPair!=null && mCurrentPair.getTabs()[1]!=null) {
            mTextView2.setVisibility(View.GONE);
            if (mCurrentPair.getTabs()[1].getType() == Tab.Type.PHOTO) {
              preDrawPhoto(mImageView2, 2, mCurrentPair.getTabs()[1].getUri());

            }
        }

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
                mCurrentFrame = thisFrame.getId();
                mCurrentTextResultShow = thisFrame.getChildAt(0).getId();
                mCurrentImgResultShow = thisFrame.getChildAt(1).getId();

                //Abrimos un dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ContentFragment.this.getActivity());
                builder.setTitle("¿Cómo será esta ficha?");

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        if (null == mCurrentPair.getTabs()[mCurrentTab - 1]) {
                            mCurrentPair.getTabs()[mCurrentTab - 1] = new Tab();
                        }
                        mCurrentPair.getTabs()[mCurrentTab - 1].setType(getTypeById(item));
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
            if (null != this.mCurrentPair.getTabs()[mCurrentTab -1].getText()){
                textDialog.setTextFromFragment(this.mCurrentPair.getTabs()[mCurrentTab - 1].getText());
                textDialog.setTextSizeFromFragment(this.mCurrentPair.getTabs()[mCurrentTab -1].getSize());

            }

            textDialog.show();
        }
        if (this.mCurrentPair.getTabs()[mCurrentTab -1].getType()==Tab.Type.PHOTO) {
            DialogPhoto cdd = new DialogPhoto((CreationActivity) getActivity());
            cdd.show();
        }
    }

    public void receivingFromDialog(EditText data){

        ImageView imageView = (ImageView)mLayout.findViewById(mCurrentImgResultShow);
        imageView.setBackground(null);
        imageView.setVisibility(View.GONE);


        TextView textView = (TextView)mLayout.findViewById(mCurrentTextResultShow);
        textView.setVisibility(View.VISIBLE);
        textView.setText("");
        textView.setText(data.getText());
        textView.setTextSize(data.getTextSize() / 2);

        mCurrentPair.getTabs()[mCurrentTab - 1].setText(data.getText().toString());
        mCurrentPair.getTabs()[mCurrentTab - 1].setSize((int) data.getTextSize());
        showContinueButton();
    }

    private void showContinueButton(){
        validatePairState();
        if (mCurrentPair.getState().equals(Pair.State.COMPLETED)){

           Button b = (Button)getActivity().findViewById(R.id.btnSgte);
            b.setVisibility(View.VISIBLE);
        }

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

    public void setPicToImg(ImageView img, int height, int width){

     Picasso.with(getActivity()).load(mCurrentPhotoPath)
                .resize(height, width)
             .centerCrop().into(img);
        mCurrentPair.getTabs()[mCurrentTab - 1].setUri(mCurrentPhotoPath);
        showContinueButton();

          /*   .centerCrop().into(new Target() {

         @Override
         public void onPrepareLoad(Drawable arg0) {
             // TODO Auto-generated method stub
             Toast.makeText(getActivity().getApplicationContext(), "Start Loading", Toast.LENGTH_SHORT).show();
         }

         @Override
         public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
             // TODO Auto-generated method stub

             ImageView imageView = (ImageView) mLayout.findViewById(mCurrentImgResultShow);
             imageView.setBackground(new BitmapDrawable(getResources(), bitmap));

             mCurrentPair.getTabs()[mCurrentTab - 1].setUri(mCurrentPhotoPath);

         }

         @Override
         public void onBitmapFailed(Drawable arg0) {
             // TODO Auto-generated method stub
             Toast.makeText(getActivity().getApplicationContext(), "Failed Loading", Toast.LENGTH_SHORT).show();
         }
     });*/
    }

    public void setPicToBackground(){


        TextView textResult = (TextView)mLayout.findViewById(mCurrentTextResultShow);
        textResult.setText("");
        textResult.setVisibility(View.GONE);
        ImageView imageView = (ImageView)mLayout.findViewById(mCurrentImgResultShow);
        imageView.setVisibility(View.VISIBLE);

        preDrawPhoto(imageView, mCurrentTab, mCurrentPhotoPath);


    }

    private void validatePairState(){
        boolean valid = false;

        if (validTab(1) && validTab(2) ){
            mCurrentPair.setState(Pair.State.COMPLETED);
        }

    }

    private boolean validTab(int tab){
        boolean valid = false;
        if (mCurrentPair!=null){
            if (mCurrentPair.getTabs()[tab -1]!=null){
                if (mCurrentPair.getTabs()[tab -1].getType()==Tab.Type.TEXT){
                    if (mCurrentPair.getTabs()[tab -1].getText()!=null && !mCurrentPair.getTabs()[tab -1].getText().isEmpty()){
                        valid = true;
                    }
                }
                if (mCurrentPair.getTabs()[tab -1].getType()==Tab.Type.PHOTO){
                    if (!mCurrentPair.getTabs()[tab -1].getUri().isEmpty()){
                        valid = true;
                    }
                }
                if(!valid){
                    mCurrentPair.getTabs()[tab -1].setState(Tab.State.IN_PROCESS);
                }
            }

        }


        return valid;
    }

    public Pair getmCurrentPair() {
        return mCurrentPair;
    }

    public void setmCurrentPair(Pair mCurrentPair) {
        this.mCurrentPair = mCurrentPair;
    }

}
