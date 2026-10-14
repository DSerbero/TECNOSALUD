package views;

import controllers.ClinicaController;
import models.Habitacion;
import models.RegistroAtencion;
import models.estructuras.ListaEnlazada;

import javax.swing.*;
import java.awt.*;

public class VistaPrincipal extends JFrame {

    private final ClinicaController controller;
    private final CardLayout cardLayout;
    private final JPanel panelCards;
    private final JLabel lblEstado;
    private JTextArea areaInicio;
    private JTextArea areaReporte;
    private JTextArea areaHabitaciones;
    private JComboBox<String> comboHabitaciones;
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
        actualizarPanelHabitaciones();
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
        panelCards.add(crearPanelCancelar(), "cancelar");
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
        panelSidebar.add(crearBotonSidebar("Cancelar cita", "cancelar", new Color(176, 60, 60)));
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
            if (accion.equals("habitacion")) {
                actualizarPanelHabitaciones();
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
        areaInicio.setText(controller.obtenerReporteResumido());

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
        JComboBox<String> comboGenero = new JComboBox<>(new String[]{"Femenino", "Masculino", "No binario", "Prefiero no decirlo"});
        JTextField txtTelefono = new JTextField();
        JCheckBox chkEps = new JCheckBox("Tiene EPS");
        JComboBox<String> comboTipoSangre = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});

        form.add(new JLabel("Documento:"));
        form.add(txtDocumento);
        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("Edad:"));
        form.add(txtEdad);
        form.add(new JLabel("Género:"));
        form.add(comboGenero);
        form.add(new JLabel("Teléfono:"));
        form.add(txtTelefono);
        form.add(new JLabel("EPS:"));
        form.add(chkEps);
        form.add(new JLabel("Tipo de sangre:"));
        form.add(comboTipoSangre);

        JButton btnGuardar = new JButton("Guardar paciente");
        btnGuardar.setBackground(new Color(29, 128, 159));
        btnGuardar.setForeground(Color.BLACK);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.addActionListener(e -> {
            String documento = txtDocumento.getText();
            String nombre = txtNombre.getText();
            String edad = txtEdad.getText();
            String genero = (String) comboGenero.getSelectedItem();
            String telefono = txtTelefono.getText();
            String tipoSangre = (String) comboTipoSangre.getSelectedItem();

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
                    comboGenero.setSelectedIndex(0);
                    txtTelefono.setText("");
                    comboTipoSangre.setSelectedIndex(0);
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

        JLabel titulo = new JLabel("Cola de atencion");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(14, 53, 71));

        JPanel form = new JPanel(new GridLayout(3, 2, 12, 12));
        form.setBackground(new Color(240, 245, 250));

        JTextField txtDocumento = new JTextField();
        JRadioButton radioConsulta = new JRadioButton("Consulta externa");
        JRadioButton radioPreop = new JRadioButton("Preoperatorio (max. 3)");
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

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        acciones.setBackground(new Color(240, 245, 250));

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

            boolean preoperatorio = radioPreop.isSelected();
            boolean ok = controller.agregarPacienteACola(documento, preoperatorio);
            if (ok) {
                mostrarEstado("Paciente agregado a la cola correctamente.", new Color(28, 142, 86));
                txtDocumento.setText("");
                radioConsulta.setSelected(true);
            } else {
                if (controller.pacienteYaEnCola(documento, preoperatorio)) {
                    mostrarEstado("El paciente ya se encuentra en esa cola.", new Color(178, 58, 58));
                } else if (preoperatorio && controller.colaPreOperatorioLlena()) {
                    mostrarEstado("Cola de preoperatorio llena (maximo 3). Paciente rechazado.", new Color(178, 58, 58));
                } else {
                    mostrarEstado("No se pudo agregar el paciente a la cola.", new Color(178, 58, 58));
                }
            }
        });

        JButton btnAtender = new JButton("Atender siguiente (consulta)");
        btnAtender.setBackground(new Color(38, 166, 118));
        btnAtender.setForeground(Color.BLACK);
        btnAtender.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAtender.addActionListener(e -> {
            if (controller.getGestor().getColaConsultaExterna().estaVacia()) {
                mostrarEstado("La cola de consulta externa esta vacia.", new Color(178, 58, 58));
                return;
            }
            RegistroAtencion atencion = controller.atenderSiguienteConsulta();
            if (atencion == null) {
                mostrarEstado("No hay consultorios disponibles para atender.", new Color(178, 58, 58));
                return;
            }
            mostrarEstado("Atendiendo: " + atencion.getPaciente().getNombre() + " -> consultorio "
                    + atencion.getHabitacionAsignada().getNumero() + ".", new Color(28, 142, 86));
        });

        JButton btnAtenderPreop = new JButton("Atender siguiente (preoperatorio)");
        btnAtenderPreop.setBackground(new Color(196, 133, 0));
        btnAtenderPreop.setForeground(Color.BLACK);
        btnAtenderPreop.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAtenderPreop.addActionListener(e -> {
            if (controller.getGestor().getColaPreOperatorio().estaVacia()) {
                mostrarEstado("La cola de preoperatorio esta vacia.", new Color(178, 58, 58));
                return;
            }
            RegistroAtencion atencion = controller.atenderSiguientePreOperatorio();
            if (atencion == null) {
                mostrarEstado("El quirofano esta ocupado. Capacidad 1.", new Color(178, 58, 58));
                return;
            }
            mostrarEstado("En preoperatorio: " + atencion.getPaciente().getNombre() + " -> quirofano "
                    + atencion.getHabitacionAsignada().getNumero() + ".", new Color(28, 142, 86));
        });

        acciones.add(btnAgregar);
        acciones.add(btnAtender);
        acciones.add(btnAtenderPreop);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(acciones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelHabitacion() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(240, 245, 250));

        JLabel titulo = new JLabel("Asignar / liberar habitacion");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(14, 53, 71));

        JPanel form = new JPanel(new GridLayout(3, 2, 12, 12));
        form.setBackground(new Color(240, 245, 250));

        JTextField txtDocumento = new JTextField();
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Consulta Externa", "Observación", "Hospitalización", "Quirófano"});
        comboHabitaciones = new JComboBox<>();

        form.add(new JLabel("Documento:"));
        form.add(txtDocumento);
        form.add(new JLabel("Tipo de habitación:"));
        form.add(comboTipo);
        form.add(new JLabel("Habitación a liberar:"));
        form.add(comboHabitaciones);

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
            if (controller.buscarAtencionActiva(documento) != null) {
                mostrarEstado("El paciente ya tiene una atencion activa.", new Color(178, 58, 58));
                return;
            }
            RegistroAtencion atencion = controller.crearAtencion(documento, tipo);
            if (atencion == null) {
                mostrarEstado("No hay habitaciones disponibles para ese tipo.", new Color(178, 58, 58));
                return;
            }

            mostrarEstado("Habitacion " + atencion.getHabitacionAsignada().getNumero() + " asignada correctamente.", new Color(28, 142, 86));
            txtDocumento.setText("");
            comboTipo.setSelectedIndex(0);
            actualizarPanelHabitaciones();
        });

        JButton btnFinalizar = new JButton("Finalizar atencion");
        btnFinalizar.setBackground(new Color(29, 128, 159));
        btnFinalizar.setForeground(Color.BLACK);
        btnFinalizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnFinalizar.addActionListener(e -> {
            String documento = txtDocumento.getText();
            if (documento == null || documento.trim().isEmpty()) {
                mostrarEstado("Ingrese el documento del paciente.", new Color(178, 58, 58));
                return;
            }
            if (controller.finalizarAtencion(documento)) {
                mostrarEstado("Atencion finalizada y habitacion liberada.", new Color(28, 142, 86));
                txtDocumento.setText("");
                actualizarPanelHabitaciones();
            } else {
                mostrarEstado("No hay atencion activa para ese paciente.", new Color(178, 58, 58));
            }
        });

        JButton btnLiberar = new JButton("Liberar habitacion");
        btnLiberar.setBackground(new Color(176, 60, 60));
        btnLiberar.setForeground(Color.BLACK);
        btnLiberar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLiberar.addActionListener(e -> {
            Object sel = comboHabitaciones.getSelectedItem();
            if (sel == null) {
                mostrarEstado("Seleccione una habitacion.", new Color(178, 58, 58));
                return;
            }
            if (controller.liberarHabitacion(sel.toString())) {
                mostrarEstado("Habitacion liberada.", new Color(28, 142, 86));
                actualizarPanelHabitaciones();
            } else {
                mostrarEstado("Habitacion no encontrada.", new Color(178, 58, 58));
            }
        });

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        acciones.setBackground(new Color(240, 245, 250));
        acciones.add(btnAsignar);
        acciones.add(btnFinalizar);
        acciones.add(btnLiberar);

        areaHabitaciones = new JTextArea(6, 40);
        areaHabitaciones.setEditable(false);
        areaHabitaciones.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        areaHabitaciones.setBackground(Color.WHITE);
        areaHabitaciones.setBorder(BorderFactory.createTitledBorder("Estado de habitaciones"));

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(new Color(240, 245, 250));
        centro.add(form);
        centro.add(Box.createVerticalStrut(15));
        centro.add(new JScrollPane(areaHabitaciones));

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(centro, BorderLayout.CENTER);
        panel.add(acciones, BorderLayout.SOUTH);
        actualizarPanelHabitaciones();
        return panel;
    }

    private JPanel crearPanelServicio() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(240, 245, 250));

        JLabel titulo = new JLabel("Registrar servicio");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(14, 53, 71));

        JPanel form = new JPanel(new GridLayout(3, 2, 12, 12));
        form.setBackground(new Color(240, 245, 250));

        JTextField txtDocumento = new JTextField();
        JComboBox<String> comboServicios = new JComboBox<>(obtenerServiciosDisponibles());
        JTextField txtPrecio = new JTextField();

        form.add(new JLabel("Documento del paciente:"));
        form.add(txtDocumento);
        form.add(new JLabel("Servicio:"));
        form.add(comboServicios);
        form.add(new JLabel("Precio (solo servicios variables):"));
        form.add(txtPrecio);

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

            double precio = 0;
            String textoPrecio = txtPrecio.getText();
            if (textoPrecio != null && !textoPrecio.trim().isEmpty()) {
                try {
                    precio = Double.parseDouble(textoPrecio.trim().replace(",", "."));
                } catch (NumberFormatException ex) {
                    mostrarEstado("El precio debe ser un numero valido.", new Color(178, 58, 58));
                    return;
                }
            }

            if (controller.buscarAtencionActiva(documento) == null) {
                mostrarEstado("No existe una atención activa para ese paciente.", new Color(178, 58, 58));
                return;
            }

            if (controller.agregarServicioAAtencion(documento, codigo, precio)) {
                mostrarEstado("Servicio registrado correctamente.", new Color(28, 142, 86));
                txtDocumento.setText("");
                txtPrecio.setText("");
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
            double total = controller.procesarPago(documento, comboMedio.getSelectedIndex());
            if (total >= 0) {
                mostrarEstado("Pago registrado. Total: $" + String.format("%,.0f", total) + ".", new Color(28, 142, 86));
                txtDocumento.setText("");
                comboMedio.setSelectedIndex(0);
            } else {
                mostrarEstado("No fue posible registrar el pago.", new Color(178, 58, 58));
            }
        });

        JButton btnPendiente = new JButton("Procesar factura pendiente (pila)");
        btnPendiente.setBackground(new Color(29, 128, 159));
        btnPendiente.setForeground(Color.BLACK);
        btnPendiente.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPendiente.addActionListener(e -> {
            String factura = controller.procesarFacturaPendiente();
            if (factura == null) {
                mostrarEstado("No hay facturas pendientes en la pila.", new Color(178, 58, 58));
            } else {
                mostrarEstado("Factura procesada: " + factura + ".", new Color(28, 142, 86));
            }
        });

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        acciones.setBackground(new Color(240, 245, 250));
        acciones.add(btnPagar);
        acciones.add(btnPendiente);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(acciones, BorderLayout.SOUTH);
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

    private JPanel crearPanelCancelar() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(240, 245, 250));

        JLabel titulo = new JLabel("Cancelar cita / reembolso");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(14, 53, 71));

        JPanel form = new JPanel(new GridLayout(2, 2, 12, 12));
        form.setBackground(new Color(240, 245, 250));

        JTextField txtDocumento = new JTextField();
        form.add(new JLabel("Documento del paciente:"));
        form.add(txtDocumento);
        form.add(new JLabel("Regla:"));
        form.add(new JLabel("Menos de 24 h => 50% de retencion"));

        JButton btnCancelar = new JButton("Cancelar atencion y calcular reembolso");
        btnCancelar.setBackground(new Color(176, 60, 60));
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.addActionListener(e -> {
            String documento = txtDocumento.getText();
            if (documento == null || documento.trim().isEmpty()) {
                mostrarEstado("Ingrese el documento del paciente.", new Color(178, 58, 58));
                return;
            }
            double reembolso = controller.cancelarCita(documento);
            if (reembolso < 0) {
                mostrarEstado("No se encontro una atencion vigente para ese paciente.", new Color(178, 58, 58));
            } else if (reembolso == 0) {
                mostrarEstado("Atencion cancelada (sin pago, no aplica reembolso).", new Color(28, 142, 86));
                txtDocumento.setText("");
            } else {
                mostrarEstado(String.format("Atencion cancelada. Reembolso: $%,.0f.", reembolso), new Color(28, 142, 86));
                txtDocumento.setText("");
            }
            actualizarPanelHabitaciones();
        });

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(btnCancelar, BorderLayout.SOUTH);
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
            areaInicio.setText(controller.obtenerReporteResumido());
        }
        mostrarEstado("Sistema listo para gestionar la clínica.", new Color(19, 88, 120));
    }

    private void actualizarPanelReportes() {
        if (areaReporte != null) {
            areaReporte.setText(controller.obtenerReporte());
        }
        mostrarEstado("Reporte actualizado.", new Color(19, 88, 120));
    }

    private void actualizarPanelHabitaciones() {
        if (areaHabitaciones != null) {
            StringBuilder sb = new StringBuilder();
            ListaEnlazada<Habitacion> habitaciones = controller.getGestor().getHabitacionesDisponibles();
            for (int i = 0; i < habitaciones.getTamano(); i++) {
                Habitacion habitacion = habitaciones.obtener(i);
                sb.append(habitacion.getNumero()).append(" | ").append(habitacion.getTipo()).append(" | ")
                        .append(habitacion.isOcupado() ? "OCUPADA" : "DISPONIBLE");
                if (habitacion.getTarifaDiaria() > 0) {
                    sb.append(" | tarifa/dia $").append(String.format("%,.0f", habitacion.getTarifaDiaria()));
                }
                sb.append("\n");
            }
            areaHabitaciones.setText(sb.toString());
        }
        if (comboHabitaciones != null) {
            String seleccionada = (String) comboHabitaciones.getSelectedItem();
            comboHabitaciones.removeAllItems();
            ListaEnlazada<Habitacion> habitaciones = controller.getGestor().getHabitacionesDisponibles();
            for (int i = 0; i < habitaciones.getTamano(); i++) {
                comboHabitaciones.addItem(habitaciones.obtener(i).getNumero());
            }
            if (seleccionada != null && habitaciones.getTamano() > 0) {
                comboHabitaciones.setSelectedItem(seleccionada);
            }
        }
    }

    private void mostrarEstado(String mensaje, Color color) {
        lblEstado.setText(mensaje);
        lblEstado.setForeground(color);
    }

    private String[] obtenerServiciosDisponibles() {
        String[] servicios = new String[controller.getGestor().getServiciosDisponibles().getTamano()];
        for (int i = 0; i < controller.getGestor().getServiciosDisponibles().getTamano(); i++) {
            var servicio = controller.getGestor().getServiciosDisponibles().obtener(i);
            servicios[i] = servicio.getCodigo() + " - " + servicio.getNombre() + " | $"
                    + String.format("%,.0f", servicio.getPrecioUnitario());
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