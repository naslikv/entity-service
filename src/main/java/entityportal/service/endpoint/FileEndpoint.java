package entityportal.service.endpoint;

import entityportal.service.model.File;
import entityportal.service.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileEndpoint {
    private final FileService fileService;

    @Autowired
    public FileEndpoint(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<File> upload(@RequestParam(value = "file") MultipartFile file) throws IOException {
        File savedFile = fileService.upload(file);
        if (savedFile != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFile);
        }
        return ResponseEntity.unprocessableEntity().build();
    }
}
