package com.example.eventmanager.service.impl;

import com.example.eventmanager.service.TicketGeneratorService;
import com.example.eventmanager.util.QrCodeUtil;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.ISpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class TicketGeneratorServiceImpl implements TicketGeneratorService {

    private final ISpringTemplateEngine templateEngine;

    @Value("${tickets.base-path:/var/app/tickets}")
    private String ticketsBasePath;

    @Override
    public String generateQrBase64(String ticketNumber) throws IOException {
        try {
            return QrCodeUtil.generateBase64Qr("Ticket#" + ticketNumber);
        } catch (Exception e) {
            log.error("QR generation failed for ticket {}", ticketNumber, e);
            throw new IOException("QR generation failed", e);
        }
    }

    @Override
    public String generateAndSavePdf(Map<String, Object> data, String relativePdfPath) throws IOException {
        // Render HTML via Thymeleaf
        Context context = new Context();
        context.setVariables(data);
        String html = templateEngine.process("eventTicket", context);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.useFastMode();
            builder.toStream(os);
            builder.run();

            byte[] pdf = os.toByteArray();

            Path path = Paths.get(ticketsBasePath).resolve(relativePdfPath.replaceFirst("^/", ""));
            Files.createDirectories(path.getParent());
            Files.write(path, pdf);

            // Return a normalized path string to store in DB (relative to base path)
            String storedPath = path.toString();
            log.debug("Ticket PDF written to {}", storedPath);
            return storedPath;
        } catch (Exception e) {
            log.error("PDF generation failed", e);
            throw new IOException("PDF generation failed", e);
        }
    }
}
