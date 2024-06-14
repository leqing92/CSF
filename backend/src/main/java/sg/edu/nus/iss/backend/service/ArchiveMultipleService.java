package sg.edu.nus.iss.backend.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.backend.models.Archive;
import sg.edu.nus.iss.backend.repository.ArchiveRepository;
import sg.edu.nus.iss.backend.repository.ImageRepository;

@Service
public class ArchiveMultipleService {

    @Autowired
    ArchiveRepository archiveRepo;
    
    @Autowired
    ImageRepository S3repo;

//Create
    public Optional<String> recordBundle(Archive archive, List<MultipartFile> files){
        try {
            String bundleId = UUID.randomUUID().toString().substring(0, 8);
            archive.setBundleId(bundleId);

            List<String> urls = new LinkedList<>();
            
            for(MultipartFile file : files){
                String url = S3repo.upload(file);                
                urls.add(url);
            }
            archive.setUrls(urls);

            archiveRepo.recordBundle(archive);

            return Optional.of(bundleId);

        } catch (IOException e) {            
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
