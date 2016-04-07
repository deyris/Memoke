package es.barcelona.dey.memoke.services;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.beans.Pair;
import es.barcelona.dey.memoke.beans.Tab;
import es.barcelona.dey.memoke.beans.TabForPlay;

/**
 * Created by deyris.drake on 7/4/16.
 */
public class PlayServices {

    public TabForPlay[] getTabsForPlay(Board board){
        ArrayList<TabForPlay> tabs = new ArrayList<TabForPlay>();
        Iterator it = board.getPairs().keySet().iterator();
        while (it.hasNext()){
            Pair pair = board.getPairs().get(it.next());
            for(Tab tab: pair.getTabs()){
                TabForPlay tabForPlay = new TabForPlay(tab);
                tabForPlay.setNumberOfPair(pair.getNumber());
                tabs.add(tabForPlay);
            }
        }
        //Desordenamos
        Collections.shuffle(tabs);
        TabForPlay[] result = new TabForPlay[tabs.size()];
        return tabs.toArray(result);
    }
}
