package com.logging.platform;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class LogGenerator {

    private static final Logger log = LoggerFactory.getLogger(LogGenerator.class);
    private static long counter = 0;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    void onStart(@Observes StartupEvent ev) {
        log.info("Quarkus application starting. Initializing log generator.");
        startGenerating();
    }

    void onStop(@Observes ShutdownEvent ev) {
        log.info("Quarkus application shutting down. Stopping log generator.");
        stopGenerating();
    }

    public void startGenerating() {
        log.info("Starting log generation service. A new log entry will be created every 10 seconds.");

        final Runnable logTask = () -> {
            try {
                MDC.put("log-generator-id", String.valueOf(++counter));
                final String logMessage = "Generated log entry: New application event occurred at " + System.currentTimeMillis();

                if(counter % 10 == 0) {
                    log.error("test exception", new RuntimeException("test exception"));
                }else if(counter % 5 == 0) {
                    log.warn("warning message");
                }else if(counter % 11 == 0) {
                    log.debug("Debug information. System time:{}", System.currentTimeMillis());
                }
                else{
                    log.info(logMessage);
                }
                counter++;
                MDC.clear();
            } catch (Exception e) {
                log.error("Error generating log.", e);
                MDC.clear();
            }
        };

        scheduler.scheduleAtFixedRate(logTask, 0, 10, TimeUnit.SECONDS);
    }

    public void stopGenerating() {
        log.info("Stopping log generation service.");

        scheduler.shutdown();

        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                log.warn("Scheduler did not terminate gracefully. Forcing shutdown.");
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("Shutdown interrupted.");
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}