package se.kth.iv1201.recruitment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

/**
 * Corresponds to the {@code person} table.
 *
 * Notes:
 * - Many legacy rows have NULL {@code username} and {@code password}
 * - Application status is handled later since it's not currently represented in DB
 */

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Integer personId;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "pnr")
    private String pnr;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role_id") //1=recruiter, 2=applicant
    private Integer roleId;

    @Column(name = "username")
    private String username;


    public Person() {
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() { //can be null
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getUsername() { //can be null
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) { //set to compare by personId, otherwise obj identity
        if (this == o) return true;
        if (!(o instanceof Person other)) return false;
        return personId != null && Objects.equals(personId, other.personId);
    }

    @Override
    public int hashCode() {
        return (personId == null) ? System.identityHashCode(this) : personId.hashCode();
    }

    @Override
    public String toString() { return "Person{personId=" + personId + ", username=" + username + ", roleId=" + roleId + ", name=" + name + ", surname=" + surname + ", email=" + email + ", pnr=" + pnr + "}"; 
    }
    }