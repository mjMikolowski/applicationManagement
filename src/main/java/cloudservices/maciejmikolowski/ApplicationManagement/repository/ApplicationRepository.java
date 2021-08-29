package cloudservices.maciejmikolowski.ApplicationManagement.repository;

import cloudservices.maciejmikolowski.ApplicationManagement.dto.Application;
import cloudservices.maciejmikolowski.ApplicationManagement.dto.ApplicationStateEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ApplicationRepository extends PagingAndSortingRepository<Application, Integer> {
    List<Application> findByName(String name, Pageable paging);

    List<Application> findByState(ApplicationStateEnum state, Pageable paging);

    List<Application> findByNameAndState(String name, ApplicationStateEnum state, Pageable paging);

    @Query("SELECT MAX(app.publishedId) FROM Application app")
    Integer getActualMaxPublishedId();
}
