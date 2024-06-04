package entityportal.service.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name="File")
@Table(name="file_entity")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    Long id;

    @Column(name="FileName")
    String fileName;

    @Column(name="Url")
    String url;

    @Column(name="CreatedBy")
    Long createdBy;

    @Column(name="ModifiedBy")
    Long modifiedBy;

    @Column(name="CreatedDate")
    Long createdDate;

    @Column(name="ModifiedDate")
    Long modifiedDate;
}
