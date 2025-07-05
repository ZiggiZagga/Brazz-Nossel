package com.ironbucket.brazznossel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

interface StorageAdapter {
	void upload(String bucket, String key, InputStream data, long contentLength, Map<String, String> metadata);
    InputStream download(String bucket, String key)  throws IOException;
    void delete(String bucket, String key);
    boolean exists(String bucket, String key);
    List<String> list(String bucket, String prefix); 
}
