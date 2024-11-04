package Java.services;

import com.medieninformatik.patientcare.repo.PatientRepo;
import com.medieninformatik.patientcare.services.NoteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class NoteServiceTest {

    @Mock
    private PatientRepo patientRepo;

    @Test
    void getPatient() {
    }

    @Test
    void calcTest() {

        NoteService yourClass = new NoteService(patientRepo);

        int expected = 6;
        int actual = yourClass.calcTest(2, 3);

        assertEquals(expected, actual);
    }
}