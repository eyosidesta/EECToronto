package com.example.EECToronto.Youtube;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class YoutubeService {

    @Value("${YOUTUBE_API_KEY:}")
    private String apiKey;

    private static final String YOUTUBE_API_URL = "https://www.googleapis.com/youtube/v3/search";
    private static final long CACHE_TTL_MS = 600000; // 10 minutes in milliseconds

    // Thread-safe in-memory cache with expiration
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    /**
     * Check if a YouTube channel has a live stream currently active.
     * Results are cached for 3 minutes to reduce API calls.
     * 
     * @param channelId The YouTube channel ID
     * @return YoutubeLiveStatusDTO with isLive status and videoId if live
     */
    public YoutubeLiveStatusDTO checkLiveStatus(String channelId) {
        if (channelId == null || channelId.trim().isEmpty()) {
            throw new RuntimeException("Channel ID is required");
        }

        // Check cache first
        CacheEntry cached = cache.get(channelId);
        if (cached != null && (System.currentTimeMillis() - cached.timestamp) < CACHE_TTL_MS) {
            return cached.value;
        }

        if (apiKey == null || apiKey.trim().isEmpty() || apiKey.equals("your-youtube-api-key-here")) {
            // If API key is not configured, return not live and cache it
            YoutubeLiveStatusDTO result = new YoutubeLiveStatusDTO(false, null);
            cache.put(channelId, new CacheEntry(result, System.currentTimeMillis()));
            return result;
        }

        try {
            // Build YouTube API URL
            String urlString = String.format(
                "%s?part=snippet&channelId=%s&eventType=live&type=video&key=%s",
                YOUTUBE_API_URL,
                channelId,
                apiKey
            );

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000); // 5 seconds timeout
            conn.setReadTimeout(5000);

            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                // Read response
                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNextLine()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();

                // Parse JSON response
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(response.toString());

                // Check if there are any live videos - following the correct response structure
                JsonNode items = jsonNode.get("items");
                if (items != null && items.isArray() && items.size() > 0) {
                    // Get the first live video
                    JsonNode firstItem = items.get(0);
                    JsonNode idNode = firstItem.get("id");
                    if (idNode != null && idNode.has("videoId")) {
                        String videoId = idNode.get("videoId").asText();
                        YoutubeLiveStatusDTO result = new YoutubeLiveStatusDTO(true, videoId);
                        // Cache the result
                        cache.put(channelId, new CacheEntry(result, System.currentTimeMillis()));
                        return result;
                    }
                }

                // No live videos found
                YoutubeLiveStatusDTO result = new YoutubeLiveStatusDTO(false, null);
                // Cache the result
                cache.put(channelId, new CacheEntry(result, System.currentTimeMillis()));
                return result;

            } else if (responseCode == 403) {
                // API key issue or quota exceeded
                System.err.println("YouTube API error: Forbidden (403). Check API key and quota.");
                return new YoutubeLiveStatusDTO(false, null);
            } else if (responseCode == 400) {
                // Bad request (invalid channel ID, etc.)
                System.err.println("YouTube API error: Bad Request (400). Check channel ID.");
                return new YoutubeLiveStatusDTO(false, null);
            } else {
                System.err.println("YouTube API error: Response code " + responseCode);
                return new YoutubeLiveStatusDTO(false, null);
            }

        } catch (IOException e) {
            System.err.println("Error calling YouTube API: " + e.getMessage());
            // Return not live on error to avoid breaking the frontend
            YoutubeLiveStatusDTO result = new YoutubeLiveStatusDTO(false, null);
            // Cache error result for shorter time (30 seconds) to allow retry
            cache.put(channelId, new CacheEntry(result, System.currentTimeMillis() - 150000));
            return result;
        }
    }

    // Cache entry with timestamp
    private static class CacheEntry {
        final YoutubeLiveStatusDTO value;
        final long timestamp;

        CacheEntry(YoutubeLiveStatusDTO value, long timestamp) {
            this.value = value;
            this.timestamp = timestamp;
        }
    }
}

