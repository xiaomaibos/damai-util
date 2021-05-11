package com.flyme.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zzzz76
 */
public class Show {
    private Integer id;
    private String name;
    private Date time;
    private Integer programId;

    private Set<Level> levels = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public Set<Level> getLevels() {
        return levels;
    }

    public void setLevels(Set<Level> levels) {
        this.levels = levels;
    }
}
