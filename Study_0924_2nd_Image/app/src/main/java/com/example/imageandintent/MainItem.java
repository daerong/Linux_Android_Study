package com.example.imageandintent;

public class MainItem {
    public MainItem(int image, String title, String man) {
        this.image = image;
        this.title = title;
        this.man = man;
    }

    int image;
    String title;
    String man;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMan() {
        return man;
    }

    public void setMan(String man) {
        this.man = man;
    }
}
