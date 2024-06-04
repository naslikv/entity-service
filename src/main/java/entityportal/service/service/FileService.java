package entityportal.service.service;

import entityportal.service.model.File;
import entityportal.service.model.User;
import entityportal.service.persistence.entity.FileEntity;
import entityportal.service.persistence.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@Slf4j
public class FileService {
    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public File upload(MultipartFile file) throws IOException {
        Path path = Paths.get("data/" + file.getOriginalFilename());
        boolean isFileExist=Files.exists(path);
        if(!isFileExist){
            try{
                Files.createDirectories(path.getParent());
            }catch(Exception e){
                log.error("Unable to create directory");
                e.printStackTrace();
            }
            Files.createFile(path);
        }
        try (OutputStream outputStream = Files.newOutputStream(path)) {
            outputStream.write(file.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        File fileToSave = new File();
        fileToSave.setName(file.getOriginalFilename());
        fileToSave.setUrl(path.toString());
        fileToSave.setCreatedOn(ZonedDateTime.now());
        fileToSave.setModifiedOn(ZonedDateTime.now());
        User user = new User();
        user.setId(1L);
        fileToSave.setCreatedBy(user);
        fileToSave.setModifiedBy(user);
        return saveFile(fileToSave);
    }

    public File saveFile(File file) {
        try {
            FileEntity entity = new FileEntity();
            entity.setFileName(file.getName());
            entity.setUrl(file.getUrl());
            entity.setCreatedBy(file.getCreatedBy().getId());
            entity.setModifiedBy(file.getModifiedBy().getId());
            entity.setCreatedDate(file.getCreatedOn().toEpochSecond());
            entity.setModifiedDate(file.getModifiedOn().toEpochSecond());
            FileEntity saved = fileRepository.save(entity);
            file.setId(saved.getId());
            return file;
        }catch (Exception e){
            e.printStackTrace();
            log.info(e.getMessage());
        }
        return null;
    }

    public File getFileByID(Long id){
        Optional<FileEntity> fileEntity=fileRepository.findById(id);
        if(fileEntity.isPresent()){
            File file=new File();
            file.setId(fileEntity.get().getId());
            file.setName(fileEntity.get().getFileName());
            file.setUrl(fileEntity.get().getUrl());
            file.setCreatedOn(ZonedDateTime.ofInstant(Instant.ofEpochSecond(fileEntity.get().getCreatedDate()), ZoneId.of("Asia/Kolkata")));
            file.setModifiedOn(ZonedDateTime.ofInstant(Instant.ofEpochSecond(fileEntity.get().getModifiedDate()), ZoneId.of("Asia/Kolkata")));
            User createdUser=new User();
            createdUser.setId(fileEntity.get().getCreatedBy());
            file.setCreatedBy(createdUser);
            User modifiedUser=new User();
            modifiedUser.setId(fileEntity.get().getModifiedBy());
            file.setModifiedBy(modifiedUser);
            return file;
        }
        return null;
    }
}
