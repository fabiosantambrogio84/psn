package com.psn;

import java.io.File;

public class Test2 {

	public static void main(String[] args) {
//		Context context = Context.getInstance();
//		System.out.println(context.getComputerSerialNumber());
//		System.out.println(context.getRunningDriveLetter());
//		
//		Context context2 = Context.getInstance();
//		System.out.println(context2.getComputerSerialNumber());
//		System.out.println(context2.getRunningDriveLetter());
		
		String filePath = "C:\\Users\\Fabio Santambrogio\\Desktop\\DA_FARE_PUZZLE\\temp\\foto1.jpg";
		/* Rename the original file */
        File originalFile = new File(filePath);
        System.out.println("Rename file '"+filePath+"' to '"+originalFile.getAbsolutePath()+".old"+"'");
        System.out.println(originalFile.renameTo(new File(originalFile.getAbsolutePath()+".old")));
	}
	
}
