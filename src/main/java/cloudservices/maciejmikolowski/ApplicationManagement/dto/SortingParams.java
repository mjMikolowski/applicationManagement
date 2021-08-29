package cloudservices.maciejmikolowski.ApplicationManagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SortingParams {
    private Integer offset = 0;
    private Integer limit = 10;
    private String sortBy = "id";
    private String direction = "asc";
}
