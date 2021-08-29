package cloudservices.maciejmikolowski.ApplicationManagement.controller;

import cloudservices.maciejmikolowski.ApplicationManagement.dto.ApplicationHistoryPoint;
import cloudservices.maciejmikolowski.ApplicationManagement.service.ApplicationHistoryPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/history")
public class ApplicationHistoryPointController {

    @Autowired
    ApplicationHistoryPointService applicationHistoryPointService;

    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity<Iterable<ApplicationHistoryPoint>> getAllHistoryPointsByApplicationId(@PathVariable Integer id) {
        return new ResponseEntity<>(applicationHistoryPointService.getAllApplicationHistoryPoints(id), HttpStatus.OK);
    }
}
