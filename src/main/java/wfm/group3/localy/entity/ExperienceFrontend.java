package wfm.group3.localy.entity;

import wfm.group3.localy.utils.Enums;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class ExperienceFrontend implements Serializable {


    private Long id;

    private String name;

    private String description;

    private Enums.ExperienceType type;

    private BigDecimal price;

    private int maxGroupSize;

    private Address location;

    private LocalTime duration;

    public ExperienceFrontend(Experience experience) {
        this.id = experience.getId();
        this.name = experience.getName();
        this.description = experience.getDescription();
        this.type = experience.getType();
        this.price = experience.getPrice();
        this.maxGroupSize = experience.getMaxGroupSize();
        this.location = experience.getLocation();
        this.duration = experience.getDuration();
    }

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
