package uz.inha.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import uz.inha.dto.BasketProductDto;
import uz.inha.entity.Order;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PdfService {
    public byte[] generateOrderCheckout(Order order, List<BasketProductDto> basketProducts) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (PdfWriter pdfWriter = new PdfWriter(outputStream)) {
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);

            // Add Order Details
            document.add(new Paragraph(String.format("Order ID: %s", order.getId())));
            document.add(new Paragraph(String.format("Order Time: %s", order.getFormattedDate())));
            document.add(new Paragraph(""));

            // Create Table for Order Items
            Table table = new Table(5);
            table.addCell("Name");
            table.addCell("Price");
            table.addCell("Count");
            table.addCell("Subtotal");
            table.addCell("Tax (1%)");

            // Add Rows for each BasketProductDto
            double total = 0.0;
            for (BasketProductDto basketProduct : basketProducts) {
                table.addCell(basketProduct.product());
                table.addCell(String.valueOf(basketProduct.productPrice()));
                table.addCell(String.valueOf(basketProduct.amount()));
                double subTotal = basketProduct.amount() * basketProduct.productPrice();
                table.addCell(String.valueOf(subTotal));
                double tax = subTotal * 0.01;
                table.addCell(String.valueOf(tax));
                total += subTotal;
            }

            // Add Table to Document
            document.add(table);

            // Calculate Tax and Total with Tax
            double taxTotal = total * 0.01;
            double totalWithTax = total + taxTotal;

            // Add Summary Information
            document.add(new Paragraph(""));
            document.add(new Paragraph(String.format("Total: %.2f", total)));
            document.add(new Paragraph(String.format("Tax (1%%): %.2f", taxTotal)));
            document.add(new Paragraph(String.format("Total with Tax: %.2f", totalWithTax)));

            // Add QR Code
            ByteArrayOutputStream qrOutputStream = new ByteArrayOutputStream();
            generateQRCodeImage(order.toString(), 150, 150, qrOutputStream);
            ImageData qrImageData = ImageDataFactory.create(qrOutputStream.toByteArray());
            Image qrImage = new Image(qrImageData);
            document.add(qrImage);

            // Close Document
            document.close();

        } catch (IOException | WriterException e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }


    @SneakyThrows
    private void generateQRCodeImage(String text, int width, int height, ByteArrayOutputStream outputStream)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
    }
}
