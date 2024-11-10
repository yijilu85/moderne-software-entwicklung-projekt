package Java.services;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;

import com.medieninformatik.patientcare.entities.*;
import com.medieninformatik.patientcare.repo.DoctorRepo;
import com.medieninformatik.patientcare.repo.PatientRepo;
import com.medieninformatik.patientcare.services.HelperService;
import com.medieninformatik.patientcare.services.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.medieninformatik.patientcare.services.AppointmentService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AppointmentServiceTest {

	@Mock
	private PatientRepo patientRepo;

	@Mock
	private DoctorRepo doctorRepo;

	@InjectMocks
	private AppointmentService appointmentService;

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

		systemAppointment = new Appointment(doctor, creator, startDateTime, endDateTime, now);
		patientAppointment = new Appointment(doctor, patient, creator, type, startDateTime, endDateTime, now);
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
	void testAppointmentLegalValues() {
//		assertTrue(patientAppointment.getEndDateTime().isAfter(patientAppointment.getStartDateTime()));
//		assertTrue(patientAppointment.getStartDateTime().isBefore(patientAppointment.getEndDateTime()));
//
//		assertTrue(patientAppointment.getEndDateTime().isAfter(now));
//		assertTrue(patientAppointment.getStartDateTime().isAfter(now.minusMinutes(1))); // Adjust to a safe margin before `now`
//
//
////		when(patientRepo.findUser(patientAppointment.getPatient().getId())).thenReturn(patientAppointment.getPatient());
////		when(doctorRepo.findUser(patientAppointment.getDoctor().getId())).thenReturn(patientAppointment.getDoctor());
////		when(doctorRepo.findUser("0")).thenReturn(new Doctor("0", "System"));
//
//		assertNotNull(patientRepo.findUser(patientAppointment.getPatient().getId()));
//		assertNotNull(doctorRepo.findUser(patientAppointment.getDoctor().getId()));
//		assertNotNull(doctorRepo.findUser("0"));
	}

	@Test
	void testSystemAppointmentEmptyFields() {
		assertEquals(systemAppointment.getNotes().size(),0);
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
		NoteService noteService = new NoteService(patientRepo, helperService);

		Patient patient = mock(Patient.class);
		Doctor doctor = mock(Doctor.class);
		User creator = doctor;
		Date date = new Date();
		String icdCode = "A00.1";
		String recommendation = "Patient soll regelmäßig Wasser trinken";

		Diagnosis diagnosis = noteService.createDiagnosis(patient, doctor, date, creator,
				icdCode, recommendation);

		appointmentService.removeAllNotes(patientAppointment);
		appointmentService.addNote(patientAppointment, diagnosis);
		assertEquals(patientAppointment.getNotes().size(), 1);
		assertNotEquals(patientAppointment.getNotes().size(), 2);

	}

	@Test
	void removeAllNotes() {
		PatientRepo patientRepo = mock(PatientRepo.class);
		HelperService helperService = new HelperService();
		NoteService noteService = new NoteService(patientRepo, helperService);

		Patient patient = mock(Patient.class);
		Doctor doctor = mock(Doctor.class);
		User creator = doctor;
		Date date = new Date();
		String icdCode = "A00.1";
		String recommendation = "Patient soll regelmäßig Wasser trinken";

		Diagnosis diagnosis1 = noteService.createDiagnosis(patient, doctor, date, creator,
				icdCode, recommendation);

		Diagnosis diagnosis2 = noteService.createDiagnosis(patient, doctor, date, creator,
				icdCode, recommendation);

		Diagnosis diagnosis3 = noteService.createDiagnosis(patient, doctor, date, creator,
				icdCode, recommendation);

		appointmentService.addNote(patientAppointment, diagnosis1);
		appointmentService.addNote(patientAppointment, diagnosis2);
		appointmentService.addNote(patientAppointment, diagnosis3);
		appointmentService.removeAllNotes(patientAppointment);
		assertEquals(patientAppointment.getNotes().size(), 0);
		assertNotEquals(patientAppointment.getNotes().size(), 1);

	}
}
