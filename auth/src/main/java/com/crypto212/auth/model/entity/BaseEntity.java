package com.crypto212.auth.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class BaseEntity {
    private long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
