package ch.heigvd.wem;

import ch.heigvd.wem.data.Metadata;
import ch.heigvd.wem.data.MyIndex;
import ch.heigvd.wem.interfaces.Index;
import ch.heigvd.wem.interfaces.Indexer;

/**
 * Created by Armand Delessert on 13.03.2017.
 */
public class MyIndexer implements Indexer {

    private int counter;
    private MyIndex index;

    public MyIndexer() {
        counter = 0;
        index = new MyIndex();
    }

    @Override
    public void index(Metadata metadata, String content) {
        index.addMetadata(metadata.getDocID(), metadata);
        counter++;
    }

    @Override
    public void finalizeIndexation() {

    }

    @Override
    public Index getIndex() {
        return index;
    }
}
