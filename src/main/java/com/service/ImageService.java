package com.service;

import com.model.Image;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    public Image addAndUpdateImage(Image image);
    public List<Image> getImages();

    public Image findImageById(int id);
    public void deleteImage(int id);
    public void initializeImage() throws IOException;
}
