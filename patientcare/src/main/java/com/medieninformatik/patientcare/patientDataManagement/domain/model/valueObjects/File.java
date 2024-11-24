package com.medieninformatik.patientcare.patientDataManagement.domain.model.valueObjects;

import com.medieninformatik.patientcare.patientDataManagement.domain.model.shared.Note;
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
}
