package edu.tufts.eecs.graphtheory.collapsiblegraph.persistance;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.Dendrograms;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * A very quick and dirty serializer for Dendrograms. Something better really deserves to be written here.
 * @author Jeremy
 */
public class DendrogramSaver {

    public static void saveDendrogram(Dendrograms dendrogramToSave, String filePath) throws IOException {

        File outputFile = new File(filePath);
        FileOutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            if (outputFile.exists()) {
                outputFile.delete();
            }
            outputFile.createNewFile();
            outputStream = new FileOutputStream(outputFile);
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(dendrogramToSave);
        } finally {
            try {
                objectOutputStream.close();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }

    }
}
