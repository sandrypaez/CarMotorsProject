package com.carmotors.carmotors.controller;

    
import com.carmotors.carmotors.model.dao.ClienteDAO;
import com.carmotors.carmotors.model.entities.Cliente;
import com.carmotors.carmotors.model.entities.OrdenServicio;
import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;

public class ClienteController {
    private ClienteDAO clienteDAO;
    private Timer reminderTimer;

    public ClienteController() {
        this.clienteDAO = new ClienteDAO();
        this.reminderTimer = new Timer();
        scheduleReminders();
    }

    public void registerClient(Cliente client) throws Exception {
        validateClient(client);
        clienteDAO.registerClient(client);
        checkAndScheduleReminders(client);
    }

    public List<Cliente> listAllClients() throws SQLException {
        return clienteDAO.listAllClients();
    }

    public void updateClient(Cliente client) throws Exception {
        validateClient(client);
        clienteDAO.updateClient(client);
        checkAndScheduleReminders(client);
    }

    public Cliente findClientById(int id) throws SQLException {
        return clienteDAO.findClientById(id);
    }

    public void applyDiscount(int clientId, double discountPercentage) throws SQLException {
        Cliente client = findClientById(clientId);
        if (client != null) {
            client.setDiscountPercentage(discountPercentage);
            clienteDAO.updateClient(client);
        }
    }

    public void addRewardPoints(int clientId, int points) throws SQLException {
        Cliente client = findClientById(clientId);
        if (client != null) {
            client.setRewardPoints(client.getRewardPoints() + points);
            clienteDAO.updateClient(client);
        }
    }

    private void validateClient(Cliente client) throws Exception {
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new Exception("Client name cannot be empty");
        }
        if (client.getIdentification() == null || client.getIdentification().trim().isEmpty()) {
            throw new Exception("Client identification cannot be empty");
        }
    }
 


    private void checkAndScheduleReminders(Cliente client) {
        List<OrdenServicio> serviceHistory = client.getServiceHistory();
        if (serviceHistory != null && !serviceHistory.isEmpty()) {
            OrdenServicio lastService = serviceHistory.get(serviceHistory.size() - 1);
            Date endDate = (Date) lastService.getFechaFin();
            if (endDate != null) {
                Date nextReminder = new Date(endDate.getTime() + (1000L * 60 * 60 * 24 * 180)); // 6 months
                if (nextReminder.after(new Date())) {
                    scheduleReminder(client.getId(), "Preventive maintenance due soon for client ID: " + client.getId(), nextReminder);
                }
            } else {
                System.out.println("No end date available for last service of client ID: " + client.getId());
            }
        }
    }
   public void aplicarBeneficios(Cliente cliente) {
    int servicios = cliente.getServiceHistory().size();
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

    try {
        clienteDAO.updateClient(cliente);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



    private void scheduleReminder(int clientId, String message, Date scheduleDate) {
        reminderTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(message + " on " + new Date());
                
            }
        }, scheduleDate);
    }

    private void scheduleReminders() {
        try {
            List<Cliente> clients = listAllClients();
            for (Cliente client : clients) {
                checkAndScheduleReminders(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
}



