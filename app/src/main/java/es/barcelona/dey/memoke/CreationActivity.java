package es.barcelona.dey.memoke;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

/**
 * Created by deyris.drake on 24/1/16.
 */
public class CreationActivity extends AppCompatActivity implements ContentFragment.OnDataPass{

    private CreationFragment mCreationFragment;
    private ContentFragment mContentFragment;
    public static int mCurrentPair = 1;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getFragmentManager().putFragment(outState, "contentfragment", mContentFragment);
    }


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

       if  (savedInstanceState!=null) {
           mContentFragment = (ContentFragment) getFragmentManager().getFragment(savedInstanceState, "contentfragment");

       } else {
           mContentFragment = new ContentFragment();

        // Get a reference to the FragmentManager
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        FragmentManager fragmentManager = getFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putInt("CURRENT_PAIR",mCurrentPair);

        mCreationFragment = new CreationFragment();
        mCreationFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.header_frame, mCreationFragment);


        fragmentTransaction.replace(R.id.content_frame, mContentFragment);
        fragmentTransaction.commit();

        }
    }


    @Override
    public void onDataPass(int data) {
        mContentFragment.receivingFromDialog(data);
    }

    @Override
    public void onDataPass(EditText data) {
        mContentFragment.receivingFromDialog(data);
    }




}
