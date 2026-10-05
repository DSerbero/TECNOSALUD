package views;

import controllers.ClinicaController;
import models.Habitacion;

import javax.swing.*;
import java.awt.*;

public class VistaPrincipal extends JFrame {

    private final ClinicaController controller;
    private final CardLayout cardLayout;
    private final JPanel panelCards;
    private final JLabel lblEstado;
    private JTextArea areaInicio;
    private JTextArea areaReporte;
    private JLabel lblPacientesResumen;
    private JLabel lblConsultasResumen;
    private JLabel lblEsperaResumen;
    private JLabel lblServiciosResumen;

    public VistaPrincipal() {
        this.controller = new ClinicaController();
        this.cardLayout = new CardLayout();
        this.panelCards = new JPanel(cardLayout);
        this.lblEstado = new JLabel("Listo");

        configurarVentana();
        add(crearLayoutPrincipal());
        actualizarPanelInicio();
        actualizarPanelReportes();
    }

    private void configurarVentana() {
        setTitle("TECNOSALUD - Gestión Clínica");
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(240, 245, 250));
    }

    private JPanel crearLayoutPrincipal() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(18, 18));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(new Color(240, 245, 250));

        JPanel panelSidebar = crearSidebar();
        panelPrincipal.add(panelSidebar, BorderLayout.WEST);

        panelCards.add(crearPanelInicio(), "inicio");
        panelCards.add(crearPanelPaciente(), "paciente");
        panelCards.add(crearPanelCola(), "cola");
        panelCards.add(crearPanelHabitacion(), "habitacion");
        panelCards.add(crearPanelServicio(), "servicio");
        panelCards.add(crearPanelPago(), "pago");
        panelCards.add(crearPanelReportes(), "reportes");

        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.setBackground(new Color(240, 245, 250));
        panelCentro.add(panelCards, BorderLayout.CENTER);

        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.setBackground(new Color(22, 52, 71));
        panelEstado.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panelEstado.add(lblEstado, BorderLayout.CENTER);

        panelCentro.add(panelEstado, BorderLayout.SOUTH);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        return panelPrincipal;
    }

    private JPanel crearSidebar() {
        JPanel panelSidebar = new JPanel();
        panelSidebar.setLayout(new BoxLayout(panelSidebar, BoxLayout.Y_AXIS));
        panelSidebar.setPreferredSize(new Dimension(240, 0));
        panelSidebar.setBackground(new Color(19, 50, 67));
        panelSidebar.setBorder(BorderFactory.createEmptyBorder(18, 14, 18, 14));

        JLabel lblLogo = new JLabel("TECNOSALUD");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSidebar.add(lblLogo);
        panelSidebar.add(Box.createVerticalStrut(30));

        panelSidebar.add(crearBotonSidebar("Inicio", "inicio", new Color(70, 127, 169)));
        panelSidebar.add(crearBotonSidebar("Registrar paciente", "paciente", new Color(25, 127, 160)));
        panelSidebar.add(crearBotonSidebar("Agregar a cola", "cola", new Color(40, 140, 180)));
        panelSidebar.add(crearBotonSidebar("Asignar habitación", "habitacion", new Color(38, 166, 118)));
        panelSidebar.add(crearBotonSidebar("Registrar servicio", "servicio", new Color(52, 166, 122)));
        panelSidebar.add(crearBotonSidebar("Registrar pago", "pago", new Color(196, 133, 0)));
        panelSidebar.add(crearBotonSidebar("Reportes", "reportes", new Color(121, 91, 170)));
        panelSidebar.add(Box.createVerticalGlue());
        panelSidebar.add(crearBotonSidebar("Salir", "salir", new Color(176, 60, 60)));

        return panelSidebar;
    }

    private JButton crearBotonSidebar(String texto, String accion, Color color) {
        JButton boton = new JButton(texto);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(220, 42));
        boton.setPreferredSize(new Dimension(220, 42));
        boton.setBackground(color);
        boton.setForeground(Color.BLACK);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addActionListener(e -> {
            if (accion.equals("salir")) {
                System.exit(0);
                return;
            }
            cardLayout.show(panelCards, accion);
            if (accion.equals("inicio")) {
                actualizarPanelInicio();
            }
            if (accion.equals("reportes")) {
                actualizarPanelReportes();
            }
        });

        return boton;
    }

    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(240, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel titulo = new JLabel("Panel principal");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titulo.setForeground(new Color(14, 53, 71));

        JPanel cardsResumen = new JPanel(new GridLayout(1, 4, 20, 20));
        cardsResumen.setBackground(new Color(240, 245, 250));

        lblPacientesResumen = crearResumenCardLabel("Pacientes", String.valueOf(controller.getGestor().getPacientesRegistrados().getTamano()), new Color(29, 128, 159));
        lblConsultasResumen = crearResumenCardLabel("Consultas", String.valueOf(controller.getGestor().getHistorialAtenciones().getTamano()), new Color(36, 154, 111));
        lblEsperaResumen = crearResumenCardLabel("En espera", String.valueOf(controller.getGestor().getColaConsultaExterna().getTamano()), new Color(204, 153, 0));
        lblServiciosResumen = crearResumenCardLabel("Servicios", String.valueOf(controller.getGestor().getPilaServiciosPrestados().getTamanio()), new Color(118, 96, 168));

        cardsResumen.add(crearResumenCard("Pacientes", lblPacientesResumen, new Color(29, 128, 159)));
        cardsResumen.add(crearResumenCard("Consultas", lblConsultasResumen, new Color(36, 154, 111)));
        cardsResumen.add(crearResumenCard("En espera", lblEsperaResumen, new Color(204, 153, 0)));
        cardsResumen.add(crearResumenCard("Servicios", lblServiciosResumen, new Color(118, 96, 168)));

        areaInicio = new JTextArea();
        areaInicio.setEditable(false);
        areaInicio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        areaInicio.setBackground(new Color(255, 255, 255));
        areaInicio.setBorder(BorderFactory.createTitledBorder("Resumen del sistema"));
        areaInicio.setText(controller.obtenerReporte());

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(cardsResumen, BorderLayout.CENTER);
        panel.add(new JScrollPane(areaInicio), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearResumenCard(String titulo, JLabel valorLabel, Color color) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(255, 255, 255));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitulo.setForeground(new Color(90, 100, 110));

        valorLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valorLabel.setForeground(color);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(valorLabel, BorderLayout.CENTER);
        return panel;
    }

    private JLabel crearResumenCardLabel(String titulo, String valor, Color color) {
        JLabel label = new JLabel(valor);
        label.setFont(new Font("Segoe UI", Font.BOLD, 28));
        label.setForeground(color);
        return label;
    }

    private JPanel crearPanelPaciente() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(240, 245, 250));

        JLabel titulo = new JLabel("Registrar paciente");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(14, 53, 71));

        JPanel form = new JPanel(new GridLayout(7, 2, 12, 12));
        form.setBackground(new Color(240, 245, 250));

        JTextField txtDocumento = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtEdad = new JTextField();
        JTextField txtGenero = new JTextField();
        JTextField txtTelefono = new JTextField();
        JCheckBox chkEps = new JCheckBox("Tiene EPS");
        JTextField txtTipoSangre = new JTextField();

        form.add(new JLabel("Documento:"));
        form.add(txtDocumento);
        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("Edad:"));
        form.add(txtEdad);
        form.add(new JLabel("Género:"));
        form.add(txtGenero);
        form.add(new JLabel("Teléfono:"));
        form.add(txtTelefono);
        form.add(new JLabel("EPS:"));
        form.add(chkEps);
        form.add(new JLabel("Tipo de sangre:"));
        form.add(txtTipoSangre);

        JButton btnGuardar = new JButton("Guardar paciente");
        btnGuardar.setBackground(new Color(29, 128, 159));
        btnGuardar.setForeground(Color.BLACK);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.addActionListener(e -> {
            String documento = txtDocumento.getText();
            String nombre = txtNombre.getText();
            String edad = txtEdad.getText();
            String genero = txtGenero.getText();
            String telefono = txtTelefono.getText();
            String tipoSangre = txtTipoSangre.getText();

            if (documento == null || documento.trim().isEmpty() || nombre == null || nombre.trim().isEmpty() ||
                    genero == null || genero.trim().isEmpty() || telefono == null || telefono.trim().isEmpty() ||
                    tipoSangre == null || tipoSangre.trim().isEmpty() || edad == null || edad.trim().isEmpty()) {
                mostrarEstado("Complete todos los campos del paciente.", new Color(178, 58, 58));
                return;
            }

            try {
                int edadNumero = Integer.parseInt(edad.trim());
                boolean ok = controller.registrarPaciente(documento, nombre, edadNumero, genero, telefono,
                        chkEps.isSelected(), tipoSangre);
                if (ok) {
                    mostrarEstado("Paciente registrado correctamente.", new Color(28, 142, 86));
                    txtDocumento.setText("");
                    txtNombre.setText("");
                    txtEdad.setText("");
                    txtGenero.setText("");
                    txtTelefono.setText("");
                    txtTipoSangre.setText("");
                    chkEps.setSelected(false);
                } else {
                    mostrarEstado("El paciente ya existe o los datos son inválidos.", new Color(178, 58, 58));
                }
            } catch (NumberFormatException ex) {
                mostrarEstado("La edad debe ser un número válido.", new Color(178, 58, 58));
            }
        });

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(btnGuardar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelCola() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(240, 245, 250));

        JLabel titulo = new JLabel("Agregar paciente a cola");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(14, 53, 71));

        JPanel form = new JPanel(new GridLayout(3, 2, 12, 12));
        form.setBackground(new Color(240, 245, 250));

        JTextField txtDocumento = new JTextField();
        JRadioButton radioConsulta = new JRadioButton("Consulta externa");
        JRadioButton radioPreop = new JRadioButton("Preoperatorio");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(radioConsulta);
        grupo.add(radioPreop);
        radioConsulta.setSelected(true);

        form.add(new JLabel("Documento:"));
        form.add(txtDocumento);
        form.add(new JLabel("Tipo de cola:"));
        form.add(radioConsulta);
        form.add(new JLabel(""));
        form.add(radioPreop);

        JButton btnAgregar = new JButton("Agregar a cola");
        btnAgregar.setBackground(new Color(29, 128, 159));
        btnAgregar.setForeground(Color.BLACK);
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAgregar.addActionListener(e -> {
            String documento = txtDocumento.getText();
            if (documento == null || documento.trim().isEmpty()) {
                mostrarEstado("Ingrese el documento del paciente.", new Color(178, 58, 58));
                return;
            }

            if (controller.buscarPacientePorDocumento(documento) == null) {
                mostrarEstado("Paciente no encontrado.", new Color(178, 58, 58));
                return;
            }

            boolean ok = controller.agregarPacienteACola(documento, radioPreop.isSelected());
            if (ok) {
                mostrarEstado("Paciente agregado a la cola correctamente.", new Color(28, 142, 86));
                txtDocumento.setText("");
                radioConsulta.setSelected(true);
            } else {
                mostrarEstado("No se pudo agregar el paciente a la cola.", new Color(178, 58, 58));
            }
        });

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(btnAgregar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelHabitacion() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(240, 245, 250));

        JLabel titulo = new JLabel("Asignar habitación");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(14, 53, 71));

        JPanel form = new JPanel(new GridLayout(2, 2, 12, 12));
        form.setBackground(new Color(240, 245, 250));

        JTextField txtDocumento = new JTextField();
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Consulta Externa", "Observación", "Hospitalización", "Quirófano"});

        form.add(new JLabel("Documento:"));
        form.add(txtDocumento);
        form.add(new JLabel("Tipo de habitación:"));
        form.add(comboTipo);

        JButton btnAsignar = new JButton("Asignar habitación");
        btnAsignar.setBackground(new Color(38, 166, 118));
        btnAsignar.setForeground(Color.BLACK);
        btnAsignar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAsignar.addActionListener(e -> {
            String documento = txtDocumento.getText();
            if (documento == null || documento.trim().isEmpty()) {
                mostrarEstado("Ingrese el documento del paciente.", new Color(178, 58, 58));
                return;
            }

            if (controller.buscarPacientePorDocumento(documento) == null) {
                mostrarEstado("Paciente no encontrado.", new Color(178, 58, 58));
                return;
            }

            Habitacion.TipoHabitacion tipo = convertirTipoHabitacion(comboTipo.getSelectedIndex());
            if (controller.crearAtencion(documento, tipo) == null) {
                mostrarEstado("No hay habitaciones disponibles para ese tipo.", new Color(178, 58, 58));
                return;
            }

            mostrarEstado("Habitación asignada correctamente.", new Color(28, 142, 86));
            txtDocumento.setText("");
            comboTipo.setSelectedIndex(0);
        });

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(btnAsignar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelServicio() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(240, 245, 250));

        JLabel titulo = new JLabel("Registrar servicio");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(14, 53, 71));

        JPanel form = new JPanel(new GridLayout(2, 2, 12, 12));
        form.setBackground(new Color(240, 245, 250));

        JTextField txtDocumento = new JTextField();
        JComboBox<String> comboServicios = new JComboBox<>(obtenerServiciosDisponibles());

        form.add(new JLabel("Documento del paciente:"));
        form.add(txtDocumento);
        form.add(new JLabel("Servicio:"));
        form.add(comboServicios);

        JButton btnRegistrar = new JButton("Registrar servicio");
        btnRegistrar.setBackground(new Color(52, 166, 122));
        btnRegistrar.setForeground(Color.BLACK);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistrar.addActionListener(e -> {
            String documento = txtDocumento.getText();
            String codigo = obtenerCodigoServicioSeleccionado((String) comboServicios.getSelectedItem());
            if (documento == null || documento.trim().isEmpty() || codigo == null || codigo.trim().isEmpty()) {
                mostrarEstado("Ingrese documento y seleccione un servicio.", new Color(178, 58, 58));
                return;
            }

            if (controller.buscarAtencionActiva(documento) == null) {
                mostrarEstado("No existe una atención activa para ese paciente.", new Color(178, 58, 58));
                return;
            }

            if (controller.agregarServicioAAtencion(documento, codigo)) {
                mostrarEstado("Servicio registrado correctamente.", new Color(28, 142, 86));
                txtDocumento.setText("");
                comboServicios.setSelectedIndex(0);
            } else {
                mostrarEstado("El servicio no existe o no se pudo registrar.", new Color(178, 58, 58));
            }
        });

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(btnRegistrar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelPago() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(240, 245, 250));

        JLabel titulo = new JLabel("Registrar pago");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(14, 53, 71));

        JPanel form = new JPanel(new GridLayout(2, 2, 12, 12));
        form.setBackground(new Color(240, 245, 250));

        JTextField txtDocumento = new JTextField();
        JComboBox<String> comboMedio = new JComboBox<>(new String[]{"Efectivo", "Tarjeta Débito", "Tarjeta Crédito", "EPS"});

        form.add(new JLabel("Documento del paciente:"));
        form.add(txtDocumento);
        form.add(new JLabel("Medio de pago:"));
        form.add(comboMedio);

        JButton btnPagar = new JButton("Registrar pago");
        btnPagar.setBackground(new Color(196, 133, 0));
        btnPagar.setForeground(Color.BLACK);
        btnPagar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPagar.addActionListener(e -> {
            String documento = txtDocumento.getText();
            if (documento == null || documento.trim().isEmpty()) {
                mostrarEstado("Ingrese el documento del paciente.", new Color(178, 58, 58));
                return;
            }
            if (controller.buscarAtencionActiva(documento) == null) {
                mostrarEstado("No existe atención activa para ese paciente.", new Color(178, 58, 58));
                return;
            }
            if (controller.procesarPago(documento, comboMedio.getSelectedIndex())) {
                mostrarEstado("Pago registrado correctamente.", new Color(28, 142, 86));
                txtDocumento.setText("");
                comboMedio.setSelectedIndex(0);
            } else {
                mostrarEstado("No fue posible registrar el pago.", new Color(178, 58, 58));
            }
        });

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(btnPagar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelReportes() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(240, 245, 250));

        JLabel titulo = new JLabel("Reportes del sistema");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(14, 53, 71));

        areaReporte = new JTextArea();
        areaReporte.setEditable(false);
        areaReporte.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        areaReporte.setBackground(Color.WHITE);
        areaReporte.setBorder(BorderFactory.createTitledBorder("Reporte general"));
        areaReporte.setText(controller.obtenerReporte());

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(areaReporte), BorderLayout.CENTER);
        return panel;
    }

    private void actualizarPanelInicio() {
        if (lblPacientesResumen != null) {
            lblPacientesResumen.setText(String.valueOf(controller.getGestor().getPacientesRegistrados().getTamano()));
        }
        if (lblConsultasResumen != null) {
            lblConsultasResumen.setText(String.valueOf(controller.getGestor().getHistorialAtenciones().getTamano()));
        }
        if (lblEsperaResumen != null) {
            lblEsperaResumen.setText(String.valueOf(controller.getGestor().getColaConsultaExterna().getTamano()));
        }
        if (lblServiciosResumen != null) {
            lblServiciosResumen.setText(String.valueOf(controller.getGestor().getPilaServiciosPrestados().getTamanio()));
        }
        if (areaInicio != null) {
            areaInicio.setText(controller.obtenerReporte());
        }
        mostrarEstado("Sistema listo para gestionar la clínica.", new Color(19, 88, 120));
    }

    private void actualizarPanelReportes() {
        if (areaReporte != null) {
            areaReporte.setText(controller.obtenerReporte());
        }
        mostrarEstado("Reporte actualizado.", new Color(19, 88, 120));
    }

    private void mostrarEstado(String mensaje, Color color) {
        lblEstado.setText(mensaje);
        lblEstado.setForeground(color);
    }

    private String[] obtenerServiciosDisponibles() {
        String[] servicios = new String[controller.getGestor().getServiciosDisponibles().getTamano()];
        for (int i = 0; i < controller.getGestor().getServiciosDisponibles().getTamano(); i++) {
            var servicio = controller.getGestor().getServiciosDisponibles().obtener(i);
            servicios[i] = servicio.getCodigo() + " - " + servicio.getNombre();
        }
        return servicios;
    }

    private String obtenerCodigoServicioSeleccionado(String textoServicio) {
        if (textoServicio == null || textoServicio.trim().isEmpty()) {
            return "";
        }
        int indiceSeparador = textoServicio.indexOf(" - ");
        if (indiceSeparador > 0) {
            return textoServicio.substring(0, indiceSeparador).trim();
        }
        return textoServicio.trim();
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

    public JButton getBtnRegistrarPaciente() { return null; }
    public JButton getBtnAgregarCola() { return null; }
    public JButton getBtnAsignarConsultorio() { return null; }
    public JButton getBtnRegistrarServicio() { return null; }
    public JButton getBtnRegistrarPago() { return null; }
    public JButton getBtnReportes() { return null; }
    public JButton getBtnSalir() { return null; }
}