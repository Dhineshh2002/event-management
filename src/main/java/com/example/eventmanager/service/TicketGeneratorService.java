package com.example.eventmanager.service;

import java.io.IOException;
import java.util.Map;

public interface TicketGeneratorService {
    String generateQrBase64(String ticketNumber) throws IOException;
    String generateAndSavePdf(Map<String, Object> data, String relativePdfPath) throws IOException;
}
