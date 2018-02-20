package com.starstar.models;

/**
 * Created by starstar on 17/5/5.
 */
public class DocNumber {
    public String getDocument_number() {
        return document_number;
    }

    public void setDocument_number(String document_number) {
        this.document_number = document_number;
    }

    public DocNumber(String document_number) {
        this.document_number = document_number;
    }

    public DocNumber() {
    }

    private String document_number;
}
