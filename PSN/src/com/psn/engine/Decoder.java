package com.psn.engine;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.psn.Configuration;
import com.psn.Utils;

public class Decoder {

    public static void decode(String filePath) throws Exception {

        long startTime = System.currentTimeMillis();

        System.out.println("Decoding file '" + filePath + "'");

        /* The input streams for reading the files */
        BufferedInputStream bin1 = null;
        BufferedInputStream bin2 = null;

        /* The output stream for writing the decoded file */
        FileOutputStream fos1 = null;

        /* Get the name of the file without extension */
        String fileNameNoExt = Utils.getFileNameNoExt(filePath);

        System.out.println("File name: " + fileNameNoExt);

        try {
            /* Create the input streams */
            bin1 = new BufferedInputStream(new FileInputStream(filePath));
            bin2 = new BufferedInputStream(new FileInputStream(".\\" + fileNameNoExt + "." + Configuration.FILE_ENCODED_EXT));

            /* Create the output stream */
            fos1 = new FileOutputStream(".\\" + fileNameNoExt + ".decx");

            /* Create the bytes buffer */
            byte[] bbuf1 = new byte[4096];
            byte[] bbuf2 = new byte[4096];

            int index = 0;
            int len1 = 0;
            int len2 = 0;
            boolean continueProcessing = true;
            int sum = 0;

            /* Create the lists for containing the processed bytes */
            // List<Byte> bytesList = new ArrayList<>();
            // List<Byte> bytesList2 = new ArrayList<>();
            // List<Byte> newList = new ArrayList<Byte>(bbuf1.length);

            /* Process the input file */
            // while (/* (len1 = bin1.read(bbuf1)) != -1 && */ (len2 = bin2.read(bbuf2)) != -1) {
            while (continueProcessing) {
                len1 = bin1.read(bbuf1);
                len2 = bin2.read(bbuf2);

                if (len1 == -1) {
                    continueProcessing = false;
                } else {
                    fos1.write(bbuf1);
                    fos1.write(bbuf2);

                    index = index + 1;

                    System.out.println("--> LEN1: " + len1);

                    sum = sum + len1 + len2;
                }
                // if (index % 2 == 0) {
                // fos1.write(bbuf1);
                // } else {
                // fos1.write(bbuf2);
                // }

                // for (int i = 0; i < bbuf1.length; i++) {
                // newList.add(bbuf1[i]);
                // newList.add(bbuf2[i]);
                // if (i % 2 == 0) {
                // newList.add(bbuf1[i]);
                // newList.add(bbuf2[i / 2]);
                // } else {
                // newList.add(bbuf1[i]);
                // }
                // if (i % 2 == 0) {
                // // byte[] bTemp = new byte[1];
                // // bTemp[0] = bbuf2[i / 2];
                // // newList.add(i + 1, bTemp[0]);
                // newList.add(i, bbuf1[0]);
                // } else {
                // newList.add(i, bbuf1[0]);
                // }
                // }
            }
            System.out.println("--> INDEX: " + index);
            System.out.println("--> SUM: " + sum);
            // for (Byte b : newList) {
            // System.out.println(b);
            // }

            /* Write the decoded file */
            // fos1.write(Bytes.toArray(newList), 0, newList.size());

            /* Process the encrypted file */
            // while ((bin2.read(bbuf2)) != -1) {
            // for (int i = 0; i < bbuf2.length; i++) {
            // bytesList2.add(bbuf2[i]);
            // }
            // }

            // System.out.println(bytesList.size() + " - " + new String(Bytes.toArray(bytesList), StandardCharsets.UTF_8));
            // System.out.println(bytesList2.size() + " - " + new String(Bytes.toArray(bytesList2), StandardCharsets.UTF_8));

            /* Reconstruct the file */
            // for (int i = 0; i < bytesList.size(); i++) {
            // if (i % 2 == 0) {
            // System.out.println("--> " + i);
            // byte[] bTemp = new byte[1];
            // bTemp[0] = bytesList2.get(i / 2);
            // newList.add(i + 1, bytesList2.get(i / 2));
            // }
            // }

            /* Write the decoded file */
            // fos1.write(Bytes.toArray(newList), 0, newList.size());

            long endTime = System.currentTimeMillis();

            System.out.println("End decoding in " + (endTime - startTime) + " millis");

        } catch (Exception e) {
            e.printStackTrace();
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
