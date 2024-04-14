package com.service;

import com.model.Background;

import java.util.List;

public interface BackgroundService {
    public Background addAndUpdateBackground(Background background);
    public List<Background> getBackgrounds();

    public Background findBackgroundById(int id);
    public void deleteBackground(int id);
}
