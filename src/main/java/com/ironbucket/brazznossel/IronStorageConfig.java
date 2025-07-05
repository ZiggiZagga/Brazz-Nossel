package com.ironbucket.brazznossel;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStoreContext;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
class IronStorageConfig {


	private StorageAdapter getMinioClientAdapter(StorageProperties storageProps, StorageConfig config ) {
		MinioClient minioClient =
		          MinioClient.builder()
		              .endpoint(config.endpoint())
		              .credentials(config.identity(), config.credential())
		              .build();
		
		return new MinioStorageAdapter(minioClient);
	}
	private StorageAdapter getJCloudsApapter(StorageProperties storageProps, StorageConfig config, Properties overrides) {		
		overrides.setProperty("jclouds.region.us-east-1.endpoint",config.endpoint());
		BlobStoreContext ctx = ContextBuilder.newBuilder("s3")
				.credentials(config.identity(), config.credential())
				.endpoint(config.endpoint())
				.overrides(overrides)
				.buildView(BlobStoreContext.class);
		return  new JCloudsStorageAdapter(ctx.getBlobStore());
	}
	private Map<String, StorageAdapter> minioStorageAdapterMap(StorageProperties storageProps) {
		Map<String, StorageAdapter> adapterMap = new HashMap<>();
		storageProps.getStorages().forEach((storageId, config) -> {	
			if("minio".equals(config.backend())) {
				adapterMap.put(storageId, getMinioClientAdapter(storageProps, config));
			}			
		});
		return adapterMap;
	}
	private Map<String, StorageAdapter> blobStorageAdapterMap(StorageProperties storageProps) {
		Map<String, StorageAdapter> adapterMap = new HashMap<>();
		Properties overrides = new Properties();
		//TODO set this in the config file
		overrides.setProperty("jclouds.s3.virtual-host-buckets", "false"); // Enable path-style access
		overrides.setProperty("jclouds.strip-expect-header", "true");
		overrides.setProperty("jclouds.regions", "us-east-1");

		overrides.setProperty("s3.service-path", "/");


		storageProps.getStorages().forEach((storageId, config) -> {						
			if("jclouds".equals(config.backend())) {
				adapterMap.put(storageId, getJCloudsApapter(storageProps, config, overrides));
			}
		});

		return adapterMap;
	}
	
	
	@Bean
	public Map<String, StorageAdapter> storageAdapterMap(StorageProperties storageProps){
		
		Map<String, StorageAdapter>  result = blobStorageAdapterMap(storageProps);
		result.putAll(minioStorageAdapterMap(storageProps));
		return result;
		
	}

	@Bean
	public MultiStorageAdapter multiStorageAdapter(Map<String, StorageAdapter> storageAdapterMap) {
		return storageId -> storageAdapterMap.getOrDefault(storageId,
				throwMissingStorage(storageId));
	}

	private StorageAdapter throwMissingStorage(String storageId) {
		throw new IllegalArgumentException("No storage adapter configured for Storage: " + storageId);
	}

	
}
