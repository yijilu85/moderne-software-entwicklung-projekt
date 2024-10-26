package com.medieninformatik.patientcare.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class File {
    private Date timestamp;
    private String url;
    private String description;

    @ManyToOne
    @JoinColumn(name = "note")
    private Note note;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID
    private Long id;

    public File() {
    }

    public File(Date timestamp, String url, String description) {
        this.timestamp = timestamp;
        this.description = description;
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
