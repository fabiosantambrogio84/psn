package com.psn.test;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.google.common.primitives.Bytes;

public class Test {

    public static void main(String[] args) throws IOException {
        // UsbReader.listDevices();

        // String basePathOfClass = Test.class.getProtectionDomain().getCodeSource().getLocation().getFile();

        FileOutputStream fileOutputStream = null;
        FileOutputStream fileOutputStream2 = null;

        try (BufferedInputStream in = (BufferedInputStream) Test.class.getResourceAsStream("test2.txt")) {
            byte[] bbuf = new byte[4096];
            int len;
            // int index = 0;
            BitSet bsOrig = new BitSet();
            BitSet bsEnc1 = new BitSet();
            BitSet bsEnc2 = new BitSet();

            List<Byte> bytesListOrig = new ArrayList<>();
            List<Byte> bytesList = new ArrayList<>();
            List<Byte> bytesList2 = new ArrayList<>();
            // fileOutputStream = new FileOutputStream("C:/temp/test2_enc1.txt");
            // fileOutputStream2 = new FileOutputStream("C:/temp/test2_enc2.txt");
            while ((len = in.read(bbuf)) != -1) {
                for (int i = 0; i < bbuf.length; i++) {
                    bytesListOrig.add(bbuf[i]);
                    if (i % 2 == 0) {
                        bytesList.add(bbuf[i]);
                    } else {
                        bytesList2.add(bbuf[i]);
                    }
                }

                // BitSet bsTemp = BitSet.valueOf(bbuf);
                // for (int i = 0; i < bsTemp.length(); i++) {
                // if (bsTemp.get(i)) {
                // bsOrig.set(i);
                // }
                // if (i % 2 == 0) {
                // bsEnc2.set(i, bsTemp.get(i));
                // } else {
                // bsEnc1.set(i, bsTemp.get(i));
                // }
                // }

                // for (int i = 0; i < len; i++) {
                // if (i % 10 != 0) {
                // bbufCopy[i] = bbuf[i];
                // } else {
                // removedBytes.put(index, bbuf[i]);
                // }
                // index = index + 1;
                // }
                // System.out.println(Hex.encodeHexString(bbufCopy));
                // fileOutputStream.write(bbufCopy, 0, len);
                // fileOutputStream.write(b2, 0, b2.length);
                // process data here: bbuf[0] thru bbuf[len - 1]
            }
            System.out.println(bytesListOrig.size() + " - " + new String(Bytes.toArray(bytesListOrig), StandardCharsets.UTF_8));
            System.out.println(bytesList.size() + " - " + new String(Bytes.toArray(bytesList), StandardCharsets.UTF_8));
            System.out.println(bytesList2.size() + " - " + new String(Bytes.toArray(bytesList2), StandardCharsets.UTF_8));

            List<Byte> newList = new ArrayList<Byte>(bytesList);
            for (int i = 0; i < bytesList.size(); i++) {
                if (i % 2 == 0) {
                    byte[] bTemp = new byte[1];
                    bTemp[0] = bytesList2.get(i / 2);
                    System.out.println(new String(bTemp, StandardCharsets.UTF_8));
                    newList.add(i + 1, bytesList2.get(i / 2));
                }
            }
            System.out.println(newList.size() + " - " + new String(Bytes.toArray(newList), StandardCharsets.UTF_8));

            // for (int j = 0; j < bsOrig.length(); j++) {
            // System.out.println(j + " - " + bsOrig.get(j));
            // }

            // BitSet bsDec = new BitSet();
            // for (int i = 0; i < bsOrig.length(); i++) {
            // if (i % 2 == 0) {
            // bsDec.set(i, bsEnc2.get(i));
            // } else {
            // bsDec.set(i, bsEnc1.get(i));
            // }
            // }
            // System.out.println("##### DECODED #####");
            // for (int j = 0; j < bsDec.length(); j++) {
            // System.out.println(j + " - " + bsDec.get(j));
            // }

            // byte[] enc1 = bsEnc1.toByteArray();
            // fileOutputStream.write(enc1, 0, enc1.length);
            //
            // byte[] enc2 = bsEnc2.toByteArray();
            // fileOutputStream2.write(enc2, 0, enc2.length);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }

        // BufferedInputStream in1 = null;
        // BufferedInputStream in2 = null;
        // FileOutputStream fos3 = null;
        // try {
        // in1 = new BufferedInputStream(new FileInputStream("C:/temp/test2_enc1.txt"));
        // in2 = new BufferedInputStream(new FileInputStream("C:/temp/test2_enc2.txt"));
        // fos3 = new FileOutputStream("C:/temp/test2_dec.txt");
        // byte[] bbuf1 = new byte[4096];
        // byte[] bbuf2 = new byte[4096];
        // BitSet bs = new BitSet();
        //
        // while ((in1.read(bbuf1)) != -1 && (in2.read(bbuf2)) != -1) {
        // BitSet bsTemp1 = BitSet.valueOf(bbuf1);
        // BitSet bsTemp2 = BitSet.valueOf(bbuf2);
        // int maxLength = bsTemp1.length();
        // if (bsTemp2.length() > maxLength) {
        // maxLength = bsTemp2.length();
        // }
        //
        // for (int i = 0; i < maxLength; i++) {
        // if (i % 2 == 0) {
        // bs.set(i, bsTemp2.get(i));
        // } else {
        // bs.set(i, bsTemp1.get(i));
        // }
        // }
        // }
        //
        // byte[] dec = bs.toByteArray();
        // fos3.write(dec, 0, dec.length);
        //
        // } catch (Exception e) {
        // e.printStackTrace();
        // } finally {
        // if (in1 != null) {
        // in1.close();
        // }
        // if (in2 != null) {
        // in2.close();
        // }
        // if (fos3 != null) {
        // fos3.close();
        // }
        // }

    }

    private static void oldStuff() {
        // for (Map.Entry<Integer, Byte> entry : removedBytes.entrySet()) {
        // System.out.println(entry.getKey() + ":" + entry.getValue());
        // }

        // String str = "Hello how are you what are you doing";
        // byte[] byteArray = str.getBytes();
        // System.out.println("Byte: " + Hex.encodeHexString(byteArray));

        // System.out.println("Contents of the byte array :: ");

        // for (int i = 0; i < byteArray.length; i++) {
        // System.out.println((char) byteArray[i]);
        // }

        // ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        // byte[] newArray = "where do you live ".getBytes();
        // buffer.position(18);
        // buffer.put(newArray);
        // System.out.println("Contents of the byte array after replacement::");

        // for (int i = 0; i < byteArray.length; i++) {
        // System.out.println((char) byteArray[i]);
        // }
    }

}
