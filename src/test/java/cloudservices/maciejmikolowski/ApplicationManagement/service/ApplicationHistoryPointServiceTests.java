package cloudservices.maciejmikolowski.ApplicationManagement.service;

import cloudservices.maciejmikolowski.ApplicationManagement.dto.Application;
import cloudservices.maciejmikolowski.ApplicationManagement.dto.ApplicationHistoryPoint;
import cloudservices.maciejmikolowski.ApplicationManagement.dto.ApplicationStateEnum;
import cloudservices.maciejmikolowski.ApplicationManagement.repository.ApplicationHistoryPointRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationHistoryPointServiceTests {

    AutoCloseable closeable;

    @InjectMocks
    ApplicationHistoryPointService applicationHistoryPointService;

    @Mock
    ApplicationHistoryPointRepository applicationHistoryPointRepository;

    Application application = new Application();

    ApplicationHistoryPoint applicationHistoryPoint = new ApplicationHistoryPoint();

    @Test
    public void test_addFirstApplicationHistoryPoint_whenAllValid_thenReturnApplicationHistoryPoint() throws Exception {
        Integer id = 1;
        application.setId(id);
        applicationHistoryPoint.setState(ApplicationStateEnum.CREATED);
        applicationHistoryPoint.setApplicationId(id);
        when(applicationHistoryPointRepository.save(any(ApplicationHistoryPoint.class))).thenReturn(applicationHistoryPoint);
        ApplicationHistoryPoint firstApplicationHistoryPoint = applicationHistoryPointService.addFirstApplicationHistoryPoint(application);
        assertEquals(applicationHistoryPoint, firstApplicationHistoryPoint);
    }

    @Test
    public void test_addFirstApplicationHistoryPoint_whenNoApplicationId_thenThrowException() {
        application.setId(null);
        assertThrows(Exception.class, () -> { applicationHistoryPointService.addFirstApplicationHistoryPoint(application); });
    }
}