package views;

import controllers.ClinicaController;
import models.Habitacion;

import javax.swing.*;
import java.awt.*;

public class VistaPrincipal extends JFrame {

    private final ClinicaController controller;

    private JButton btnRegistrarPaciente;
    private JButton btnAgregarCola;
    private JButton btnAsignarConsultorio;
    private JButton btnRegistrarServicio;
    private JButton btnRegistrarPago;
    private JButton btnReportes;
    private JButton btnSalir;

    public VistaPrincipal() {
        this.controller = new ClinicaController();

        configurarVentana();
        JPanel contenido = crearContenido();
        add(contenido, BorderLayout.CENTER);
    }

    private void configurarVentana() {
        setTitle("TECNOSALUD - Gestión Clínica");
        setSize(950, 620);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(238, 245, 250));
    }

    private JPanel crearContenido() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(20, 20));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(new Color(238, 245, 250));

        JLabel lblTitulo = new JLabel("TECNOSALUD", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(new Color(0, 102, 153));

        JLabel lblSubtitulo = new JLabel("Sistema de gestión clínica", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSubtitulo.setForeground(new Color(70, 90, 110));

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(255, 255, 255));
        panelHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 235), 1),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)
        ));
        panelHeader.add(lblTitulo, BorderLayout.NORTH);
        panelHeader.add(lblSubtitulo, BorderLayout.SOUTH);

        JPanel panelBotones = new JPanel(new GridLayout(3, 3, 20, 20));
        panelBotones.setBackground(new Color(238, 245, 250));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnRegistrarPaciente = crearBoton("Registrar Paciente", new Color(29, 128, 159));
        btnAgregarCola = crearBoton("Agregar a Cola", new Color(29, 128, 159));
        btnAsignarConsultorio = crearBoton("Asignar Consultorio", new Color(36, 154, 111));
        btnRegistrarServicio = crearBoton("Registrar Servicio", new Color(36, 154, 111));
        btnRegistrarPago = crearBoton("Registrar Pago", new Color(204, 153, 0));
        btnReportes = crearBoton("Mostrar Reportes", new Color(118, 96, 168));
        btnSalir = crearBoton("Salir", new Color(176, 60, 60));

        btnRegistrarPaciente.addActionListener(e -> registrarPaciente());
        btnAgregarCola.addActionListener(e -> agregarPacienteACola());
        btnAsignarConsultorio.addActionListener(e -> asignarConsultorio());
        btnRegistrarServicio.addActionListener(e -> registrarServicio());
        btnRegistrarPago.addActionListener(e -> registrarPago());
        btnReportes.addActionListener(e -> mostrarReportes());
        btnSalir.addActionListener(e -> System.exit(0));

        panelBotones.add(btnRegistrarPaciente);
        panelBotones.add(btnAgregarCola);
        panelBotones.add(btnAsignarConsultorio);
        panelBotones.add(btnRegistrarServicio);
        panelBotones.add(btnRegistrarPago);
        panelBotones.add(btnReportes);
        panelBotones.add(new JLabel(""));
        panelBotones.add(btnSalir);
        panelBotones.add(new JLabel(""));

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        return panelPrincipal;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private void registrarPaciente() {
        String documento = solicitarTexto("Documento del paciente:");
        if (documento == null || documento.trim().isEmpty()) {
            return;
        }

        String nombre = solicitarTexto("Nombre del paciente:");
        if (nombre == null || nombre.trim().isEmpty()) {
            return;
        }

        Integer edad = solicitarEntero("Edad del paciente:");
        if (edad == null) {
            return;
        }

        String genero = solicitarTexto("Género:");
        if (genero == null || genero.trim().isEmpty()) {
            return;
        }

        String telefono = solicitarTexto("Teléfono:");
        if (telefono == null || telefono.trim().isEmpty()) {
            return;
        }

        String eps = solicitarTexto("¿Tiene EPS? (si/no):");
        if (eps == null || eps.trim().isEmpty()) {
            return;
        }

        String tipoSangre = solicitarTexto("Tipo de sangre:");
        if (tipoSangre == null || tipoSangre.trim().isEmpty()) {
            return;
        }

        boolean exito = controller.registrarPaciente(documento, nombre, edad, genero, telefono,
                eps.equalsIgnoreCase("si"), tipoSangre);

        if (exito) {
            mostrarMensaje("Paciente registrado correctamente.", "Éxito");
        } else {
            mostrarError("El paciente ya existe o los datos son inválidos.");
        }
    }

    private void agregarPacienteACola() {
        String documento = solicitarTexto("Ingrese documento del paciente:");
        if (documento == null || documento.trim().isEmpty()) {
            return;
        }

        if (controller.buscarPacientePorDocumento(documento) == null) {
            mostrarError("Paciente no encontrado.");
            return;
        }

        String[] opciones = {"Consulta externa", "Preoperatorio"};
        int seleccion = JOptionPane.showOptionDialog(this, "Seleccione la cola", "Agregar a cola",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

        boolean agregado = controller.agregarPacienteACola(documento, seleccion == 1);
        if (agregado) {
            mostrarMensaje("Paciente agregado a la cola correctamente.", "Éxito");
        } else {
            mostrarError("No se pudo agregar al paciente a la cola.");
        }
    }

    private void asignarConsultorio() {
        String documento = solicitarTexto("Ingrese documento del paciente:");
        if (documento == null || documento.trim().isEmpty()) {
            return;
        }

        if (controller.buscarPacientePorDocumento(documento) == null) {
            mostrarError("Paciente no encontrado.");
            return;
        }

        String[] opciones = {"Consulta Externa", "Observación", "Hospitalización", "Quirófano"};
        int seleccion = JOptionPane.showOptionDialog(this, "Seleccione el tipo de habitación",
                "Asignar consultorio/sala", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, opciones, opciones[0]);

        Habitacion.TipoHabitacion tipo = convertirTipoHabitacion(seleccion);
        if (controller.crearAtencion(documento, tipo) == null) {
            mostrarError("No hay habitaciones disponibles del tipo solicitado.");
            return;
        }

        mostrarMensaje("Atención creada correctamente.", "Éxito");
    }

    private void registrarServicio() {
        String documento = solicitarTexto("Ingrese documento del paciente:");
        if (documento == null || documento.trim().isEmpty()) {
            return;
        }

        if (controller.buscarAtencionActiva(documento) == null) {
            mostrarError("No existe una atención activa para ese paciente.");
            return;
        }

        String codigoServicio = solicitarTexto("Código del servicio médico:");
        if (codigoServicio == null || codigoServicio.trim().isEmpty()) {
            return;
        }

        if (controller.agregarServicioAAtencion(documento, codigoServicio)) {
            mostrarMensaje("Servicio registrado correctamente para el paciente.", "Éxito");
        } else {
            mostrarError("Servicio no encontrado o no se pudo registrar.");
        }
    }

    private void registrarPago() {
        String documento = solicitarTexto("Ingrese documento del paciente:");
        if (documento == null || documento.trim().isEmpty()) {
            return;
        }

        if (controller.buscarAtencionActiva(documento) == null) {
            mostrarError("No existe atención activa para ese paciente.");
            return;
        }

        String[] medios = {"Efectivo", "Tarjeta Débito", "Tarjeta Crédito", "EPS"};
        int seleccion = JOptionPane.showOptionDialog(this, "Seleccione el medio de pago", "Registrar pago",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, medios, medios[0]);

        if (controller.procesarPago(documento, seleccion)) {
            mostrarMensaje("Pago registrado correctamente.", "Éxito");
        } else {
            mostrarError("No fue posible registrar el pago.");
        }
    }

    private void mostrarReportes() {
        JOptionPane.showMessageDialog(this, controller.obtenerReporte(), "Reportes", JOptionPane.INFORMATION_MESSAGE);
    }

    private String solicitarTexto(String mensaje) {
        return JOptionPane.showInputDialog(this, mensaje, "Entrada", JOptionPane.QUESTION_MESSAGE);
    }

    private Integer solicitarEntero(String mensaje) {
        try {
            String valor = JOptionPane.showInputDialog(this, mensaje, "Entrada", JOptionPane.QUESTION_MESSAGE);
            if (valor == null || valor.trim().isEmpty()) {
                return null;
            }
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            mostrarError("Debe ingresar un número válido.");
            return null;
        }
    }

    private Habitacion.TipoHabitacion convertirTipoHabitacion(int seleccion) {
        switch (seleccion) {
            case 1:
                return Habitacion.TipoHabitacion.OBSERVACION;
            case 2:
                return Habitacion.TipoHabitacion.HOSPITALIZACION;
            case 3:
                return Habitacion.TipoHabitacion.QUIROFANO;
            case 0:
            default:
                return Habitacion.TipoHabitacion.CONSULTA_EXTERNA;
        }
    }

    private void mostrarMensaje(String texto, String titulo) {
        JOptionPane.showMessageDialog(this, texto, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public JButton getBtnRegistrarPaciente() { return btnRegistrarPaciente; }
    public JButton getBtnAgregarCola() { return btnAgregarCola; }
    public JButton getBtnAsignarConsultorio() { return btnAsignarConsultorio; }
    public JButton getBtnRegistrarServicio() { return btnRegistrarServicio; }
    public JButton getBtnRegistrarPago() { return btnRegistrarPago; }
    public JButton getBtnReportes() { return btnReportes; }
    public JButton getBtnSalir() { return btnSalir; }
}