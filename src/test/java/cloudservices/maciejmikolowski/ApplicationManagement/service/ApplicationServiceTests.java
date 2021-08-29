package cloudservices.maciejmikolowski.ApplicationManagement.service;

import cloudservices.maciejmikolowski.ApplicationManagement.dto.*;
import cloudservices.maciejmikolowski.ApplicationManagement.repository.ApplicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ApplicationServiceTests {

    AutoCloseable closeable;

    @InjectMocks
    ApplicationService applicationService;

    Application application1 = new Application();
    Application application2 = new Application();
    ApplicationHistoryPoint applicationHistoryPoint = new ApplicationHistoryPoint();
    ApplicationWithHistory applicationWithHistory = new ApplicationWithHistory();
    List<Application> mockedResult = new ArrayList<>();
    List<ApplicationHistoryPoint> mockedResultFromHistoryPoint = new ArrayList<>();

    @Mock
    ApplicationRepository applicationRepository;

    @Mock
    ApplicationHistoryPointService applicationHistoryPointService;

    @Test
    public void test_getAllActualApplications_whenNoFilterParameters_thenReturnListWithDefaultFilterParameters() {
        mockedResult.add(application1);
        mockedResult.add(application2);
        Page<Application> mockedPageResult = new PageImpl<Application>(mockedResult);
        Integer offset = 0;
        Integer limit = 10;
        Sort sort = Sort.by(Sort.Direction.ASC,"id");
        SortingParams sortingParams = SortingParams.builder()
                .direction("asc")
                .limit(limit)
                .offset(offset)
                .sortBy("id")
                .build();
        when(applicationRepository.findAll(PageRequest.of(offset, limit, sort))).thenReturn(mockedPageResult);
        List<Application> result = applicationService.getAllActualApplications(sortingParams, null, null);
        assertEquals(result, mockedResult);
    }

    @Test
    public void test_getAllActualApplications_whenNameIsNotNull_thenReturnFilterByName() {
        String name = "Test name";
        application1.setName(name);
        mockedResult = new ArrayList<>();
        mockedResult.add(application1);
        Page<Application> mockedPageResult = new PageImpl<Application>(mockedResult);
        Integer offset = 0;
        Integer limit = 10;
        Sort sort = Sort.by(Sort.Direction.ASC,"id");
        SortingParams sortingParams = SortingParams.builder()
                .direction("asc")
                .limit(limit)
                .offset(offset)
                .sortBy("id")
                .build();
        when(applicationRepository.findByName(name, PageRequest.of(offset, limit, sort))).thenReturn(mockedResult);
        List<Application> result = applicationService.getAllActualApplications(sortingParams, name, null);
        assertEquals(result, mockedResult);
    }

    @Test
    public void test_getAllActualApplications_whenStateIsNotNull_thenReturnFilterByState() {
        ApplicationStateEnum state = ApplicationStateEnum.REJECTED;
        application1.setState(state);
        mockedResult = new ArrayList<>();
        mockedResult.add(application1);
        Page<Application> mockedPageResult = new PageImpl<Application>(mockedResult);
        Integer offset = 0;
        Integer limit = 10;
        Sort sort = Sort.by(Sort.Direction.ASC,"id");
        SortingParams sortingParams = SortingParams.builder()
                .direction("asc")
                .limit(limit)
                .offset(offset)
                .sortBy("id")
                .build();
        when(applicationRepository.findByState(state, PageRequest.of(offset, limit, sort))).thenReturn(mockedResult);
        List<Application> result = applicationService.getAllActualApplications(sortingParams, null, state);
        assertEquals(result, mockedResult);
    }

    @Test
    public void test_getAllActualApplications_whenNameAndStateIsNotNull_thenReturnFilterByNameAndState() {
        ApplicationStateEnum state = ApplicationStateEnum.REJECTED;
        String name = "Test name";
        application1.setState(state);
        application1.setName(name);
        mockedResult = new ArrayList<>();
        mockedResult.add(application1);
        Page<Application> mockedPageResult = new PageImpl<Application>(mockedResult);
        Integer offset = 0;
        Integer limit = 10;
        Sort sort = Sort.by(Sort.Direction.ASC,"id");
        SortingParams sortingParams = SortingParams.builder()
                .direction("asc")
                .limit(limit)
                .offset(offset)
                .sortBy("id")
                .build();
        when(applicationRepository.findByNameAndState(name, state, PageRequest.of(offset, limit, sort))).thenReturn(mockedResult);
        List<Application> result = applicationService.getAllActualApplications(sortingParams, name, state);
        assertEquals(result, mockedResult);
    }

    @Test
    public void test_getApplicationByIdWithHistory_whenApplicationExists_thenReturnValid() throws Exception {
        Integer id = 1;
        application1.setId(id);
        mockedResultFromHistoryPoint = new ArrayList<>();
        mockedResultFromHistoryPoint.add(applicationHistoryPoint);
        applicationWithHistory.setChangesHistory(mockedResultFromHistoryPoint);
        when(applicationRepository.findById(id)).thenReturn(Optional.of(application1));
        when(applicationHistoryPointService.getAllApplicationHistoryPoints(id)).thenReturn(mockedResultFromHistoryPoint);
        ApplicationWithHistory applicationWithHistory = applicationService.getApplicationWithHistoryById(id);
        assertEquals(applicationWithHistory, applicationWithHistory);
    }

    @Test
    public void test_getApplicationByIdWithHistory_whenNoApplication_thenThrowException() {
        Integer id = 1;
        when(applicationRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> { applicationService.getApplicationWithHistoryById(id); });
    }

    @Test
    public void test_createApplication_whenAllRequiredFieldsSet_thenReturnCreatedApplication() throws Exception {
        Integer id = 1;
        application1.setId(id);
        application1.setName("test name");
        application1.setContent("test content");
        when(applicationRepository.save(application1)).thenReturn(application1);
        application1.setState(ApplicationStateEnum.CREATED);
        Application createdApplication = applicationService.createApplication(application1);
        assertEquals(createdApplication, application1);
    }

    @Test
    public void test_createApplication_whenNoNameSet_thenThrowException() {
        Integer id = 1;
        application1.setId(id);
        application1.setContent("test content");
        assertThrows(Exception.class, () -> { applicationService.createApplication(application1); });
    }

    @Test
    public void test_createApplication_whenNoContentSet_thenThrowException() {
        Integer id = 1;
        application1.setId(id);
        application1.setName("test name");
        assertThrows(Exception.class, () -> { applicationService.createApplication(application1); });
    }

    @Test
    public void test_createApplication_whenNoContentAndNameSet_thenThrowException() {
        Integer id = 1;
        application1.setId(id);
        assertThrows(Exception.class, () -> { applicationService.createApplication(application1); });
    }

    @Test
    public void test_modifyApplicationContent_whenStateIsCreated_thenReturnModifiedApplication() throws Exception {
        Application changedApplication = modifyApplicationContent(ApplicationStateEnum.CREATED, "test content - changed");
        assertEquals(changedApplication, application1);
    }

    @Test
    public void test_modifyApplicationContent_whenStateIsVerified_thenReturnModifiedApplication() throws Exception {
        Application changedApplication = modifyApplicationContent(ApplicationStateEnum.VERIFIED, "test content - changed");
        assertEquals(changedApplication, application1);
    }

    @Test
    public void test_modifyApplicationContent_whenStateIsNotCreatedOrVerified_thenThrowException() {
        assertThrows(Exception.class, () -> { modifyApplicationContent(ApplicationStateEnum.REJECTED, "test content - changed"); });
    }

    @Test
    public void test_modifyApplicationContent_whenContentIsEmpty_thenThrowException() {
        assertThrows(Exception.class, () -> { modifyApplicationContent(ApplicationStateEnum.ACCEPTED, ""); });
    }

    @Test
    public void test_modifyApplicationContent_whenNoApplication_thenThrowException() {
        Integer id = 1;
        application1.setId(id);
        when(applicationRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> { applicationService.modifyApplicationContent(application1); });
    }

    private Application modifyApplicationContent(ApplicationStateEnum state, String content) throws Exception {
        Integer id = 1;
        application1.setId(id);
        application1.setState(state);
        application1.setContent("test content - not changed");
        when(applicationRepository.findById(id)).thenReturn(Optional.of(application1));
        application1.setContent(content);
        when(applicationRepository.save(application1)).thenReturn(application1);
        return applicationService.modifyApplicationContent(application1);
    }

    @Test
    public void test_deleteApplication_whenCorrectStateAndReasonNotEmpty_thenReturnDeletedApplication() throws Exception {
        Application deletedApplication = deleteApplication("test reason", ApplicationStateEnum.CREATED);
        application1.setState(ApplicationStateEnum.DELETED);
        assertEquals(application1, deletedApplication);
    }

    @Test
    public void test_deleteApplication_whenCorrectStateAndReasonEmpty_thenThrowException() {
        assertThrows(Exception.class, () -> { deleteApplication("", ApplicationStateEnum.CREATED); });
    }

    @Test
    public void test_deleteApplication_whenInorrectStateAndReasonNotEmpty_thenThrowException() {
        assertThrows(Exception.class, () -> { deleteApplication("test reason", ApplicationStateEnum.VERIFIED); });
    }

    @Test
    public void test_deleteApplication_whenNoApplication_thenThrowException() {
        Integer id = 1;
        applicationHistoryPoint.setId(id);
        when(applicationRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> { applicationService.deleteApplication(applicationHistoryPoint); });
    }

    private Application deleteApplication(String reason, ApplicationStateEnum state) throws Exception {
        Integer id = 1;
        applicationHistoryPoint.setApplicationId(id);
        applicationHistoryPoint.setReason(reason);
        applicationHistoryPoint.setState(ApplicationStateEnum.DELETED);
        application1.setId(id);
        application1.setState(state);
        when(applicationRepository.findById(id)).thenReturn(Optional.of(application1));
        when(applicationRepository.save(application1)).thenReturn(application1);
        return applicationService.deleteApplication(applicationHistoryPoint);
    }

    @Test
    public void test_verifyApplication_whenCorrectState_thenReturnVerifiedApplication() throws Exception {
        Application verifiedApplication = verifyApplication(ApplicationStateEnum.CREATED);
        application1.setState(ApplicationStateEnum.VERIFIED);
        assertEquals(verifiedApplication, application1);
    }

    @Test
    public void test_verifyApplication_whenInCorrectState_thenReturnVerifiedApplication() {
        assertThrows(Exception.class, () -> { verifyApplication(ApplicationStateEnum.VERIFIED); });
    }

    private Application verifyApplication(ApplicationStateEnum state) throws Exception {
        Integer id = 1;
        application1.setId(id);
        application1.setState(state);
        when(applicationRepository.findById(id)).thenReturn(Optional.of(application1));
        when(applicationRepository.save(application1)).thenReturn(application1);
        return applicationService.verifyApplication(id);
    }

    @Test
    public void test_rejectApplication_whenAcceptedStateAndReasonNotEmpty_thenReturnDeletedApplication() throws Exception {
        Application rejectedApplication = rejectApplication("test reason", ApplicationStateEnum.ACCEPTED);
        application1.setState(ApplicationStateEnum.REJECTED);
        assertEquals(application1, rejectedApplication);
    }

    @Test
    public void test_rejectApplication_whenVerifiedStateAndReasonNotEmpty_thenReturnDeletedApplication() throws Exception {
        Application rejectedApplication = rejectApplication("test reason", ApplicationStateEnum.VERIFIED);
        application1.setState(ApplicationStateEnum.REJECTED);
        assertEquals(application1, rejectedApplication);
    }

    @Test
    public void test_rejectApplication_whenAcceptStateAndReasonEmpty_thenThrowException() {
        assertThrows(Exception.class, () -> { rejectApplication("", ApplicationStateEnum.ACCEPTED); });
    }

    @Test
    public void test_rejectApplication_whenIncorrectStateAndReasonNotEmpty_thenThrowException() {
        assertThrows(Exception.class, () -> { rejectApplication("test reason", ApplicationStateEnum.DELETED); });
    }

    @Test
    public void test_rejectApplication_whenNoApplication_thenThrowException() {
        Integer id = 1;
        applicationHistoryPoint.setId(id);
        when(applicationRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> { applicationService.rejectApplication(applicationHistoryPoint); });
    }

    private Application rejectApplication(String reason, ApplicationStateEnum state) throws Exception {
        Integer id = 1;
        applicationHistoryPoint.setApplicationId(id);
        applicationHistoryPoint.setReason(reason);
        applicationHistoryPoint.setState(ApplicationStateEnum.DELETED);
        application1.setId(id);
        application1.setState(state);
        when(applicationRepository.findById(id)).thenReturn(Optional.of(application1));
        when(applicationRepository.save(application1)).thenReturn(application1);
        return applicationService.rejectApplication(applicationHistoryPoint);
    }

    @Test
    public void test_acceptApplication_whenCorrectState_thenReturnAcceptedApplication() throws Exception {
        Application acceptedApplication = acceptApplication(ApplicationStateEnum.VERIFIED);
        application1.setState(ApplicationStateEnum.ACCEPTED);
        assertEquals(acceptedApplication, application1);
    }

    @Test
    public void test_acceptApplication_whenInCorrectState_thenReturnVerifiedApplication() {
        assertThrows(Exception.class, () -> { acceptApplication(ApplicationStateEnum.CREATED); });
    }

    private Application acceptApplication(ApplicationStateEnum state) throws Exception {
        Integer id = 1;
        application1.setId(id);
        application1.setState(state);
        when(applicationRepository.findById(id)).thenReturn(Optional.of(application1));
        when(applicationRepository.save(application1)).thenReturn(application1);
        return applicationService.acceptApplication(id);
    }

    @Test
    public void test_publishApplication_whenCorrectState_thenReturnAcceptedApplication() throws Exception {
        Application acceptedApplication = publishApplication(ApplicationStateEnum.ACCEPTED);
        application1.setState(ApplicationStateEnum.PUBLISHED);
        assertEquals(acceptedApplication, application1);
    }

    @Test
    public void test_publishApplication_whenInCorrectState_thenReturnVerifiedApplication() {
        assertThrows(Exception.class, () -> { publishApplication(ApplicationStateEnum.CREATED); });
    }

    private Application publishApplication(ApplicationStateEnum state) throws Exception {
        Integer id = 1;
        application1.setId(id);
        application1.setState(state);
        when(applicationRepository.findById(id)).thenReturn(Optional.of(application1));
        when(applicationRepository.save(application1)).thenReturn(application1);
        return applicationService.publishApplication(id);
    }
}