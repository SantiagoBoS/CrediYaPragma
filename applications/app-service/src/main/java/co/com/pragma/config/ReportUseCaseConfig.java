package co.com.pragma.config;

import co.com.pragma.model.reports.gateways.ReportRepository;
import co.com.pragma.usecase.reports.ReportUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReportUseCaseConfig {
    @Bean
    public ReportUseCase reportUseCase(
            ReportRepository reportRepository
    ) {
        return new ReportUseCase(
                reportRepository
        );
    }
}
