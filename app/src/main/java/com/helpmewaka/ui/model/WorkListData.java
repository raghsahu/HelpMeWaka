package com.helpmewaka.ui.model;

/**
 * Created by Ravindra Birla on 14/09/2019.
 */
public class WorkListData {
    public String title;
    public String name;
    public int image;

    public WorkListData(String title, String name, int image) {
        this.title = title;
        this.name = name;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
