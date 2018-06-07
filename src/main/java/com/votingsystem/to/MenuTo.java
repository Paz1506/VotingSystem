package com.votingsystem.to;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Paz1506
 * Transfer object for Menu entity.
 */

public class MenuTo extends RootTo implements Serializable {

    public MenuTo(Integer id, String name, LocalDateTime date) {
        super(id);
        this.name = name;
        this.date = date;
    }

    @NotNull
    @NotBlank
    @Size(min = 3, max = 100, message = "length must between 3 and 100 characters")
    private String name;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;


    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MenuTo{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", id=" + id +
                '}';
    }
}
