package wfm.group3.localy.entity;

import wfm.group3.localy.utils.Enums;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private LocalDate birthday;

    @Column
    @Enumerated(EnumType.STRING)
    private Enums.PersonType type;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "person_experience",
            joinColumns = {@JoinColumn(name = "person_id")},
            inverseJoinColumns = {@JoinColumn(name = "experience_id")}
    )
    private Set<Experience> offers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Enums.PersonType getType() {
        return type;
    }

    public void setType(Enums.PersonType type) {
        this.type = type;
    }

    public Set<Experience> getOffers() {
        return offers;
    }

    public void setOffers(Set<Experience> offers) {
        this.offers = offers;
    }
}
