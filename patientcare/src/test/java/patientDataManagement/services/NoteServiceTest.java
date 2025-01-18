package patientDataManagement.services;

import com.medieninformatik.patientcare.appointmentManagement.domain.model.Appointment;
import com.medieninformatik.patientcare.appointmentManagement.services.AppointmentService;
import com.medieninformatik.patientcare.patientDataManagement.infrastructure.repositories.NoteRepo;
import com.medieninformatik.patientcare.shared.services.HelperService;

import com.medieninformatik.patientcare.patientDataManagement.domain.model.Diagnosis;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.Treatment;

import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.DoctorRepo;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.PatientRepo;
import com.medieninformatik.patientcare.patientDataManagement.services.NoteService;
import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.domain.model.Patient;
import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoteServiceTest {

    @Mock
    private PatientRepo patientRepo;
    private NoteService noteService;
    private HelperService helperService;
    @Mock
    private NoteRepo noteRepo;
    @Mock
    private DoctorRepo doctorRepo;
    @Mock
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        this.helperService = new HelperService();
        this.noteService = new NoteService(patientRepo, helperService, appointmentService, noteRepo, doctorRepo);
    }

    //unit tests for diagnosis class
    @Test
    void createDiagnosis_referencesNotNull() {
        Patient patient = new Patient("John", "Doe");
        Doctor doctor = new Doctor("Dr. Francis", "Smith");
        Appointment appointment = mock(Appointment.class);

        User creator = doctor;
        Date date = new Date();
        String icdCode = "A00.1";
        String recommendation = "Patient soll regelmäßig Wasser trinken";

        Diagnosis diagnosis = noteService.createDiagnosis(patient, doctor, appointment, date,
                icdCode, recommendation);

        assertNotNull(diagnosis.getPatient());
        assertNotNull(diagnosis.getDoctor());
        assertNotNull(diagnosis.getIcdCode());
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
        Appointment appointment = mock(Appointment.class);
        Date date = new Date();
        String action = "Tetanus Impfung verabreicht";
        String icdCode = "J45.0";
        String recommendation = "Entspannung";

        Treatment treatment = noteService.createTreatment(patient, doctor, appointment, date,
                icdCode, recommendation, action);

        assertNotNull(treatment.getPatient());
        assertNotNull(treatment.getDoctor());
//        assertNotNull(treatment.getCreator());
//        assertNotNull(treatment.getDate());
        assertNotNull(treatment.getIcdCode());
        assertNotNull(treatment.getRecommendation());
        assertNotNull(treatment.getAction());
    }

    @Test
    void createTreatment_ShouldThrowExecptionOnNullRefrences() {
        Patient patient = new Patient("John", "Doe");
        Doctor doctor = new Doctor("Dr. Francis", "Smith");
        Appointment appointment = mock(Appointment.class);
        Date date = new Date();
        String recommendation = "Patient soll regelmäßig Wasser trinken";
        String icdCode = "J45.0";
        Diagnosis diagnosis = new Diagnosis("A00.1", "Empfehlungstext");
        String action = "Tetanus Impfung verabreicht";

        assertThrows(IllegalArgumentException.class, () -> noteService.createTreatment(null, doctor, appointment, date,
                icdCode, recommendation, action));
        assertThrows(IllegalArgumentException.class, () -> noteService.createTreatment(patient, null, appointment, date,
                icdCode, recommendation, action));
        assertThrows(IllegalArgumentException.class, () -> noteService.createTreatment(patient, doctor, appointment,
                null,
                icdCode,recommendation, action));
        assertThrows(IllegalArgumentException.class, () -> noteService.createTreatment(patient, doctor, appointment, null,
                icdCode, recommendation, action));
        assertThrows(IllegalArgumentException.class, () -> noteService.createTreatment(patient, doctor, appointment,
                date,
                null, recommendation, action));
        assertThrows(IllegalArgumentException.class, () -> noteService.createTreatment(patient, doctor, appointment,
                date,
                icdCode, null,
                 null));
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
        Patient patient1 = mock(Patient.class);
        Patient patient2 = mock(Patient.class);
        Doctor doctor1 = mock(Doctor.class);
        Doctor doctor2 = mock(Doctor.class);

        when(patient1.getId()).thenReturn(1L);
        when(patient2.getId()).thenReturn(2L);
        when(doctor1.getId()).thenReturn(3L);
        when(doctor2.getId()).thenReturn(4L);

        Appointment appointmentCorrect = mock(Appointment.class);

        when(appointmentCorrect.getPatient()).thenReturn(patient1);
        when(appointmentCorrect.getDoctor()).thenReturn(doctor1);

        Appointment appointmentIncorrect = mock(Appointment.class);

        when(appointmentIncorrect.getPatient()).thenReturn(patient2);
        when(appointmentIncorrect.getDoctor()).thenReturn(doctor2);

        User creator = doctor1;
        Date date = new Date();
        String icdCode = "A00.1";
        String recommendation = "Patient soll regelmäßig Wasser trinken";

        Diagnosis diagnosis = noteService.createDiagnosis(patient1, doctor1, appointmentCorrect, date,
                icdCode, recommendation);

        assertTrue(noteService.noteUsersEqualsAppointmentUsers(appointmentCorrect, diagnosis));
        assertFalse(noteService.noteUsersEqualsAppointmentUsers(appointmentIncorrect, diagnosis));

        // check if note or appointments are not null
        assertThrows(IllegalArgumentException.class, () -> noteService.addDiagnosisToAppointment(null, diagnosis));
        assertThrows(IllegalArgumentException.class, () -> noteService.addDiagnosisToAppointment(appointmentCorrect, null));
    }

    @Test
    void noteFileType_isValid() {

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