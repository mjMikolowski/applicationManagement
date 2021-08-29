package cloudservices.maciejmikolowski.ApplicationManagement.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.sun.istack.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Application {
    @Id
    @GeneratedValue
    private Integer id;
    private ApplicationStateEnum state;
    @NotNull
    private String name;
    @NotNull
    private String content;
    @Column(unique=true)
    private Integer publishedId;

    public Boolean isInStateFromList(List<ApplicationStateEnum> statesList) {
        return statesList.stream()
                .anyMatch(applicationStateEnum -> applicationStateEnum.equals(state));
    }
}
