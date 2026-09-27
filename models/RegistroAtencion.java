package models;

import models.estructuras.ListaEnlazada;

public class RegistroAtencion {
    private Paciente paciente;
    private Habitacion habitacionAsignada;
    private ListaEnlazada<ServicioMedico> serviciosAdquiridos;
    private double totalPagado;
    private double totalDescuentos;
    private String medioPago;
    private boolean pagado;

    public RegistroAtencion(Paciente paciente, Habitacion habitacionAsignada) {
        this.paciente = paciente;
        this.habitacionAsignada = habitacionAsignada;
        this.serviciosAdquiridos = new ListaEnlazada<>();
        this.totalPagado = 0;
        this.totalDescuentos = 0;
        this.medioPago = "";
        this.pagado = false;
    }

    public void agregarServicio(ServicioMedico servicio) {
        serviciosAdquiridos.agregar(servicio);
    }

    public void procesarPago(int opcionMedioPago) {
        String[] medios = {"Efectivo", "Tarjeta Débito", "Tarjeta Crédito", "EPS"};
        this.medioPago = medios[opcionMedioPago];

        double subtotal = 0;
        double descuentoTotal = 0;

        for (int i = 0; i < serviciosAdquiridos.getTamano(); i++) {
            ServicioMedico s = serviciosAdquiridos.obtener(i);
            double precio = s.getPrecioUnitario();
            double desc = 0;

            if (paciente.isTieneEps() && opcionMedioPago == 3) {
                desc = precio;
            } else {
                if (paciente.getEdad() < 5) {
                    if (!s.isEsCirugiaEmergencia()) {
                        desc = precio * 0.20;
                    }
                } else if (paciente.getEdad() >= 65) {
                    if (s.isEsConsulta() || s.isEsHospitalizacion()) {
                        desc = precio * 0.30;
                    }
                }
            }
            subtotal += (precio - desc);
            descuentoTotal += desc;
        }

        if (opcionMedioPago == 1 || opcionMedioPago == 2) {
            subtotal += subtotal * 0.02;
        }

        this.totalPagado = subtotal;
        this.totalDescuentos = descuentoTotal;
        this.pagado = true;
    }

    public Paciente getPaciente() { return paciente; }
    public Habitacion getHabitacionAsignada() { return habitacionAsignada; }
    public ListaEnlazada<ServicioMedico> getServiciosAdquiridos() { return serviciosAdquiridos; }
    public double getTotalPagado() { return totalPagado; }
    public double getTotalDescuentos() { return totalDescuentos; }
    public String getMedioPago() { return medioPago; }
    public boolean isPagado() { return pagado; }
}