package com.ironbucket.brazznossel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/*@RestController
@RequestMapping("/storage")*/
@RequiredArgsConstructor
class StorageController {
	private final StorageService storageService;

	String storageId="tenant-a";
    @PostMapping("/{bucket}/{key}")
    public ResponseEntity<String> upload(
            @PathVariable String bucket,
            @PathVariable String key,
            @RequestHeader HttpHeaders headers,
            InputStream body,
            @RequestHeader("Content-Length") long contentLength) {

        Map<String, String> metadata = headers.toSingleValueMap();
        boolean ok = storageService.upload(storageId,bucket, key, body, contentLength, metadata);
        if(ok) {
        	return ResponseEntity.ok("Uploaded " + key);
        }else {
        	return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        
    }

    @GetMapping("/{bucket}/{key}")
    public ResponseEntity<byte[]> download(@PathVariable String bucket, @PathVariable String key) throws IOException {
    	
    	return  ResponseEntity.ok(storageService.download(storageId,bucket, key));
    }
    @DeleteMapping("/{bucket}/{key}")
    public ResponseEntity<String> delete(
            @PathVariable String bucket,
            @PathVariable String key) {

       
    	storageService.delete(storageId, bucket, key);
        return ResponseEntity.ok("Deleted: " + key);
    }
    @GetMapping("/{bucket}")
    public ResponseEntity<List<String>> listObjects(
            @PathVariable String bucket,
            @RequestParam(required = false, defaultValue = "") String prefix) {
        List<String> keys = storageService.list(storageId,bucket, prefix);
        return ResponseEntity.ok(keys);
    }
    @PostConstruct
    public void init() {
    	
    }
}
