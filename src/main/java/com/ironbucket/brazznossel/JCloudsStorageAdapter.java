package com.ironbucket.brazznossel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.options.ListContainerOptions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class JCloudsStorageAdapter implements StorageAdapter {

	private final BlobStore blobStore;


    @Override
    public void upload(String bucket, String key, InputStream data, long contentLength, Map<String, String> metadata) {
    	if (!blobStore.containerExists(bucket)) {
    	    blobStore.createContainerInLocation(null, bucket);
    	}
        Blob blob = blobStore.blobBuilder(key)
            .payload(data)
            .contentLength(contentLength)
            .userMetadata(metadata)
            .build();
        blobStore.putBlob(bucket, blob);
    }

    @Override
    public InputStream download(String bucket, String key) throws IOException {
        Blob blob = blobStore.getBlob(bucket, key);
        return blob.getPayload().openStream();
    }

    @Override
    public void delete(String bucket, String key) {
        blobStore.removeBlob(bucket, key);
    }

    @Override
    public boolean exists(String bucket, String key) {
        return blobStore.blobExists(bucket, key);
    }

    @Override
    public List<String> list(String bucket, String prefix) {
        return blobStore.list(bucket, ListContainerOptions.Builder.prefix(prefix))
            .stream()
            .map(StorageMetadata::getName)
            .toList();
    }
}
