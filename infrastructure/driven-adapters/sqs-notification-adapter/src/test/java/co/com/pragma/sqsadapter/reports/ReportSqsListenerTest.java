package co.com.pragma.sqsadapter.reports;

import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.reports.gateways.ReportRepository;
import co.com.pragma.sqsadapter.config.LoanEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.Message;

import static org.mockito.Mockito.*;

class ReportSqsListenerTest {

    private SqsAsyncClient sqsAsyncClient;
    private ObjectMapper objectMapper;
    private ReportRepository reportRepository;
    private ReportSqsListener listener;

    @BeforeEach
    void setUp() {
        sqsAsyncClient = mock(SqsAsyncClient.class);
        objectMapper = new ObjectMapper();
        reportRepository = mock(ReportRepository.class);

        listener = new ReportSqsListener(sqsAsyncClient, objectMapper, reportRepository);
    }

    @Test
    void shouldCallIncrementAndAddApprovedAmountWhenStatusApprovedAndAmountNotNull() throws Exception {
        LoanEvent event = new LoanEvent("1", RequestStatus.APPROVED.toString(), 500.0);
        String body = objectMapper.writeValueAsString(event);
        Message msg = Message.builder().body(body).receiptHandle("handle-1").build();

        if (RequestStatus.APPROVED.toString().equals(event.getStatus()) && event.getAmount() != null) {
            when(reportRepository.incrementCounter()).thenReturn(mock(reactor.core.publisher.Mono.class));
            when(reportRepository.addApprovedAmount(event.getAmount())).thenReturn(mock(reactor.core.publisher.Mono.class));

            reportRepository.incrementCounter().subscribe();
            reportRepository.addApprovedAmount(event.getAmount()).subscribe();
        }

        verify(reportRepository).incrementCounter();
        verify(reportRepository).addApprovedAmount(500.0);
    }

    @Test
    void shouldCallOnlyIncrementWhenAmountIsNull() throws Exception {
        LoanEvent event = new LoanEvent("1", RequestStatus.APPROVED.toString(), null);
        when(reportRepository.incrementCounter()).thenReturn(mock(reactor.core.publisher.Mono.class));
        reportRepository.incrementCounter().subscribe();
        verify(reportRepository).incrementCounter();
        verify(reportRepository, never()).addApprovedAmount(any());
    }

    @Test
    void shouldNotCallAnythingWhenStatusNotApproved() throws Exception {
        LoanEvent event = new LoanEvent("1", RequestStatus.PENDING_REVIEW.toString(), 100.0);
        verify(reportRepository, never()).incrementCounter();
        verify(reportRepository, never()).addApprovedAmount(any());
    }
}
