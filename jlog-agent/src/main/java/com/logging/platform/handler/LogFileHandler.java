package com.logging.platform.handler;

import com.logging.platform.intrface.LogProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public class LogFileHandler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(LogFileHandler.class);

    private final File logFile;
    private final LogProcessor processor;
    private volatile boolean keepRunning = true;

    public LogFileHandler(File logFile, LogProcessor processor) {
        this.logFile = logFile;
        this.processor = processor;
    }

    public void stopTailing() {
        keepRunning = false;
    }

    @Override
    public void run() {
        try (final var raf = new RandomAccessFile(logFile, "r")) {
            var lastPosition = raf.length();

            raf.seek(lastPosition);

            log.info("Hande log file[{}], position [{}] ", logFile.getAbsolutePath(), lastPosition);

            while (keepRunning) {
                long currentLength = logFile.length();
                if (currentLength < lastPosition) {
                    log.info("Changed new file log");
                    lastPosition = 0;
                    raf.seek(0);
                }

                String line = raf.readLine();
                if (line != null) {
                    String decodedLine = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                    processor.process(decodedLine);
                    lastPosition = raf.getFilePointer();
                } else {
                    Thread.sleep(1000);
                }
            }
        } catch (IOException e) {
            log.error("Error handle file, message[{}]", e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}