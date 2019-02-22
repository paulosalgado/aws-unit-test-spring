package com.example.awsunittest;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.testcontainers.containers.localstack.LocalStackContainer;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AwsUnitTestApplicationTests {

    @ClassRule
    public static LocalStackContainer localstack = new LocalStackContainer().withServices(S3);

    private AmazonS3 s3;

    private static final String BUCKET_NAME = "testbucket";
    private static final String FILE_NAME = "test_file.json";
    private static final String JSON_CONTENT = "{\"name\": \"paulo\", \"email\": \"pjosalgado@gmail.com\"}";

    @Before
    public void setup() {

        s3 = AmazonS3Client
                .builder()
                .withEndpointConfiguration(localstack.getEndpointConfiguration(S3))
                .withCredentials(localstack.getDefaultCredentialsProvider())
                .build();

        s3.createBucket(BUCKET_NAME);
        s3.putObject(BUCKET_NAME, FILE_NAME, JSON_CONTENT);
    }

    @Test
    public void shouldFindFile() {

        S3Object object = s3.getObject(BUCKET_NAME, FILE_NAME);

        assertThat("should find the file", object, notNullValue());
    }

}
