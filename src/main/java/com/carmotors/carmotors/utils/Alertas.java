package com.carmotors.carmotors.utils;

import com.carmotors.carmotors.model.entities.Cliente;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

public class Alertas {
    private String correoOrigen;
    private String contrasena;

    public Alertas(String correoOrigen, String contrasena) {
        this.correoOrigen = correoOrigen;
        this.contrasena = contrasena;
    }

    public void enviarAlertas(List<Cliente> clientes) {
        LocalDate hoy = LocalDate.now();
        for (Cliente cliente : clientes) {
            try {
                if (cliente.getFechaCompra() != null &&
                    cliente.getFechaCompra().plusDays(2).equals(hoy)) {
                    enviarCorreo(cliente);
                }
            } catch (Exception e) {
                System.err.println("Error con cliente ID " + cliente.getId() + ": " + e.getMessage());
            }
        }
    }

    private void enviarCorreo(Cliente cliente) {
        String asunto = "¡Gracias por tu compra!";
        String cuerpo = "Hola " + cliente.getNombre() + ",\n\n" +
                "Han pasado 2 días desde tu compra y queremos agradecerte.\n\n" +
                "¡Tienes " + cliente.getRewardPoints() + " puntos acumulados en tu cuenta!\n" +
                "Aprovecha nuestras nuevas promociones exclusivas para ti.\n\n" +
                "¡Gracias por confiar en nosotros!";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session sesion = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(correoOrigen, contrasena);
            }
        });

        try {
            Message mensaje = new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress(correoOrigen));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(cliente.getCorreoElectronico()));
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);

            Transport.send(mensaje);
            System.out.println("✅ Correo enviado a: " + cliente.getCorreoElectronico());
        } catch (MessagingException e) {
            System.err.println("❌ Error al enviar correo a " + cliente.getCorreoElectronico() + ": " + e.getMessage());
        }
    }
}
