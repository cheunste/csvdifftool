package VarexpInterface;

import java.io.*;


/**
 * Created by Stephen on 4/20/2018.
 */

/*
    This class is to handle any file IO another with any file formatting methods
 */
public class fileHandler {

    /*
        This opens up a file and returns a reference to that file
     */
    public BufferedReader readInput(String filePath) throws IOException {
        FileInputStream in = null;


        try {
            File f = new File(filePath);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            return br;
        } catch (IOException e) {
            throw new IOException("Error: Cannot open file in " + filePath);
        }


    }

    /*
    This closes a given file
     */

    public void closeFile(BufferedReader fileBR) throws IOException {

        try {
            fileBR.close();
        } catch (IOException e) {
            throw new IOException("Error: Cannot close output stream");
        }
    }

    /*
    This method checks to see if a varexp variable has the exact number of fields stated in the manual.
    As of PcVue 11, this means 250 fields. If there are not enoug hfeilds, then just bandage it and add
    in commons
     */
    public String appendFiles(String variable, int fieldNum) {

        int varLength = variable.length();
        if (varLength <= fieldNum) {
            for (int i = 0; i <= (fieldNum - varLength); i++) {
                variable.concat(",");
            }
        }
        return variable;
    }

    /*
    TODO: implement the functions for exporting files. Do this after figuring out the quereis
     */

    public void exportFiles(String outputPath, String fileName) {

    }


}
