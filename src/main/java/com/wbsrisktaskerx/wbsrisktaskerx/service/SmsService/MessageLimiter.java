package com.wbsrisktaskerx.wbsrisktaskerx.service.SmsService;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class MessageLimiter {
    private final Bucket bucket;

    public MessageLimiter() {
        this.bucket = Bucket4j.builder()
                .addLimit(Bandwidth.simple(1, Duration.ofSeconds(2))) // 1 SMS / 2s
                .build();
    }

    public boolean tryConsume() {
        return bucket.tryConsume(1);
    }
}
