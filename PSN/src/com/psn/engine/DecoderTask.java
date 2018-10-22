package com.psn.engine;

import com.psn.models.FileStatus;
import com.psn.models.PSNFile;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class DecoderTask extends Task<ObservableList<PSNFile>> {

    ObservableList<PSNFile> observableFileList = null;

    public DecoderTask(ObservableList<PSNFile> observableFileList) {
        this.observableFileList = observableFileList;
    }

    @Override
    protected ObservableList<PSNFile> call() throws Exception {

        for (int i = 0; i < observableFileList.size(); i++) {
            PSNFile psnFile = observableFileList.get(i);
            try{
            	String psnFilePath = psnFile.getAbsolutePath();
            	Decoder.decode(psnFilePath);            	
            	psnFile.setStatus(FileStatus.DECODED);
                observableFileList.set(i, psnFile);
                updateProgress(i, observableFileList.size());
            }catch(Exception e){
            	psnFile.setStatus(FileStatus.ERROR_DECODING);
                observableFileList.set(i, psnFile);
                updateProgress(i, observableFileList.size());
                System.out.println("SONO QUI");
            }
        }
        return observableFileList;
    }
}
