package com.service.serviceImpl;

import com.model.Image;
import com.repository.ImageRepository;
import com.service.ImageService;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.mock.web.MockMultipartFile;
import java.util.List;
@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository){
        this.imageRepository = imageRepository;
    }
    @Override
    public Image addAndUpdateImage(Image image){
        return imageRepository.save(image);
    }
    @Override
    public List<Image> getImages(){
        return imageRepository.findAll();
    }
    @Override
    public Image findImageById(int id){

        return imageRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteImage(int id){
        imageRepository.deleteById(id);
    }

    @Override
    public void initializeImage() throws IOException {
        Path path = Paths.get("/photos/user.jpg");
        String name = "user.jpg";
        String originalFileName = "user.jpg";
        String contentType = "image/jpeg";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
            System.out.println("ОШИБКА ПРИ ИНИЦИАЛИЗАЦИИ IMAGE");
        }
        MultipartFile result = new MockMultipartFile(name,
                originalFileName, contentType, content);
        Image image = new Image();
        image.setName(result.getName());
        image.setOriginalFileName(result.getOriginalFilename());
        image.setContentType(result.getContentType());
        image.setSize(result.getSize());
        image.setBytes(result.getBytes());
        imageRepository.save(image);
    }
}
