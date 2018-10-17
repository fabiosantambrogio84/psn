package com.psn.usb;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbServices;

import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

public class UsbReader {

    public static void listDevices() {

        // Create the libusb context
        Context context = new Context();

        // Initialize the libusb context
        int result = LibUsb.init(context);
        if (result < 0) {
            throw new LibUsbException("Unable to initialize libusb", result);
        }

        // Read the USB device list
        DeviceList list = new DeviceList();
        result = LibUsb.getDeviceList(context, list);
        if (result < 0) {
            throw new LibUsbException("Unable to get device list", result);
        }

        try {
            // Iterate over all devices and list them
            for (Device device : list) {
                int address = LibUsb.getDeviceAddress(device);
                int busNumber = LibUsb.getBusNumber(device);
                DeviceDescriptor descriptor = new DeviceDescriptor();
                result = LibUsb.getDeviceDescriptor(device, descriptor);
                if (result < 0) {
                    throw new LibUsbException("Unable to read device descriptor", result);
                }
                System.out.format("Bus %03d, Device %03d: Vendor %04x, Product %04x%n", busNumber, address, descriptor.idVendor(),
                        descriptor.idProduct());

            }
        } finally {
            // Ensure the allocated device list is freed
            LibUsb.freeDeviceList(list, true);
        }

        // Deinitialize the libusb context
        LibUsb.exit(context);
    }

    private static void processDevice(final UsbDevice device) {
        // When device is a hub then process all child devices
        if (device.isUsbHub()) {
            final UsbHub hub = (UsbHub) device;
            List<UsbDevice> l = hub.getAttachedUsbDevices();
            for (UsbDevice child : l) {
                processDevice(child);
            }
        }

        // When device is not a hub then dump its name.
        else {
            try {
                dumpName(device);
            } catch (Exception e) {
                // On Linux this can fail because user has no write permission
                // on the USB device file. On Windows it can fail because
                // no libusb device driver is installed for the device
                System.err.println("Ignoring problematic device: " + e);
            }
        }
    }

    private static void dumpName(final UsbDevice device) throws UnsupportedEncodingException, UsbException {
        // Read the string descriptor indices from the device descriptor.
        // If they are missing then ignore the device.
        final UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
        final byte iManufacturer = desc.iManufacturer();
        final byte iProduct = desc.iProduct();
        if (iManufacturer == 0 || iProduct == 0)
            return;

        // Dump the device name
        System.out.println(device.getString(iManufacturer) + " " + device.getString(iProduct));
    }

    public static void main(final String[] args) throws UsbException {
        // Get the USB services and dump information about them
        final UsbServices services = UsbHostManager.getUsbServices();

        // Dump the root USB hub
        processDevice(services.getRootUsbHub());
    }

    private static UsbDevice findDevice(UsbHub hub) {
        for (UsbDevice device : (List<UsbDevice>) hub.getAttachedUsbDevices()) {
            UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
            System.out.println(desc.idVendor() + " - " + desc.idProduct() + " - " + device.isUsbHub());
            // if (desc.idVendor() == vendorId && desc.idProduct() == productId) return device;
            // if (device.isUsbHub())
            // {
            // device = findDevice((UsbHub) device, vendorId, productId);
            // if (device != null) return device;
            // }
        }
        return null;
    }

}
