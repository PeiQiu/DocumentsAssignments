package com.starstar.models;

public class Doc {

    private String title;
   private String type;
   private String html_url;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public Boolean getSignificant() {
        return significant;
    }

    public void setSignificant(Boolean significant) {
        this.significant = significant;
    }

    private Boolean significant;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDocument_number() {
        return document_number;
    }

    public void setDocument_number(String document_number) {
        this.document_number = document_number;
    }

    private String document_number;

    public Doc() {
    }


}
