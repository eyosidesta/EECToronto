package com.example.EECToronto.Youtube;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/youtube")
@CrossOrigin(origins = {"https://www.geecvancouver.ca", "https://geecvancouver.ca", "https://geecvancouver.vercel.app", "http://localhost:3036"})
public class YoutubeController {

    private final YoutubeService youtubeService;

    @Autowired
    public YoutubeController(YoutubeService youtubeService) {
        this.youtubeService = youtubeService;
    }

    /**
     * Public endpoint to check if a YouTube channel has a live stream.
     * Results are cached for 3 minutes to reduce API calls.
     * 
     * @param channelId The YouTube channel ID (required query parameter)
     * @return YoutubeLiveStatusDTO with isLive status and videoId if live
     */
    @GetMapping("/live-status")
    public ResponseEntity<?> getLiveStatus(@RequestParam(required = true) String channelId) {
        try {
            YoutubeLiveStatusDTO status = youtubeService.checkLiveStatus(channelId);
            return ResponseEntity.ok(status);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Error checking live status"));
        }
    }

    // Helper class for error responses
    private static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}

