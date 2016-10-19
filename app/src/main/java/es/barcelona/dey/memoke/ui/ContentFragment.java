package es.barcelona.dey.memoke.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import es.barcelona.dey.memoke.R;
import es.barcelona.dey.memoke.beans.Pair;
import es.barcelona.dey.memoke.beans.Tab;
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
            inicializeFragment();
        }

        return mLayout;
    }

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

        contentPresenter.onViewCreated(mFrameTab1,mFrameTab2, savedInstanceState);

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

        contentPresenter.fillImageWithTab1();
        contentPresenter.fillImageWithTab2();

    }



    public void openDialogText(Pair currentPair, int idCurrentTab){
        DialogText textDialog = new DialogText((CreationActivity) getActivity());
        textDialog.setTextFromFragment(currentPair.getTabs()[idCurrentTab].getText());
        textDialog.setTextSizeFromFragment(currentPair.getTabs()[idCurrentTab].getSize());

        textDialog.show();
    }

    public void openEmptyDialogText(){
        DialogText textDialog = new DialogText((CreationActivity) getActivity());

        textDialog.show();
    }

    public void openDialogPhoto(){
        DialogPhoto cdd = new DialogPhoto((CreationActivity) getActivity());
        cdd.show();
    }

    public void addingOnPreDrawListener(ImageView imageViewTmp, String uriTemp, int tabTmp){
        ContentPresenter.finalHeight = imageViewTmp.getMeasuredHeight();
        ContentPresenter.finalWidth = imageViewTmp.getMeasuredWidth();

        int tempImg = contentPresenter.getmCurrentImgResultShow();
        String tempPhoto = contentPresenter.getmCurrentPhotoPath();
        int tempTab = contentPresenter.getmCurrentTab();

        contentPresenter.setmCurrentImgResultShow(imageViewTmp.getId());
        contentPresenter.setmCurrentPhotoPath(uriTemp);
        contentPresenter.setmCurrentTab(tabTmp);
        setPicToImg(imageViewTmp, ContentPresenter.finalHeight, ContentPresenter.finalWidth);

        contentPresenter. setmCurrentImgResultShow(tempImg);
        contentPresenter.setmCurrentPhotoPath(tempPhoto);
        contentPresenter.setmCurrentTab(tempTab);
    }

    public void showNextButton(){
        Button b = (Button) getActivity().findViewById(R.id.btnSgte);
        b.setVisibility(View.VISIBLE);
    }

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


    /*  Métodos internos para interaccion entre los objetos del fragment   */

    public void instancePresenter(){
        if (null==contentPresenter){
            contentPresenter = new ContentPresenter();
            contentPresenter.setView(ContentFragment.this);
        }
    }

    @Override
    public String getCurrentPairFromContext(Bundle savedInstanceState){
        String jsonCurrentPair = null;
        boolean existDataInInstance = savedInstanceState!=null;
        if (existDataInInstance || existCurrentPairFromArguments()) {
            if (null != getArguments() && null == savedInstanceState) {
                savedInstanceState = getArguments();
                existDataInInstance = true;
            }
            if (existDataInInstance) {
                jsonCurrentPair = savedInstanceState.getString(CreationPresenter.PARAM_CURRENT_PAIR);
            }

        }

        return jsonCurrentPair;
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
        preDrawPhoto(mImageView1,1,contentPresenter.getmCurrentPair().getTabs()[0].getUri());
    }
    @Override
    public void preDrawPhoto2(){
        mTextView2.setVisibility(View.GONE);

        preDrawPhoto(mImageView2,2,contentPresenter.getmCurrentPair().getTabs()[1].getUri());
    }

    private void preDrawPhoto(ImageView imageView, int currentTabTemp, String uri){
        final ImageView imageViewTmp = imageView;
        final int tabTmp = currentTabTemp;
        final String uriTemp = uri;
        ViewTreeObserver vto = imageViewTmp.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                imageViewTmp.getViewTreeObserver().removeOnPreDrawListener(this);

                addingOnPreDrawListener(imageViewTmp,  uriTemp, tabTmp);

                return true;
            }
        });
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

    public void setPicToImg(ImageView img, int height, int width){

        Picasso.with(getActivity()).load(contentPresenter.getmCurrentPhotoPath())
                .resize(height, width)
                .centerCrop().into(img);
        contentPresenter.getmCurrentPair().getTabs()[contentPresenter.getmCurrentTab() - 1].setUri(contentPresenter.getmCurrentPhotoPath());
        contentPresenter.manageVisibilityNextButton();

    }

    private boolean existCurrentPairFromArguments(){
        return getArguments()!=null && getArguments().getString(CreationPresenter.PARAM_CURRENT_PAIR)!=null;
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

    @Override
    public void openingCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean existCameraToHandleIntent = takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null;
        contentPresenter.openingCameraIfExist(existCameraToHandleIntent);

    }

    @Override
    public void manageIntent(File photoFile){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photoFile));

        startActivityForResult(takePictureIntent, ContentPresenter.REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void openingGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        startActivityForResult(intent, ContentPresenter.REQUEST_SELECT_PICTURE);
    }



    @Override
    public void setPicToBackground(){

        TextView textResult = (TextView)mLayout.findViewById(contentPresenter.getmCurrentTextResultShow());
        textResult.setText("");
        textResult.setVisibility(View.GONE);
        ImageView imageView = (ImageView)mLayout.findViewById(contentPresenter.getmCurrentImgResultShow());
        imageView.setVisibility(View.VISIBLE);

        preDrawPhoto(imageView, contentPresenter.getmCurrentTab(), contentPresenter.getmCurrentPhotoPath());

    }

    public ContentPresenter getContentPresenter() {
        return contentPresenter;
    }
}
