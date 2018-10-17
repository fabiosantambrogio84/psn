package com.psn.models;

public enum FileStatus {

    START("Start"), ENCODING("In codifica"), ENCODED("Codificato"), ERROR_ENCODING("Errore nella codifica"), DECODING(
            "In decodifica"), DECODED("Decodificato"), ERROR_DECODING("Errore nella decodifica");

    private String label;

    FileStatus(String label) {
        this.label = label;
    }

    /**
     * Retrieves the label associated to the status.
     * 
     * @return The label.
     */
    public String getLabel() {
        return label;
    }

}
