package com.psn.engine;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;

import com.psn.Configuration;
import com.psn.Utils;
import com.psn.test.PerformanceTest;

public class Encoder {

    public static void encode(String filePath) throws Exception {

        long startTime = System.currentTimeMillis();

        System.out.println("Encoding file '" + filePath + "'");

        /* The output streams for overwritting the original file and creating the encoded file */
        FileOutputStream fos1 = null;
        FileOutputStream fos2 = null;

        /* Get the name of the file without extension */
        String fileNameNoExt = Utils.getFileNameNoExt(filePath);

        System.out.println("File name: " + fileNameNoExt);

        try (BufferedInputStream in = (BufferedInputStream) PerformanceTest.class.getResourceAsStream(filePath)) {

            /* Create the bytes buffer */
            byte[] bbuf = new byte[4096];

            /* Create the lists for containing the processed bytes */
            // List<Byte> bytesListOrig = new ArrayList<>();
            // List<Byte> bytesList = new ArrayList<>();
            // List<Byte> bytesList2 = new ArrayList<>();

            /* Create the output streams */
            fos1 = new FileOutputStream(".\\" + fileNameNoExt + ".tmp");
            fos2 = new FileOutputStream(".\\" + fileNameNoExt + "." + Configuration.FILE_ENCODED_EXT);

            System.out.println("Processing bytes...");

            /* Process bytes */
            int len = 0;
            int index = 0;
            while ((len = in.read(bbuf)) != -1) {
                if (index % 2 == 0) {
                    fos1.write(bbuf, 0, len);
                } else {
                    fos2.write(bbuf, 0, len);
                }
                index = index + 1;
                // bytesListOrig.addAll(Bytes.asList(bbuf));
                // for (int i = 0; i < bbuf.length; i++) {
                // if (i % 2 == 0) {
                // bytesList.add(bbuf[i]);
                // // fos1.write(bbuf[i], 0, 1);
                // } else {
                // bytesList2.add(bbuf[i]);
                // }
                // }
            }

            // System.out.println("Overwritting original file...");
            //
            // /* Overwrite the bytes of the original file */
            // fos1.write(Bytes.toArray(bytesList), 0, bytesList.size());
            //
            // System.out.println("Original file successfully overwritten");
            //
            // System.out.println("Writting encoded file...");
            //
            // /* Write the encoded file */
            // fos2.write(Bytes.toArray(bytesList2), 0, bytesList2.size());
            //
            // System.out.println("Encoded file successfully written");

            // System.out.println(bytesListOrig.size() + " - " + new String(Bytes.toArray(bytesListOrig), StandardCharsets.UTF_8));
            // System.out.println(bytesList.size() + " - " + new String(Bytes.toArray(bytesList), StandardCharsets.UTF_8));
            // System.out.println(bytesList2.size() + " - " + new String(Bytes.toArray(bytesList2), StandardCharsets.UTF_8));

            long endTime = System.currentTimeMillis();

            System.out.println("End encoding in " + (endTime - startTime) + " millis");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos1 != null) {
                fos1.close();
            }
            if (fos2 != null) {
                fos2.close();
            }
        }
    }
}
