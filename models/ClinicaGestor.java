package models;

import models.estructuras.Cola;
import models.estructuras.ListaEnlazada;
import models.estructuras.Pila;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ClinicaGestor {

    private ListaEnlazada<RegistroAtencion> historialAtenciones;
    private ListaEnlazada<Paciente> pacientesRegistrados;
    private ListaEnlazada<Habitacion> habitacionesDisponibles;
    private ListaEnlazada<ServicioMedico> serviciosDisponibles;
    private Cola<Paciente> colaConsultaExterna;
    private Cola<Paciente> colaPreOperatorio;
    private Pila<ServicioMedico> pilaServiciosPrestados;
    private int[] pagosPorMedio;

    public ClinicaGestor() {
        this.historialAtenciones = new ListaEnlazada<>();
        this.pacientesRegistrados = new ListaEnlazada<>();
        this.habitacionesDisponibles = new ListaEnlazada<>();
        this.serviciosDisponibles = new ListaEnlazada<>();
        this.colaConsultaExterna = new Cola<>();
        this.colaPreOperatorio = new Cola<>(3);
        this.pilaServiciosPrestados = new Pila<>();
        this.pagosPorMedio = new int[4];
        inicializarHabitaciones();
        inicializarServicios();
    }

    private void inicializarHabitaciones() {
        agregarHabitacion(new Habitacion("C-101", Habitacion.TipoHabitacion.CONSULTA_EXTERNA));
        agregarHabitacion(new Habitacion("C-102", Habitacion.TipoHabitacion.CONSULTA_EXTERNA));
        agregarHabitacion(new Habitacion("O-201", Habitacion.TipoHabitacion.OBSERVACION));
        agregarHabitacion(new Habitacion("H-301", Habitacion.TipoHabitacion.HOSPITALIZACION));
        agregarHabitacion(new Habitacion("Q-401", Habitacion.TipoHabitacion.QUIROFANO));
    }

    private void inicializarServicios() {
        agregarServicioDisponible(new ServicioMedico("SVC-001", "Consulta general", 50000, true, false, false));
        agregarServicioDisponible(new ServicioMedico("SVC-002", "Control cardiológico", 120000, true, false, false));
        agregarServicioDisponible(new ServicioMedico("SVC-003", "Hospitalización", 180000, false, true, false));
        agregarServicioDisponible(new ServicioMedico("SVC-004", "Cirugía de emergencia", 350000, false, false, true));
        agregarServicioDisponible(new ServicioMedico("SVC-005", "Rayos X", 80000, false, false, false));
    }

    public boolean agregarHabitacion(Habitacion habitacion) {
        if (habitacion == null) {
            return false;
        }
        habitacionesDisponibles.agregar(habitacion);
        return true;
    }

    public boolean agregarServicioDisponible(ServicioMedico servicio) {
        if (servicio == null) {
            return false;
        }
        serviciosDisponibles.agregar(servicio);
        return true;
    }

    public boolean registrarPaciente(Paciente paciente) {
        if (paciente == null || buscarPacientePorDocumento(paciente.getDocumento()) != null) {
            return false;
        }
        pacientesRegistrados.agregar(paciente);
        return true;
    }

    public boolean agregarPacienteACola(Paciente paciente, boolean esPreOperatorio) {
        if (paciente == null) {
            return false;
        }
        if (esPreOperatorio) {
            return colaPreOperatorio.encolar(paciente);
        }
        return colaConsultaExterna.encolar(paciente);
    }

    public Paciente buscarPacientePorDocumento(String documento) {
        if (documento == null || documento.trim().isEmpty()) {
            return null;
        }
        for (int i = 0; i < pacientesRegistrados.getTamano(); i++) {
            Paciente paciente = pacientesRegistrados.obtener(i);
            if (paciente.getDocumento().equalsIgnoreCase(documento.trim())) {
                return paciente;
            }
        }
        return null;
    }

    public Habitacion buscarHabitacionDisponible(Habitacion.TipoHabitacion tipo) {
        for (int i = 0; i < habitacionesDisponibles.getTamano(); i++) {
            Habitacion habitacion = habitacionesDisponibles.obtener(i);
            if (habitacion.getTipo() == tipo && !habitacion.isOcupado()) {
                habitacion.setOcupado(true);
                return habitacion;
            }
        }
        return null;
    }

    public ServicioMedico buscarServicioPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return null;
        }
        for (int i = 0; i < serviciosDisponibles.getTamano(); i++) {
            ServicioMedico servicio = serviciosDisponibles.obtener(i);
            if (servicio.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return servicio;
            }
        }
        return null;
    }

    public RegistroAtencion iniciarAtencion(Paciente paciente, Habitacion habitacion) {
        if (paciente == null || habitacion == null) {
            return null;
        }
        RegistroAtencion atencion = new RegistroAtencion(paciente, habitacion, LocalDateTime.now());
        historialAtenciones.agregar(atencion);
        return atencion;
    }

    public RegistroAtencion buscarAtencionActivaPorPaciente(String documento) {
        if (documento == null || documento.trim().isEmpty()) {
            return null;
        }
        for (int i = 0; i < historialAtenciones.getTamano(); i++) {
            RegistroAtencion atencion = historialAtenciones.obtener(i);
            if (atencion.getPaciente().getDocumento().equalsIgnoreCase(documento.trim())) {
                return atencion;
            }
        }
        return null;
    }

    public boolean agregarServicioAAtencion(RegistroAtencion atencion, ServicioMedico servicio) {
        if (atencion == null || servicio == null) {
            return false;
        }
        atencion.agregarServicio(servicio);
        registrarServicioPrestado(servicio);
        return true;
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
        if (atencion == null) {
            return;
        }

        String[] medios = {"Efectivo", "Tarjeta Débito", "Tarjeta Crédito", "EPS"};
        if (opcionMedioPago < 0 || opcionMedioPago >= medios.length) {
            return;
        }

        atencion.setMedioPago(medios[opcionMedioPago]);
        pagosPorMedio[opcionMedioPago]++;

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

    public String obtenerReporte() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("=== REPORTE CLÍNICA TECNOSALUD ===\n");
        reporte.append("Pacientes registrados: ").append(pacientesRegistrados.getTamano()).append("\n");
        reporte.append("Atenciones registradas: ").append(historialAtenciones.getTamano()).append("\n");
        reporte.append("Pacientes en cola consulta externa: ").append(colaConsultaExterna.getTamano()).append("\n");
        reporte.append("Pacientes en preoperatorio: ").append(colaPreOperatorio.getTamano()).append("\n");
        reporte.append("Servicios prestados: ").append(pilaServiciosPrestados.getTamanio()).append("\n");
        reporte.append("Pagos por medio:\n");
        String[] medios = {"Efectivo", "Tarjeta Débito", "Tarjeta Crédito", "EPS"};
        for (int i = 0; i < pagosPorMedio.length; i++) {
            reporte.append("- ").append(medios[i]).append(": ").append(pagosPorMedio[i]).append("\n");
        }
        return reporte.toString();
    }

    public ListaEnlazada<RegistroAtencion> getHistorialAtenciones() { return historialAtenciones; }
    public ListaEnlazada<Paciente> getPacientesRegistrados() { return pacientesRegistrados; }
    public ListaEnlazada<Habitacion> getHabitacionesDisponibles() { return habitacionesDisponibles; }
    public ListaEnlazada<ServicioMedico> getServiciosDisponibles() { return serviciosDisponibles; }
    public Cola<Paciente> getColaConsultaExterna() { return colaConsultaExterna; }
    public Cola<Paciente> getColaPreOperatorio() { return colaPreOperatorio; }
    public Pila<ServicioMedico> getPilaServiciosPrestados() { return pilaServiciosPrestados; }
    public int[] getPagosPorMedio() { return pagosPorMedio; }
}