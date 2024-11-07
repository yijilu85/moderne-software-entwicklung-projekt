package Java.services;

import com.medieninformatik.patientcare.*;
import java.time.LocalDateTime;
import java.time.Month;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestAppointment {

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

		systemAppointment = new Appointment(doctor, creator, startDateTime, endDateTime, now);
		patientAppointment = new Appointment(doctor, patient, creator, "offline", startDateTime, endDateTime, now);
	}

	@Test
	void testAppointmentNotNull() {
		assertNotNull(patientAppointment.getDoctor());
		assertNotNull(patientAppointment.getPatient());
		assertNotNull(patientAppointment.getStartDateTime());
		assertNotNull(patientAppointment.getEndDateTime());
		assertNotNull(patientAppointment.getCreator());
		assertNotNull(patientAppointment.getEditDateTime());
		assertNotNull(patientAppointment.getType());
	}

	@Test
	void testSystemAppointmentNotNull() {
		assertNotNull(systemAppointment.getDoctor());
		assertNotNull(systemAppointment.getStartDateTime());
		assertNotNull(systemAppointment.getEndDateTime());
		assertNotNull(systemAppointment.getCreator());
		assertNotNull(systemAppointment.getEditDateTime());
	}

	@Test
	void testAppointmentLegalValues() {
		assertTrue(patientAppointment.getEndDateTime().isAfter(patientAppointment.getStartDateTime()));
		assertTrue(patientAppointment.getStartDateTime().isBefore(patientAppointment.getEndDateTime()));

		assertTrue(patientAppointment.getEndDateTime().isAfter(now));
		assertTrue(patientAppointment.getStartDateTime().isAfter(now.minusMinutes(1))); // Adjust to a safe margin before `now`

		when(patientRepo.findUser(patientAppointment.getPatient().getId())).thenReturn(patientAppointment.getPatient());
		when(doctorRepo.findUser(patientAppointment.getDoctor().getId())).thenReturn(patientAppointment.getDoctor());
		when(doctorRepo.findUser("0")).thenReturn(new Doctor("0", "System"));

		assertNotNull(patientRepo.findUser(patientAppointment.getPatient().getId()));
		assertNotNull(doctorRepo.findUser(patientAppointment.getDoctor().getId()));
		assertNotNull(doctorRepo.findUser("0"));
	}

	@Test
	void testSystemAppointmentEmptyFields() {
		assertNull(systemAppointment.getNote());
		assertNull(systemAppointment.getPatient());
		assertNull(systemAppointment.getType());
		assertEquals(systemAppointment.getEditDateTime(), now);
	}
}
