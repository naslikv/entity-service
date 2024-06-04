package entityportal.service.persistence.entity;

import jakarta.persistence.*;
import lombok.Cleanup;
import lombok.Data;

@Data
@Entity(name="ImportJob")
@Table(name="import_job_entity")
public class ImportJobEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    Long id;

    @Column(name="TypeID")
    Long typeID;

    @Column(name="FileID")
    Long fileID;

    @Column(name="BatchReference")
    String batchReference;

    @Column(name="CreatedBy")
    Long createdBy;

    @Column(name="ModifiedBy")
    Long modifiedBy;

    @Column(name="CreatedDate")
    Long createdDate;

    @Column(name="ModifiedDate")
    Long modifiedDate;
}
