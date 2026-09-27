package models;

import models.estructuras.ListaEnlazada;
import java.time.LocalDateTime;

public class RegistroAtencion {
    private Paciente paciente;
    private Habitacion habitacionAsignada;
    private ListaEnlazada<ServicioMedico> serviciosAdquiridos;
    private double totalPagado;
    private double totalDescuentos;
    private String medioPago;
    private boolean pagado;
    private LocalDateTime fechaHoraReserva; 

    public RegistroAtencion(Paciente paciente, Habitacion habitacionAsignada, LocalDateTime fechaHoraReserva) {
        this.paciente = paciente;
        this.habitacionAsignada = habitacionAsignada;
        this.serviciosAdquiridos = new ListaEnlazada<>();
        this.totalPagado = 0;
        this.totalDescuentos = 0;
        this.medioPago = "";
        this.pagado = false;
        this.fechaHoraReserva = fechaHoraReserva;
    }

    public void agregarServicio(ServicioMedico servicio) {
        serviciosAdquiridos.agregar(servicio);
    }

    public Paciente getPaciente() { return paciente; }
    public Habitacion getHabitacionAsignada() { return habitacionAsignada; }
    public ListaEnlazada<ServicioMedico> getServiciosAdquiridos() { return serviciosAdquiridos; }
    public double getTotalPagado() { return totalPagado; }
    public void setTotalPagado(double totalPagado) { this.totalPagado = totalPagado; }
    public double getTotalDescuentos() { return totalDescuentos; }
    public void setTotalDescuentos(double totalDescuentos) { this.totalDescuentos = totalDescuentos; }
    public String getMedioPago() { return medioPago; }
    public void setMedioPago(String medioPago) { this.medioPago = medioPago; }
    public boolean isPagado() { return pagado; }
    public void setPagado(boolean pagado) { this.pagado = pagado; }
    public LocalDateTime getFechaHoraReserva() { return fechaHoraReserva; }
}