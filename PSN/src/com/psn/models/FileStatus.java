package com.psn.models;

public enum FileStatus {

    START("In attesa", null), ENCODED("Codificato","-fx-background-color: #47d8e5;"), DECODED("Decodificato","-fx-background-color: #6ff271;"), ERROR_ENCODING("Errore","-fx-background-color: #e51b1b;"), ERROR_DECODING("Errore","-fx-background-color: #e51b1b;");

    private String label;
    
    private String uiRowStyle;

    FileStatus(String label, String uiRowStyle) {
        this.label = label;
        this.uiRowStyle = uiRowStyle;
    }

    /**
     * Retrieves the label associated to the status.
     * 
     * @return The label.
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * Retrieves the row UI style associated to the status.
     * 
     * @return The UI style of a row .
     */
    public String getRowUiStyle() {
        return uiRowStyle;
    }

}
