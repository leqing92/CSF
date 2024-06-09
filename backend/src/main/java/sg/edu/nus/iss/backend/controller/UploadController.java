package sg.edu.nus.iss.backend.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.backend.models.Archive;
import sg.edu.nus.iss.backend.service.ArchiveService;

@Controller
@RequestMapping(path="/api")
public class UploadController {
    
    @Autowired
    ArchiveService archiveSvc;
    
    @PostMapping(path = "/upload")
    public ResponseEntity<Object> postMethodName(
        @RequestPart String name,
        @RequestPart String title,
        @RequestPart String comments, 
        @RequestPart MultipartFile zipFile) {
       
        Archive archive = new Archive();
        archive.setName(name);
        archive.setTitle(title);
        archive.setDate(new Date());
        archive.setComments(comments);
        // System.out.println(archive.toString());

        Optional<String> bundleIdOpt = archiveSvc.recordBundle(archive, zipFile);
        if (bundleIdOpt.isPresent()) {
            String bundleId = bundleIdOpt.get();
            Map<String, String> response = new HashMap<>();
            response.put("bundleId", bundleId);

            return ResponseEntity.status(201).body(response);
        } 
        else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create bundle");

            return ResponseEntity.status(500).body(error);
        }        
    }
    
    @GetMapping(path = "/bundle/{id}")
    public ResponseEntity<Object> getBundleById(@PathVariable String id) {
        System.out.println(id);
        Optional<Archive> archiveOpt = archiveSvc.getBundleByBundleId(id);
        if(archiveOpt.isPresent()){
            return ResponseEntity.status(200).body(archiveOpt.get());
        }
        else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "ID: " + id + " is not found");

            return ResponseEntity.status(404).body(error);
        }
    }
    
    @GetMapping(path = "/bundles")
    public ResponseEntity<String> getBundle() {
        return ResponseEntity.status(200).body(archiveSvc.getBundles());
    }
    
}
