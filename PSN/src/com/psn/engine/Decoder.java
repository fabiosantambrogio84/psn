package com.psn.engine;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.psn.Configuration;
import com.psn.Utils;

public class Decoder {

    @SuppressWarnings("unused")
	public static void decode(String filePath) throws Exception {

        long startTime = System.currentTimeMillis();
        System.out.println("Decoding file '" + filePath + "'");

        /* The input streams for reading the files */
        BufferedInputStream bin1 = null;
        BufferedInputStream bin2 = null;

        /* The output stream for writing the temporary decoded file */
        FileOutputStream fos1 = null;

        /* Get the file name without extension */
        String fileNameNoExt = Utils.getFileNameNoExt(filePath);
        
        /* Create the path for the temporary decoded file */
        String decodedFilePath = filePath + ".decx";

        System.out.println("File name: " + fileNameNoExt);

        try {
            /* Create the input streams */
            bin1 = new BufferedInputStream(new FileInputStream(filePath));
            bin2 = new BufferedInputStream(new FileInputStream(".\\" + fileNameNoExt + "." + Configuration.FILE_ENCODED_EXT));

            /* Create the output stream */
            fos1 = new FileOutputStream(decodedFilePath);

            /* Create the bytes buffer */
            byte[] bbuf1 = new byte[4096];
            byte[] bbuf2 = new byte[4096];

            int index = 0;
            int len1 = 0;
            int len2 = 0;
            boolean continueProcessing = true;

            /* Process the input file */
            while (continueProcessing) {
                len1 = bin1.read(bbuf1);
                len2 = bin2.read(bbuf2);

                if (len1 == -1) {
                    continueProcessing = false;
                } else {
                    fos1.write(bbuf1);
                    fos1.write(bbuf2);

                    index = index + 1;
                }
            }
            
            /* Close the streams */
            if (bin1 != null) {
                bin1.close();
            }
            if (bin2 != null) {
                bin2.close();
            }
            if (fos1 != null) {
                fos1.close();
            }
            
            /* Rename the original file to '.old' version */
            File originalFile = new File(filePath);
            System.out.println("Rename file '"+filePath+"' to '"+originalFile.getAbsolutePath()+".old"+"'");
            String originalFileOldVersionPath = originalFile.getAbsolutePath()+".old";
            originalFile.renameTo(new File(originalFileOldVersionPath));
            
            /* Rename the '.decx' version of the file to the original extension */
            File decodedFile = new File(decodedFilePath);
            System.out.println("Rename file '"+decodedFilePath+"' to '"+filePath+"'");
            decodedFile.renameTo(new File(filePath));
            
            /* Remove the '.old' version of the original file */
            System.out.println("Delete file '"+originalFileOldVersionPath+"'");
            File originalFileOldVersion = new File(originalFileOldVersionPath);
            originalFileOldVersion.delete();
            
            long endTime = System.currentTimeMillis();
            System.out.println("End decoding in " + (endTime - startTime) + " millis");
        } catch (Exception e) {
            e.printStackTrace();
            try{
            	new File(decodedFilePath).delete();
            }catch(Exception e1){
            	System.out.println("Errore cancellazione file");
            }
            throw e;
        } finally {
            if (bin1 != null) {
                bin1.close();
            }
            if (bin2 != null) {
                bin2.close();
            }
            if (fos1 != null) {
                fos1.close();
            }
        }

    }
}
