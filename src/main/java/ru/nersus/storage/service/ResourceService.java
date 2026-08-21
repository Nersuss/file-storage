package ru.nersus.storage.service;

import io.minio.*;
import io.minio.errors.MinioException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.nersus.storage.dto.DirectoryCreateRsDto;
import ru.nersus.storage.dto.ResourceRsDto;
import ru.nersus.storage.dto.ResourceType;
import ru.nersus.storage.dto.UploadRsDto;
import ru.nersus.storage.entity.User;

import java.io.IOException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ResourceService {

    MinioClient minio;

    @Value("${minio.bucket.name}")
    @NonFinal
    String bucketName;

    @NonFinal
    String userFolder = "user-%s-files/";

    public ResourceRsDto getFile(String path, User user) throws MinioException, IOException {
        userFolder = String.format(userFolder, user.getId());

        GetObjectResponse file = minio.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(userFolder + path)
                .build());
        return new ResourceRsDto(path, file.object(), (long) file.read(), ResourceType.file);
    }

    public void delete(String path, User user) throws MinioException {
        userFolder = String.format(userFolder, user.getId());

        minio.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(userFolder + path)
                .build());
    }

    public void download(String path, User user) throws MinioException {
        userFolder = String.format(userFolder, user.getId());

        minio.downloadObject(
                DownloadObjectArgs.builder()
                        .bucket(bucketName)
                        .object(userFolder + path)
                        .filename("minioArchive.zip")
                        .build());
    }

    public ResourceRsDto moveResource(String from, String to, User user) {
        return null;
    }

    public ResourceRsDto resourcesSearch(String query, User user) {
        return null;
    }

    public UploadRsDto addFile(MultipartFile file, String path, User user) throws MinioException, IOException {
        userFolder = String.format(userFolder, user.getId());

        minio.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(userFolder + path + file.getOriginalFilename())
                .stream(file.getInputStream(), file.getSize(), null)
                .contentType(file.getContentType())
                .build());
        return new UploadRsDto(Collections.singletonList(new ResourceRsDto(path, file.getOriginalFilename(), file.getSize(), ResourceType.file)));
    }

    public ResourceRsDto getDirectory(String path, User user) throws MinioException {
        userFolder = String.format(userFolder, user.getId());

        GetObjectResponse file = minio.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(userFolder + path)
                .build());
        return new ResourceRsDto(path, file.object(), null, ResourceType.directory);
    }

    public DirectoryCreateRsDto createDirectory(String path, User user) {
        return null;
    }

}
