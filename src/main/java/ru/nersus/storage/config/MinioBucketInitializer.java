package ru.nersus.storage.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class MinioBucketInitializer implements ApplicationRunner {

    MinioClient minioClient;

    @Value("${minio.bucket.name}")
    @NonFinal
    String bucketName;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        minioMakeBucket();
    }

    public void minioMakeBucket() throws MinioException {
        boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bucketName)
                .build());
        if (!isBucketExists) {
            log.info("Bucket not exists");
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build());
            log.info("Bucket '{}' successfully created", bucketName);
        }
    }

}
