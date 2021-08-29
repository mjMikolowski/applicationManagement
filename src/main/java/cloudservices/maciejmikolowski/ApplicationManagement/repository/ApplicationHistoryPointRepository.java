package cloudservices.maciejmikolowski.ApplicationManagement.repository;

import cloudservices.maciejmikolowski.ApplicationManagement.dto.ApplicationHistoryPoint;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ApplicationHistoryPointRepository extends PagingAndSortingRepository<ApplicationHistoryPoint, Integer> {
    @Query("SELECT appHis FROM ApplicationHistoryPoint appHis WHERE appHis.applicationId = ?1")
    List<ApplicationHistoryPoint> findAllByApplicationId(Integer applicationId);
}
