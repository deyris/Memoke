package es.barcelona.dey.memoke.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import es.barcelona.dey.memoke.beans.Board;
import es.barcelona.dey.memoke.beans.Pair;
import es.barcelona.dey.memoke.beans.Tab;
import es.barcelona.dey.memoke.beans.TabForGame;

/**
 * Created by deyris.drake on 7/4/16.
 */
public class PlayServices {

    public TabForGame[] getTabsForPlay(Board board){
        ArrayList<TabForGame> tabs = new ArrayList<TabForGame>();
        Iterator it = board.getPairs().keySet().iterator();
        while (it.hasNext()){
            Pair pair = board.getPairs().get(it.next());
            for(Tab tab: pair.getTabs()){
                TabForGame tabForGame = new TabForGame(tab);
                tabForGame.setNumberOfPair(pair.getNumber());
                tabs.add(tabForGame);
            }
        }
        //Desordenamos
        Collections.shuffle(tabs);
        TabForGame[] result = new TabForGame[tabs.size()];
        return tabs.toArray(result);
    }
}
