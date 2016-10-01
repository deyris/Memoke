package es.barcelona.dey.memoke.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.barcelona.dey.memoke.R;
import es.barcelona.dey.memoke.beans.Pair;
import es.barcelona.dey.memoke.beans.Tab;
import es.barcelona.dey.memoke.presenters.ContentPresenter;
import es.barcelona.dey.memoke.views.ContentView;

/**
 * Created by deyris.drake on 18/2/16.
 */
public class ContentFragment extends Fragment implements ContentView{

    public static final String TAG = "MMKContentFragment";

    LinearLayout mLayout;

    FrameLayout mFrameTab1;

    FrameLayout mFrameTab2;

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
    public ContentPresenter contentPresenter;

    @Override
    public Context getContext(){
        return this.getActivity();
    }


    public interface OnDataPass {
        public void onDataPass(int data);
        public void onDataPass(EditText data);

    }

    public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }

    public static ContentFragment newInstance(Bundle arguments){
        ContentFragment f = new ContentFragment();

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

        try{
            mCallback = (FragmentIterationListener) getActivity();
        }catch(Exception ex){

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

        instancePresenter();

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

        setListenerFrame(mFrameTab1,1);
        setListenerFrame(mFrameTab2,2);

        String jsonCurrentPair = getCurrentPairFromContext(savedInstanceState);

        if(null!=jsonCurrentPair) {
            mCurrentPair = contentPresenter.getCurrentPair(jsonCurrentPair);
            contentPresenter.fillPairOnView(jsonCurrentPair);

        }

        fillNumberInCurrentPair();

        //Comprobamos botones de Anterior y Siguiente
       contentPresenter.controlButtonsAntSgte();
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Serializamos nuestro currentPair
        if (null!=mCurrentPair && mCurrentPair.getState()!=Pair.State.EMPTY) {
            instancePresenter();
            String jsonCurrentPair = contentPresenter.getJsonCurrentPair(mCurrentPair);
            outState.putString(CreationActivity.PARAM_CURRENT_PAIR, jsonCurrentPair);

        }

    }



    @Override
    public void onDestroy(){
        super.onDestroy();
        //Salvamos en fichero
       // TabDatabase.addPair(this.getActivity(), idCurrentPair);
    }

     /* Métodos Override de la view*/

    @Override
    public void fillFirstTab(){
        fillHandlerWithTextAndHideImg(mTextView1.getId(), 1, mImageView1);

    }
    @Override
    public void fillSecondTab(){
        fillHandlerWithTextAndHideImg(mTextView2.getId(), 2, mImageView2);

    }



    @Override
    public void fillImgsWithCurrent(){

        fillImageWithTab(1,mTextView1,mImageView1);
        fillImageWithTab(2,mTextView2,mImageView2);
        /*if (mCurrentPair!=null && mCurrentPair.getTabs()[0]!=null) {

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
        }*/

    }

    @Override
    public  void showContinueButton(){
        if (null!=mCurrentPair) {
            validatePairState();
            Button b = (Button) getActivity().findViewById(R.id.btnSgte);
            if (mCurrentPair.getState().equals(Pair.State.COMPLETED) || mCurrentPair.getState().equals(Pair.State.SAVED)) {
                b.setVisibility(View.VISIBLE);
            } else {
                b.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void showAntButton(){
        if (null!=mCurrentPair) {
            Button btnAnt = (Button) getActivity().findViewById(R.id.btnAnt);
            if (mCurrentPair.getNumber() > 1) {
                btnAnt.setVisibility(View.VISIBLE);
            } else {
                btnAnt.setVisibility(View.GONE);
            }
        }
    }



    @Override
    public void fillTextInTab(int idText, String val, int size){
        final TextView mText = (TextView) mLayout.findViewById(idText);

        mText.setText("");
        mText.setText(val);
        mText.setTextSize(size);

    }

    @Override
    public void hideImageInTab(int idText, int idImg){
        final TextView mText = (TextView) mLayout.findViewById(idText);
        final ImageView imgToHide = (ImageView) mLayout.findViewById(idImg);

        imgToHide.setVisibility(View.GONE);
        imgToHide.setBackground(null);

        mText.setVisibility(View.VISIBLE);
    }




    /*Metodos internos para interaccion entre los objetos del fragment*/

    public void instancePresenter(){
        if (null==contentPresenter){
            contentPresenter = new ContentPresenter();
            contentPresenter.setView(ContentFragment.this);
        }
    }

    private String getCurrentPairFromContext(Bundle savedInstanceState){
        String jsonCurrentPair = null;

        if (savedInstanceState!=null || existCurrentPairFromArguments()) {
            if (null != getArguments() && null == savedInstanceState) {
                savedInstanceState = getArguments();

            }
            if (savedInstanceState != null) {
                jsonCurrentPair = savedInstanceState.getString(CreationActivity.PARAM_CURRENT_PAIR);
            }

        }

        return jsonCurrentPair;
    }

    public void fillHandlerWithTextAndHideImg(int textId, int position, ImageView imageView){
        final Handler handler = new Handler();
        final int finalTextId = textId;
        final int finalPosition = position;
        final ImageView finalImageView = imageView;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fillResultWithCurrent(finalTextId, finalPosition, finalImageView);

            }
        }, 500); // after 0.5 sec
    }

    public void fillNumberInCurrentPair(){
        if (getArguments()!=null && getArguments().getInt(CreationActivity.PARAM_CURRENT_PAIR_NUMBER)!=0) {
            int currentPair = getArguments().getInt(CreationActivity.PARAM_CURRENT_PAIR_NUMBER);
            if (null==mCurrentPair || mCurrentPair.getNumber() < currentPair){
                mCurrentPair = new Pair();
            }
            mCurrentPair.setNumber(getArguments().getInt(CreationActivity.PARAM_CURRENT_PAIR_NUMBER));
        }
    }

    private void fillResultWithCurrent(int idText, int tab, ImageView imgToHide1){
        final  ImageView imgToHide = imgToHide1;
        final TextView mText = (TextView) mLayout.findViewById(idText);
        if (mCurrentPair!=null && mCurrentPair.getTabs()[tab - 1] != null) {

            if (mCurrentPair.getTabs()[tab -1].getType()==Tab.Type.TEXT) {
                //Ocultar foto de ese frame
                contentPresenter.hideImageInTab(idText,imgToHide.getId());

                if (contentPresenter.existTextToShowInView(mCurrentPair,tab)) {
                    contentPresenter.fillTextInTab(mCurrentPair,tab, idText);

                }

            }

        }
    }

    private void fillImageWithTab(int tab, TextView textview, ImageView imageView){
        if (mCurrentPair!=null && mCurrentPair.getTabs()[tab - 1]!=null) {

            if (mCurrentPair.getTabs()[tab - 1].getType() == Tab.Type.PHOTO) {
                textview.setVisibility(View.GONE);
                preDrawPhoto(imageView,tab,mCurrentPair.getTabs()[tab - 1].getUri());
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

    private boolean existCurrentPairFromArguments(){
        return getArguments()!=null && getArguments().getString(CreationActivity.PARAM_CURRENT_PAIR)!=null;
    }

    public  void setListenerFrame(FrameLayout frame, int tab) {

        contentPresenter.markCurrentView(frame,tab);
        frame.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Un texto", "Una foto", "Una figura"};
                //Actualizamos estado de la ficha, siempre que se clicke en el frame, pasa a IN PROGRESS
                contentPresenter.putTabIN_PROCESS(mCurrentPair);

                //Determinamos en que ficha estamos basándonos en la marca puesta por el presenter
                mCurrentTab = contentPresenter.getMarkOfCurrentView(v);


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
                        mCurrentPair.getTabs()[mCurrentTab - 1].setType(contentPresenter.getTypeById(item));
                        initChargeTab();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    private void initChargeTab(){

        Dialog dialog = contentPresenter.showDialogFromFrame(this.mCurrentPair,mCurrentTab-1,(CreationActivity) getActivity());
        dialog.show();

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

        ImageView imageView = (ImageView)mLayout.findViewById(mCurrentImgResultShow);
        imageView.setBackground(null);
        imageView.setVisibility(View.GONE);

        //Actualizamos textView
        TextView textView = (TextView)mLayout.findViewById(mCurrentTextResultShow);
        textView.setVisibility(View.VISIBLE);
        textView.setText("");
        textView.setText(data.getText());
        textView.setTextSize(data.getTextSize() / 2);

        //Actualizamos currentPair
        mCurrentPair.getTabs()[mCurrentTab - 1].setText(data.getText().toString());
        mCurrentPair.getTabs()[mCurrentTab - 1].setSize((int) data.getTextSize());

        showContinueButton();
    }


    private void openingCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = contentPresenter.createFileFromPhoto();

            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = "file:" + photoFile.getAbsolutePath();


           /* try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }*/
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQUEST_IMAGE_CAPTURE){
            setPicToBackground();
        }else if (requestCode==REQUEST_SELECT_PICTURE){
            if (null!=data && null!=data.getData()) {
                Uri selectedImage = data.getData();
                mCurrentPhotoPath = selectedImage.toString();
                setPicToBackground();
            }
        }


    }

    public void setPicToImg(ImageView img, int height, int width){

     Picasso.with(getActivity()).load(mCurrentPhotoPath)
                .resize(height, width)
             .centerCrop().into(img);
        mCurrentPair.getTabs()[mCurrentTab - 1].setUri(mCurrentPhotoPath);
        showContinueButton();

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
        if (null!=mCurrentPair && !mCurrentPair.getState().equals(Pair.State.EMPTY)) {
            if (mCurrentPair.getState() != Pair.State.SAVED &&
                    (validTab(1) && validTab(2))) {
                mCurrentPair.setState(Pair.State.COMPLETED);
            }
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
                    if (null!= mCurrentPair.getTabs()[tab -1].getUri() && !mCurrentPair.getTabs()[tab -1].getUri().isEmpty()){
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
