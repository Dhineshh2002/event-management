package com.example.eventmanager.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PdfService {
    private final TemplateEngine templateEngine;

    public byte[] generateTicketPdf(Map<String, Object> data) throws IOException {
        Context context = new Context();
        context.setVariables(data);
        String html = templateEngine.process("eventTicket", context);
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        }
    }
}
