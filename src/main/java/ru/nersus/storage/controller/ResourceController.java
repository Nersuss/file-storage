package ru.nersus.storage.controller;

import io.minio.errors.MinioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nersus.storage.dto.DirectoryCreateRsDto;
import ru.nersus.storage.dto.ResourceRsDto;
import ru.nersus.storage.dto.UploadRsDto;
import ru.nersus.storage.entity.User;
import ru.nersus.storage.service.ResourceService;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Rest-controller for resources (files, folders)")
@RequestMapping("/api")
public class ResourceController {

    ResourceService resourceService;

    @GetMapping(value = "/resource")
    @Operation(summary = "Get resource info")
    ResourceRsDto getResource(@RequestParam(name = "path") String path,
                              @AuthenticationPrincipal User user) throws MinioException, IOException {
        return resourceService.getFile(URLDecoder.decode(path, Charset.defaultCharset()), user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/resource")
    @Operation(summary = "Delete resource")
    void deleteResource(@RequestParam(name = "path") String path,
                        @AuthenticationPrincipal User user) throws MinioException {
        resourceService.delete(path, user);
    }

    @GetMapping(value = "/resource/download", produces = "application/octet-stream")
    @Operation(summary = "Download resource")
    void getResourceDownload(@RequestParam(name = "path") String path,
                             @AuthenticationPrincipal User user) throws MinioException {
        resourceService.download(path, user);
    }

    @PostMapping("/resource/move")
    @Operation(summary = "Move or copy resource")
    ResourceRsDto postResourceMove(@RequestParam(name = "from") String from,
                                 @RequestParam(name = "to") String to,
                                 @AuthenticationPrincipal User user) throws MinioException, IOException {
        return resourceService.moveResource(from, to, user);
    }

    @PostMapping("/resource/search")
    @Operation(summary = "Resources search")
    ResourceRsDto getResourcesSearch(@RequestParam(name = "query") String query,
                                 @AuthenticationPrincipal User user) throws MinioException, IOException {
        return resourceService.resourcesSearch(query, user);
    }

    @PostMapping("/resource")
    @Operation(summary = "Upload resource")
    @ResponseStatus(HttpStatus.CREATED)
    UploadRsDto postResource(@RequestParam(name = "file") MultipartFile file,
                             @RequestParam(name = "path") String path,
                             @AuthenticationPrincipal User user) throws MinioException, IOException {
        return resourceService.addFile(file, path, user);
    }

    @GetMapping("/directory")
    @Operation(summary = "Get folder information")
    ResourceRsDto getDirectory(@RequestParam(name = "path") String path,
                               @AuthenticationPrincipal User user) throws MinioException {
        return resourceService.getDirectory(path, user);
    }

    @PostMapping("/directory")
    @Operation(summary = "Create folder")
    @ResponseStatus(HttpStatus.CREATED)
    DirectoryCreateRsDto postDirectory(@RequestParam(name = "path") String path,
                                       @AuthenticationPrincipal User user) throws MinioException, IOException {
        return resourceService.createDirectory(path, user);
    }

}
