package com.medieninformatik.patientcare.patientDataManagement.domain.model;

import com.medieninformatik.patientcare.patientDataManagement.domain.model.shared.Note;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class NoteFile extends Note {
    private String url;
    private String description;
    private String mimeType;


    public NoteFile() {
    }

    public NoteFile(String url, String description, String mimeType) {
        this.description = description;
        this.url = url;
        this.mimeType = mimeType;
        this.setNoteType(this.getClass().getSimpleName().toUpperCase());
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

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
