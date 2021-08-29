package cloudservices.maciejmikolowski.ApplicationManagement.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JsonExceptionString {
    private String errorInfo;
}
