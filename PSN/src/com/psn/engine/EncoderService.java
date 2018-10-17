package com.psn.engine;

import com.psn.models.PSNFile;

import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class EncoderService extends Service<ObservableList<PSNFile>> {

    ObservableList<PSNFile> observableList = null;

    public EncoderService(ObservableList<PSNFile> observableList) {
        this.observableList = observableList;
    }

    @Override
    protected Task<ObservableList<PSNFile>> createTask() {
        return new EncoderTask(observableList);
    }

}
