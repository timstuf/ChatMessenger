package com.nure.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "my_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;


    @Column(unique = true, nullable = false)
    private String login;
    @JsonIgnore
    @Column(nullable = false)
    private String password;
    @JsonIgnore
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "userFrom")
    @JsonBackReference(value = "sent")
    private List<Message> sentMessages = new ArrayList<>();

    @OneToMany(mappedBy = "userTo")
    @JsonBackReference(value = "received")
    private List<Message> received = new ArrayList<>();

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.status = Status.ACTIVE;
    }

    public User(String login) {
        this.login = login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return login.equals(user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
    public enum Status {ACTIVE, LOGGED_OUT}
}

