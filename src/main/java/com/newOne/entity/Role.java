package com.newOne.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "roles")
public class Role extends TrackingColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "role_name", nullable = false, unique = true, length = 50)
    private String roleName;

    @OneToMany
    private List<User> users;

    // Constructors
    public Role() {
    }

    public Role(String roleName) {
        this.roleName = roleName;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
