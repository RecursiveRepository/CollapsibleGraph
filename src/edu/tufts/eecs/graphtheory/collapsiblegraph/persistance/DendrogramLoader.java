package edu.tufts.eecs.graphtheory.collapsiblegraph.persistance;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.Dendrogram;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * A really quick and dirty Deserializer for Dendrograms. There really ought to be something better written than serializing / deserializing Dendrogram objects.
 * @author Jeremy
 */
public class DendrogramLoader {
    
    public static Dendrogram loadDendrogram(String filePath) {
        File inputFile = new File(filePath);
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Dendrogram retrievedDendrogram = null;
        try {
            fis = new FileInputStream(inputFile);
            ois = new ObjectInputStream(fis);
            retrievedDendrogram = (Dendrogram)ois.readObject();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return retrievedDendrogram;
    }
}
