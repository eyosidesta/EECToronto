package com.example.EECToronto.Youtube;

public class YoutubeLiveStatusDTO {
    private boolean isLive;
    private String videoId;

    public YoutubeLiveStatusDTO() {
    }

    public YoutubeLiveStatusDTO(boolean isLive, String videoId) {
        this.isLive = isLive;
        this.videoId = videoId;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}

