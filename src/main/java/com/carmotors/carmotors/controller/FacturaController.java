package com.carmotors.carmotors.controller;

import com.carmotors.carmotors.model.dao.FacturaDAO;
import com.carmotors.carmotors.model.entities.Factura;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FacturaController {
    private final FacturaDAO facturaDAO = new FacturaDAO();

    public void generarFacturaPDF(Factura factura) throws Exception {
        String folder = "invoices";
        File dir = new File(folder);
        if (!dir.exists()) dir.mkdirs();

        String fileName = folder + "/factura_" + factura.getIdOrden() + ".pdf";

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        // Encabezado de la factura
        document.add(new Paragraph("FACTURA ELECTRÓNICA", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        document.add(new Paragraph("Taller: Motores & Ruedas"));
        document.add(new Paragraph("NIT: 900123456-7"));
        document.add(new Paragraph("Dirección: Calle 123 # 45-67, Bogotá"));
        document.add(new Paragraph("Teléfono: 3100000000"));
        document.add(new Paragraph(" "));

        // Información del cliente y factura
        document.add(new Paragraph("ID Orden: " + factura.getIdOrden()));
        document.add(new Paragraph("Fecha emisión: " + new SimpleDateFormat("yyyy-MM-dd").format(factura.getFechaEmision())));
        document.add(new Paragraph("Subtotal: $" + factura.getSubtotal()));
        document.add(new Paragraph("Impuestos (19%): $" + factura.getImpuestos()));
        document.add(new Paragraph("Total: $" + factura.getTotal()));
        document.add(new Paragraph("CUFE: " + factura.getCufe()));
        document.add(new Paragraph(" "));

        // Generar y agregar código QR
        String qrPath = folder + "/qr_" + factura.getIdOrden() + ".png";
        generateQRCodeImage(factura.getQrUrl(), 150, 150, qrPath);
        com.itextpdf.text.Image qr = com.itextpdf.text.Image.getInstance(qrPath);
        qr.setAlignment(Element.ALIGN_CENTER);
        document.add(qr);

        document.add(new Paragraph(" "));  // espacio
        document.add(new Paragraph("Gracias por su preferencia.", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10)));

        document.close();

        // Guardar en la base de datos
        facturaDAO.guardarFactura(factura);
    }

    private void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
