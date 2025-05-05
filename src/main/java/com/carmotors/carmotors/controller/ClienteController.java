package com.carmotors.carmotors.controller;

import com.carmotors.carmotors.model.dao.ClienteDAO;
import com.carmotors.carmotors.model.entities.Cliente;
import com.carmotors.carmotors.model.entities.OrdenServicio;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ClienteController {
    private ClienteDAO clienteDAO;
    private Timer reminderTimer;

    public ClienteController(Connection conn) {
        if (conn == null) {
            throw new IllegalArgumentException("La conexión a la base de datos no puede ser nula");
        }
        this.clienteDAO = new ClienteDAO(conn);
        this.reminderTimer = new Timer();
    }

    public void registrarCliente(Cliente cliente) throws Exception {
        validarCliente(cliente);
        cliente.setFechaCompra(LocalDate.now());
        clienteDAO.create(cliente);
        checkAndScheduleReminders(cliente);
    }

    public List<Cliente> listarTodosClientes() throws SQLException {
        return clienteDAO.readAll();
    }

    public void actualizarCliente(Cliente cliente) throws Exception {
        validarCliente(cliente);
        clienteDAO.update(cliente);
        checkAndScheduleReminders(cliente);
    }

    public Cliente buscarClientePorId(int id) throws SQLException {
        return clienteDAO.read(id);
    }

    public void aplicarDescuento(int clientId, double discountPercentage) throws SQLException {
        Cliente cliente = buscarClientePorId(clientId);
        if (cliente != null) {
            cliente.setDiscountPercentage(discountPercentage);
            clienteDAO.update(cliente);
        }
    }

    public void agregarPuntosRecompensa(int clientId, int points) throws SQLException {
        Cliente cliente = buscarClientePorId(clientId);
        if (cliente != null) {
            cliente.setRewardPoints(cliente.getRewardPoints() + points);
            clienteDAO.update(cliente);
        }
    }

    private void validarCliente(Cliente cliente) throws Exception {
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre del cliente no puede estar vacío");
        }
        if (cliente.getIdentificacion() == null || cliente.getIdentificacion().trim().isEmpty()) {
            throw new Exception("La identificación del cliente no puede estar vacía");
        }
        if (cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty()) {
            throw new Exception("El teléfono del cliente no puede estar vacío");
        }
        if (cliente.getCorreoElectronico() == null || cliente.getCorreoElectronico().trim().isEmpty()) {
            throw new Exception("El correo electrónico del cliente no puede estar vacío");
        }
        if (!cliente.getCorreoElectronico().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new Exception("El correo electrónico no tiene un formato válido");
        }
        if (cliente.getDireccion() == null || cliente.getDireccion().trim().isEmpty()) {
            throw new Exception("La dirección del cliente no puede estar vacía");
        }
    }

    private void checkAndScheduleReminders(Cliente cliente) {
        List<OrdenServicio> serviceHistory = cliente.getServiceHistory();
        if (serviceHistory != null && !serviceHistory.isEmpty()) {
            for (OrdenServicio service : serviceHistory) {
                java.sql.Date endDate = service.getFechaFin();
                if (endDate != null) {
                    LocalDate endLocalDate = endDate.toLocalDate();
                    LocalDate nextReminderDate = endLocalDate.plusMonths(6);
                    if (!nextReminderDate.isBefore(LocalDate.now())) {
                        java.util.Date reminderDate = java.sql.Date.valueOf(nextReminderDate);
                        String message = String.format("Mantenimiento preventivo próximo para cliente %s (ID: %d)", cliente.getNombre(), cliente.getId());
                        scheduleReminder(cliente.getId(), message, reminderDate);
                    }
                }
            }
        } else if (cliente.getFechaCompra() != null &&
                cliente.getFechaCompra().plusDays(2).isEqual(LocalDate.now())) {
            enviarCorreo(cliente);
        } else {
            System.out.println("No hay historial de servicios ni fecha de compra válida para el cliente ID: " + cliente.getId());
        }
    }

    public void aplicarBeneficios(Cliente cliente) throws SQLException {
        int servicios = cliente.getServiceHistory() != null ? cliente.getServiceHistory().size() : 0;
        int puntos = cliente.getRewardPoints();

        if (servicios >= 10) {
            cliente.setDiscountPercentage(15);
            cliente.setRewardPoints(puntos + 100);
        } else if (servicios >= 5) {
            cliente.setDiscountPercentage(5);
            cliente.setRewardPoints(puntos + 50);
        } else {
            cliente.setDiscountPercentage(0);
        }

        clienteDAO.update(cliente);
    }

    private void scheduleReminder(int clientId, String message, java.util.Date scheduleDate) {
        reminderTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(message + " en " + new java.util.Date());
            }
        }, scheduleDate);
    }



    private void enviarCorreo(Cliente cliente) {
        // Implementa la lógica real de envío de correo aquí
        System.out.println("Enviando correo a " + cliente.getCorreoElectronico() + " para cliente ID: " + cliente.getId());
    }
}
