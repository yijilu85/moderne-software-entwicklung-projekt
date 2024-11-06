package Java.services;

import com.medieninformatik.patientcare.*;

import Java.util.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@Test
class TestAppointment()
{
	@Mock
	private PatientRepo patientRepo;
	private DoctorRepo doctorRepo;
	private AppointmentService appointmentService = new AppointmentService(patientRepo, doctorRepo);
	private Appointment systemAppointment;
	private Appointment patientAppointment;


	@Mock
	mockAppointment()
	{
		//Doctor-constructor Doctor(id, name)
		Doctor doctor = new Doctor("1","Dr. House");
		//Patient-constructor Patient(id, name)
		Patient patient = new Patient("1001","Max Mustermann");
		//User 'System' has id '0'
		User creator = new Doctor("0","System");

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startDateTime = LocalDateTime.of(2024, Month.NOVEMBER, 25, 12, 0);
		LocalDateTime endDateTime = LocalDateTime.of(2024, Month.NOVEMBER, 25, 12, 15);

		//Appointment-Constructor Appointment(doctor, creator, startDate, endDate, editDateTime)
		systemAppointment = new Appointment(doctor, creator, startDateTime, endDateTime, now);

		//Apppointment-Constructor Appointment(doctor, patient, creator, type, startDateTime, endDateTime, editDateTime)
		patientAppointment = new Appointment(doctor, patient, creator, "offline", startDateTime, endDateTime, now );
	}

	@Test
	testAppointmentNotNull(patientAppointment)
	{
		assertNotNull(patientAppointment.Doctor);
		assertNotNull(patientAppointment.Patient);
		assertNotNull(patientAppointment.startDateTime);
		assertNotNull(patientAppointment.endDateTime);
		assertNotNull(patientAppointment.Creator);
		assertNotNull(patientAppointment.editDateTime);
		assertNotNull(patientAppointment.type);
	}
	@Test
	testAppointmentNotNull(systemAppointment)
	{
		assertNotNull(systemAppointment.Doctor);
		assertNotNull(systemAppointment.startDateTime);
		assertNotNull(systemAppointment.endDateTime);
		assertNotNull(systemAppointment.Creator);
		assertNotNull(systemAppointment.editDateTime);
	}

	@Test
	testAppointmentLegalValues(patientAppointment)
	{
		assertThat(patientAppointment.endDateTime.isAfter(patientAppointment.startDateTime));
		assertThat(patientAppointment.startDateTime.isBefore(patientAppointment.endDateTime));

		assertThat(patientAppointment.endDateTime.isAfter(now);
		assertThat(patientAppointment.startDateTime.isAfter();

		assertNotNull(patientRepo.findUser(patientAppointment.patient.id));
		assertNotNull(doctorRepo.findUser(patientAppointment.doctor.id));
		//needs a doctor in doctor repo with id '0' and name "System"
		assertNotNull(doctorRepo.findUser("0"));
	}

	@Test
	testAppointmentEmpty(systemAppointment)
	{
		assertNull(systemAppointment.note);
		assertNull(systemAppointment.patient);
		assertNull(systemAppointment.type);
		assertEquals(systemAppointment.editDateTime, now);
	}

}