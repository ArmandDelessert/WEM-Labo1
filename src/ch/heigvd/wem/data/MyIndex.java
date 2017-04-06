package ch.heigvd.wem.data;

import ch.heigvd.wem.interfaces.Index;

import java.util.*;

/**
 * Created by Armand Delessert on 13.03.2017.
 */
public class MyIndex extends Index {

    private SortedMap<Long, Metadata> documentsMetadata;
    private SortedMap<Long, Map<String, Integer>> index;
    private SortedMap<String, Map<Long, Integer>> invertedIndex;

    public MyIndex() {
        documentsMetadata = new TreeMap<>();
        index = new TreeMap<>();
        invertedIndex = new TreeMap<>();
    }

    /**
     * Indexation d'un document.
     * @param metadata
     * @param wordsAndTheirFrequencies
     */
    public void addDocument(Metadata metadata, Map<String, Integer> wordsAndTheirFrequencies) {
        addMetadata(metadata.getDocID(), metadata);
        addToIndex(metadata.getDocID(), wordsAndTheirFrequencies);
    }

    private void addMetadata(long docID, Metadata metadata) {
        documentsMetadata.put(docID, metadata);
    }

    private void addToIndex(long docID, Map<String, Integer> wordsAndTheirFrequencies) {
        index.put(docID, wordsAndTheirFrequencies);
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
                }
                else {
                    // Ajout du DocID à l'index inversé pour le mot actuel
                    docIDs.put(entry.getKey(), wordWithItsFrequency.getValue());
                }
            }
        }
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
