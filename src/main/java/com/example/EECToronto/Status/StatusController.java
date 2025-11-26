package com.example.EECToronto.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/status")
public class StatusController {
    private final StatusService statusService;
    @Autowired
    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    public List<Status> getAllStatus() {
        return statusService.getAllStatusService();
    }
    @GetMapping("/{status_id}")
    public Status getStatus(@PathVariable Long status_id) {
        return statusService.getStatusByIdService(status_id);
    }

    @PostMapping
    public void addStatus(@RequestBody Status status) {
        statusService.addStatus(status);
    }
}
