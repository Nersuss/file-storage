package ru.nersus.storage.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.UUID;

@RedisHash("session")
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class Session implements Serializable {
    private UUID id;
    private int userId;
    private String email;
}
