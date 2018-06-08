package com.votingsystem.to;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Paz1506
 * Transfer object for User entity.
 */

public class UserTo extends RootTo implements Serializable {

    public UserTo() {
    }

    public UserTo(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserTo(Integer id, String name, String email, String password) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @NotBlank
    @NotNull
    @Size(min = 3, max = 100, message = "length must between 5 and 16 characters")
    private String name;

    @Email
    @NotBlank
    @NotNull
    @Size(max = 100, message = "length must < 100 characters")
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 5, max = 16, message = "length must between 5 and 16 characters")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                '}';
    }
}
