package es.barcelona.dey.memoke;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import es.barcelona.dey.memoke.beans.Pair;

/**
 * Created by deyris.drake on 24/1/16.
 */
public class CreationActivity extends AppCompatActivity implements ContentFragment.OnDataPass{

    private CreationFragment mCreationFragment;
    static ContentFragment mContentFragment;
    public static int mCurrentPair = 1;


    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("DEY", "Estoy en CreationActivity.onDestroy");
    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);
            Log.d("DEY", "Estoy en CreationActivity.onCreate");



        // Get a reference to the FragmentManager
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        FragmentManager fragmentManager = getFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putInt("CURRENT_PAIR", mCurrentPair);

        mCreationFragment = new CreationFragment();
        mCreationFragment.setArguments(bundle);

            fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.header_frame, mCreationFragment);

if (mContentFragment==null) {
    Log.d("AHORA", "Estoy entrando a agregar el fragment");
    mContentFragment = ContentFragment.newInstance(savedInstanceState);
    fragmentTransaction.add(R.id.content_frame, mContentFragment);
    fragmentTransaction.commit();
}else{
    Log.d("AHORA", "Tengo fragment porque no es null");

}




    }



    @Override
    public void onDataPass(int data) {
        if (mContentFragment!=null) {
            mContentFragment.receivingFromDialog(data);
        }
    }

    @Override
    public void onDataPass(EditText data) {
        if (mContentFragment!=null) {
            mContentFragment.receivingFromDialog(data);
        }
    }




}
