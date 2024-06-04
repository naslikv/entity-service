package entityportal.service.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name="ImportType")
@Table(name="import_type_entity")
public class ImportTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    Long id;

    @Column(name="Description")
    String description;

    @Column(name="Type")
    String type;
}
