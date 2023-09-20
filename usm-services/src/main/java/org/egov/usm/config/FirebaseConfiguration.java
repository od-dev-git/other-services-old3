package org.egov.usm.config;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

@Configuration
@EnableConfigurationProperties(FirebaseProperties.class)
public class FirebaseConfiguration {
	
	@Autowired
	private FirebaseProperties firebaseProperties;


	@Bean
    GoogleCredentials googleCredentials() {
        try {
            if (firebaseProperties.getServiceAccount() != null) {
                try( InputStream is = firebaseProperties.getServiceAccount().getInputStream()) {
                    return GoogleCredentials.fromStream(is);
                }                
            } 
            else {
                // Use standard credentials chain. Useful when running inside GKE
                return GoogleCredentials.getApplicationDefault();
            }
        } 
        catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    @Bean
    FirebaseApp firebaseApp(GoogleCredentials credentials) {
        FirebaseOptions options = FirebaseOptions.builder()
          .setCredentials(credentials)
          .build();
 
        if(CollectionUtils.isEmpty(FirebaseApp.getApps()))
        	return FirebaseApp.initializeApp(options);
        return FirebaseApp.getInstance(FirebaseApp.DEFAULT_APP_NAME);
    }

    @Bean
    FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
