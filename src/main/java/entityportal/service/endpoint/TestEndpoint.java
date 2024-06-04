package entityportal.service.endpoint;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestEndpoint {
//    private final JobLauncher jobLauncher;
//    private final Job temperatureImportJob;
//
//    @Autowired
//    public TestEndpoint(JobLauncher jobLauncher, Job temperatureImportJob) {
//        this.jobLauncher = jobLauncher;
//        this.temperatureImportJob = temperatureImportJob;
//    }
//
//    @PostMapping()
//    public ResponseEntity<Object> initiateImport() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
//        jobLauncher.run(temperatureImportJob,new JobParameters());
//        return ResponseEntity.ok("Initiated");
//    }
}
