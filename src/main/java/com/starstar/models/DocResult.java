package com.starstar.models;

import java.util.List;

public class DocResult {
    private Integer count;

    public String getPrevious_page_url() {
        return previous_page_url;
    }

    public void setPrevious_page_url(String previous_page_url) {
        this.previous_page_url = previous_page_url;
    }

    private String previous_page_url;
    public Integer getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Integer total_pages) {
        this.total_pages = total_pages;
    }

    private Integer total_pages;
    public DocResult() {
    }

    private String description;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public List<DocNumber> getResults() {
        return results;
    }

    public void setResults(List<DocNumber> results) {
        this.results = results;
    }

    private String next_page_url;
    private List<DocNumber> results;


}
