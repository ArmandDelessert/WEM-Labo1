package ch.heigvd.wem;

import ch.heigvd.wem.data.Metadata;
import ch.heigvd.wem.data.MyIndex;
import ch.heigvd.wem.interfaces.Index;
import ch.heigvd.wem.interfaces.Indexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
            BufferedReader reader = Files.newBufferedReader(Paths.get("C:\\Users\\ArmandDelessert\\Documents\\IdeaProjects\\WEM-Labo1\\common_words"));

            String line = null;
            while ((line = reader.readLine()) != null) {
                stopWords.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("The file containing stopwords was not found.");
            System.exit(1);
        }
    }

    @Override
    public void index(Metadata metadata, String content) {
//        index.addMetadata(metadata.getDocID(), metadata); // DEBUG

        // Séparation du contenu en tokens
        List<String> tokens = Arrays.asList(content.split(" "));

        // Filtrage des tokens
        for (int i = 0; i < tokens.size(); ++i) {
            tokens.get(i).toLowerCase();
            // Suppression de toute la ponctuation, à l'exception des apostrophes suivies d'une lettre
            tokens.get(i).replaceAll("(?!'[a-zA-Z0-9])\\p{Punct}", ""); // Regex : (?!'[a-zA-Z0-9])\p{P}

            if (stopWords.contains(tokens.get(i))) {
                tokens.remove(i); // TODO: Provoque parfois des UnsupportedOperationException. Ne supporte pas la concurrence ?
            }
        }

        // Ajout de la page à l'index
        index.addDocument(metadata, tokens);

        counter++;
    }

    @Override
    public void finalizeIndexation() {
        // Maintenant que tous les documents ont étés traités, construire l'index inversé contenant, pour chaque mot, les ids des documentsles contenant ainsi que leur fréquences.

        index.createInvertedIndex();
    }

    @Override
    public Index getIndex() {
        return index;
    }
}
