package com.starstar.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Review {
    private String belongtoUsername;
    private Integer rank;
    private Doc doc;
    public String getDocListId() {
        return docListId;
    }

    public void setDocListId(String docListId) {
        this.docListId = docListId;
    }

    private String docListId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    private String id;

    public Review() {
    }

    public Review(String docListId,String belongto, Doc doc, Integer rank ) {
        this.docListId=docListId;
        this.belongtoUsername = belongto;
        this.rank = rank;
        this.doc = doc;
    }

    public String getBelongtoUsername() {
        return belongtoUsername;
    }

    public void setBelongtoUsername(String belongtoUsername) {
        this.belongtoUsername = belongtoUsername;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Doc getDoc() {
        return doc;
    }

    public void setDoc(Doc doc) {
        this.doc = doc;
    }
}
