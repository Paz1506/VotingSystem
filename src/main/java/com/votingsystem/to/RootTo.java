package com.votingsystem.to;


import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Paz1506
 * Base class for all transfer objects.
 */

public abstract class RootTo {

    protected Integer id;

    public RootTo() {
    }

    public RootTo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonIgnore
    public boolean isNew() {
        return getId() == null;
    }
}
