package Java.services;

import com.medieninformatik.patientcare.entities.*;
import com.medieninformatik.patientcare.entities.Diagnosis;
import com.medieninformatik.patientcare.entities.Treatment;

import com.medieninformatik.patientcare.repo.PatientRepo;
import com.medieninformatik.patientcare.services.NoteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoteServiceTest {

    @Mock
    private PatientRepo patientRepo;
    private NoteService noteService = new NoteService(patientRepo);

    //unit tests for diagnosis class
    @Test
    void createDiagnosis_referencesNotNull() {
        Patient patient = new Patient("John", "Doe");
        Doctor doctor = new Doctor("Dr. Francis", "Smith");
        User creator = doctor;
        Date date = new Date();
        String icdCode = "A00.1";
        String recommendation = "Patient soll regelmäßig Wasser trinken";

        Diagnosis diagnosis = noteService.createDiagnosis(patient, doctor, date, creator,
                icdCode, recommendation);

        assertNotNull(diagnosis.patient);
        assertNotNull(diagnosis.doctor);
        assertNotNull(diagnosis.creator);
        assertNotNull(diagnosis.date);
        assertNotNull(diagnosis.icdCode);
    }

    @Test
    void icdCodeIsValid() {
        //check for icdCode 10 or icdCode 11 pattern - allow both
        String icdCode10 = "J45.0";
        String icdCode11 = "5A11";
        String invalidIcdCode1 = "12ZASD12";

        assertTrue(noteService.isValidIcdCode(icdCode10));
        assertTrue(noteService.isValidIcdCode(icdCode11));
        assertFalse(noteService.isValidIcdCode(invalidIcdCode1));
    }

    //unit tests for treatment class

    @Test
    void createTreatment_referencesNotNull() {
        Patient patient = new Patient("John", "Doe");
        Doctor doctor = new Doctor("Dr. Francis", "Smith");
        User creator = doctor;
        Date date = new Date();
        String action = "Tetanus Impfung verabreicht";
        Diagnosis diagnosis = mock(Diagnosis.class);
        Treatment treatment = noteService.createTreatment(patient, doctor, date, creator,
                diagnosis, action);

        assertNotNull(treatment.patient);
        assertNotNull(treatment.doctor);
        assertNotNull(treatment.creator);
        assertNotNull(treatment.date);
        assertNotNull(treatment.diagnosis);
        assertNotNull(treatment.action);
    }

    @Test
    void createTreatment_ShouldThrowExecptionOnNullRefrences() {
        Patient patient = new Patient("John", "Doe");
        Doctor doctor = new Doctor("Dr. Francis", "Smith");
        User creator = doctor;
        Date date = new Date();

        Diagnosis diagnosis = new Diagnosis(patient, doctor, creator, date, "A00.1", "Empfehlungstext");
        String action = "Tetanus Impfung verabreicht";

        assertThrows(IllegalArgumentException.class, () -> noteService.createTreatment(null, doctor, date, creator,
                diagnosis, action));
        assertThrows(IllegalArgumentException.class, () -> noteService.createTreatment(patient, null, date, creator,
                diagnosis, action));
        assertThrows(IllegalArgumentException.class, () -> noteService.createTreatment(patient, doctor, null, creator,
                diagnosis, action));
        assertThrows(IllegalArgumentException.class, () -> noteService.createTreatment(patient, doctor, date, null,
                diagnosis, action));
        assertThrows(IllegalArgumentException.class, () -> noteService.createTreatment(patient, doctor, date, creator,
                null, action));
        assertThrows(IllegalArgumentException.class, () -> noteService.createTreatment(patient, doctor, date, creator,
                diagnosis, null));
    }

    @Test
    void actionHasMinLength() {
        // check if action has min 10 chars
        String actionValid = "Tetanus Impfung verabreicht";
        String actionInvalid = "Tetanus";
        assertTrue(noteService.actionHasMinLength(actionValid));
        assertFalse(noteService.actionHasMinLength(actionInvalid));
    }

    //unit tests note super class

    @Test
    // check if note users have same id as appointment users
    void addNoteToAppointment_valid() {
        Patient patient1 = new Patient("John", "Doe");
        Patient patient2 = new Patient("Jana", "Doe");
        Doctor doctor1 = new Doctor("Dr. Francis", "Smith");
        Doctor doctor2 = new Doctor("Dr. Jana", "Smith");

        when(patient1.getId()).thenReturn(1L);
        when(patient2.getId()).thenReturn(2L);
        when(doctor1.getId()).thenReturn(3L);
        when(doctor2.getId()).thenReturn(4L);

        Appointment appointmentCorrect = mock(Appointment.class);

        when(appointmentCorrect.getPatient()).thenReturn(patient1);
        when(appointmentCorrect.getDoctor()).thenReturn(doctor1);

        Appointment appointmentIncorrect = mock(Appointment.class);

        when(appointmentCorrect.getPatient()).thenReturn(patient2);
        when(appointmentCorrect.getDoctor()).thenReturn(doctor2);

        User creator = doctor1;
        Date date = new Date();
        String icdCode = "A00.1";
        String recommendation = "Patient soll regelmäßig Wasser trinken";

        Diagnosis diagnosis = noteService.createDiagnosis(patient1, doctor1, date, creator,
                icdCode, recommendation);

        assertTrue(noteService.noteUsersEqualsAppointmentUsers(appointmentCorrect, diagnosis));
        assertFalse(noteService.noteUsersEqualsAppointmentUsers(appointmentIncorrect, diagnosis));

        // check if note or appointments are not null
        assertThrows(IllegalArgumentException.class, () -> noteService.addNoteToAppointment(null, diagnosis));
        assertThrows(IllegalArgumentException.class, () -> noteService.addNoteToAppointment(appointmentCorrect, null));
    }

    @Test
    void noteFileType_isValid(){

        // ony allow text files, pdf files, image files, audio files, video files
        String mimeTypeValidText = "text/plain";
        String mimeTypeValidPdf = "application/pdf";
        String mimeTypeValidImage = "image/jpg";
        String mimeTypeValidVideo = "video/mp4";
        String mimeTypeInvalid1 = "application/xml";
        String mimeTypeInvalid2 = "font/woff";

        assertTrue(noteService.noteFileTypeIsValidMime(mimeTypeValidText));
        assertTrue(noteService.noteFileTypeIsValidMime(mimeTypeValidPdf));
        assertTrue(noteService.noteFileTypeIsValidMime(mimeTypeValidImage));
        assertTrue(noteService.noteFileTypeIsValidMime(mimeTypeValidVideo));
        assertFalse(noteService.noteFileTypeIsValidMime(mimeTypeInvalid1));
        assertFalse(noteService.noteFileTypeIsValidMime(mimeTypeInvalid2));
    }
}