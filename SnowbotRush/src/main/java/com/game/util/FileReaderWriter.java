package com.game.util;

import java.io.*;

public class FileReaderWriter {
    static public void saveNewFile(File dir, String name, String content) throws FileNotFoundException {
        File myFile = null;
        try {
            myFile = new File(dir, name);
            myFile.delete();
            myFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (myFile!=null){
            PrintWriter output = new PrintWriter(myFile);
            output.print(content);
            output.close();
        }
    }

    /**
     * Method reads the content of the file and returns it as String
     * ***/
    static public String readHighScoresFile(File dir, String name) throws FileNotFoundException {
        File myFile = null;
        try {
            myFile = new File(dir, name);
            if (myFile.createNewFile()) {
                return null;
            } else {
                FileReader fileInput = new FileReader(myFile);
                BufferedReader input = new BufferedReader(fileInput);

                // read the content
                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = input.readLine()) != null) {
                    buffer.append(line);
                }
                String content = buffer.toString();

                return content;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
