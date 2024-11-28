package com.medieninformatik.patientcare.userManagement.domain.model.valueObjects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalSpeciality {
    private final Speciality speciality;

    public enum Speciality {
        ALLGEMEINMEDIZIN("Allgemeinmedizin"),
        KARDIOLOGIE("Kardiologie"),
        DERMATOLOGIE("Dermatologie"),
        NEUROLOGIE("Neurologie"),
        PSYCHIATRIE("Psychiatrie"),
        CHIRURGIE("Chirurgie"),
        GYNAEKOLOGIE("Gynäkologie"),
        ORTHOPAEDIE("Orthopädie"),
        PAEDIATRIE("Pädiatrie"),
        RADIOLOGIE("Radiologie"),
        UROLOGIE("Urologie"),
        HNO("Hals-Nasen-Ohren-Heilkunde"),
        ZAHNMEDIZIN("Zahnmedizin"),
        ANAESTHESIOLOGIE("Anästhesiologie"),
        ONKOLOGIE("Onkologie"),
        ENDOKRINOLOGIE("Endokrinologie"),
        GASTROENTEROLOGIE("Gastroenterologie");

        private final String label;

        // Constructor for the enum
        Speciality(String label) {
            this.label = label;
        }

        // Getter for the German name
        public String getLabel() {
            return label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    public MedicalSpeciality(Speciality speciality) {this.speciality = speciality;}

    public Speciality getSpeciality() {
        return this.speciality;
    }

    @Override
    public String toString() {
        return "Fachgebiet: " + this.speciality.getLabel();
    }
}

