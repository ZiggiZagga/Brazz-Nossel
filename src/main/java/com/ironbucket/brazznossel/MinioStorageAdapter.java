package com.ironbucket.brazznossel;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class MinioStorageAdapter implements StorageAdapter {

	private final MinioClient minioClient;


    @Override
    public void upload(String bucket, String key, InputStream data, long contentLength, Map<String, String> metadata) {
    	try {
			if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
				minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
			}
			minioClient.putObject(PutObjectArgs.builder()
					.bucket(bucket)
					.contentType("application/octet-stream")
					.stream(data, contentLength, -1)
					.build());
			
		} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
				| InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
				| IllegalArgumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
    }

    @Override
    public InputStream download(String bucket, String key) throws IOException {
    	InputStream stream = null;
		try {
			stream = minioClient.getObject(
				    GetObjectArgs.builder()
				        .bucket(bucket)
				        .object(key)
				        .build()
				);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ErrorResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InsufficientDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InternalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return stream;
    }

    @Override
    public void delete(String bucket, String key) {
    	//TODO return boolean
    	try {
			minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(key).build());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ErrorResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InsufficientDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InternalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
    }

    @Override
    public boolean exists(String bucket, String key) {
    	try {
    		//statObject is designed to fetch metadata about an object.
    		//If the object doesn’t exist, it throws an exception you can catch and interpret.
            minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucket)
                    .object(key)
                    .build()
            );
            return true;
        } catch (ErrorResponseException e) {
            if ("NoSuchKey".equals(e.errorResponse().code())) {
                return false;
            } else {
                throw new RuntimeException("Error checking object existence", e);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error", e);
        }
    }

    @Override
    public List<String> list(String bucket, String prefix) {
    	List<String> objects = new ArrayList<String>();
    	minioClient.listObjects(ListObjectsArgs.builder().bucket(bucket).prefix(prefix).build()).forEach(obj->{
    		try {
				objects.add(obj.get().objectName());
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ErrorResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InsufficientDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InternalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	});;
        return objects;
    }
}
