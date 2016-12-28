package es.barcelona.dey.memoke.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.squareup.picasso.Target;

import java.io.File;
import java.security.PrivateKey;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.barcelona.dey.memoke.R;
import es.barcelona.dey.memoke.beans.Pair;
import es.barcelona.dey.memoke.beans.Tab;
import es.barcelona.dey.memoke.clients.MemokeApp;
import es.barcelona.dey.memoke.interactors.ContentInteractor;
import es.barcelona.dey.memoke.presenters.ContentPresenter;
import es.barcelona.dey.memoke.presenters.CreationPresenter;
import es.barcelona.dey.memoke.views.ContentView;

/**
 * Created by deyris.drake on 18/2/16.
 */
public class ContentFragment extends Fragment implements ContentView{

    public static final String TAG = "MMKContentFragment";

    LinearLayout mLayout;

    FrameLayout mFrameTab1;

    FrameLayout mFrameTab2;


    TextView  mTextView1;
    TextView  mTextView2;

    ImageView  mImageView1;
    ImageView  mImageView2;

    private FragmentIterationListener mCallback = null;
    public ContentPresenter contentPresenter;

    @Override
    public Context getContext(){
        return this.getActivity();
    }


    String[] perms = {"android.permission.CAMERA","android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};

    int permsRequestCode = 200;



    @Override

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 200:

                boolean cameraAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                if (!cameraAccepted){
                    hasPermission("android.permission.CAMERA");
                }
                boolean storageReadAccepted = grantResults[1]==PackageManager.PERMISSION_GRANTED;
                if (!storageReadAccepted){
                    hasPermission("android.permission.READ_EXTERNAL_STORAGE");
                }
                boolean storageWriteAccepted = grantResults[2]==PackageManager.PERMISSION_GRANTED;
                if (!storageWriteAccepted){
                    hasPermission("android.permission.WRITE_EXTERNAL_STORAGE");
                }
                break;


        }

    }

    private boolean hasPermission(String permission){

        if(MemokeApp.canMakeSmores()){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return(getActivity().checkSelfPermission(permission)==PackageManager.PERMISSION_GRANTED);
            }

        }

        return true;

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


    //El Activity que contiene el Fragment ha terminado su creación
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (MemokeApp.canMakeSmores()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(perms, permsRequestCode);
            }
        }
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
            inicializeFragment();
        }

        return mLayout;
    }

    @Override
    public void inicializeFragment(){
        mFrameTab1 = (FrameLayout) mLayout.findViewById(R.id.editContent1);
        mFrameTab2 = (FrameLayout) mLayout.findViewById(R.id.editContent2);


        mTextView1 = (TextView) mLayout.findViewById(R.id.txtContent1);
        mTextView2 = (TextView) mLayout.findViewById(R.id.txtContent2);

        mImageView1 = (ImageView) mLayout.findViewById(R.id.imgContent1);
        mImageView2 = (ImageView) mLayout.findViewById(R.id.imgContent2);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListenerFrame(mFrameTab1,1);
        setListenerFrame(mFrameTab2,2);

        contentPresenter.onViewCreated(savedInstanceState, getArguments());

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        instancePresenter();
        contentPresenter.onSavingInstanceState(outState);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //Salvamos en fichero
       // TabDatabase.addPair(this.getActivity(), idCurrentPair);
    }



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

        contentPresenter.fillImageWithTab1();
        contentPresenter.fillImageWithTab2();

    }


    @Override
    public void openDialogText(Pair currentPair, int idCurrentTab){
        DialogText textDialog = new DialogText((CreationActivity) getActivity());
        textDialog.setTextFromFragment(currentPair.getTabs()[idCurrentTab].getText());
        textDialog.setTextSizeFromFragment(currentPair.getTabs()[idCurrentTab].getSize());

        textDialog.show();
    }

    @Override
    public void openEmptyDialogText(){
        DialogText textDialog = new DialogText((CreationActivity) getActivity());

        textDialog.show();
    }

    @Override
    public void openDialogPhoto(){
        DialogPhoto cdd = new DialogPhoto((CreationActivity) getActivity());
        cdd.show();
    }


    @Override
    public void showNextButton(){
        Button b = (Button) getActivity().findViewById(R.id.btnSgte);
        b.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideNextButton(){
        Button b = (Button) getActivity().findViewById(R.id.btnSgte);
        b.setVisibility(View.GONE);
    }

    @Override
    public void showAntButton(){
        Button btnAnt = (Button) getActivity().findViewById(R.id.btnAnt);
        btnAnt.setVisibility(View.VISIBLE);

    }
    @Override
    public void hideAntButton(){
        Button btnAnt = (Button) getActivity().findViewById(R.id.btnAnt);
        btnAnt.setVisibility(View.GONE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       contentPresenter.onActivityResult(requestCode,data);


    }



    @Override
    public void fillNumberInCurrentPairByArguments(){
        contentPresenter.createNewPairInCurrentPair(getArguments());

    }

    @Override
    public void fillResultWithCurrent(int idText, int tab, ImageView imgToHide1){
        final  ImageView imgToHide = imgToHide1;
        contentPresenter.onFillResultWithCurrent(imgToHide.getId(), tab, idText);

    }

    @Override
    public void preDrawPhoto1(){
        mTextView1.setVisibility(View.GONE);
        preDrawPhoto(mImageView1,contentPresenter.getmCurrentPair().getTabs()[0].getUri());
        actualiceCurrentTabValues(mImageView1, 1, contentPresenter.getmCurrentPair().getTabs()[0].getUri());
    }
    @Override
    public void preDrawPhoto2(){
        mTextView2.setVisibility(View.GONE);

        preDrawPhoto(mImageView2,contentPresenter.getmCurrentPair().getTabs()[1].getUri());
        actualiceCurrentTabValues(mImageView2,2,contentPresenter.getmCurrentPair().getTabs()[1].getUri());
    }


    @Override
    public  void setListenerFrame(FrameLayout frame, int tab) {

        contentPresenter.markCurrentView(frame,tab);
        frame.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Un texto", "Una foto", "Una figura"};
                //Actualizamos estado de la ficha, siempre que se clicke en el frame, pasa a IN PROGRESS
                contentPresenter.putTabIN_PROCESS(contentPresenter.getmCurrentPair());

                //Determinamos en que ficha estamos basándonos en la marca puesta por el presenter
                contentPresenter.setmCurrentTab(contentPresenter.getMarkOfCurrentView(v));


                //Determinamos donde mostrar el resultado
                FrameLayout thisFrame = (FrameLayout)mLayout.findViewById(v.getId());
                contentPresenter.setmCurrentFrame(thisFrame.getId());
                contentPresenter.setmCurrentTextResultShow(thisFrame.getChildAt(0).getId());
                contentPresenter.setmCurrentImgResultShow(thisFrame.getChildAt(1).getId());

                //Abrimos un dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ContentFragment.this.getActivity());
                builder.setTitle("¿Cómo será esta ficha?");

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        contentPresenter.whatDoWithTheSelectionOfFrame(item);
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    @Override
    public void openingCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean existCameraToHandleIntent = takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null;
        contentPresenter.openingCameraIfExist(existCameraToHandleIntent);

    }

    @Override
    public void manageIntent(File photoFile){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

       /*takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photoFile));*/


       startActivityForResult(takePictureIntent,ContentPresenter.REQUEST_IMAGE_CAPTURE);

    }


    @Override
    public void openingGallery(){

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ContentPresenter.REQUEST_SELECT_PICTURE);
    }

    @Override
    public void setPicToBackground(){

        TextView textResult = (TextView)mLayout.findViewById(contentPresenter.getmCurrentTextResultShow());
        textResult.setText("");
        textResult.setVisibility(View.GONE);
        ImageView imageView = (ImageView)mLayout.findViewById(contentPresenter.getmCurrentImgResultShow());
        imageView.setVisibility(View.VISIBLE);

        preDrawPhoto(imageView, contentPresenter.getmCurrentPhotoPath());
        actualiceCurrentTabValues(imageView, contentPresenter.getmCurrentTab(), contentPresenter.getmCurrentPhotoPath());
    }

    public void receivingFromDialog(int data){
        contentPresenter.onReceiveFromDialog(data);
    }

    public void receivingFromDialog(EditText data){

        ImageView imageView = (ImageView)mLayout.findViewById(contentPresenter.getmCurrentImgResultShow());
        imageView.setBackground(null);
        imageView.setVisibility(View.GONE);

        //Actualizamos textView
        TextView textView = (TextView)mLayout.findViewById(contentPresenter.getmCurrentTextResultShow());
        textView.setVisibility(View.VISIBLE);
        textView.setText("");
        textView.setText(data.getText());
        textView.setTextSize(data.getTextSize() / 2);

        //Actualizamos currentPair
        contentPresenter.getmCurrentPair().getTabs()[contentPresenter.getmCurrentTab() - 1].setText(data.getText().toString());
        contentPresenter.getmCurrentPair().getTabs()[contentPresenter.getmCurrentTab() - 1].setSize((int) data.getTextSize());

        contentPresenter.manageVisibilityNextButton();
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

    private boolean existCurrentPairFromArguments(){
        return getArguments()!=null && getArguments().getString(CreationPresenter.PARAM_CURRENT_PAIR)!=null;
    }


    private void preDrawPhoto(ImageView imageView,String uri){

        Picasso.Builder builder = new Picasso.Builder(getActivity().getApplicationContext());
        builder.listener(new Picasso.Listener() {
                             @Override
                             public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                                 exception.printStackTrace();
                             };});

        Picasso.with(getActivity()).load(new File(uri))
                .fit()
                .centerCrop().into(imageView);

    }

    private void actualiceCurrentTabValues(ImageView imageView, int currentTabTemp, String uri) {
        contentPresenter.setmCurrentImgResultShow(imageView.getId());
        contentPresenter.setmCurrentPhotoPath(uri);
        contentPresenter.setmCurrentTab(currentTabTemp);

        contentPresenter.getmCurrentPair().getTabs()[contentPresenter.getmCurrentTab() - 1].setUri(contentPresenter.getmCurrentPhotoPath());


        contentPresenter.manageVisibilityNextButton();
    }

    private void instancePresenter(){
        if (null==contentPresenter){
            contentPresenter = new ContentPresenter(new ContentInteractor(getContext().getApplicationContext()));
            contentPresenter.setView(ContentFragment.this);
        }
    }

   /* private void addingOnPreDrawListener(ImageView imageViewTmp, String uriTemp, int tabTmp){
        ContentPresenter.finalHeight = imageViewTmp.getMeasuredHeight();
        ContentPresenter.finalWidth = imageViewTmp.getMeasuredWidth();

        int tempImg = contentPresenter.getmCurrentImgResultShow();
        String tempPhoto = contentPresenter.getmCurrentPhotoPath();
        int tempTab = contentPresenter.getmCurrentTab();

        contentPresenter.setmCurrentImgResultShow(imageViewTmp.getId());
        contentPresenter.setmCurrentPhotoPath(uriTemp);
        contentPresenter.setmCurrentTab(tabTmp);

        setPicToImg(imageViewTmp, ContentPresenter.finalHeight, ContentPresenter.finalWidth);

        if (contentPresenter.getmCurrentPair().getTabs()[contentPresenter.getmCurrentTab() - 1]==null){
            contentPresenter.getmCurrentPair().getTabs()[contentPresenter.getmCurrentTab() - 1]=new Tab();

        }

        contentPresenter.getmCurrentPair().getTabs()[contentPresenter.getmCurrentTab() - 1].setUri(contentPresenter.getmCurrentPhotoPath());
        contentPresenter.manageVisibilityNextButton();

        contentPresenter. setmCurrentImgResultShow(tempImg);
        contentPresenter.setmCurrentPhotoPath(tempPhoto);
        contentPresenter.setmCurrentTab(tempTab);
    }*/


    public ContentPresenter getContentPresenter() {
        return contentPresenter;
    }


}
