package com.psn.system;

import com.psn.Configuration;

import oshi.json.SystemInfo;
import oshi.json.hardware.ComputerSystem;
import oshi.json.hardware.HardwareAbstractionLayer;
import oshi.json.hardware.UsbDevice;
import oshi.json.software.os.OperatingSystem;

public class SystemUtils {

	private SystemInfo systemInfo;
	
	private HardwareAbstractionLayer hal;
	
    private OperatingSystem operatingSystem;
	
    public SystemUtils(){
    	systemInfo = new SystemInfo();
    	hal = systemInfo.getHardware();
    	operatingSystem = systemInfo.getOperatingSystem();
    }
    
	public SystemInfo getSystemInfo() {
		return systemInfo;
	}

	public void setSystemInfo(SystemInfo systemInfo) {
		this.systemInfo = systemInfo;
	}

	public HardwareAbstractionLayer getHal() {
		return hal;
	}

	public void setHal(HardwareAbstractionLayer hal) {
		this.hal = hal;
	}

	public OperatingSystem getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(OperatingSystem operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getComputerSerialNumber(){
		String serialNumber = null;
		try{
			ComputerSystem computerSystem = hal.getComputerSystem();
			serialNumber = computerSystem.getSerialNumber();
		}catch(Exception e){
			e.printStackTrace();
		}
		return serialNumber;
	}
	
	public String getUsbSerialNumber(){
		String serialNumber = null;
		try{
			serialNumber = findUsbSerialNumber(hal.getUsbDevices(true));
		}catch(Exception e){
			e.printStackTrace();
		}
		return serialNumber;			
	}
	
	private String findUsbSerialNumber(UsbDevice[] usbDevices){
		String serialNumber = null;
        for (UsbDevice usbDevice : usbDevices) {
        	System.out.println(usbDevice.toPrettyJSON());
            String vendorId = usbDevice.getVendorId();
            String productId = usbDevice.getProductId();
            String serialNumb = usbDevice.getSerialNumber();
            UsbDevice[] connDevs = usbDevice.getConnectedDevices();
//            System.out.println("VENDOR ID: "+vendorId + " - PRODUCT ID: "+productId + " - SERIAL NUMBER: "+serialNumb);
//            System.out.println("CONN LENGTH: "+connDevs.length);
//            System.out.println("--> 1: "+Configuration.VENDOR_ID.equals(vendorId));
//            System.out.println("--> 2: "+Configuration.PRODUCT_ID.equals(productId));
//            System.out.println("--> 3: "+(serialNumb != null));
//            System.out.println("--> 4: "+(connDevs.length == 0));
//            if (Configuration.VENDOR_ID.equals(vendorId) && Configuration.PRODUCT_ID.equals(productId) && serialNumb != null
//                    && connDevs.length == 0) {
//            	return serialNumb;
//            } 
            if(connDevs.length == 0){
            	//System.out.println(vendorId + " - "+productId);
            }
            else {
            	serialNumber = findUsbSerialNumber(connDevs);
            }
        }
        return serialNumber;
	}
		
}
