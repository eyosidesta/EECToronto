package com.example.EECToronto.Message;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BulkMessageService {
    public List<BulkMessages> getAllBulkMessages() {
        return List.of(new BulkMessages(
                List.of("++1647829303", "+16479298123"),
                "Hi {name}, how is it going, I hope you are doing great",
                "Eyosias",
                1L,
                1L));
    }
}
