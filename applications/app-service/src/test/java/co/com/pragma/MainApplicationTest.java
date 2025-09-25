package co.com.pragma;

import co.com.pragma.sqsadapter.notification.SqsNotificationServiceAdapter;
import co.com.pragma.sqsadapter.reports.ReportSqsListener;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MainApplicationTest {

    @MockBean
    private SqsNotificationServiceAdapter sqsNotificationServiceAdapter;

    @MockBean
    private ReportSqsListener reportSqsListener;

    @Test
    void mainMethodRunsWithoutErrors() {
        System.setProperty("spring.profiles.active", "test");
        MainApplication.main(new String[]{});
    }
}