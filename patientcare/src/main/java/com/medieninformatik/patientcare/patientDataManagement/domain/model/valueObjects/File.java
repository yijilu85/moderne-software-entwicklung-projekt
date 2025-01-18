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
    private String mimeType;


    @ManyToOne
    @JoinColumn(name = "note_id")
    private Note note;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID
    private Long id;

    public File() {
    }

    public File(Date timestamp, String url, String description, String mimeType) {
        this.timestamp = timestamp;
        this.description = description;
        this.url = url;
        this.mimeType = mimeType;
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
