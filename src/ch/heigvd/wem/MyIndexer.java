package ch.heigvd.wem;

import ch.heigvd.wem.data.Metadata;
import ch.heigvd.wem.data.MyIndex;
import ch.heigvd.wem.interfaces.Index;
import ch.heigvd.wem.interfaces.Indexer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by Armand Delessert on 13.03.2017.
 */
public class MyIndexer implements Indexer {

    private int counter;
    private MyIndex index;
    private List<String> stopWords;

    public MyIndexer() {
        counter = 0;
        index = new MyIndex();
        stopWords = new LinkedList<>();

        // Lecture du fichier contenant les stop words
        try {
            File stopWordsFile = new File("common_words");
            stopWords = Files.readAllLines(stopWordsFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("The file containing stopwords was not found.");
            System.exit(1);
        }
    }

    @Override
    public void index(Metadata metadata, String content) {
        // Séparation du contenu en tokens
        List<String> tokens = tokenize(content);

        // Ajout de la page à l'index
        index.addDocument(metadata, tokens);

        counter++;
    }

    /**
     * Séparation du texte du document en mots-clés et suppression de la ponctuation.
     *
     * @param content
     * @return
     */
    private List<String> tokenize(String content) {
        // Séparation du contenu en tokens
        List<String> tokens = new ArrayList<>(Arrays.asList(content.split("[ \\t\\r\\n]+"))); // "new ArrayList()" car "Arrays.asList()" retourne une liste non-modifiable.

        // Filtrage des tokens
        for (int i = 0; i < tokens.size(); ++i) {
            tokens.set(i, tokens.get(i).toLowerCase());
            // Suppression de toute la ponctuation, à l'exception des apostrophes précédées et suivies d'une lettre.
            // Cette partie supprime toute la ponctuation sauf les apostrophes : [^\p{L}\p{N}']
            // Cette partie supprime les apostrophes en début et en fin de mot : ((\B'\b)|(\b'\B))
            tokens.set(i, tokens.get(i).replaceAll("[^\\p{L}\\p{N}']|((\\B'\\b)|(\\b'\\B))", ""));

            if (stopWords.contains(tokens.get(i))) {
                tokens.remove(i--);
            }
        }

        // Suppression des doublons
        tokens = new ArrayList<>(new LinkedHashSet<>(tokens));

        Collections.sort(tokens);

        return tokens;
    }

    @Override
    public void finalizeIndexation() {
        // Maintenant que tous les documents ont étés traités, construire l'index inversé contenant, pour chaque mot, les ID des documents les contenant ainsi que leur fréquence.
        index.createInvertedIndex();
    }

    @Override
    public Index getIndex() {
        return index;
    }
}
