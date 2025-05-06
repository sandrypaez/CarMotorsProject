package com.carmotors.carmotors.controller;

import com.carmotors.carmotors.model.dao.FacturaDAO;
import com.carmotors.carmotors.model.entities.Factura;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.UUID;

public class FacturaController {
    private final FacturaDAO facturaDAO = new FacturaDAO();

    public void generarFacturaPDF(Factura factura) throws Exception {
        String folder = "invoices";
        File dir = new File(folder);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = folder + "/factura_" + factura.getIdOrden() + ".pdf";
        String qrPath = folder + "/qr_" + factura.getIdOrden() + ".png";

        Document document = new Document(PageSize.A4, 50, 50, 30, 50);
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 28, BaseColor.DARK_GRAY);
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.WHITE);
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{1, 3}); // Logo:Texto proporcionalmente

// Logo
        String logoPath = "logo.png";
        File logoFile = new File(logoPath);
        if (logoFile.exists()) {
            Image logo = Image.getInstance(logoPath);
            logo.scaleToFit(100, 100);
            PdfPCell logoCell = new PdfPCell(logo, false);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerTable.addCell(logoCell);
        } else {
            // Celda vacía si no hay logo
            PdfPCell emptyCell = new PdfPCell();
            emptyCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(emptyCell);
        }

// Título
        Paragraph title = new Paragraph("Taller Automotriz\nMotores & Ruedas", titleFont);
        PdfPCell titleCell = new PdfPCell(title);
        titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        titleCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(titleCell);

        document.add(headerTable);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        // Sección azul "Factura Electrónica"
        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell(new Phrase("Factura Electrónica", sectionFont));
        cell.setBackgroundColor(new BaseColor(25, 118, 210));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(10);
        header.addCell(cell);
        document.add(header);

        document.add(new Paragraph("\n"));

        // Información del cliente
        Paragraph infoCliente = new Paragraph();
        infoCliente.add(new Phrase("Cliente: ", labelFont));
        infoCliente.add(new Phrase(factura.getNombreCliente() + "\n", normalFont));
        infoCliente.add(new Phrase("Identificación: ", labelFont));
        infoCliente.add(new Phrase(factura.getDocumentoCliente() + "\n", normalFont));
        infoCliente.add(new Phrase("Dirección: ", labelFont));
        infoCliente.add(new Phrase(factura.getDireccionCliente() + "\n", normalFont));
        infoCliente.add(new Phrase("Teléfono: ", labelFont));
        infoCliente.add(new Phrase(factura.getTelefono() + "\n", normalFont));
        document.add(infoCliente);

        document.add(new Paragraph("\n"));

        // Tabla de servicios
        PdfPTable tabla = new PdfPTable(new float[]{4, 2, 3});
        tabla.setWidthPercentage(100);
        tabla.addCell(coloredHeader("Descripción"));
        tabla.addCell(coloredHeader("Cantidad"));
        tabla.addCell(coloredHeader("Precio Unitario"));

        tabla.addCell(stdCell(factura.getDescripcionServicio()));
        tabla.addCell(stdCell("1"));
        tabla.addCell(stdCell(String.format("%,.0f", factura.getPrecioUnitario())));

        PdfPCell total = new PdfPCell(new Phrase("Total", labelFont));
        total.setColspan(2);
        total.setHorizontalAlignment(Element.ALIGN_RIGHT);
        total.setPadding(5);
        tabla.addCell(total);

        tabla.addCell(stdCell(String.format("%,.0f", factura.getTotal())));
        document.add(tabla);

        // Info adicional
        Paragraph datos = new Paragraph();
        datos.setSpacingBefore(10);
        String inf = UUID.randomUUID().toString().substring(0, 8);
        datos.add(new Phrase("Código Factura: ", labelFont));
        datos.add(new Phrase("INV-" + inf + "\n", normalFont));

        datos.add(new Phrase("Fecha: ", labelFont));
        datos.add(new Phrase(new SimpleDateFormat("yyyy-MM-dd").format(factura.getFechaEmision()) + "\n", normalFont));

        datos.add(new Phrase("Vehículo: ", labelFont));
        datos.add(new Phrase(factura.getVehiculo() + "\n\n", normalFont));

        datos.add(new Phrase("Subtotal: ", labelFont));
        datos.add(new Phrase(String.format("%,.2f", factura.getSubtotal()) + "\n", normalFont));

        datos.add(new Phrase("IVA: ", labelFont));
        datos.add(new Phrase("19%: " + String.format("%,.2f", factura.getImpuestos()) + "\n", normalFont));

        datos.add(new Phrase("Total: ", labelFont));
        datos.add(new Phrase(String.format("%,.2f", factura.getTotal()) + "\n\n", normalFont));

        datos.add(new Phrase("CUFE: ", labelFont));
        datos.add(new Phrase(factura.getCufe(), normalFont));

        document.add(datos);

        document.add(new Paragraph("\n"));

        // QR
        generateQRCodeImage(factura.getQrUrl(), 150, 150, qrPath);
        Image qr = Image.getInstance(qrPath);
        qr.scaleToFit(100, 100);
        qr.setAlignment(Image.ALIGN_LEFT);
        document.add(qr);

        // Firma
        String firmaPath = "firma.png";
        File firmaFile = new File(firmaPath);
        if (firmaFile.exists()) {
            Image firma = Image.getInstance(firmaPath);
            firma.scaleToFit(210, 90);
            firma.setAlignment(Image.ALIGN_RIGHT);
            document.add(firma);
        }

        Paragraph pie = new Paragraph("Firma Digital: [Simulada para pruebas]", normalFont);
        pie.setAlignment(Element.ALIGN_RIGHT);
        document.add(pie);

        document.close();

        // Send email with PDF and QR
        File pdfFile = new File(fileName);
        File qrFile = new File(qrPath);
        if (pdfFile.exists() && qrFile.exists() && !factura.getEmail().isEmpty()) {
            enviarFacturaPorCorreo(factura.getEmail(), pdfFile, qrFile);
        }
    }

    public void enviarFacturaPorCorreo(String destinatario, File pdf, File imagenQR) {
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

            MimeBodyPart cuerpo = new MimeBodyPart();
            cuerpo.setText("Cordial saludo. \nAdjuntamos su factura electrónica. \nGracias por preferirnos. \n\n- CarMotors");

            MimeBodyPart adjuntoPDF = new MimeBodyPart();
            adjuntoPDF.attachFile(pdf);

            MimeBodyPart adjuntoPNG = new MimeBodyPart();
            adjuntoPNG.attachFile(imagenQR);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(cuerpo);
            multipart.addBodyPart(adjuntoPDF);
            multipart.addBodyPart(adjuntoPNG);

            message.setContent(multipart);
            Transport.send(message);

            System.out.println("✅ Correo enviado a: " + destinatario);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PdfPCell coloredHeader(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.WHITE)));
        cell.setBackgroundColor(new BaseColor(25, 118, 210));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(8);
        return cell;
    }

    private PdfPCell stdCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 12)));
        cell.setPadding(5);
        return cell;
    }

    private void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public String obtenerCorreoPorOrden(int idOrden) {
        String sql = "SELECT c.correo_electronico FROM Clientes c " +
                "JOIN Vehiculos v ON c.id_cliente = v.id_cliente " +
                "JOIN OrdenesServicio o ON o.id_vehiculo = v.id_vehiculo " +
                "WHERE o.id_orden = ?";
        try (Connection conn = com.carmotors.carmotors.model.dao.ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idOrden);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("correo_electronico");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Factura obtenerDatosFactura(int idOrden) throws SQLException {
        return facturaDAO.obtenerDatosFactura(idOrden);
    }
}