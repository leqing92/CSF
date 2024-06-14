package sg.edu.nus.iss.backend.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.backend.models.Archive;
import sg.edu.nus.iss.backend.service.ArchiveMultipleService;

@Controller
@RequestMapping(path = "/api/multiple")
public class MultiUploadController {
    
    @Autowired
    ArchiveMultipleService archiveMultiSvc;

    @PostMapping(path = "/upload")
    public ResponseEntity<Object> postMethodName(
        @RequestPart String name,
        @RequestPart String title,
        @RequestPart(value ="comments",required = false) String comments, 
        @RequestPart List<MultipartFile> files) {

        if (comments == null || comments.isEmpty()) {
            comments = "";
        }

        Archive archive = new Archive();
        archive.setName(name);
        archive.setTitle(title);
        archive.setDate(new Date());
        archive.setComments(comments);
        // System.out.pritln(archive.toString());
        
        Optional<String> bundleIdOpt = archiveMultiSvc.recordBundle(archive, files);
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
}
