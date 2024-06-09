package sg.edu.nus.iss.backend.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import sg.edu.nus.iss.backend.models.Archive;
import sg.edu.nus.iss.backend.models.BundleInfo;
import sg.edu.nus.iss.backend.models.CustomMultipartFile;
import sg.edu.nus.iss.backend.repository.ArchiveRepository;
import sg.edu.nus.iss.backend.repository.ImageRepository;

@Service
public class ArchiveService {
    @Autowired
    ArchiveRepository archiveRepo;

    @Autowired
    ImageRepository S3repo;

//Create
    public Optional<String> recordBundle(Archive archive, MultipartFile zipFile){
        try {
            String bundleId = UUID.randomUUID().toString().substring(0, 8);
            archive.setBundleId(bundleId);

            List<String> urls = new LinkedList<>();
            List<MultipartFile> files = unZip(zipFile);
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

    // https://www.baeldung.com/java-compress-and-uncompress
    public List<MultipartFile> unZip(MultipartFile zipFile){
        List<MultipartFile> files = new LinkedList<>();        

        try (ZipInputStream zis = new ZipInputStream(zipFile.getInputStream())) {
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                if (!entry.isDirectory()) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        byteArrayOutputStream.write(buffer, 0, len);
                    }

                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    MultipartFile multipartFile = new CustomMultipartFile(bytes, entry.getName());
                    files.add(multipartFile);
                }

                entry = zis.getNextEntry();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return files;
    }

// Read
    public Optional<Archive> getBundleByBundleId(String id){
        return archiveRepo.getBundleByBundleId(id);
    }

    public String getBundles(){
        List<BundleInfo> results = archiveRepo.getBundles();
        JsonArrayBuilder jArrayBuilder = Json.createArrayBuilder();

        for(BundleInfo result : results){
            jArrayBuilder.add(result.toJson());
        }

        return jArrayBuilder.build().toString();
    }

}
