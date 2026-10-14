package models;

import models.estructuras.Cola;
import models.estructuras.ListaEnlazada;
import models.estructuras.Pila;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ClinicaGestor {

    private static final String[] MEDIOS_PAGO = {"Efectivo", "Tarjeta Debito", "Tarjeta Credito", "EPS"};

    private ListaEnlazada<RegistroAtencion> historialAtenciones;
    private ListaEnlazada<Paciente> pacientesRegistrados;
    private ListaEnlazada<Habitacion> habitacionesDisponibles;
    private ListaEnlazada<ServicioMedico> serviciosDisponibles;
    private Cola<Paciente> colaConsultaExterna;
    private Cola<Paciente> colaPreOperatorio;
    private Pila<ServicioMedico> pilaServiciosPrestados;
    private int[] pagosPorMedio;
    private double[] montosPorMedio;

    private double totalRecaudado;
    private double totalDescuentos;
    private double totalReembolsado;
    private double totalRetenido;
    private long totalEsperaMinutos;
    private int cantidadEsperas;

    public ClinicaGestor() {
        this.historialAtenciones = new ListaEnlazada<>();
        this.pacientesRegistrados = new ListaEnlazada<>();
        this.habitacionesDisponibles = new ListaEnlazada<>();
        this.serviciosDisponibles = new ListaEnlazada<>();
        this.colaConsultaExterna = new Cola<>();
        this.colaPreOperatorio = new Cola<>(3);
        this.pilaServiciosPrestados = new Pila<>();
        this.pagosPorMedio = new int[4];
        this.montosPorMedio = new double[4];
        inicializarHabitaciones();
        inicializarServicios();
    }

    private void inicializarHabitaciones() {
        agregarHabitacion(new Habitacion("C-101", Habitacion.TipoHabitacion.CONSULTA_EXTERNA));
        agregarHabitacion(new Habitacion("C-102", Habitacion.TipoHabitacion.CONSULTA_EXTERNA));
        agregarHabitacion(new Habitacion("O-201", Habitacion.TipoHabitacion.OBSERVACION, 120000));
        agregarHabitacion(new Habitacion("H-301", Habitacion.TipoHabitacion.HOSPITALIZACION, 200000));
        agregarHabitacion(new Habitacion("H-302", Habitacion.TipoHabitacion.HOSPITALIZACION, 450000));
        agregarHabitacion(new Habitacion("Q-401", Habitacion.TipoHabitacion.QUIROFANO));
    }

    private void inicializarServicios() {
        agregarServicioDisponible(new ServicioMedico("SVC-001", "Consulta general", 70000, true, false, false));
        agregarServicioDisponible(new ServicioMedico("SVC-002", "Consulta especializada", 150000, true, false, false));
        agregarServicioDisponible(new ServicioMedico("SVC-003", "Examen de laboratorio", 40000, false, false, false));
        agregarServicioDisponible(new ServicioMedico("SVC-004", "Imagenologia (ecografia)", 120000, false, false, false));
        agregarServicioDisponible(new ServicioMedico("SVC-005", "Rayos X", 60000, false, false, false));
        agregarServicioDisponible(new ServicioMedico("SVC-006", "Hospitalizacion por dia (estandar)", 200000, false, true, false));
        agregarServicioDisponible(new ServicioMedico("SVC-007", "Cirugia menor (tarifa base)", 1200000, false, false, false));
        agregarServicioDisponible(new ServicioMedico("SVC-008", "Cirugia mayor (tarifa base)", 6000000, false, false, false));
        agregarServicioDisponible(new ServicioMedico("SVC-009", "Vacunacion", 30000, false, false, false));
        agregarServicioDisponible(new ServicioMedico("SVC-010", "Farmacia (medicamentos)", 0, false, false, false));
        agregarServicioDisponible(new ServicioMedico("SVC-011", "Ambulancia / traslado", 180000, false, false, false));
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
            if (colaPreOperatorio.estaLlena()) {
                return false;
            }
            paciente.setHoraLlegadaCola(LocalDateTime.now());
            return colaPreOperatorio.encolar(paciente);
        }
        if (colaConsultaExterna.contiene(paciente)) {
            return false;
        }
        paciente.setHoraLlegadaCola(LocalDateTime.now());
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
            if (atencion.getPaciente().getDocumento().equalsIgnoreCase(documento.trim())
                    && !atencion.isPagado() && !atencion.isCancelada()) {
                return atencion;
            }
        }
        return null;
    }

    public boolean agregarServicioAAtencion(RegistroAtencion atencion, ServicioMedico servicio) {
        if (atencion == null || servicio == null) {
            return false;
        }
        if (atencion.isPagado() || atencion.isCancelada()) {
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
        return agregarPacienteACola(paciente, true);
    }

    public void registrarServicioPrestado(ServicioMedico servicio) {
        pilaServiciosPrestados.push(servicio);
    }

    public RegistroAtencion atenderSiguientePreOperatorio() {
        Paciente paciente = colaPreOperatorio.verFrente();
        if (paciente == null) {
            return null;
        }
        Habitacion habitacion = buscarHabitacionDisponible(Habitacion.TipoHabitacion.QUIROFANO);
        if (habitacion == null) {
            return null;
        }
        colaPreOperatorio.desencolar();
        registrarTiempoEspera(paciente);
        return iniciarAtencion(paciente, habitacion);
    }

    public RegistroAtencion atenderSiguienteConsulta() {
        Paciente paciente = colaConsultaExterna.verFrente();
        if (paciente == null) {
            return null;
        }
        Habitacion habitacion = buscarHabitacionDisponible(Habitacion.TipoHabitacion.CONSULTA_EXTERNA);
        if (habitacion == null) {
            return null;
        }
        colaConsultaExterna.desencolar();
        registrarTiempoEspera(paciente);
        return iniciarAtencion(paciente, habitacion);
    }

    private void registrarTiempoEspera(Paciente paciente) {
        if (paciente.getHoraLlegadaCola() == null) {
            return;
        }
        long minutos = ChronoUnit.MINUTES.between(paciente.getHoraLlegadaCola(), LocalDateTime.now());
        totalEsperaMinutos += Math.max(0, minutos);
        cantidadEsperas++;
        paciente.setHoraLlegadaCola(null);
    }

    public boolean finalizarAtencion(String documento) {
        RegistroAtencion atencion = buscarAtencionActivaPorPaciente(documento);
        if (atencion == null) {
            return false;
        }
        if (atencion.getHabitacionAsignada() != null) {
            atencion.getHabitacionAsignada().setOcupado(false);
        }
        return true;
    }

    public boolean liberarHabitacion(String numero) {
        if (numero == null || numero.trim().isEmpty()) {
            return false;
        }
        for (int i = 0; i < habitacionesDisponibles.getTamano(); i++) {
            Habitacion habitacion = habitacionesDisponibles.obtener(i);
            if (habitacion.getNumero().equalsIgnoreCase(numero.trim())) {
                habitacion.setOcupado(false);
                return true;
            }
        }
        return false;
    }

    public boolean procesarPago(RegistroAtencion atencion, int opcionMedioPago) {
        if (atencion == null || atencion.isPagado() || atencion.isCancelada()) {
            return false;
        }
        if (opcionMedioPago < 0 || opcionMedioPago >= MEDIOS_PAGO.length) {
            return false;
        }
        if (atencion.getServiciosAdquiridos().getTamano() == 0) {
            return false;
        }

        Paciente paciente = atencion.getPaciente();
        Habitacion habitacion = atencion.getHabitacionAsignada();

        long dias = 1;
        if (habitacion != null
                && (habitacion.getTipo() == Habitacion.TipoHabitacion.OBSERVACION
                || habitacion.getTipo() == Habitacion.TipoHabitacion.HOSPITALIZACION)) {
            long horas = ChronoUnit.HOURS.between(atencion.getFechaHoraReserva(), LocalDateTime.now());
            dias = Math.max(1, (horas + 23) / 24);
        }

        double subtotal = 0;
        double descuentoTotal = 0;

        for (int i = 0; i < atencion.getServiciosAdquiridos().getTamano(); i++) {
            ServicioMedico servicio = atencion.getServiciosAdquiridos().obtener(i);
            double precio = servicio.getPrecioUnitario();

            if (servicio.isEsHospitalizacion()) {
                double tarifaDia = precio;
                if (habitacion != null && habitacion.getTarifaDiaria() > 0) {
                    tarifaDia = habitacion.getTarifaDiaria();
                }
                precio = tarifaDia * dias;
            }

            double desc = 0;
            if (paciente.isTieneEps() && opcionMedioPago == 3) {
                desc = precio;
            } else {
                if (paciente.getEdad() < 5 && !servicio.isEsCirugiaEmergencia()) {
                    desc = precio * 0.20;
                } else if (paciente.getEdad() >= 65 && (servicio.isEsConsulta() || servicio.isEsHospitalizacion())) {
                    desc = precio * 0.30;
                }
            }

            double monto = precio - desc;
            atencion.agregarMontoPagado(monto);
            subtotal += monto;
            descuentoTotal += (precio - monto);
        }

        if (opcionMedioPago == 1 || opcionMedioPago == 2) {
            subtotal += subtotal * 0.02;
        }

        atencion.setMedioPago(MEDIOS_PAGO[opcionMedioPago]);
        atencion.setTotalPagado(subtotal);
        atencion.setTotalDescuentos(descuentoTotal);
        atencion.setPagado(true);

        pagosPorMedio[opcionMedioPago]++;
        montosPorMedio[opcionMedioPago] += subtotal;
        totalRecaudado += subtotal;
        totalDescuentos += descuentoTotal;
        return true;
    }

    public double cancelarAtencion(String documento, LocalDateTime fechaCancelacion) {
        RegistroAtencion objetivo = null;
        for (int i = historialAtenciones.getTamano() - 1; i >= 0; i--) {
            RegistroAtencion atencion = historialAtenciones.obtener(i);
            if (atencion.getPaciente().getDocumento().equalsIgnoreCase(documento)
                    && !atencion.isCancelada()) {
                objetivo = atencion;
                break;
            }
        }
        if (objetivo == null) {
            return -1;
        }
        objetivo.setCancelada(true);
        if (objetivo.getHabitacionAsignada() != null) {
            objetivo.getHabitacionAsignada().setOcupado(false);
        }
        if (!objetivo.isPagado()) {
            return 0;
        }
        long horas = ChronoUnit.HOURS.between(objetivo.getFechaHoraReserva(), fechaCancelacion);
        double reembolso = horas < 24 ? objetivo.getTotalPagado() * 0.50 : objetivo.getTotalPagado();
        objetivo.setValorReembolsado(reembolso);
        totalReembolsado += reembolso;
        totalRetenido += objetivo.getTotalPagado() - reembolso;
        for (int i = 0; i < MEDIOS_PAGO.length; i++) {
            if (MEDIOS_PAGO[i].equals(objetivo.getMedioPago())) {
                montosPorMedio[i] -= objetivo.getTotalPagado();
                pagosPorMedio[i]--;
                break;
            }
        }
        totalRecaudado -= objetivo.getTotalPagado();
        return reembolso;
    }

    public ServicioMedico procesarFacturaPendiente() {
        return pilaServiciosPrestados.pop();
    }

    public String obtenerReporte() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("=== REPORTE CLINICA TECNOSALUD ===\n");

        int totalAtendidos = historialAtenciones.getTamano();

        reporte.append("\n-- PACIENTES ATENDIDOS --\n");
        reporte.append("1. Total de pacientes atendidos: ").append(totalAtendidos).append("\n");
        reporte.append("2. Pacientes registrados en el sistema: ").append(pacientesRegistrados.getTamano()).append("\n");

        reporte.append("\n3. Pacientes por genero (atendidos) y monto recaudado:\n");
        ListaEnlazada<String> generosVistos = new ListaEnlazada<>();
        for (int i = 0; i < totalAtendidos; i++) {
            String genero = historialAtenciones.obtener(i).getPaciente().getGenero();
            if (generosVistos.contiene(genero)) {
                continue;
            }
            generosVistos.agregar(genero);
            int cantidad = 0;
            double monto = 0;
            for (int j = 0; j < totalAtendidos; j++) {
                RegistroAtencion atencion = historialAtenciones.obtener(j);
                if (!atencion.getPaciente().getGenero().equals(genero)) {
                    continue;
                }
                cantidad++;
                if (atencion.isPagado() && !atencion.isCancelada()) {
                    monto += atencion.getTotalPagado();
                }
            }
            double porcentajePacientes = totalAtendidos == 0 ? 0 : cantidad * 100.0 / totalAtendidos;
            reporte.append("   - ").append(genero).append(": ").append(cantidad).append(" paciente(s) (")
                    .append(porcentaje(porcentajePacientes)).append(")");
            if (monto > 0) {
                reporte.append(" | recaudado ").append(dinero(monto));
                if (totalRecaudado > 0) {
                    reporte.append(" (").append(porcentaje(monto * 100.0 / totalRecaudado)).append(")");
                }
            }
            reporte.append("\n");
        }
        if (generosVistos.estaVacia()) {
            reporte.append("   Sin atenciones registradas.\n");
        }

        reporte.append("\n4. Monto total recaudado: ").append(dinero(totalRecaudado)).append("\n");
        reporte.append("5. Monto total de descuentos aplicados: ").append(dinero(totalDescuentos)).append("\n");
        reporte.append("6. Total reembolsado por cancelacion: ").append(dinero(totalReembolsado)).append("\n");
        reporte.append("7. Retenido por cancelacion tardia (< 24 h): ").append(dinero(totalRetenido)).append("\n");

        reporte.append("\n8. Monto recaudado por medio de pago:\n");
        int totalPagos = 0;
        for (int i = 0; i < pagosPorMedio.length; i++) {
            totalPagos += pagosPorMedio[i];
        }
        for (int i = 0; i < montosPorMedio.length; i++) {
            double monto = montosPorMedio[i];
            reporte.append("   - ").append(MEDIOS_PAGO[i]).append(": ").append(dinero(monto));
            if (totalRecaudado > 0) {
                reporte.append(" (").append(porcentaje(monto * 100.0 / totalRecaudado)).append(")");
            }
            reporte.append(" | pacientes: ").append(pagosPorMedio[i]);
            if (totalPagos > 0) {
                reporte.append(" (").append(porcentaje(pagosPorMedio[i] * 100.0 / totalPagos)).append(")");
            }
            reporte.append("\n");
        }

        reporte.append("\n9. Monto recaudado por servicio:\n");
        ListaEnlazada<String> nombresVistos = new ListaEnlazada<>();
        boolean hayServicios = false;
        for (int i = 0; i < totalAtendidos; i++) {
            RegistroAtencion atencion = historialAtenciones.obtener(i);
            if (!atencion.isPagado() || atencion.isCancelada()) {
                continue;
            }
            for (int j = 0; j < atencion.getServiciosAdquiridos().getTamano(); j++) {
                ServicioMedico servicio = atencion.getServiciosAdquiridos().obtener(j);
                String nombre = servicio.getNombre();
                if (nombresVistos.contiene(nombre)) {
                    continue;
                }
                nombresVistos.agregar(nombre);
                double monto = 0;
                int veces = 0;
                for (int k = 0; k < totalAtendidos; k++) {
                    RegistroAtencion otra = historialAtenciones.obtener(k);
                    if (!otra.isPagado() || otra.isCancelada()) {
                        continue;
                    }
                    for (int l = 0; l < otra.getServiciosAdquiridos().getTamano(); l++) {
                        if (!otra.getServiciosAdquiridos().obtener(l).getNombre().equals(nombre)) {
                            continue;
                        }
                        veces++;
                        if (l < otra.getMontosPagados().getTamano()) {
                            monto += otra.getMontosPagados().obtener(l);
                        }
                    }
                }
                hayServicios = true;
                reporte.append("   - ").append(nombre).append(": ").append(dinero(monto));
                if (totalRecaudado > 0) {
                    reporte.append(" (").append(porcentaje(monto * 100.0 / totalRecaudado)).append(")");
                }
                reporte.append(" | veces: ").append(veces).append("\n");
            }
        }
        if (!hayServicios) {
            reporte.append("   Sin servicios pagados.\n");
        }

        reporte.append("\n10. Monto recaudado por rango de edad:\n");
        String[] rangos = {"< 5 anos", "5 - 64 anos", ">= 65 anos"};
        int[] cantidadRango = new int[3];
        double[] montoRango = new double[3];
        for (int i = 0; i < totalAtendidos; i++) {
            int idx = historialAtenciones.obtener(i).getPaciente().getEdad() < 5 ? 0
                    : (historialAtenciones.obtener(i).getPaciente().getEdad() >= 65 ? 2 : 1);
            cantidadRango[idx]++;
            RegistroAtencion atencion = historialAtenciones.obtener(i);
            if (atencion.isPagado() && !atencion.isCancelada()) {
                montoRango[idx] += atencion.getTotalPagado();
            }
        }
        for (int i = 0; i < 3; i++) {
            reporte.append("   - ").append(rangos[i]).append(": ").append(cantidadRango[i]).append(" paciente(s)");
            if (totalAtendidos > 0) {
                reporte.append(" (").append(porcentaje(cantidadRango[i] * 100.0 / totalAtendidos)).append(")");
            }
            reporte.append(" | recaudado ").append(dinero(montoRango[i]));
            if (totalRecaudado > 0) {
                reporte.append(" (").append(porcentaje(montoRango[i] * 100.0 / totalRecaudado)).append(")");
            }
            reporte.append("\n");
        }

        reporte.append("\n11. Pacientes mas pagador / menos pagador:\n");
        RegistroAtencion masPagador = null;
        RegistroAtencion menosPagador = null;
        for (int i = 0; i < totalAtendidos; i++) {
            RegistroAtencion atencion = historialAtenciones.obtener(i);
            if (!atencion.isPagado() || atencion.isCancelada()) {
                continue;
            }
            if (masPagador == null || atencion.getTotalPagado() > masPagador.getTotalPagado()) {
                masPagador = atencion;
            }
            if (menosPagador == null || atencion.getTotalPagado() < menosPagador.getTotalPagado()) {
                menosPagador = atencion;
            }
        }
        if (masPagador == null) {
            reporte.append("   Sin pagos registrados.\n");
        } else {
            reporte.append("   Mas pagador: ").append(masPagador.getPaciente().getDocumento()).append(" - ")
                    .append(masPagador.getPaciente().getNombre()).append(" - ").append(dinero(masPagador.getTotalPagado())).append("\n");
            reporte.append("   Menos pagador: ").append(menosPagador.getPaciente().getDocumento()).append(" - ")
                    .append(menosPagador.getPaciente().getNombre()).append(" - ").append(dinero(menosPagador.getTotalPagado())).append("\n");
        }

        reporte.append("\n12. Colas de atencion:\n");
        reporte.append("   - Pacientes en cola de atencion (consulta externa): ").append(colaConsultaExterna.getTamano()).append("\n");
        reporte.append("   - Pacientes en cola de pre-operatorio: ").append(colaPreOperatorio.getTamano())
                .append(" (maximo 3)").append("\n");

        reporte.append("13. Habitaciones:\n");
        int ocupadas = 0;
        for (int i = 0; i < habitacionesDisponibles.getTamano(); i++) {
            Habitacion habitacion = habitacionesDisponibles.obtener(i);
            if (habitacion.isOcupado()) {
                ocupadas++;
            }
            reporte.append("   - ").append(habitacion.getNumero()).append(" (").append(habitacion.getTipo())
                    .append(") - ").append(habitacion.isOcupado() ? "Ocupada" : "Disponible");
            if (habitacion.getTarifaDiaria() > 0) {
                reporte.append(" | tarifa/dia ").append(dinero(habitacion.getTarifaDiaria()));
            }
            reporte.append("\n");
        }
        reporte.append("   - Resumen: ").append(ocupadas).append(" ocupada(s) / ")
                .append(habitacionesDisponibles.getTamano() - ocupadas).append(" disponible(s)\n");

        reporte.append("\n14. Promedios:\n");
        if (totalAtendidos > 0) {
            LocalDateTime primera = historialAtenciones.obtener(0).getFechaHoraReserva();
            LocalDateTime ultima = historialAtenciones.obtener(totalAtendidos - 1).getFechaHoraReserva();
            long dias = Math.max(1, ChronoUnit.DAYS.between(primera.toLocalDate(), ultima.toLocalDate()) + 1);
            reporte.append("   - Promedio de hospitalizaciones por dia: ")
                    .append(String.format("%.2f", contarHospitalizaciones() / (double) dias))
                    .append(" (durante ").append(dias).append(" dia(s))\n");
        } else {
            reporte.append("   - Promedio de hospitalizaciones por dia: 0.00\n");
        }
        if (cantidadEsperas > 0) {
            reporte.append("   - Promedio de tiempo de espera en cola: ")
                    .append(String.format("%.1f", totalEsperaMinutos / (double) cantidadEsperas))
                    .append(" minutos (").append(cantidadEsperas).append(" paciente(s))\n");
        } else {
            reporte.append("   - Promedio de tiempo de espera en cola: Sin datos\n");
        }
        reporte.append("   - Servicios prestados (pila): ").append(pilaServiciosPrestados.getTamanio()).append("\n");

        return reporte.toString();
    }

    private int contarHospitalizaciones() {
        int contador = 0;
        for (int i = 0; i < historialAtenciones.getTamano(); i++) {
            RegistroAtencion atencion = historialAtenciones.obtener(i);
            if (atencion.getHabitacionAsignada() != null
                    && atencion.getHabitacionAsignada().getTipo() == Habitacion.TipoHabitacion.HOSPITALIZACION) {
                contador++;
            }
        }
        return contador;
    }

    public boolean isColaPreOperatorioLlena() {
        return colaPreOperatorio.estaLlena();
    }

    public boolean estaEnCola(Paciente paciente, boolean esPreOperatorio) {
        if (paciente == null) {
            return false;
        }
        if (esPreOperatorio) {
            return colaPreOperatorio.contiene(paciente);
        }
        return colaConsultaExterna.contiene(paciente);
    }

    public int[] getPagosPorMedio() {
        return pagosPorMedio;
    }

    public double[] getMontosPorMedio() {
        return montosPorMedio;
    }

    public double getTotalRecaudado() {
        return totalRecaudado;
    }

    public double getTotalDescuentos() {
        return totalDescuentos;
    }

    public double getTotalReembolsado() {
        return totalReembolsado;
    }

    public double getTotalRetenido() {
        return totalRetenido;
    }

    public long getTotalEsperaMinutos() {
        return totalEsperaMinutos;
    }

    public int getCantidadEsperas() {
        return cantidadEsperas;
    }

    public ListaEnlazada<RegistroAtencion> getHistorialAtenciones() {
        return historialAtenciones;
    }

    public ListaEnlazada<Paciente> getPacientesRegistrados() {
        return pacientesRegistrados;
    }

    public ListaEnlazada<Habitacion> getHabitacionesDisponibles() {
        return habitacionesDisponibles;
    }

    public ListaEnlazada<ServicioMedico> getServiciosDisponibles() {
        return serviciosDisponibles;
    }

    public Cola<Paciente> getColaConsultaExterna() {
        return colaConsultaExterna;
    }

    public Cola<Paciente> getColaPreOperatorio() {
        return colaPreOperatorio;
    }

    public Pila<ServicioMedico> getPilaServiciosPrestados() {
        return pilaServiciosPrestados;
    }

    private String dinero(double valor) {
        return String.format("$%,.0f", valor);
    }

    private String porcentaje(double valor) {
        return String.format("%.1f%%", valor);
    }

    public String obtenerReporteResumido() {
        StringBuilder resumen = new StringBuilder();
        resumen.append("Pacientes registrados: ").append(pacientesRegistrados.getTamano()).append("\n");
        resumen.append("Atenciones registradas: ").append(historialAtenciones.getTamano()).append("\n");
        resumen.append("En cola consulta externa: ").append(colaConsultaExterna.getTamano()).append("\n");
        resumen.append("En cola pre-operatorio: ").append(colaPreOperatorio.getTamano()).append(" (maximo 3)\n");
        resumen.append("Servicios pendientes (pila): ").append(pilaServiciosPrestados.getTamanio()).append("\n");
        resumen.append("Total recaudado: ").append(dinero(totalRecaudado)).append("\n");
        resumen.append("Descuentos: ").append(dinero(totalDescuentos)).append("\n");
        resumen.append("Reembolsado: ").append(dinero(totalReembolsado)).append("\n");
        return resumen.toString();
    }
}
