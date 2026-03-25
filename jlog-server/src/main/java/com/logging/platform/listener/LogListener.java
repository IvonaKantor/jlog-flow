package com.logging.platform.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.logging.platform.mapper.LogDataMapper;
import com.logging.platform.models.LogData;
import com.logging.platform.repository.LogEntityRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

@ApplicationScoped
public class LogListener {

    private static final Logger log = LoggerFactory.getLogger(LogListener.class);

    @Inject
    private LogDataMapper logDataMapper;

    @Inject
    private LogEntityRepository logEntityRepository;

    @Inject
    private ObjectMapper objectMapper;

    @Incoming("logs")
    @Transactional
    public void processLogEntry(String logLine) {
        if (logLine == null || logLine.isBlank()) {
            return;
        }

        try {
            final var root = (ObjectNode) objectMapper.readTree(logLine);
            final var timestamp = parseTimestamp(root.path("timestamp").asText(null));
            root.putNull("timestamp");

            final var logData = objectMapper.treeToValue(root, LogData.class);
            logData.setTimestamp(timestamp);

            logEntityRepository.persist(logDataMapper.map(logData));
        } catch (Exception e) {
            log.error("Failed to persist log entry: {} | payload={}", e.getMessage(), logLine, e);
        }
    }

    private Date parseTimestamp(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        final var trimmed = value.trim();

        try {
            return Date.from(Instant.parse(trimmed));
        } catch (DateTimeParseException ignored) {
        }

        try {
            return Date.from(OffsetDateTime.parse(trimmed).toInstant());
        } catch (DateTimeParseException ignored) {
        }

        try {
            final var localDateTime = LocalDateTime.parse(trimmed);
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException ignored) {
        }

        try {
            final var localDate = LocalDate.parse(trimmed, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException ignored) {
        }

        throw new IllegalArgumentException("Unsupported timestamp format: " + value);
    }
}