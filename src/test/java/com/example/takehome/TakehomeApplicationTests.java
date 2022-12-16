package com.example.takehome;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.endpoint.ApiVersion;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
class TakehomeApplicationTests {

    @Test
    void contextLoads() {
        assertTrue("API VERSION", ApiVersion.LATEST.equals(ApiVersion.V3));
    }
}
