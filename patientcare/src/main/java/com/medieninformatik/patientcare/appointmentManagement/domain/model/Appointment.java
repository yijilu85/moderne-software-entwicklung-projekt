package com.medieninformatik.patientcare.appointmentManagement.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.shared.Note;
import com.medieninformatik.patientcare.userManagement.domain.model.Patient;
import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Appointment{

    public Appointment() {
    }

    public enum Type{
        ONLINE,
        OFFLINE
    };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID
    private Long id;

    @ManyToOne
    @JoinColumn
    private Patient patient;

    @ManyToOne
    @JoinColumn
    private Doctor doctor;

    @ManyToOne
    @JoinColumn
    private User creator;

    private LocalDateTime createdAt;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime editDateTime;
    private String title;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("appointment")
    private List<Note> notes;

    private Type type;


    //Konstruktor
    public Appointment(Doctor doctor, User creator, LocalDateTime startDateTime,LocalDateTime endDateTime, LocalDateTime createdAt){

        this.doctor = doctor;
        this.creator = creator;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.createdAt = createdAt;
        this.notes = new ArrayList<>();
}

    //Konstruktor
    public Appointment(Doctor doctor, Patient patient, User creator, Type type, LocalDateTime startDateTime,LocalDateTime endDateTime, LocalDateTime createdAt){

        this.doctor = doctor;
        this.patient = patient;
        this.creator = creator;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.createdAt = createdAt;
        this.notes = new ArrayList<>();
    }
    /////////////////////////////////////////////////////////////////////////Getter
    public long getId() {
        return this.id;
    }

    public LocalDateTime getStartDateTime() {
        return this.startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return this.endDateTime;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getEditDateTime() {
        return this.editDateTime;
    }

    public User getCreator() {
        return this.creator;
    }

    public Type getType() {
        return this.type;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public Doctor getDoctor() {
        return this.doctor;
    }

    public List<Note> getNotes() {
        return this.notes;
    }

    /////////////////////////////////////////////////////////////////////////Setter
    public void setCreatedAt() {
        this.createdAt = createdAt;
    }

    public void setCreator() {
        this.creator = creator;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setEditDateTime(LocalDateTime editDateTime) {
        this.editDateTime = editDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void addNote(Note note) {
        this.notes.add(note);
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void removeSingleNote(Note note) {

        if (this.notes.size() != 0){
            for(int i = 0; i < this.notes.size(); i++){
                if (this.notes.get(i).getId().equals(note.getId())){
                    this.notes.remove(i);
                    break;
                }
            }
        }
    }

    public void removeAllNotes() {
        if (this.notes.size() != 0) {
            this.notes.clear();
        }
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", patient=" + patient +
                ", doctor=" + doctor +
                ", creator=" + creator +
                ", createdAt=" + createdAt +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", editDateTime=" + editDateTime +
                ", title='" + title + '\'' +
                ", notes=" + notes +
                ", type=" + type +
                '}';
    }


    public void clearPatient(){
        this.patient = null;
    }
}