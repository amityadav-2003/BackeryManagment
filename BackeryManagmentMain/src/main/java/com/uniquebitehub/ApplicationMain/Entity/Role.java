package com.uniquebitehub.ApplicationMain.Entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(
    name = "roles",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "role_name")
    }
)
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==========================
    // PRIMARY KEY
    // ==========================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================
    // FIELDS
    // ==========================
    @Column(name = "role_name", nullable = false, length = 20)
    private String roleName;   // ADMIN, STAFF, CUSTOMER

    // ==========================
    // RELATIONSHIP
    // ==========================
    @OneToMany(mappedBy = "role")
    private List<User> users;

    // ==========================
    // CONSTRUCTOR
    // ==========================
    public Role() {
    }

    // ==========================
    // GETTERS & SETTERS
    // ==========================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
