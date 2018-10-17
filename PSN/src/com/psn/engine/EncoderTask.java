package com.psn.engine;

import com.psn.models.FileStatus;
import com.psn.models.PSNFile;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class EncoderTask extends Task<ObservableList<PSNFile>> {

    ObservableList<PSNFile> observableFileList = null;

    public EncoderTask(ObservableList<PSNFile> observableFileList) {
        this.observableFileList = observableFileList;
    }

    @Override
    protected ObservableList<PSNFile> call() throws Exception {

        for (int i = 0; i < observableFileList.size(); i++) {
            PSNFile psnFile = observableFileList.get(i);
            Thread.sleep(3000);
            psnFile.setStatus(FileStatus.ENCODED);
            observableFileList.set(i, psnFile);
            updateProgress(i, observableFileList.size());
        }
        //
        // for (int i = 0; i < 500; i++) {
        // updateProgress(i, 500);
        // Thread.sleep(5);
        // }

        return observableFileList;
    }
}
