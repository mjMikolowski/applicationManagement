package cloudservices.maciejmikolowski.ApplicationManagement.service;

import cloudservices.maciejmikolowski.ApplicationManagement.dto.Application;
import cloudservices.maciejmikolowski.ApplicationManagement.dto.ApplicationHistoryPoint;
import cloudservices.maciejmikolowski.ApplicationManagement.dto.ApplicationStateEnum;
import cloudservices.maciejmikolowski.ApplicationManagement.dto.ApplicationWithHistory;
import cloudservices.maciejmikolowski.ApplicationManagement.dto.SortingParams;
import cloudservices.maciejmikolowski.ApplicationManagement.repository.ApplicationRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    ApplicationHistoryPointService applicationHistoryPointService;

    public List<Application> getAllActualApplications(SortingParams sortingParams, String name, ApplicationStateEnum state) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortingParams.getDirection()), sortingParams.getSortBy());
        Pageable paging = PageRequest.of(sortingParams.getOffset(), sortingParams.getLimit(), sort);
        if (name != null && state != null) {
            return applicationRepository.findByNameAndState(name, state, paging);
        } else if (name != null) {
            return applicationRepository.findByName(name, paging);
        } else if (state != null) {
            return applicationRepository.findByState(state, paging);
        }
        return applicationRepository.findAll(paging).toList();
    }

    public ApplicationWithHistory getApplicationWithHistoryById(Integer id) throws Exception {
        Application application = getApplicationById(id);
        List<ApplicationHistoryPoint> historyList = applicationHistoryPointService.getAllApplicationHistoryPoints(id);
        return buildApplicationWithHistory(application, historyList);
    }

    public Application createApplication(Application application) throws Exception {
        hasRequiredFields(application);

        application.setState(ApplicationStateEnum.CREATED);
        Application savedApplication = applicationRepository.save(application);
        applicationHistoryPointService.addFirstApplicationHistoryPoint(application);
        return savedApplication;
    }

    public Application modifyApplicationContent(Application application) throws Exception {
        Application foundApplication = getApplicationById(application.getId());
        List<ApplicationStateEnum> availableStates = List.of(ApplicationStateEnum.CREATED, ApplicationStateEnum.VERIFIED);
        if (foundApplication.isInStateFromList(availableStates)) {
            if (StringUtils.isNotEmpty(application.getContent())) {
                foundApplication.setContent(application.getContent());
            }
            return applicationRepository.save(foundApplication);
        } else {
            throw new Exception("Application content cannot be changed in actual state");
        }
    }

    public Application deleteApplication(ApplicationHistoryPoint applicationHistoryPoint) throws Exception {
        if (StringUtils.isNotEmpty(applicationHistoryPoint.getReason())) {
            Application application = getApplicationById(applicationHistoryPoint.getApplicationId());
            List<ApplicationStateEnum> availableStates = List.of(ApplicationStateEnum.CREATED);
            if (application.isInStateFromList(availableStates)) {
                applicationHistoryPoint.setState(ApplicationStateEnum.DELETED);
                applicationHistoryPointService.addApplicationHistoryPoint(applicationHistoryPoint);
                application.setState(ApplicationStateEnum.DELETED);
                return applicationRepository.save(application);
            } else {
                throw new Exception("Application is not in correct state to delete");
            }
        } else {
            throw new Exception("Empty reason of rejection");
        }
    }

    public Application verifyApplication(Integer id) throws Exception {
        Application application = getApplicationById(id);
        List<ApplicationStateEnum> availableStates = List.of(ApplicationStateEnum.CREATED);
        if (application.isInStateFromList(availableStates)) {
            application.setState(ApplicationStateEnum.VERIFIED);
            ApplicationHistoryPoint applicationHistoryPoint = buildApplicationHistoryPoint(application.getId(), application.getState(), null);
            applicationHistoryPointService.addApplicationHistoryPoint(applicationHistoryPoint);
            return applicationRepository.save(application);
        } else {
            throw new Exception("Application is not in correct state to verify");
        }
    }

    public Application rejectApplication(ApplicationHistoryPoint applicationHistoryPoint) throws Exception {
        Application application = getApplicationById(applicationHistoryPoint.getApplicationId());
        if (StringUtils.isNotEmpty(applicationHistoryPoint.getReason())) {
            List<ApplicationStateEnum> availableStates = List.of(ApplicationStateEnum.VERIFIED, ApplicationStateEnum.ACCEPTED);
            if (application.isInStateFromList(availableStates)) {
                application.setState(ApplicationStateEnum.REJECTED);
                applicationHistoryPoint = buildApplicationHistoryPoint(application.getId(), application.getState(), applicationHistoryPoint.getReason());
                applicationHistoryPointService.addApplicationHistoryPoint(applicationHistoryPoint);
                return applicationRepository.save(application);
            } else {
                throw new Exception("Application is not in correct state to reject");
            }
        } else {
            throw new Exception("Empty reason of rejection");
        }
    }

    public Application acceptApplication(Integer id) throws Exception {
        Application application = getApplicationById(id);
        List<ApplicationStateEnum> availableStates = List.of(ApplicationStateEnum.VERIFIED);
        if (application.isInStateFromList(availableStates)) {
            application.setState(ApplicationStateEnum.ACCEPTED);
            ApplicationHistoryPoint applicationHistoryPoint = buildApplicationHistoryPoint(id, application.getState(), null);
            applicationHistoryPointService.addApplicationHistoryPoint(applicationHistoryPoint);
            return applicationRepository.save(application);
        } else {
            throw new Exception("Application is not in correct state to accept");
        }
    }

    public Application publishApplication(Integer id) throws Exception {
        Application application = getApplicationById(id);
        List<ApplicationStateEnum> availableStates = List.of(ApplicationStateEnum.ACCEPTED);
        if (application.isInStateFromList(availableStates)) {
            application.setState(ApplicationStateEnum.PUBLISHED);
            application.setPublishedId(getActualUniquePublishedId());
            ApplicationHistoryPoint applicationHistoryPoint = buildApplicationHistoryPoint(application.getId(), application.getState(), null);
            applicationHistoryPointService.addApplicationHistoryPoint(applicationHistoryPoint);
            return applicationRepository.save(application);
        } else {
            throw new Exception("Application is not in correct state to publish");
        }
    }

    private Integer getActualUniquePublishedId() {
        Integer result = applicationRepository.getActualMaxPublishedId();
        if (result != null) {
            return result + 1;
        } else {
            return 1;
        }
    }

    private void hasRequiredFields(Application application) throws Exception {
        if (StringUtils.isEmpty(application.getContent()) || StringUtils.isEmpty(application.getName())) {
            throw new Exception("Application name and content cannot be empty");
        }
    }

    private ApplicationHistoryPoint buildApplicationHistoryPoint(Integer id, ApplicationStateEnum state, String reason) {
        ApplicationHistoryPoint applicationHistoryPoint = new ApplicationHistoryPoint();
        applicationHistoryPoint.setApplicationId(id);
        applicationHistoryPoint.setReason(reason);
        applicationHistoryPoint.setState(state);
        return  applicationHistoryPoint;
    }

    private ApplicationWithHistory buildApplicationWithHistory(Application application, List<ApplicationHistoryPoint> historyList) {
        ApplicationWithHistory applicationWithHistory = new ApplicationWithHistory();
        applicationWithHistory.setId(application.getId());
        applicationWithHistory.setState(application.getState());
        applicationWithHistory.setContent(application.getContent());
        applicationWithHistory.setName(application.getName());
        applicationWithHistory.setChangesHistory(historyList);
        return applicationWithHistory;
    }

    private Application getApplicationById(Integer id) throws Exception {
        Optional<Application> application = applicationRepository.findById(id);
        if (application.isPresent()) {
            return application.get();
        } else {
            throw new Exception("Application with requested id not found");
        }
    }
}
