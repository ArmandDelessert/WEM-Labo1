package ch.heigvd.wem.tools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by P-A Curty on 27.03.17.
 */
public class StringToStringToInt {

    private HashMap<String, StringToInt> hm = new HashMap<>();

    public String toString() {
        return hm.toString();
    }

//    public String toString() {
//        Iterator<Map.Entry<String, StringToInt>> i = hm.entrySet().iterator();
//        if (! i.hasNext())
//            return "";
//
//        StringBuilder sb = new StringBuilder();
//        for (;;) {
//            Map.Entry<String, StringToInt> e = i.next();
//            StringToInt value = e.getValue();
//
//            //sb.append(e.getKey());
//            sb.append(" : ");
//            sb.append(value.toString());
//
//            if (!i.hasNext()) return sb.toString();
//        }
//    }

    public void clear() {
        hm.clear();
    }

    public void increment(String k, String doc, Integer tf) {
        if (hm.containsKey(k)) {
            StringToInt sti = hm.get(k);
            sti.increment(doc, tf);
        }
        else {
            StringToInt tmpSti = new StringToInt();
            tmpSti.increment(doc, tf);
            hm.put(k, tmpSti);
        }
    }

    public void increment(String k, String doc) {
        this.increment(k, doc, 1);
    }

    public void sum(StringToStringToInt h) {
        Iterator<Map.Entry<String, StringToInt>> it = hm.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, StringToInt> pairs = it.next();
            String k = pairs.getKey();
            StringToInt v = pairs.getValue();

            Iterator<Map.Entry<String, Integer>> subIt = v.getHashMap().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Integer> subPairs = subIt.next();
                increment(k, subPairs.getKey(), subPairs.getValue());
            }
        }
    }

    public void sort(String k) {
        if (hm.containsKey(k)) {
            hm.get(k).sortByKey();
        }
    }
}
