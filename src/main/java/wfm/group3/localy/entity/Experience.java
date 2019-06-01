package wfm.group3.localy.entity;

import wfm.group3.localy.utils.Enums;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Experience implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    @Enumerated(EnumType.STRING)
    private Enums.ExperienceType type;

    @Column
    private BigDecimal price;

    @Column
    private int maxGroupSize;

    @ManyToOne
    private Address location;

    @Column
    private LocalTime duration;

    @ManyToMany(mappedBy = "offers")
    private Set<Person> offeredBy = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Enums.ExperienceType getType() {
        return type;
    }

    public void setType(Enums.ExperienceType type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getMaxGroupSize() {
        return maxGroupSize;
    }

    public void setMaxGroupSize(int maxGroupSize) {
        this.maxGroupSize = maxGroupSize;
    }

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    public Set<Person> getOfferedBy() {
        return offeredBy;
    }

    public void setOfferedBy(Set<Person> offeredBy) {
        this.offeredBy = offeredBy;
    }

    @Override
    public String toString() {
        return "Experience{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                '}';
    }
}
