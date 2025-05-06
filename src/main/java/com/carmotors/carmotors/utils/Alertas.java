package com.carmotors.carmotors.utils;

import com.carmotors.carmotors.model.entities.Cliente;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Alertas {

    private String correoOrigen;
    private String contrasena;

    public Alertas(String correoOrigen, String contrasena) {
        this.correoOrigen = correoOrigen;
        this.contrasena = contrasena;
    }

    public void programarAlertaPostFactura(Cliente cliente) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            try {
                enviarCorreoAlertaPuntos(cliente);
            } catch (Exception e) {
                System.err.println("❌ Error al enviar alerta de puntos: " + e.getMessage());
            }
        }, 2, TimeUnit.MINUTES);
    }

    private void enviarCorreoAlertaPuntos(Cliente cliente) {
        String asunto = "🎁 ¡Has ganado puntos!";
        String cuerpo = "Hola " + cliente.getNombre() + ",\n\n"
                + "Gracias por tu compra reciente. Acabas de ganar "
                + cliente.getRewardPoints() + " puntos en tu cuenta.\n"
                + "Recuerda que puedes redimirlos en tus próximas visitas.\n\n"
                + "🚗 CarMotors te agradece por preferirnos.";

        enviarCorreo(cliente.getCorreoElectronico(), asunto, cuerpo);
    }

    public void enviarPromocion(String correoDestino) {
        String asunto = "🎉 ¡15% de descuento esta semana en Motores & Ruedas!";

        String cuerpoHTML = """ 
        <html>
        <body style='font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;'>
            <div style='max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 10px;'>
                <h2 style='text-align: center; color: #2c3e50;'>¡Vuelta al taller!</h2>
                <p style='text-align: center; font-size: 18px;'>🎁 Esta semana tienes...</p>
                <h1 style='text-align: center; color: #27ae60;'>15% de descuento</h1>
                <p style='text-align: center;'>En todos nuestros <strong>servicios de mantenimiento</strong> y repuestos seleccionados.</p>
                <p style='text-align: center;'>Utiliza el siguiente cupón:</p>
                <h2 style='text-align: center; background-color: #f1c40f; display: inline-block; padding: 10px 20px; border-radius: 5px;'>MOTO15</h2>
                <p style='text-align: center;'>(Introdúcelo al agendar tu cita)</p>
                <div style='text-align: center; margin-top: 20px;'>
                    <a href='https://taller-motores.com/promociones' 
                       style='background-color: #3498db; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>
                        VER PROMOCIONES
                    </a>
                </div>
                <p style='text-align: center; margin-top: 20px; color: #7f8c8d;'>Gracias por confiar en <strong>Motores & Ruedas</strong></p>
            </div>
        </body>
        </html>
        """;
        enviarCorreoHTML(correoDestino, asunto, cuerpoHTML);
    }

    private void enviarCorreo(String destinatario, String asunto, String cuerpo) {
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
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);

            Transport.send(mensaje);
            System.out.println("✅ Correo enviado a: " + destinatario);
        } catch (MessagingException e) {
            System.err.println("❌ Error al enviar correo a " + destinatario + ": " + e.getMessage());
        }
    }

    private void enviarCorreoHTML(String destinatario, String asunto, String cuerpoHTML) {
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
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject(asunto);
            mensaje.setContent(cuerpoHTML, "text/html; charset=utf-8");

            Transport.send(mensaje);
            System.out.println("✅ Correo enviado a: " + destinatario);
        } catch (MessagingException e) {
            System.err.println("❌ Error al enviar correo a " + destinatario + ": " + e.getMessage());
}
}
}