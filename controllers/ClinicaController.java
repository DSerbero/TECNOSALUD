package controllers;

import models.ClinicaGestor;
import models.Habitacion;
import models.Paciente;
import models.RegistroAtencion;
import models.ServicioMedico;

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

    public RegistroAtencion crearAtencion(String documento, Habitacion.TipoHabitacion tipoHabitacion) {
        Paciente paciente = gestor.buscarPacientePorDocumento(documento);
        if (paciente == null) {
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

    public boolean agregarServicioAAtencion(String documento, String codigoServicio) {
        RegistroAtencion atencion = gestor.buscarAtencionActivaPorPaciente(documento);
        if (atencion == null) {
            return false;
        }

        ServicioMedico servicio = gestor.buscarServicioPorCodigo(codigoServicio);
        if (servicio == null) {
            return false;
        }

        return gestor.agregarServicioAAtencion(atencion, servicio);
    }

    public boolean procesarPago(String documento, int opcionMedioPago) {
        RegistroAtencion atencion = gestor.buscarAtencionActivaPorPaciente(documento);
        if (atencion == null) {
            return false;
        }

        if (atencion.getServiciosAdquiridos().getTamano() == 0) {
            return false;
        }

        gestor.procesarPago(atencion, opcionMedioPago);
        return true;
    }

    public String obtenerReporte() {
        return gestor.obtenerReporte();
    }
}
