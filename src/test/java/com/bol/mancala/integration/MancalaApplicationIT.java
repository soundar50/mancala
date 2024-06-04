package com.bol.mancala.integration;

import com.bol.mancala.MancalaApplication;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MancalaApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Disabled
public class MancalaApplicationIT {
}
