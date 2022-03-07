package com.fairydeck.task.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String filename;
    private int min;
    private int max;
    private String charMap;
    private BigInteger count;

    @Enumerated
    private Status status;

    public Job(int min, int max, BigInteger limit, String charMap) {
        this.min = min;
        this.max = max;
        this.count = limit;
        this.charMap = charMap;
    }
}
