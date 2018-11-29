package com.psn.usb;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class StreamGobbler implements Runnable {
    private InputStream inputStream;
    private Consumer<String> consumer;
    private StringBuilder stringBuilder;
 
    public StreamGobbler(InputStream inputStream, Consumer<String> consumer, StringBuilder stringBuilder) {
        this.inputStream = inputStream;
        this.consumer = consumer;
        this.stringBuilder = stringBuilder;
    }
     
    @Override
    public void run() {
    	new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(l -> stringBuilder.append(l));
    	//new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
    }
}
