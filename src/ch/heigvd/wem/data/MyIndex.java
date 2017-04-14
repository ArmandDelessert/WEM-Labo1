package ch.heigvd.wem.data;

import ch.heigvd.wem.interfaces.Index;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Armand Delessert on 13.03.2017.
 */
public class MyIndex extends Index {

    private static Double LOG2 = Math.log(2);

    private class WStruct implements Serializable {
        public Double weight;
        public Double TFiDF;
        public Double wTFiDF;
        WStruct(){
            this.weight = 0D;
            this.TFiDF = 0D;
            this.wTFiDF = 0D;
        }
    }

    private SortedMap<Long, Metadata> documentsMetadata;
    private SortedMap<Long, Map<String, Integer>> index;
    private SortedMap<String, Map<Long, Integer>> invertedIndex;

    private SortedMap<Long, Integer> wMaximas;
    private SortedMap<Long, Double> TFiDFMaximas;
    private SortedMap<String, Map<Long, WStruct>> ranks;

    /**
     * Constructor
     */
    public MyIndex() {
        documentsMetadata = new TreeMap<>();
        index = new TreeMap<>();
        invertedIndex = new TreeMap<>();
        wMaximas = new TreeMap<>();
        TFiDFMaximas = new TreeMap<>();
        ranks = new TreeMap<>();
    }

    /**
     * PRIVATE METHODS
     */
    private Double log2(Integer n) {
        return Math.log(n) / LOG2;
    }

    private void addMetadata(long docID, Metadata metadata) {
        documentsMetadata.put(docID, metadata);
    }

    private void addToIndex(long docID, Map<String, Integer> wordsAndTheirFrequencies) {
        index.put(docID, wordsAndTheirFrequencies);
    }

    /**
     * Find wMaximas in termFrequencies
     */
    private Integer getMax(long docID) {
        Integer max = 0;
        for (Map.Entry<String, Integer> wordFrequency : this.index.get(docID).entrySet())
            max = (max < wordFrequency.getValue()) ? wordFrequency.getValue() : max;

        return max;
    }

    /**
     * Indexation d'un document.
     * @param metadata
     * @param wordsAndTheirFrequencies
     */
    public void addDocument(Metadata metadata, Map<String, Integer> wordsAndTheirFrequencies) {
        addMetadata(metadata.getDocID(), metadata);
        addToIndex(metadata.getDocID(), wordsAndTheirFrequencies);
        this.wMaximas.put(metadata.getDocID(), getMax(metadata.getDocID()));
    }

    /**
     * Création de l'index inversé.
     * Indexe inversé : Pour chaque mot, une liste des documents dans lesquels le mot apparaît (ainsi que sa fréquence d'apparition) est liée.
     */
    public void createInvertedIndex() {
        for (Map.Entry<Long, Map<String, Integer>> entry : index.entrySet()) {
            for (Map.Entry<String, Integer> wordWithItsFrequency : entry.getValue().entrySet()) {
                // Récupération de l'entrée de l'index inversé (DocID, fréquence) correspondant au mot actuel
                Map<Long, Integer> docIDs = invertedIndex.get(wordWithItsFrequency.getKey());
                if (docIDs == null) {
                    // Ajout du nouveau mot à l'index inversé
                    invertedIndex.put(wordWithItsFrequency.getKey(), new TreeMap<>(Collections.singletonMap(entry.getKey(), wordWithItsFrequency.getValue())));
                    ranks.put(wordWithItsFrequency.getKey(), new TreeMap<>());
                }
                else {
                    // Ajout du DocID à l'index inversé pour le mot actuel
                    docIDs.put(entry.getKey(), wordWithItsFrequency.getValue());
                }
            }
        }
    }


    /**
     * Generators
     */
    public void calcWeights() {
        for (Map.Entry<String, Map<Long, Integer>> words : invertedIndex.entrySet()) {
            for (Map.Entry<Long, Integer> docs : words.getValue().entrySet()) {
                if (!this.ranks.get(words.getKey()).containsKey(docs.getKey()))
                    this.ranks.get(words.getKey()).put(docs.getKey(), new WStruct());

                Integer freq = docs.getValue();
                WStruct weights = this.ranks.get(words.getKey()).get(docs.getKey());
                weights.weight = (double) (freq / this.wMaximas.get(docs.getKey()));
                weights.TFiDF = log2(freq+1) * log2(this.index.size() / words.getValue().size());

                if(!this.TFiDFMaximas.containsKey(docs.getKey()) || weights.TFiDF > this.TFiDFMaximas.get(docs.getKey()))
                    this.TFiDFMaximas.put(docs.getKey(), weights.TFiDF);
            }
        }

        for(Map.Entry<String, Map<Long, WStruct>> words : this.ranks.entrySet()) {
            for(Map.Entry<Long, WStruct> rank : words.getValue().entrySet()) {
                Double maxTFiDF = this.TFiDFMaximas.get(rank.getKey());
                WStruct weights = rank.getValue();
                weights.wTFiDF = weights.TFiDF / maxTFiDF;
            }
        }
    }

    /**
     * Getters
     */
    public SortedMap<Long, Map<String, Integer>> getIndex() {
        return this.index;
    }

    public SortedMap<String, Map<Long, Integer>> getInvertedIndex() {
        return this.invertedIndex;
    }

    public Metadata getMetadata(long docID) {
        return documentsMetadata.get(docID);
    }

    public int getWordOccurrenceInThisDoc(String word, long docID) {
        return invertedIndex.get(word).get(docID);
    }

    public int getWordOccurrenceInAllDocs(String word) {
        int wordOccurrence = 0;
        Map<Long, Integer> map = invertedIndex.get(word);
        if (map != null) {
            for (Map.Entry<Long, Integer> entry : map.entrySet()) {
                wordOccurrence += entry.getValue();
            }
        }
        return wordOccurrence;
    }
}
