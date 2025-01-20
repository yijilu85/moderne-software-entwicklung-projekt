package appointmentManagement.services;

import com.medieninformatik.patientcare.appointmentManagement.domain.model.Appointment;
import com.medieninformatik.patientcare.appointmentManagement.infrastructure.repositories.AppointmentRepo;
import com.medieninformatik.patientcare.appointmentManagement.services.AppointmentService;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.Diagnosis;
import com.medieninformatik.patientcare.patientDataManagement.services.NoteService;
import com.medieninformatik.patientcare.shared.services.HelperService;
import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.domain.model.Patient;
import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.DoctorRepo;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.PatientRepo;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.repositories.NoteRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class AppointmentServiceTest {

    @Mock
    private PatientRepo patientRepo;

    @Mock
    private NoteRepo noteRepo;

    @Mock
    private DoctorRepo doctorRepo;

    @Mock
    private AppointmentRepo appointmentRepo;

    @InjectMocks
    private AppointmentService appointmentService;

    @InjectMocks
    private NoteService noteService;

    private Appointment systemAppointment;
    private Appointment patientAppointment;

    private LocalDateTime now;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        Doctor doctor = new Doctor("1", "Dr. House");
        Patient patient = new Patient("1001", "Max Mustermann");
        User creator = new Doctor("0", "System");

        now = LocalDateTime.now();
        LocalDateTime startDateTime = LocalDateTime.of(2024, Month.NOVEMBER, 25, 12, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, Month.NOVEMBER, 25, 12, 15);
        Appointment.Type type = Appointment.Type.OFFLINE;

        this.systemAppointment = new Appointment(doctor, creator, startDateTime, endDateTime, now);
        this.patientAppointment = new Appointment(doctor, patient, creator, type, startDateTime, endDateTime, now);
    }

    @Test
    void createAppointmentSlot() {
        appointmentService.createAppointmentSlot(this.systemAppointment.getDoctor(),
                this.systemAppointment.getCreator(), this.systemAppointment.getStartDateTime(),
                this.systemAppointment.getEndDateTime(), this.systemAppointment.getCreatedAt());
    }

    @Test
    void scheduleAppointment() {
        appointmentService.scheduleAppointment(this.systemAppointment, patientAppointment.getPatient(), patientAppointment.getType());
    }

    @Test
    void testAppointmentNotNull() {
        assertNotNull(patientAppointment.getDoctor());
        assertNotNull(patientAppointment.getPatient());
        assertNotNull(patientAppointment.getStartDateTime());
        assertNotNull(patientAppointment.getEndDateTime());
        assertNotNull(patientAppointment.getCreator());
//		assertNotNull(patientAppointment.getEditDateTime());
        assertNotNull(patientAppointment.getType());
    }

    @Test
    void testSystemAppointmentNotNull() {
        assertNotNull(systemAppointment.getDoctor());
        assertNotNull(systemAppointment.getStartDateTime());
        assertNotNull(systemAppointment.getEndDateTime());
        assertNotNull(systemAppointment.getCreator());
//		assertNotNull(systemAppointment.getEditDateTime());
    }

    @Test
    void testSystemAppointmentEmptyFields() {
        assertEquals(systemAppointment.getNotes().size(), 0);
        assertNull(systemAppointment.getPatient());
        assertNull(systemAppointment.getType());
//		assertEquals(systemAppointment.getEditDateTime(), now);
    }

    @Test
    void testAppointStartEndDates() {
        LocalDateTime startDateTimeValid = LocalDateTime.of(2024, Month.NOVEMBER, 25, 12, 0);
        LocalDateTime endDateTimeValid = LocalDateTime.of(2024, Month.NOVEMBER, 25, 12, 15);

        LocalDateTime startDateTimeInvalid = LocalDateTime.of(2024, Month.NOVEMBER, 25, 12, 0);
        LocalDateTime endDateTimeInvalid = LocalDateTime.of(2024, Month.NOVEMBER, 25, 11, 15);

        assertTrue(appointmentService.isStartAppointmentBeforeToEndAppointment(startDateTimeValid, endDateTimeValid));
        assertFalse(appointmentService.isStartAppointmentBeforeToEndAppointment(startDateTimeInvalid, endDateTimeInvalid));

        assertTrue(appointmentService.isEndAppointmentBeforeStartAppointment(startDateTimeInvalid, endDateTimeInvalid));
        assertFalse(appointmentService.isEndAppointmentBeforeStartAppointment(startDateTimeValid, endDateTimeValid));
    }

    @Test
    void testDateInFuture() {
        LocalDateTime dateInPast = LocalDateTime.now().minusDays(2);
        LocalDateTime dateInFuture = LocalDateTime.now().plusDays(2);

        assertTrue(appointmentService.isDateInFuture(dateInFuture));
        assertFalse(appointmentService.isDateInFuture(dateInPast));
    }

    @Test
    void testAddNote() {
        PatientRepo patientRepo = mock(PatientRepo.class);
        HelperService helperService = new HelperService();
        NoteService noteService = new NoteService(patientRepo, helperService, appointmentService, noteRepo, doctorRepo);

        Patient patient = mock(Patient.class);
        Doctor doctor = mock(Doctor.class);
        Appointment appointment = mock(Appointment.class);
        User creator = doctor;
        Date date = new Date();
        String icdCode = "A00.1";
        String recommendation = "Patient soll regelmäßig Wasser trinken";

        Diagnosis diagnosis = noteService.createDiagnosis(patient, doctor, appointment, date,
                icdCode, recommendation);

        appointmentService.removeAllNotes(patientAppointment);
        appointmentService.addNote(patientAppointment, diagnosis);
        assertEquals(patientAppointment.getNotes().size(), 1);
        assertNotEquals(patientAppointment.getNotes().size(), 2);

    }

//    @Test
//    void removeAllNotes() {
//        PatientRepo patientRepo = mock(PatientRepo.class);
//        HelperService helperService = new HelperService();
//        NoteService noteService = new NoteService(patientRepo, helperService, noteRepo, doctorRepo){
//
//        Patient patient = mock(Patient.class);
//        Doctor doctor = mock(Doctor.class);
//        User creator = doctor;
//        Date date = new Date();
//        String icdCode = "A00.1";
//        String recommendation = "Patient soll regelmäßig Wasser trinken";
//
//        Diagnosis diagnosis1 = noteService.createDiagnosis(patient, doctor, date, creator,
//                icdCode, recommendation);
//
//        Diagnosis diagnosis2 = noteService.createDiagnosis(patient, doctor, date, creator,
//                icdCode, recommendation);
//
//        Diagnosis diagnosis3 = noteService.createDiagnosis(patient, doctor, date, creator,
//                icdCode, recommendation);
//
//        appointmentService.addNote(patientAppointment, diagnosis1);
//        appointmentService.addNote(patientAppointment, diagnosis2);
//        appointmentService.addNote(patientAppointment, diagnosis3);
//        appointmentService.removeAllNotes(patientAppointment);
//        assertEquals(patientAppointment.getNotes().size(), 0);
//        assertNotEquals(patientAppointment.getNotes().size(), 1);
//
//    }
}
