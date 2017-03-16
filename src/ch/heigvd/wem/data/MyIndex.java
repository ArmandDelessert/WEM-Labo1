package ch.heigvd.wem.data;

import ch.heigvd.wem.interfaces.Index;

import java.util.HashMap;

/**
 * Created by Armand Delessert on 13.03.2017.
 */
public class MyIndex extends Index {

    private HashMap<Long, Metadata> metadataHashMap;

    public MyIndex() {
        metadataHashMap = new HashMap<>();
    }

    public void addMetadata(long docID, Metadata metadata) {
        metadataHashMap.put(docID, metadata);
    }

    public Metadata getMetadata(long docID) {
        return metadataHashMap.get(docID);
    }
}
