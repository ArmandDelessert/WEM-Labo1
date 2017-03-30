package ch.heigvd.wem.tools;

import java.util.*;
import java.util.Map.Entry;

/**
 * Very simple (and scholastic) implementation of a Writable associative array for String to Int.
 */
public class StringToInt {
	
	private HashMap<String, Integer> hm = new HashMap<>();

	public void clear() {
		hm.clear();
	}

	public HashMap<String, Integer> getHashMap() {
		return this.hm;
	}

	public String toString() {
		return hm.toString();
	}

//	public String toString() {
//		Iterator<Entry<String, Integer>> i = hm.entrySet().iterator();
//		if (! i.hasNext())
//			return "()";
//
//		StringBuilder sb = new StringBuilder();
//		sb.append(hm.size() + " : ");
//		for (;;) {
//			Entry<String, Integer> e = i.next();
//            String key = e.getKey();
//            Integer value = e.getValue();
//
//			sb.append('(');
//			sb.append(key);
//			sb.append(", ");
//			sb.append(value);
//			sb.append(')');
//
//			if (i.hasNext()) sb.append(", ");
//			else return sb.toString();
//		}
//	}

	public void increment(String t) {
		int count = 1;
		if (hm.containsKey(t)) {
			count = hm.get(t) + count;
		}
		hm.put(t, count);
	}

	public void increment(String t, Integer value) {
		int count = value;
		if (hm.containsKey(t)) {
            count = hm.get(t) + count;
		}
        hm.put(t, count);
	}

	public void sum(StringToInt h) {
		Iterator<Entry<String, Integer>> it = h.hm.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Integer> pairs = it.next();
			increment(pairs.getKey(), pairs.getValue());
		}
	}

    public void sortByKey() {
        LinkedList<Entry<String, Integer>> list = new LinkedList<>( hm.entrySet() );
        Collections.sort( list, (o1, o2) -> ( o1.getKey() ).compareTo( o2.getKey() ));

        HashMap<String, Integer> result = new HashMap<>();
        for (Entry<String, Integer> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        hm = result;
    }
}
