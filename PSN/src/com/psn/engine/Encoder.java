package com.psn.engine;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.psn.Configuration;
import com.psn.Utils;

public class Encoder {

	private static Logger LOGGER = LoggerFactory.getLogger(Encoder.class);
	
    public static void encode(String filePath) throws Exception {

        long startTime = System.currentTimeMillis();
        LOGGER.info("Encoding file '"+filePath+"'");

        /* The output streams for overwriting the original file and creating the encoded files 
         * 
         * One encoded file will be saved in the same folder of the original file
         * The second encoded file, containing only a minimum portion of the original file, will be saved in the usb
         */
        FileOutputStream fos1 = null;
        FileOutputStream fos2 = null;
        FileOutputStream fos3 = null;

        /* Get the file name without extension */
        String fileNameNoExt = Utils.getFileNameNoExt(filePath);
        
        /* Create the path for the temporary version of the original file */
        String originalFileTmpPath = filePath + ".tmp";
        
        /* Create the path of the encoded file that will be saved on usb */
        String encodedFilePathUsb = ".\\" + fileNameNoExt + "." + Configuration.FILE_ENCODED_EXT;
        
        /* Create the path of the encoded file that will be saved on the same folder of the original file */
        String encodedFilePath = filePath + "." + Configuration.FILE_ENCODED_EXT;
        
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath))) {

            /* Create the bytes buffer */
            byte[] bbuf = new byte[4096];

            /* Create the output streams */
            fos1 = new FileOutputStream(originalFileTmpPath);
            fos2 = new FileOutputStream(encodedFilePathUsb);
            fos3 = new FileOutputStream(encodedFilePath);

            LOGGER.info("Processing bytes...");

            /* Process bytes */
            int len = 0;
            int index = 0;
            while ((len = in.read(bbuf)) != -1) {
                if (index % 2 == 0) {
                    fos1.write(bbuf, 0, len);
                } else {
                    if(Configuration.INDEX_SET.contains(index)){
                    	fos2.write(bbuf, 0, len);
                    } else{
                    	fos3.write(bbuf, 0, len);
                    }
                }
                index = index + 1;
            }

            /* Close the streams */
            if (fos1 != null) {
                fos1.close();
            }
            if (fos2 != null) {
                fos2.close();
            }
            if(fos3 != null){
            	fos3.close();
            }
            in.close();
            
            LOGGER.info("Encoded file '"+encodedFilePath+"' successfully created.");
            
            /* Rename the original file to '.old' version */
            File originalFile = new File(filePath);
            LOGGER.info("Rename file '"+filePath+"' to '"+originalFile.getAbsolutePath()+".old"+"'");
            String originalFileOldVersionPath = originalFile.getAbsolutePath()+".old";
            originalFile.renameTo(new File(originalFileOldVersionPath));
            
            /* Rename the '.tmp' version of the original file to the original extension */
            File newOriginalFile = new File(originalFileTmpPath);
            LOGGER.info("Rename file '"+originalFileTmpPath+"' to '"+filePath+"'");
            newOriginalFile.renameTo(new File(filePath));
            
            /* Delete the '.old' version of the original file */
            LOGGER.info("Deleting file '"+originalFileOldVersionPath+"'");
            File originalFileOldVersion = new File(originalFileOldVersionPath);
            boolean deleteResult = originalFileOldVersion.delete();
            LOGGER.info("File '"+originalFileOldVersionPath+"' deleted? "+deleteResult);
            
            long endTime = System.currentTimeMillis();
            LOGGER.info("End encoding in " + (endTime - startTime) + " millis");
        } catch (Exception e) {
            LOGGER.error("Exception: ", e);
            try{
            	new File(originalFileTmpPath).delete();
            	new File(encodedFilePath).delete();
            }catch(Exception e1){
            	LOGGER.error("Errore cancellazione file");
            }
            throw e;
        } finally {
            if (fos1 != null) {
                fos1.close();
            }
            if (fos2 != null) {
                fos2.close();
            }
            if (fos3 != null) {
                fos3.close();
            }
        }
    }
}
