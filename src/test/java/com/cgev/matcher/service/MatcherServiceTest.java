package com.cgev.matcher.service;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MatcherServiceTest {
    @Autowired
    private MatcherService service;

    private MultipartFile fourEmployeesFile;
    private MultipartFile sixteenEmployeesFile;

    @Before
    public void initEmployees() throws IOException {
        File file = new File("src/test/resources/4_employees.csv");
        FileInputStream input = new FileInputStream(file);
        fourEmployeesFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));

        File file2 = new File("src/test/resources/16_employees.csv");
        FileInputStream input2 = new FileInputStream(file2);
        sixteenEmployeesFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input2));
    }

    @Test
    public void testFourEmployeesOutput() {
        double avgScore = service.matchEmployees(fourEmployeesFile).getAvgScore();
        assertThat(avgScore).isEqualTo(65.0);
    }

    @Test
    public void testSixteenEmployeesOutput() {
        double avgScore = service.matchEmployees(sixteenEmployeesFile).getAvgScore();
        assertThat(avgScore).isEqualTo(73.75);
    }
}
