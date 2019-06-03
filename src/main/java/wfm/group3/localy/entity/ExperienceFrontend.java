package wfm.group3.localy.entity;

import wfm.group3.localy.utils.Enums;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class ExperienceFrontend implements Serializable {

    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_TIME;

    private Long id;

    private String name;

    private String description;

    private Enums.ExperienceType type;

    private BigDecimal price;

    private int maxGroupSize;

    private String address;

    private String duration;

    private String startTime;

    public ExperienceFrontend(Experience experience) {
        this.id = experience.getId();
        this.name = experience.getName();
        this.description = experience.getDescription();
        this.type = experience.getType();
        this.price = experience.getPrice();
        this.maxGroupSize = experience.getMaxGroupSize();
        this.address = experience.getLocation().getCity() + ", " + experience.getLocation().getStreet();
        this.duration = String.format("%02d", experience.getDuration().toHoursPart()) + ":" + String.format("%02d", experience.getDuration().toMinutesPart());
        if(experience.getStartTime() != null)
            this.startTime = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(experience.getStartTime());
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDuration() {
        return duration;
    }

    public String getStartTime() {
        return startTime;
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
