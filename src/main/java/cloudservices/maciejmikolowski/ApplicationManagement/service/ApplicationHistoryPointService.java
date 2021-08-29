package cloudservices.maciejmikolowski.ApplicationManagement.service;

import cloudservices.maciejmikolowski.ApplicationManagement.dto.Application;
import cloudservices.maciejmikolowski.ApplicationManagement.dto.ApplicationHistoryPoint;
import cloudservices.maciejmikolowski.ApplicationManagement.dto.ApplicationStateEnum;
import cloudservices.maciejmikolowski.ApplicationManagement.repository.ApplicationHistoryPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ApplicationHistoryPointService {

    @Autowired
    ApplicationHistoryPointRepository applicationHistoryPointRepository;

    public List<ApplicationHistoryPoint> getAllApplicationHistoryPoints(Integer applicationId) {
        return applicationHistoryPointRepository.findAllByApplicationId(applicationId);
    }

    public ApplicationHistoryPoint addFirstApplicationHistoryPoint(Application application) throws Exception {
        if (application.getId() != null) {
            ApplicationHistoryPoint applicationHistoryPoint = new ApplicationHistoryPoint();
            applicationHistoryPoint.setApplicationId(application.getId());
            applicationHistoryPoint.setModificationDate(new Date());
            applicationHistoryPoint.setState(ApplicationStateEnum.CREATED);
            return applicationHistoryPointRepository.save(applicationHistoryPoint);
        } else {
            throw new Exception("History point of application with no id");
        }
    }

    public ApplicationHistoryPoint addApplicationHistoryPoint(ApplicationHistoryPoint applicationHistoryPoint) {
        applicationHistoryPoint.setModificationDate(new Date());
        return applicationHistoryPointRepository.save(applicationHistoryPoint);
    }
}
