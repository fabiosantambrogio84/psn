package com.psn.engine;

import com.psn.models.PSNFile;

import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class DecoderService extends Service<ObservableList<PSNFile>> {

    ObservableList<PSNFile> observableList = null;

    public DecoderService(ObservableList<PSNFile> observableList) {
        this.observableList = observableList;
    }

    @Override
    protected Task<ObservableList<PSNFile>> createTask() {
        return new DecoderTask(observableList);
    }

}
