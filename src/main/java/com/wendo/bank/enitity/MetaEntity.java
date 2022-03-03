package com.wendo.bank.enitity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@MappedSuperclass
@Data
public class MetaEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    protected Long id;
    protected ZonedDateTime createdTimestamp;
    protected ZonedDateTime updatedTimestamp;

}
