package com.LogFile.LogFile.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = @Index(columnList = "time", unique = true))
public class Logfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String date;
    @Column(unique = true)
    private String time;
    private String severity;
    private String thread;
    private String className;
    @Column(columnDefinition = "TEXT")
    private String message;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Logfile logfile = (Logfile) o;
        return Objects.equals(time, logfile.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time);
    }

}
