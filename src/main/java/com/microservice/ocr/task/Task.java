package com.microservice.ocr.task;

public class Task {
    private String url;

    public Task() {
    }

    public Task(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return " Url: " + url;
    }
}
