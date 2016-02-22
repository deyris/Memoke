package es.barcelona.dey.memoke.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.List;

import es.barcelona.dey.memoke.beans.Pair;
import es.barcelona.dey.memoke.beans.Tab;

/**
 * Created by deyris.drake on 23/2/16.
 */
public class TabDatabase {

    private static final Gson GSON = new Gson();

    private static final String PREF_PAIR_LIST = "prefDronesList";
    private static final String PREF_PAIR_SELECTED = "prefDroneSelected";

    private static List<Pair> pairs = null;

    private static Pair selectedPair = null;

    public static Pair getSelectedDrone(Context context) {
        if (selectedPair == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String value = preferences.getString(PREF_PAIR_SELECTED, null);
            if (value != null) {
                selectedPair = GSON.fromJson(value, Pair.class);
            } else {
                selectedPair = null;
            }
        }
        return selectedPair;
    }




}
