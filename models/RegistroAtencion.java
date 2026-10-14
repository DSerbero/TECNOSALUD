package models;

import models.estructuras.ListaEnlazada;
import java.time.LocalDateTime;

public class RegistroAtencion {
    private Paciente paciente;
    private Habitacion habitacionAsignada;
    private ListaEnlazada<ServicioMedico> serviciosAdquiridos;
    private ListaEnlazada<Double> montosPagados;
    private double totalPagado;
    private double totalDescuentos;
    private String medioPago;
    private boolean pagado;
    private LocalDateTime fechaHoraReserva;
    private boolean cancelada;
    private double valorReembolsado;

    public RegistroAtencion(Paciente paciente, Habitacion habitacionAsignada, LocalDateTime fechaHoraReserva) {
        this.paciente = paciente;
        this.habitacionAsignada = habitacionAsignada;
        this.serviciosAdquiridos = new ListaEnlazada<>();
        this.montosPagados = new ListaEnlazada<>();
        this.totalPagado = 0;
        this.totalDescuentos = 0;
        this.medioPago = "";
        this.pagado = false;
        this.fechaHoraReserva = fechaHoraReserva;
        this.cancelada = false;
        this.valorReembolsado = 0;
    }

    public void agregarServicio(ServicioMedico servicio) {
        serviciosAdquiridos.agregar(servicio);
    }

    public void agregarMontoPagado(double monto) {
        montosPagados.agregar(monto);
    }

    public Paciente getPaciente() { return paciente; }
    public Habitacion getHabitacionAsignada() { return habitacionAsignada; }
    public ListaEnlazada<ServicioMedico> getServiciosAdquiridos() { return serviciosAdquiridos; }
    public ListaEnlazada<Double> getMontosPagados() { return montosPagados; }
    public double getTotalPagado() { return totalPagado; }
    public void setTotalPagado(double totalPagado) { this.totalPagado = totalPagado; }
    public double getTotalDescuentos() { return totalDescuentos; }
    public void setTotalDescuentos(double totalDescuentos) { this.totalDescuentos = totalDescuentos; }
    public String getMedioPago() { return medioPago; }
    public void setMedioPago(String medioPago) { this.medioPago = medioPago; }
    public boolean isPagado() { return pagado; }
    public void setPagado(boolean pagado) { this.pagado = pagado; }
    public LocalDateTime getFechaHoraReserva() { return fechaHoraReserva; }
    public boolean isCancelada() { return cancelada; }
    public void setCancelada(boolean cancelada) { this.cancelada = cancelada; }
    public double getValorReembolsado() { return valorReembolsado; }
    public void setValorReembolsado(double valorReembolsado) { this.valorReembolsado = valorReembolsado; }
}
