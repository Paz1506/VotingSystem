package com.votingsystem.to;

import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * ************************************
 * Transfer object for User
 * ************************************
 */
public class UserTo extends RootTo implements Serializable {

    public UserTo(){}

    public UserTo(Integer id, String name, String email, String password) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @NotBlank
    @Size(min = 3, max = 100)
//    @SafeHtml // https://stackoverflow.com/questions/17480809
    private String name;

    @Email
    @NotBlank
    @Size(max = 100)
//    @SafeHtml
    private String email;

    @NotBlank
    @Size(min = 5, max = 32, message = "length must between 5 and 32 characters")
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
