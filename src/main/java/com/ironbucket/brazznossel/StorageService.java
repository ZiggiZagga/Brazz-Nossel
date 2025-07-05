package com.ironbucket.brazznossel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StorageService {
	private final MultiStorageAdapter storageAdapter;
    
    @PreAuthorize("hasAuthority('storage:upload')")
    public boolean upload(String storageId, String bucket, String key,
            InputStream body,
            long contentLength,
            Map<String, String> metadata) {
        //TODO properly verify the upload
    	storageAdapter.forStorage(storageId).upload(bucket, key, body, contentLength, metadata);
        return true;
    }


    @PreAuthorize("hasAuthority('storage:download')")
    public byte[] download(String storageId, String bucket, String key) throws IOException {
        try (InputStream in = storageAdapter.forStorage(storageId).download(bucket, key)) {
            return in.readAllBytes();
        }
    }
    
    @PreAuthorize("hasAuthority('storage:delete')")
    public boolean delete(String storageId, String bucket, String key) {
    	//TODO properly verify the delete
    	storageAdapter.forStorage(storageId).delete(bucket, key);
        return true;
    }
    @PreAuthorize("hasAuthority('storage:list')")
    public List<String> list(String storageId, String bucket, String prefix) {
        List<String> keys = storageAdapter.forStorage(storageId).list(bucket, prefix);
        return keys;
    }
    
}
