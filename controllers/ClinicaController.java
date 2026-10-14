package controllers;

import models.ClinicaGestor;
import models.Habitacion;
import models.Paciente;
import models.RegistroAtencion;
import models.ServicioMedico;
import java.time.LocalDateTime;

public class ClinicaController {

    private final ClinicaGestor gestor;

    public ClinicaController() {
        this.gestor = new ClinicaGestor();
    }

    public ClinicaGestor getGestor() {
        return gestor;
    }

    public boolean registrarPaciente(String documento, String nombre, int edad, String genero,
                                     String telefono, boolean tieneEps, String tipoSangre) {
        if (documento == null || nombre == null || genero == null || telefono == null || tipoSangre == null) {
            return false;
        }
        if (documento.trim().isEmpty() || nombre.trim().isEmpty()) {
            return false;
        }
        if (edad < 0 || edad > 120) {
            return false;
        }
        Paciente paciente = new Paciente(documento.trim(), nombre.trim(), edad, genero.trim(),
                telefono.trim(), tieneEps, tipoSangre.trim());
        return gestor.registrarPaciente(paciente);
    }

    public Paciente buscarPacientePorDocumento(String documento) {
        return gestor.buscarPacientePorDocumento(documento);
    }

    public boolean agregarPacienteACola(String documento, boolean esPreOperatorio) {
        Paciente paciente = gestor.buscarPacientePorDocumento(documento);
        if (paciente == null) {
            return false;
        }
        return gestor.agregarPacienteACola(paciente, esPreOperatorio);
    }

    public boolean pacienteYaEnCola(String documento, boolean esPreOperatorio) {
        Paciente paciente = gestor.buscarPacientePorDocumento(documento);
        if (paciente == null) {
            return false;
        }
        return gestor.estaEnCola(paciente, esPreOperatorio);
    }

    public boolean colaPreOperatorioLlena() {
        return gestor.isColaPreOperatorioLlena();
    }

    public RegistroAtencion atenderSiguienteConsulta() {
        return gestor.atenderSiguienteConsulta();
    }

    public RegistroAtencion atenderSiguientePreOperatorio() {
        return gestor.atenderSiguientePreOperatorio();
    }

    public RegistroAtencion crearAtencion(String documento, Habitacion.TipoHabitacion tipoHabitacion) {
        Paciente paciente = gestor.buscarPacientePorDocumento(documento);
        if (paciente == null) {
            return null;
        }
        if (gestor.buscarAtencionActivaPorPaciente(documento) != null) {
            return null;
        }
        Habitacion habitacion = gestor.buscarHabitacionDisponible(tipoHabitacion);
        if (habitacion == null) {
            return null;
        }
        return gestor.iniciarAtencion(paciente, habitacion);
    }

    public RegistroAtencion buscarAtencionActiva(String documento) {
        return gestor.buscarAtencionActivaPorPaciente(documento);
    }

    public boolean agregarServicioAAtencion(String documento, String codigoServicio, double precioVariable) {
        RegistroAtencion atencion = gestor.buscarAtencionActivaPorPaciente(documento);
        if (atencion == null) {
            return false;
        }
        ServicioMedico servicio = gestor.buscarServicioPorCodigo(codigoServicio);
        if (servicio == null) {
            return false;
        }
        if (servicio.getPrecioUnitario() <= 0) {
            if (precioVariable <= 0) {
                return false;
            }
            servicio = new ServicioMedico(servicio.getCodigo(), servicio.getNombre(), precioVariable, false, false, false);
        }
        return gestor.agregarServicioAAtencion(atencion, servicio);
    }

    public double procesarPago(String documento, int opcionMedioPago) {
        RegistroAtencion atencion = gestor.buscarAtencionActivaPorPaciente(documento);
        if (atencion == null) {
            return -1;
        }
        if (atencion.isPagado()) {
            return -1;
        }
        if (atencion.getServiciosAdquiridos().getTamano() == 0) {
            return -1;
        }
        if (!gestor.procesarPago(atencion, opcionMedioPago)) {
            return -1;
        }
        return atencion.getTotalPagado();
    }

    public String procesarFacturaPendiente() {
        ServicioMedico servicio = gestor.procesarFacturaPendiente();
        if (servicio == null) {
            return null;
        }
        return servicio.getCodigo() + " - " + servicio.getNombre() + " ($" + String.format("%,.0f", servicio.getPrecioUnitario()) + ")";
    }

    public boolean finalizarAtencion(String documento) {
        return gestor.finalizarAtencion(documento);
    }

    public boolean liberarHabitacion(String numero) {
        return gestor.liberarHabitacion(numero);
    }

    public double cancelarCita(String documento) {
        return gestor.cancelarAtencion(documento, LocalDateTime.now());
    }

    public String obtenerReporte() {
        return gestor.obtenerReporte();
    }

    public String obtenerReporteResumido() {
        return gestor.obtenerReporteResumido();
    }
}
