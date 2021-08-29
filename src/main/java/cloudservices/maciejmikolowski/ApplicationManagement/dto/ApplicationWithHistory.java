package cloudservices.maciejmikolowski.ApplicationManagement.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApplicationWithHistory extends Application {
    private List<ApplicationHistoryPoint> changesHistory;
}
