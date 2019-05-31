package wfm.group3.localy.entity;

import wfm.group3.localy.utils.Enums;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
public class Experience {

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
}
