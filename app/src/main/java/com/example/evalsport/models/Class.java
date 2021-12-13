package com.example.evalsport.models;

import java.util.List;

public class Class {
    private String name;
    private List<Sport> sports;

    public Class(String name, List<Sport> sports) {
        this.name = name;
        this.sports = sports;
    }

    public String getName() {
        return name;
    }

    public List<Sport> getSports() {
        return sports;
    }


}
