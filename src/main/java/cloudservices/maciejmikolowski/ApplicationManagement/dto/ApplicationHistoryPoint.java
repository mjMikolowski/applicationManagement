package cloudservices.maciejmikolowski.ApplicationManagement.dto;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class ApplicationHistoryPoint {
    @Id
    @GeneratedValue
    private Integer id;
    @NotNull
    private Integer applicationId;
    private Date modificationDate;
    private ApplicationStateEnum state;
    private String reason;
}
