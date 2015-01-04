package com.palmak.themagichat.cards;

/**
 * Created by Matthew on 12/3/2014.
 */
public class Expansion implements Comparable<Expansion> {

    Long id;
    String shortName;
    String name;

    public Expansion(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    public Expansion(Long id, String name, String shortName) {
        this(name, shortName);
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int compareTo(Expansion exp) {
        return name.compareTo(exp.getName());
    }
}