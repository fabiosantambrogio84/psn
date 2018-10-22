package com.psn;

import java.io.File;

import com.psn.system.SystemUtils;

public class Context {

	private SystemUtils systemUtils;
	
	private static Context instance;
    
	public Context(){
		this.setSystemUtils(new SystemUtils());
	}

	public static Context getInstance(){
	    if(instance == null){
	        instance = new Context();
	    }
	    return instance;
	}

	public String getComputerSerialNumber(){
		return systemUtils.getComputerSerialNumber();
	}
	
	public String getRunningDriveLetter(){
		String driveLetter = null;
		File f = new File(".");
		String absolutePath = f.getAbsolutePath();
		if(absolutePath.contains(":")){
			driveLetter = absolutePath.substring(0, absolutePath.indexOf(':'));
		} else{
			throw new RuntimeException();
		}
		f.delete();
		return driveLetter;
	}
	
	public SystemUtils getSystemUtils() {
		return systemUtils;
	}

	public void setSystemUtils(SystemUtils systemUtils) {
		this.systemUtils = systemUtils;
	}
	
}
