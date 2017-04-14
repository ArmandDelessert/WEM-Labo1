package ch.heigvd.wem;

import ch.heigvd.wem.interfaces.Index;
import ch.heigvd.wem.interfaces.Retriever;

import java.util.Map;

/**
 * Created by Armand Delessert on 06.04.2017.
 */
public class MyRetriever extends Retriever {
    public MyRetriever(Index index, WeightingType weightingType) {
        super(index, weightingType);
    }

    @Override
    public Map<String, Double> searchDocument(Integer docmentId) {
        return null;
    }

    @Override
    public Map<Long, Double> searchTerm(String term) {
        return null;
    }

    @Override
    public Map<Long, Double> executeQuery(String query) {
        return null;
    }
}
