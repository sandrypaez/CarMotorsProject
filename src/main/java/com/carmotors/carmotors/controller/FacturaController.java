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
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

public class FacturaController {
    private final FacturaDAO facturaDAO = new FacturaDAO();

    public void generarFacturaPDF(Factura factura) throws Exception {
        String folder = "invoices";
        File dir = new File(folder);
        if (!dir.exists()) dir.mkdirs();

        String fileName = folder + "/factura_" + factura.getIdOrden() + ".pdf";
        String qrPath = folder + "/qr_" + factura.getIdOrden() + ".png";

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font negrita = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font normal = FontFactory.getFont(FontFactory.HELVETICA, 12);

        Paragraph header = new Paragraph("Taller Automotriz Motores & Ruedas\n", tituloFont);
        header.setAlignment(Element.ALIGN_CENTER);
        header.add(new Paragraph("NIT: 123456789-0\nDirección: 123 Main St, Bogotá\nTeléfono: +57 123456789\n\n", normal));
        document.add(header);

        document.add(new Paragraph("Factura #: INV-" + UUID.randomUUID().toString().substring(0, 8), negrita));
        document.add(new Paragraph("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(factura.getFechaEmision()), normal));
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Subtotal: $" + String.format("%,.2f", factura.getSubtotal()), normal));
        document.add(new Paragraph("IVA (19%): $" + String.format("%,.2f", factura.getImpuestos()), normal));
        document.add(new Paragraph("Total: $" + String.format("%,.2f", factura.getTotal()), negrita));
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("CUFE: " + factura.getCufe(), normal));
        document.add(new Paragraph("QR: " + factura.getQrUrl(), normal));
        document.add(Chunk.NEWLINE);

        generateQRCodeImage(factura.getQrUrl(), 150, 150, qrPath);
        Image qr = Image.getInstance(qrPath);
        qr.setAlignment(Element.ALIGN_CENTER);
        document.add(qr);

        document.close();
        facturaDAO.guardarFactura(factura);

        String correoCliente = obtenerCorreoPorOrden(factura.getIdOrden());
        if (correoCliente != null && !correoCliente.isEmpty()) {
            enviarFacturaPorCorreo(correoCliente, new File(fileName), new File(qrPath));
            javax.swing.JOptionPane.showMessageDialog(null, "✅ Factura generada y enviada al correo: " + correoCliente);
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "⚠️ Factura generada, pero no se encontró correo del cliente.");
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

    private void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
    public static File generarPDF(Factura factura, String clienteNombre, String identificacion, String direccionCliente,
                                  String descripcionServicio, int cantidad, double precioUnitario,
                                  String vehiculo, String logoPath, String firmaPath, String qrUrl) throws Exception {

        String folder = "invoices";
        new File(folder).mkdirs();

        String qrPath = folder + "/qr_" + factura.getIdOrden() + ".png";
        generarQR(qrUrl, 150, 150, qrPath);

        String nombreArchivo = folder + "/factura_" + factura.getIdOrden() + ".pdf";
        Document document = new Document(PageSize.A4, 50, 50, 30, 50);
        PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
        document.open();

        // Tipografías
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.WHITE);
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        // Logo
        if (logoPath != null) {
            Image logo = Image.getInstance(logoPath);
            logo.scaleToFit(70, 70);
            logo.setAlignment(Element.ALIGN_LEFT);
            document.add(logo);
        }

        Paragraph title = new Paragraph("Taller Automotriz\nMotores & Ruedas", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);

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
        infoCliente.add(new Phrase(clienteNombre + "\n", normalFont));
        infoCliente.add(new Phrase("Identificación: ", labelFont));
        infoCliente.add(new Phrase(identificacion + "\n", normalFont));
        infoCliente.add(new Phrase("Dirección: ", labelFont));
        infoCliente.add(new Phrase(direccionCliente + "\n", normalFont));
        document.add(infoCliente);

        document.add(new Paragraph("\n"));

        // Tabla de servicios
        PdfPTable tabla = new PdfPTable(3);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{4, 2, 3});

        PdfPCell head1 = new PdfPCell(new Phrase("Descripción", sectionFont));
        PdfPCell head2 = new PdfPCell(new Phrase("Cantidad", sectionFont));
        PdfPCell head3 = new PdfPCell(new Phrase("Precio Unitario", sectionFont));
        BaseColor azul = new BaseColor(25, 118, 210); // ✅ Correcto
        for (PdfPCell c : new PdfPCell[]{head1, head2, head3}) {
            c.setBackgroundColor(azul);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            c.setPadding(8);
            tabla.addCell(c);
        }

        tabla.addCell(new Phrase(descripcionServicio, normalFont));
        tabla.addCell(String.valueOf(cantidad));
        tabla.addCell(String.format("%,.0f", precioUnitario));

        PdfPCell totalCell = new PdfPCell(new Phrase("Total", labelFont));
        totalCell.setColspan(2);
        totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tabla.addCell(totalCell);
        tabla.addCell(String.format("%,.0f", cantidad * precioUnitario));

        document.add(tabla);

        document.add(new Paragraph("\n"));

        // Info adicional
        Paragraph datos = new Paragraph();
        datos.setSpacingBefore(10);
        String inf = UUID.randomUUID().toString().substring(0, 8);
        datos.add(new Phrase("Inféprvinte: ", labelFont));
        datos.add(new Phrase("INV-" + inf + "\n", normalFont));

        datos.add(new Phrase("Fecha: ", labelFont));
        datos.add(new Phrase(new SimpleDateFormat("yyyy-MM-dd").format(factura.getFechaEmision()) + "\n", normalFont));

        datos.add(new Phrase("Vehículo: ", labelFont));
        datos.add(new Phrase(vehiculo + "\n\n", normalFont));

        datos.add(new Phrase("Subtotal: ", labelFont));
        datos.add(new Phrase(String.format("%,.0f", factura.getSubtotal()) + "\n", normalFont));

        datos.add(new Phrase("IVA: ", labelFont));
        datos.add(new Phrase("19%: " + String.format("%,.0f", factura.getImpuestos()) + "\n", normalFont));

        datos.add(new Phrase("Total: ", labelFont));
        datos.add(new Phrase(String.format("%,.0f", factura.getTotal()) + "\n\n", normalFont));

        datos.add(new Phrase("CUFE: ", labelFont));
        datos.add(new Phrase(factura.getCufe(), normalFont));

        document.add(datos);

        document.add(new Paragraph("\n"));

        // QR
        Image qr = Image.getInstance(qrPath);
        qr.scaleToFit(100, 100);
        qr.setAlignment(Image.ALIGN_LEFT);
        document.add(qr);

        // Firma
        if (firmaPath != null) {
            Image firma = Image.getInstance(firmaPath);
            firma.scaleToFit(120, 50);
            firma.setAlignment(Image.ALIGN_RIGHT);
            document.add(firma);
        }

        Paragraph pie = new Paragraph("Firma Digital: [Simulada para pruebas]", normalFont);
        pie.setAlignment(Element.ALIGN_RIGHT);
        document.add(pie);

        document.close();
        return new File(nombreArchivo);
    }

    private static void generarQR(String text, int width, int height, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix matrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(matrix, "PNG", path);
    }


    public String obtenerCorreoPorOrden(int idOrden) {
        String sql = "SELECT c.correo_electronico FROM Clientes c " +
                     "JOIN Vehiculos v ON c.id_cliente = v.id_cliente " +
                     "JOIN OrdenesServicio o ON o.id_vehiculo = v.id_vehiculo " +
                     "WHERE o.id_orden = ?";
        try (Connection conn = com.carmotors.carmotors.model.dao.ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idOrden);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("correo_electronico");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Factura obtenerDatosFactura(int idOrden) throws SQLException {
        return facturaDAO.obtenerDatosFactura(idOrden);
    }
}

