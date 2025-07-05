package com.ironbucket.brazznossel;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/storage/s3")
class S3Controller {
	private final StorageService storageService;

	@GetMapping(path="/upload/**")
	public  Mono<String> hello(@AuthenticationPrincipal Jwt principal) {	
		String user = "UNKNOWN";		
		if(principal != null) {
			user = principal.getClaimAsString("preferred_username");
		}
		System.out.println(storageService.list("tenant-a", "analytics-eu", "incoming"));
        return Mono.just("Hello brazznossel user: "+user);
	}
	@PostConstruct
	public void init() {
		System.setProperty("jclouds.debug", "true");
		System.setProperty("jclouds.wire.log", "true");

	}
}
