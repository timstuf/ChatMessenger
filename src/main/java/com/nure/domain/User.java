package com.nure.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nure.database.repositories.impl.UserRepository;
import com.nure.exceptions.NoUserException;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @JsonBackReference
    private List<Message> sentMessages = new ArrayList<>();

    @OneToMany(mappedBy = "userTo")
    @JsonBackReference
    private List<Message> received = new ArrayList<>();

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.status = Status.ACTIVE;
    }
    public static User asObject(String name){
        UserRepository userRepository = UserRepository.getInstance();
                return userRepository.getUserByLogin(name).orElseThrow(()-> new NoUserException(name));
    }
    public enum Status {ACTIVE, LOGGED_OUT}
}

