package ch.heigvd.wem.data;

import ch.heigvd.wem.interfaces.Index;

import java.util.*;

/**
 * Created by Armand Delessert on 13.03.2017.
 */
public class MyIndex extends Index {

    private Map<Long, Metadata> documentsMetadata;
    private Map<Long, List<String>> index; // TODO: Ajouter la fréquence de chaque mot dans le document
    private Map<String, List<Long>> invertedIndex; // TODO: Ajouter la fréquence de chaque mot pour chaque document ici également

    public MyIndex() {
        documentsMetadata = new TreeMap<>();
        index = new TreeMap<>();
        invertedIndex = new TreeMap<>();
    }

    /**
     * Indexation d'un document.
     * @param metadata
     * @param tokens
     */
    public void addDocument(Metadata metadata, List<String> tokens) {
        addMetadata(metadata.getDocID(), metadata);
        addToIndex(metadata.getDocID(), tokens);
    }

    /**
     * Création de l'index inversé.
     * Indexe inversé : Pour chaque mot, une liste des documents dans lesquels le mot apparaît est liée.
     */
    public void createInvertedIndex() {
        long docID = 0;
        for (Map.Entry<Long, List<String>> entry : index.entrySet()) {
            docID = entry.getKey();
            for (String word : entry.getValue()) {
                if (invertedIndex.containsKey(word)) {
                    invertedIndex.get(word).add(docID);
                    Collections.sort(invertedIndex.get(word));
                }
                else {
                    invertedIndex.put(word, Collections.singletonList(docID));
                }
            }
        }
    }

    private void addMetadata(long docID, Metadata metadata) {
        documentsMetadata.put(docID, metadata);
    }

    private void addToIndex(long docID, List<String> words) {
        index.put(docID, words);
    }

    public Metadata getMetadata(long docID) {
        return documentsMetadata.get(docID);
    }
}
