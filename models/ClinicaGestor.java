package models;

import models.estructuras.Cola;
import models.estructuras.ListaEnlazada;
import models.estructuras.Pila;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ClinicaGestor {
    
    private ListaEnlazada<RegistroAtencion> historialAtenciones;
    private Cola<Paciente> colaConsultaExterna;
    private Cola<Paciente> colaPreOperatorio;
    private Pila<ServicioMedico> pilaServiciosPrestados;
    private int[] pagosPorMedio; 

    public ClinicaGestor() {
        this.historialAtenciones = new ListaEnlazada<>();
        this.colaConsultaExterna = new Cola<>(); 
        this.colaPreOperatorio = new Cola<>(3); 
        this.pilaServiciosPrestados = new Pila<>();
        this.pagosPorMedio = new int[4]; 
    }

    
    public void registrarAtencion(RegistroAtencion atencion) {
        historialAtenciones.agregar(atencion);
    }

    public boolean encolarPreOperatorio(Paciente paciente) {
        return colaPreOperatorio.encolar(paciente);
    }

    public void registrarServicioPrestado(ServicioMedico servicio) {
        pilaServiciosPrestados.push(servicio);
    }

    public void procesarPago(RegistroAtencion atencion, int opcionMedioPago) {
        String[] medios = {"Efectivo", "Tarjeta Débito", "Tarjeta Crédito", "EPS"};
        atencion.setMedioPago(medios[opcionMedioPago]);

        if (opcionMedioPago >= 0 && opcionMedioPago < 4) {
            pagosPorMedio[opcionMedioPago]++;
        }

        double subtotal = 0;
        double descuentoTotal = 0;
        Paciente paciente = atencion.getPaciente();

        for (int i = 0; i < atencion.getServiciosAdquiridos().getTamano(); i++) {
            ServicioMedico s = atencion.getServiciosAdquiridos().obtener(i);
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

        atencion.setTotalPagado(subtotal);
        atencion.setTotalDescuentos(descuentoTotal);
        atencion.setPagado(true);
    }


    public double procesarCancelacionReembolso(RegistroAtencion atencion, LocalDateTime fechaCancelacion) {
        if (!atencion.isPagado()) {
            return 0.0;
        }

        long horasDiferencia = ChronoUnit.HOURS.between(fechaCancelacion, atencion.getFechaHoraReserva());
        double reembolso = atencion.getTotalPagado();

        if (horasDiferencia < 24 && horasDiferencia > 0) {
            reembolso = reembolso * 0.50;
        }
        
        return reembolso;
    }

    public ListaEnlazada<RegistroAtencion> getHistorialAtenciones() { return historialAtenciones; }
    public Cola<Paciente> getColaConsultaExterna() { return colaConsultaExterna; }
    public Cola<Paciente> getColaPreOperatorio() { return colaPreOperatorio; }
    public Pila<ServicioMedico> getPilaServiciosPrestados() { return pilaServiciosPrestados; }
    public int[] getPagosPorMedio() { return pagosPorMedio; }
}