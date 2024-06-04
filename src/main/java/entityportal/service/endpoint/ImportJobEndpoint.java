package entityportal.service.endpoint;

import entityportal.service.model.ImportJob;
import entityportal.service.model.ImportType;
import entityportal.service.service.ImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/imports")
@Slf4j
public class ImportJobEndpoint {
    private final ImportService importService;

    @Autowired
    public ImportJobEndpoint(ImportService importService) {
        this.importService = importService;
    }

    @GetMapping()
    public ResponseEntity<List<ImportJob>> getImportJobs() {
        List<ImportJob> importJobs = importService.getImportJobs();
        if (importJobs != null && !importJobs.isEmpty()) {
            return ResponseEntity.ok(importJobs);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/types")
    public ResponseEntity<List<ImportType>> getImportTypes() {
        List<ImportType> importTypes = importService.getImportTypes();
        if (importTypes != null && !importTypes.isEmpty()) {
            return ResponseEntity.ok(importTypes);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<Object> initiateImport(@RequestBody ImportJob request) {
        try {
            log.info("initiate import");
            importService.runJob(request);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.accepted().body(request);
    }
}
