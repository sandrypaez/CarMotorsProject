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
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

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

public void enviarFacturaPorCorreo(String destinatario, File archivoAdjunto) {
    String remitente = "parracamila870@gmail.com";
    String password = "x c t t q p v u n r p u h s y j"; 

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(remitente, password);
        }
    });

    try {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(remitente));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        message.setSubject("Factura de su servicio - Motores & Ruedas");

        // Cuerpo del mensaje
        MimeBodyPart texto = new MimeBodyPart();
        texto.setText("Adjunto encontrará su factura electrónica.");

        // Adjuntar PDF
        MimeBodyPart adjunto = new MimeBodyPart();
        adjunto.attachFile(archivoAdjunto);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(texto);
        multipart.addBodyPart(adjunto);

        message.setContent(multipart);
        Transport.send(message);

        System.out.println("📧 Factura enviada exitosamente a: " + destinatario);

    } catch (Exception e) {
        e.printStackTrace();
    }
}


    private void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public Factura obtenerDatosFactura(int idOrden) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
}
