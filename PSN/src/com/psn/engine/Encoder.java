package com.psn.engine;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.psn.Configuration;
import com.psn.Utils;

public class Encoder {

	final static Logger logger = Logger.getLogger(Encoder.class);
	
    public static void encode(String filePath) throws Exception {

        long startTime = System.currentTimeMillis();
        logger.info("Encoding file '"+filePath+"'");

        /* The output streams for overwriting the original file and creating the encoded files 
         * 
         * One encoded file will be saved in the same folder of the original file
         * The second encoded file, containing only a minimum portion of the original file, will be saved in the usb
         */
        OutputStream fos1 = null;
        OutputStream fos2 = null;
        OutputStream fos3 = null;

        /* Get the file name without extension */
        String fileNameNoExt = Utils.getFileNameNoExt(filePath);
        
        /* Create the path for the temporary version of the original file */
        String originalFileTmpPath = filePath + ".tmp";
        
        /* Create the path of the encoded file that will be saved on usb */
        String encodedFilePathUsb = ".\\" + fileNameNoExt + "." + Configuration.FILE_ENCODED_EXT;
        
        /* Create the path of the encoded file that will be saved on the same folder of the original file */
        String encodedFilePath = filePath + "." + Configuration.FILE_ENCODED_EXT;
        
//        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath), Configuration.BUFFER_SIZE)) {
        try (FileInputStream in = new FileInputStream(filePath)) {	
            
        	/* Create the bytes buffer */
            byte[] bbuf = new byte[Configuration.BUFFER_SIZE];

            /* Create the output streams */
            fos1 = new BufferedOutputStream(new FileOutputStream(originalFileTmpPath), Configuration.BUFFER_SIZE);
            fos2 = new BufferedOutputStream(new FileOutputStream(encodedFilePathUsb), Configuration.BUFFER_SIZE);
            fos3 = new BufferedOutputStream(new FileOutputStream(encodedFilePath), Configuration.BUFFER_SIZE);

//            Path path = Paths.get(originalFileTmpPath);
//            try(BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"))){
//            	writer.write("To be, or not to be. That is the question.");
//            }catch(IOException ex){
//            	ex.printStackTrace();
//            }
            
            logger.info("Processing bytes...");

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
            
            logger.info("Encoded file '"+encodedFilePath+"' successfully created.");
            
            /* Rename the original file to '.old' version */
            File originalFile = new File(filePath);
            logger.info("Rename file '"+filePath+"' to '"+originalFile.getAbsolutePath()+".old"+"'");
            String originalFileOldVersionPath = originalFile.getAbsolutePath()+".old";
            originalFile.renameTo(new File(originalFileOldVersionPath));
            
            /* Rename the '.tmp' version of the original file to the original extension */
            File newOriginalFile = new File(originalFileTmpPath);
            logger.info("Rename file '"+originalFileTmpPath+"' to '"+filePath+"'");
            newOriginalFile.renameTo(new File(filePath));
            
            /* Delete the '.old' version of the original file */
            logger.info("Deleting file '"+originalFileOldVersionPath+"'");
            File originalFileOldVersion = new File(originalFileOldVersionPath);
            boolean deleteResult = originalFileOldVersion.delete();
            logger.info("File '"+originalFileOldVersionPath+"' deleted? "+deleteResult);
            
            long endTime = System.currentTimeMillis();
            logger.info("End encoding in " + (endTime - startTime) + " millis");
        } catch (Exception e) {
            logger.error("Exception: ", e);
            try{
            	new File(originalFileTmpPath).delete();
            	new File(encodedFilePath).delete();
            }catch(Exception e1){
            	logger.error("Errore cancellazione file");
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
