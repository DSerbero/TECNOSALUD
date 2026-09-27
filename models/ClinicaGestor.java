package models;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ClinicaGestor {

    // Extrae la lógica que tenías en RegistroAtencion
    public static void procesarPago(RegistroAtencion atencion, int opcionMedioPago) {
        String[] medios = {"Efectivo", "Tarjeta Débito", "Tarjeta Crédito", "EPS"};
        atencion.setMedioPago(medios[opcionMedioPago]);

        double subtotal = 0;
        double descuentoTotal = 0;
        Paciente paciente = atencion.getPaciente();

        for (int i = 0; i < atencion.getServiciosAdquiridos().getTamano(); i++) {
            ServicioMedico s = atencion.getServiciosAdquiridos().obtener(i);
            double precio = s.getPrecioUnitario();
            double desc = 0;

            if (paciente.isTieneEps() && opcionMedioPago == 3) {
                desc = precio; // EPS cubre todo
            } else {
                if (paciente.getEdad() < 5) {
                    if (!s.isEsCirugiaEmergencia()) {
                        desc = precio * 0.20; // 20% dscto
                    }
                } else if (paciente.getEdad() >= 65) {
                    if (s.isEsConsulta() || s.isEsHospitalizacion()) {
                        desc = precio * 0.30; // 30% dscto
                    }
                }
            }
            subtotal += (precio - desc);
            descuentoTotal += desc;
        }

        // Recargo 2% tarjetas
        if (opcionMedioPago == 1 || opcionMedioPago == 2) {
            subtotal += subtotal * 0.02;
        }

        atencion.setTotalPagado(subtotal);
        atencion.setTotalDescuentos(descuentoTotal);
        atencion.setPagado(true);
    }

    // Nueva regla para reembolsos basada en el tiempo
    public static double procesarCancelacionReembolso(RegistroAtencion atencion, LocalDateTime fechaCancelacion) {
        if (!atencion.isPagado()) {
            return 0.0;
        }

        long horasDiferencia = ChronoUnit.HOURS.between(fechaCancelacion, atencion.getFechaHoraReserva());
        double reembolso = atencion.getTotalPagado();

        // Regla: Cancelación con menos de 24 horas => 50% de retención
        if (horasDiferencia < 24 && horasDiferencia > 0) {
            reembolso = reembolso * 0.50;
        }
        
        return reembolso;
    }
}