package entityportal.service.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity(name="Custom")
@Table(name="custom_entity")
public class CustomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    Long id;

    @Column(name="Date")
    Long date;

    @Column(name="Type")
    String type;

    @Column(name="Value")
    String value;

    @Column(name="CreatedBy")
    Long createdBy;
}
