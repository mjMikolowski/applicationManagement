package cloudservices.maciejmikolowski.ApplicationManagement.controller;

import cloudservices.maciejmikolowski.ApplicationManagement.dto.Application;
import cloudservices.maciejmikolowski.ApplicationManagement.dto.ApplicationStateEnum;
import cloudservices.maciejmikolowski.ApplicationManagement.dto.ApplicationWithHistory;
import cloudservices.maciejmikolowski.ApplicationManagement.dto.ApplicationHistoryPoint;
import cloudservices.maciejmikolowski.ApplicationManagement.dto.SortingParams;
import cloudservices.maciejmikolowski.ApplicationManagement.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ApplicationController {

    @Autowired
    ApplicationService applicationService;

    @GetMapping
    public @ResponseBody
    ResponseEntity<Iterable<Application>> getAllApplication(SortingParams sortingParams,
                                                            @RequestParam(required = false) String name,
                                                            @RequestParam(required = false) ApplicationStateEnum state) {
        return new ResponseEntity<>(applicationService.getAllActualApplications(sortingParams, name, state), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<ApplicationWithHistory> getApplicationByIdWithHistory(@PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(applicationService.getApplicationWithHistoryById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public @ResponseBody ResponseEntity<Application> createApplication(@RequestBody Application application) throws Exception {
        return new ResponseEntity<>(applicationService.createApplication(application), HttpStatus.CREATED);
    }

    @PatchMapping("/modify")
    public @ResponseBody ResponseEntity<Application> modifyApplicationContent(@RequestBody Application application) throws Exception {
        return new ResponseEntity<>(applicationService.modifyApplicationContent(application), HttpStatus.OK);
    }

    @PutMapping("/delete")
    public @ResponseBody ResponseEntity<Application> deleteApplication(@RequestBody ApplicationHistoryPoint applicationHistoryPoint) throws Exception {
        return new ResponseEntity<>(applicationService.deleteApplication(applicationHistoryPoint), HttpStatus.OK);
    }

    @PutMapping("/verify")
    public @ResponseBody ResponseEntity<Application> verifyApplication(@RequestParam Integer id) throws Exception {
        return new ResponseEntity<>(applicationService.verifyApplication(id), HttpStatus.OK);
    }

    @PutMapping("/reject")
    public @ResponseBody ResponseEntity<Application> rejectApplication(@RequestBody ApplicationHistoryPoint applicationHistoryPoint) throws Exception {
        return new ResponseEntity<>(applicationService.rejectApplication(applicationHistoryPoint), HttpStatus.OK);
    }

    @PutMapping("/accept")
    public @ResponseBody ResponseEntity<Application> acceptApplication(@RequestParam Integer id) throws Exception {
        return new ResponseEntity<>(applicationService.acceptApplication(id), HttpStatus.OK);
    }

    @PutMapping("/publish")
    public @ResponseBody ResponseEntity<Application> publishApplication(@RequestParam Integer id) throws Exception {
        return new ResponseEntity<>(applicationService.publishApplication(id), HttpStatus.OK);
    }
}
