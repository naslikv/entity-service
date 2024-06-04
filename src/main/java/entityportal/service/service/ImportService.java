package entityportal.service.service;

import entityportal.service.config.CustomEntityImportConfiguration;
import entityportal.service.config.CustomEntityProcessor;
import entityportal.service.model.Entity;
import entityportal.service.model.File;
import entityportal.service.model.ImportJob;
import entityportal.service.model.ImportType;
import entityportal.service.persistence.ImportJobQueryRepository;
import entityportal.service.persistence.entity.ImportJobEntity;
import entityportal.service.persistence.entity.ImportTypeEntity;
import entityportal.service.persistence.repository.ImportJobRepository;
import entityportal.service.persistence.repository.ImportTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class ImportService {
    private final ImportJobQueryRepository importJobQueryRepository;
    private final ImportTypeRepository importTypeRepository;
    private final CustomEntityImportConfiguration customEntityImportConfiguration;
    private final FileService fileService;
    private final DataSource dataSource;
    private final JobRepository jobRepository;
    private final JpaTransactionManager jpaTransactionManager;
    private final JobLauncher jobLauncher;
    private final JdbcBatchItemWriter<Entity> temperatureWriter;
    private final ImportJobRepository importJobRepository;

    @Autowired
    public ImportService(ImportJobQueryRepository importJobQueryRepository,
                         ImportTypeRepository importTypeRepository,
                         CustomEntityImportConfiguration customEntityImportConfiguration,
                         FileService fileService, DataSource dataSource,
                         JobRepository jobRepository,
                         JpaTransactionManager jpaTransactionManager, JobLauncher jobLauncher,
                         JdbcBatchItemWriter<Entity> temperatureWriter,
                         ImportJobRepository importJobRepository) {
        this.importJobQueryRepository = importJobQueryRepository;
        this.importTypeRepository = importTypeRepository;
        this.customEntityImportConfiguration = customEntityImportConfiguration;
        this.fileService = fileService;
        this.dataSource = dataSource;
        this.jobRepository = jobRepository;
        this.jpaTransactionManager = jpaTransactionManager;
        this.jobLauncher = jobLauncher;
        this.temperatureWriter = temperatureWriter;
        this.importJobRepository = importJobRepository;
    }

    public List<ImportJob> getImportJobs() {
        return importJobQueryRepository.getAllImports();
    }

    public List<ImportType> getImportTypes() {
        List<ImportTypeEntity> entityList = importTypeRepository.findAll();
        if (!entityList.isEmpty()) {
            return entityList.stream()
                    .map(entity -> {
                        ImportType importType = new ImportType();
                        importType.setId(entity.getId());
                        importType.setKey(entity.getType());
                        importType.setDescription(entity.getDescription());
                        return importType;
                    }).toList();
        }
        return null;
    }

    @Async
    public void runJob(ImportJob request) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        if (request.getFile() != null && request.getFile().getId() != null) {
            File file = fileService.getFileByID(request.getFile().getId());
            if (file != null) {
                //Initiate the reader
                FlatFileItemReader<Entity> reader = customEntityImportConfiguration.customEntityFlatFileItemReader(file.getUrl());
                CustomEntityProcessor processor = customEntityImportConfiguration.customEntityProcessor();
                Step step = customEntityImportConfiguration.customEntityReadStep(jobRepository, jpaTransactionManager, reader,
                        processor, temperatureWriter);
                Job job = customEntityImportConfiguration.customEntityImportJob(jobRepository, step);
                ImportJobEntity importJobEntity = new ImportJobEntity();
                importJobEntity.setFileID(request.getFile().getId());
                importJobEntity.setTypeID(request.getType().getId());
                importJobEntity.setCreatedBy(request.getCreatedBy().getId());
                importJobEntity.setModifiedBy(request.getModifiedBy().getId());
                importJobEntity.setCreatedDate(Instant.now().getEpochSecond());
                importJobEntity.setModifiedDate(Instant.now().getEpochSecond());
                try {
                    importJobEntity = importJobRepository.save(importJobEntity);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
                importJobEntity.setBatchReference(jobExecution.getId().toString());
                importJobRepository.save(importJobEntity);
            }
        }
    }
}
