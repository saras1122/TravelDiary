package com.example.notes_app;

import java.sql.Array;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Note {

    String title;
    String content;
    Timestamp timestamp;
    ArrayList<Integer> arr;
    ArrayList<Integer> start;
    ArrayList<Integer> end;
    String color;
    Boolean flag=false;
    String imageUrl;
    public Note(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public ArrayList<Integer> getArr() {
        return arr;
    }

    public void setArr(ArrayList<Integer> arr) {
        this.arr = arr;
    }
    public ArrayList<Integer> getStart() {
        return start;
    }

    public void setStart(ArrayList<Integer> start) {
        this.start = start;
    }
    public ArrayList<Integer> getEnd() {
        return end;
    }

    public void setEnd(ArrayList<Integer> end) {
        this.end = end;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
