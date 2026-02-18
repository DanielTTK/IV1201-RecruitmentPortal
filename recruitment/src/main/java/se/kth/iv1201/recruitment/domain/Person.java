package se.kth.iv1201.recruitment.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a row in the {@code person} table.
 *
 * <p>
 * This entity is used for both newly registered users and migrated legacy users.
 * Legacy users may have {@code NULL} values for {@code username} and {@code password}.
 * </p>
 *
 * <p>
 * The {@code roleId} is kept as an integer column for now (instead of a {@code Role} relation)
 * reduce coupling while implementing UC 5.2 and UC 5.3.
 * </p>
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

    /**
     * Role identifier stored in DB (1=recruiter, 2=applicant).
     */
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "username")
    private String username;

    /**
     * True if the row comes from the imported legacy DB.
     */
    @Column(name = "is_legacy", nullable = false)
    private boolean isLegacy = false;



    /**
     * Applications created by this person.
     *
     * <p>
     * Mapped by {@link Application#getPerson()}.
     * </p>
     */
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private List<Application> applications = new ArrayList<>();



    /**
     * Availability periods registered for this person.
     *
     * <p>
     * Mapped by {@link Availability#getPerson()}.
     * </p>
     */
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private List<Availability> availabilities = new ArrayList<>();




    /**
     * Competences and years of experience registered for this person.
     *
     * <p>
     * Mapped by {@link CompetenceProfile#getPerson()}.
     * </p>
     */
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private List<CompetenceProfile> competenceProfiles = new ArrayList<>();


    /**
     * Required by JPA, should not be used directly.
     */    
    public Person() {}

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


    public String getPassword() { 
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


    public String getUsername() { 
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public boolean isLegacy() {
    return isLegacy;
    }

    public void setLegacy(boolean legacy) {
        isLegacy = legacy;
    }


    public List<Application> getApplications() { 
        return applications; 
    }
    public List<Availability> getAvailabilities() { 
        return availabilities; 
    }
    public List<CompetenceProfile> getCompetenceProfiles() { 
        return competenceProfiles; 
    }


    /**
     * Equality is based on the primary key (personId).
     *
     * @param o The object to compare with.
     * @return {@code true} if both objects represent the same persisted row.
     */
    @Override
    public boolean equals(Object o) { //set to compare by personId, otherwise obj identity
        if (this == o) return true;
        if (!(o instanceof Person other)) return false;
        return personId != null && Objects.equals(personId, other.personId);
    }

    @Override
    public String toString() { return "Person{personId=" + personId + ", username=" + username + ", roleId=" + roleId + ", name=" + name + ", surname=" + surname + ", email=" + email + ", pnr=" + pnr + ", isLegacy=" + isLegacy + "}"; 
        }
    }