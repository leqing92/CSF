package sg.edu.nus.iss.backend.service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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
    // convert <multipartfile> zip file into List<Multipartfile>
    public List<MultipartFile> unZip(MultipartFile zipFile){
        List<MultipartFile> files = new LinkedList<>();        
        
        try (ZipInputStream zis = new ZipInputStream(zipFile.getInputStream())) {
            // reads the next ZIP file entry
            // returns null when there are no more entries
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                // use to skip directories
                if (!entry.isDirectory()) {
                    ByteArrayOutputStream byteArrOS = new ByteArrayOutputStream();

                    // a fixed size byte array to hold temporary data to be read from zipinputstream in below
                    byte[] buffer = new byte[1024];

                    // the actual amount of data read from the zip entry into the buffer
                    int len;

                    // zis.read() get input stream data and write into buffer
                    // Each time the loop iterates, it reads new data into the same buffer, overwriting the previous contents.
                    // until all input is writen, the buffer will return -1 or 0 and end the loop
                    while ((len = zis.read(buffer)) > 0) {

                        // (btye[] to be wrtitten, start offset of byes to be written, the actual amount of data read from the zip entry into the buffer) 
                        byteArrOS.write(buffer, 0, len);
                    }
                    // convert the accumulated data from zip entry to byte[]
                    byte[] bytes = byteArrOS.toByteArray();

                    //convert byte[] to MultiPartFile
                    MultipartFile multipartFile = new CustomMultipartFile(bytes, entry.getName());
                    files.add(multipartFile);
                }
                // jump to next file in zip
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

    //return List<BundleInfo> to JsonArrayString
    public String getBundles(){
        List<BundleInfo> results = archiveRepo.getBundles();
        JsonArrayBuilder jArrayBuilder = Json.createArrayBuilder();

        for(BundleInfo result : results){
            jArrayBuilder.add(result.toJson());
        }

        return jArrayBuilder.build().toString();
    }

    //return List<BundleInfo>
    public List<BundleInfo> getBundlesAsObject(){        

        return archiveRepo.getBundles();
    }

    //zip multiple file to zipfile
    public static MultipartFile zipFiles(List<MultipartFile> files, String zipFileName) throws IOException {
        try (ByteArrayOutputStream byteArrOS = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(byteArrOS)) {

            for (MultipartFile file : files) {
                // Get the original filename
                String originalFilename = file.getOriginalFilename();

                // Add a new ZIP entry with the original filename
                zos.putNextEntry(new ZipEntry(originalFilename));

                // Copy the content of the multipart file to the ZIP output stream
                InputStream inputStream = file.getInputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                inputStream.close();
                zos.closeEntry();
            }

            // Finish writing ZIP file
            zos.finish();

            // Convert the ZIP content to a byte array
            byte[] zipContent = byteArrOS.toByteArray();

            // Create a new MultipartFile from the byte array
            return new CustomMultipartFile(zipContent, zipFileName);
        }
    }

}
