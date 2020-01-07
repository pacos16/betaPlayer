package com.example.musicplayer.model;


import java.io.Serializable;

public class Song implements Serializable {

    private String name;
    private String group;
    private String info;
    private int duration;
    private String path;



    public Song(String title,String group,int duration,String path){
        this.name=title;
        this.group=group;
        this.path=path;
        this.duration=duration;

    }
    public Song(){}

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public String getInfo() {
        return info;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
