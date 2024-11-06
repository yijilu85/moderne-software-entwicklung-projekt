package Java.services;

import com.medieninformatik.patientcare.services.NoteService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.medieninformatik.patientcare.services.HelperService;

public class HelperServiceTest {

    private HelperService helperService = new HelperService();

    @Test
    void validateDate() {
        // check for valid dates – use format dd.MM.yyyy
        String validDateFormat = "15.03.2023";
        String dateFormat = "dd.MM.yyyy";
        String invalidDateFormat = "2023-03-15";
        String impossibleDate = "30.02.2023";

        assertTrue(helperService.isValidDate(validDateFormat, dateFormat));
        assertFalse(helperService.isValidDate(invalidDateFormat, dateFormat));
        assertFalse(helperService.isValidDate(impossibleDate, dateFormat));
    }
}
