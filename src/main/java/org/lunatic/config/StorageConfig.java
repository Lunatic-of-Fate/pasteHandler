package org.lunatic.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class StorageConfig {
    @Bean
    public AmazonS3 init() {
        try {
            Resource resource = new ClassPathResource("credentials");
            PropertiesCredentials propertiesCredentials = new PropertiesCredentials(resource.getInputStream());
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(propertiesCredentials.getAWSAccessKeyId(),
                    propertiesCredentials.getAWSSecretKey());

            return AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withEndpointConfiguration(
                            new AmazonS3ClientBuilder.EndpointConfiguration(
                                    "storage.yandexcloud.net", "ru-central1"
                            )
                    )
                    .withPathStyleAccessEnabled(true) // Включите, если используете Yandex S3
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке учетных данных", e);
        }
    }
}
