package com.example.EECToronto.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping(path="api/message")
public class BulkMessageController {

    private final BulkMessageService bulkMessageService;

    @Autowired
    public BulkMessageController(BulkMessageService bulkMessageService) {
        this.bulkMessageService = bulkMessageService;
    }
    @GetMapping
    public List<BulkMessages> getAllMessages() {
        return bulkMessageService.getAllBulkMessages();
    }
}
