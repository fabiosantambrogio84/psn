package com.psn;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.psn.usb.StreamGobbler;

public class Context {

	//private SystemUtils systemUtils;
	
	private static final String COMMAND = "wmic path win32_pnpentity where \"deviceid like '%USB%'and name like '%archiviazione%'\" get PNPDeviceID";
	private static final String VID_REGEX = "(VID_[a-zA-Z0-9]{4})";
	private static final String PID_REGEX = "(PID_[a-zA-Z0-9]{4})";
	
	private static Context instance;
    
	public Context(){
		//this.setSystemUtils(new SystemUtils());
	}

	public static Context getInstance(){
	    if(instance == null){
	        instance = new Context();
	    }
	    return instance;
	}

	/*
	public String getComputerSerialNumber(){
		return systemUtils.getComputerSerialNumber();
	}
	*/
	
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

	public void checkUsbVidPid() throws Exception{
		StringBuilder stringBuilder = new StringBuilder();
		boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

		ProcessBuilder builder = new ProcessBuilder();
		if (isWindows) {
		    builder.command("cmd.exe", "/c", COMMAND);
		} else {
		    // builder.command("sh", "-c", "ls");
			throw new RuntimeException("Sistema operativo non supportato");
		}
		builder.directory(new File(System.getProperty("user.home")));
		
		// Run the command
		Process process = builder.start();
		StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println, stringBuilder);
		Executors.newSingleThreadExecutor().submit(streamGobbler);
		int exitCode = process.waitFor();
		if(exitCode != 0){
			process.destroyForcibly();
		}
		
		// Get the output command 
		String result = StringUtils.upperCase(stringBuilder.toString());
		
		// Lists for the vids and pids extracted from the command output
		List<String> vids = new ArrayList<String>();
		List<String> pids = new ArrayList<String>();
		
		// Extract the vids
		Pattern vidPattern = Pattern.compile(VID_REGEX);
        Matcher vidMatcher = vidPattern.matcher(result);
        while (vidMatcher.find()) {
            String match = vidMatcher.group(0);
            vids.add(match);
        }
        
        // Extract the pids
        Pattern pidPattern = Pattern.compile(PID_REGEX);
        Matcher pidMatcher = pidPattern.matcher(result);
        while (pidMatcher.find()) {
            String match = pidMatcher.group(0);
            pids.add(match);
        }
        
        for(int i=0; i<vids.size(); i++){
        	System.out.println("Vid -> "+vids.get(i));
        }
        for(int i=0; i<pids.size(); i++){
        	System.out.println("Pid -> "+pids.get(i));
        }
	}
	
	/*
	public String getSerialNumber(UsbDevice[] usbDevices) {
        String result = null;
        for (UsbDevice usbDevice : usbDevices) {
            String vendorId = usbDevice.getVendorId();
            String productId = usbDevice.getProductId();
            String serialNumber = usbDevice.getSerialNumber();
            UsbDevice[] connDevs = usbDevice.getConnectedDevices();
            
            if (Configuration.VENDOR_ID.equals(vendorId) && Configuration.PRODUCT_ID.equals(productId) && serialNumber != null
                    && connDevs.length == 0) {
                return serialNumber;
            } else {
                result = getSerialNumber(connDevs);
            }
        }
        return result;
	}
	*/
	/*
	public SystemUtils getSystemUtils() {
		return systemUtils;
	}

	public void setSystemUtils(SystemUtils systemUtils) {
		this.systemUtils = systemUtils;
	}
	*/
	
}
