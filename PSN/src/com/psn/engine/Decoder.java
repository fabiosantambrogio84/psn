package com.psn.engine;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.psn.Configuration;
import com.psn.Utils;

public class Decoder {

	private static Logger LOGGER = LoggerFactory.getLogger(Decoder.class);
	
    @SuppressWarnings("unused")
	public static void decode(String filePath) throws Exception {

        long startTime = System.currentTimeMillis();
        LOGGER.info("Decoding file '" + filePath + "'");

        /* The input streams for reading the files */
        BufferedInputStream bin1 = null;
        BufferedInputStream bin2 = null;
        BufferedInputStream bin3 = null;

        /* The output stream for writing the temporary decoded file */
        FileOutputStream fos1 = null;

        /* Get the file name without extension */
        String fileNameNoExt = Utils.getFileNameNoExt(filePath);
        
        /* Get the path of the encoded file saved on the usb */
        //String encodedFilePathUsb = ".\\" + fileNameNoExt + "." + Configuration.FILE_ENCODED_EXT;
        String encodedFilePathUsb = "E:\\" + fileNameNoExt + "." + Configuration.FILE_ENCODED_EXT;
        
        /* Get the path of the encoded file saved on the same folder of the original file */
        String encodedFilePath = filePath + "." + Configuration.FILE_ENCODED_EXT;
        
        /* Create the path for the temporary decoded file */
        String decodedFilePath = filePath + ".decx";

        try {
            /* Create the input streams */
            bin1 = new BufferedInputStream(new FileInputStream(filePath));
            bin2 = new BufferedInputStream(new FileInputStream(encodedFilePathUsb));
            bin3 = new BufferedInputStream(new FileInputStream(encodedFilePath));

            /* Create the output stream */
            fos1 = new FileOutputStream(decodedFilePath);

            /* Create the bytes buffer */
            byte[] bbuf1 = new byte[4096];
            byte[] bbuf2 = new byte[4096];
            byte[] bbuf3 = new byte[4096];

            int index = 0;
            int len1 = 0;
            int len2 = 0;
            int len3 = 0;
            boolean continueProcessing = true;

            /* Process the input file */
            while (continueProcessing) {
                len1 = bin1.read(bbuf1);
                
                if (len1 == -1) {
                    continueProcessing = false;
                } else {
                	fos1.write(bbuf1);
                	index = index + 1;
                	
                    if(Configuration.INDEX_SET.contains(index)){
                    	len2 = bin2.read(bbuf2);
                    	fos1.write(bbuf2);
                    	index = index + 1;
                    } else{
                    	len3 = bin3.read(bbuf3);
                    	fos1.write(bbuf3);
                    	index = index + 1;
                    }
                    
                }
                //index = index + 1;
            }
            
            /* Close the streams */
            if (bin1 != null) {
                bin1.close();
            }
            if (bin2 != null) {
                bin2.close();
            }
            if(bin3 != null){
            	bin3.close();
            }
            if (fos1 != null) {
                fos1.close();
            }
            
            
            LOGGER.info("File '"+filePath+"' successfully decoded");
            
            /* Rename the original file to '.old' version */
            File originalFile = new File(filePath);
            LOGGER.info("Rename file '"+filePath+"' to '"+originalFile.getAbsolutePath()+".old"+"'");
            String originalFileOldVersionPath = originalFile.getAbsolutePath()+".old";
            originalFile.renameTo(new File(originalFileOldVersionPath));
            
            /* Rename the '.decx' version of the file to the original extension */
            File decodedFile = new File(decodedFilePath);
            LOGGER.info("Rename file '"+decodedFilePath+"' to '"+filePath+"'");
            decodedFile.renameTo(new File(filePath));
            
            /* Remove the '.old' version of the original file */
            LOGGER.info("Deleting file '"+originalFileOldVersionPath+"'");
            File originalFileOldVersion = new File(originalFileOldVersionPath);
            boolean deleteResult = originalFileOldVersion.delete();
            LOGGER.info("File '"+originalFileOldVersionPath+"' deleted? "+deleteResult);
            
            /* Remove the encoded file '.encx' saved on the usb */
            LOGGER.info("Deleting file '"+encodedFilePathUsb+"'");
            File encodedFileUsb = new File(encodedFilePathUsb);
            boolean deleteResult2 = encodedFileUsb.delete();
            LOGGER.info("File '"+encodedFilePathUsb+"' deleted? "+deleteResult2);
            
            /* Remove the encoded file '.encx' saved on the same folder of the original file */
            LOGGER.info("Deleting file '"+encodedFilePath+"'");
            File encodedFile = new File(encodedFilePath);
            boolean deleteResult3 = encodedFile.delete();
            LOGGER.info("File '"+encodedFilePath+"' deleted? "+deleteResult3);
            
            long endTime = System.currentTimeMillis();
            LOGGER.info("End decoding in " + (endTime - startTime) + " millis");
        } catch (Exception e) {
        	LOGGER.error("Exception: ", e);
            try{
            	new File(decodedFilePath).delete();
            }catch(Exception e1){
            	LOGGER.error("Errore cancellazione file");
            }
            throw e;
        } finally {
            if (bin1 != null) {
                bin1.close();
            }
            if (bin2 != null) {
                bin2.close();
            }
            if(bin3 != null){
            	bin3.close();
            }
            if (fos1 != null) {
                fos1.close();
            }
        }

    }
}
