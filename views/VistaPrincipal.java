package views;

import javax.swing.*;
import java.awt.*;

public class VistaPrincipal extends JFrame {

    private JButton btnRegistrarPaciente;
    private JButton btnAgregarCola;
    private JButton btnAsignarConsultorio;
    private JButton btnRegistrarServicio;
    private JButton btnRegistrarPago;
    private JButton btnReportes;
    private JButton btnSalir;

    public VistaPrincipal() {
        setTitle("Clínica TECNOSALUD - Sistema de Gestión");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        JMenu menuAtencion = new JMenu("Atención al Paciente");
        menuAtencion.add(new JMenuItem("Registrar Paciente"));
        menuAtencion.add(new JMenuItem("Agregar a Cola de Espera"));

        JMenu menuInstalaciones = new JMenu("Instalaciones y Servicios");
        menuInstalaciones.add(new JMenuItem("Asignar Consultorio / Ingresar a Sala"));
        menuInstalaciones.add(new JMenuItem("Registrar Servicio Médico"));

        JMenu menuFacturacion = new JMenu("Facturación y Reportes");
        menuFacturacion.add(new JMenuItem("Registrar Pago"));
        menuFacturacion.add(new JMenuItem("Mostrar Reportes Estadísticos"));

        menuBar.add(menuAtencion);
        menuBar.add(menuInstalaciones);
        menuBar.add(menuFacturacion);

        setJMenuBar(menuBar);

        JPanel panelBotones = new JPanel(new GridLayout(3, 3, 20, 20));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        btnRegistrarPaciente = new JButton("Registrar Paciente");
        btnAgregarCola = new JButton("Agregar a Cola");
        btnAsignarConsultorio = new JButton("Asignar Consultorio/Sala");
        btnRegistrarServicio = new JButton("Registrar Servicio");
        btnRegistrarPago = new JButton("Registrar Pago");
        btnReportes = new JButton("Mostrar Reportes");
        

        btnSalir = new JButton("Salir del Sistema");
        btnSalir.setForeground(Color.RED);
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalir.addActionListener(e -> System.exit(0));
        panelBotones.add(btnRegistrarPaciente);

        panelBotones.add(btnRegistrarPaciente);
        panelBotones.add(btnAgregarCola);
        panelBotones.add(btnAsignarConsultorio);
        panelBotones.add(btnRegistrarServicio);
        panelBotones.add(btnRegistrarPago);
        panelBotones.add(btnReportes);
        
        panelBotones.add(new JLabel("")); 
        panelBotones.add(btnSalir);
        panelBotones.add(new JLabel(""));

        add(panelBotones, BorderLayout.CENTER);
    }

    public JButton getBtnRegistrarPaciente() { return btnRegistrarPaciente; }
    public JButton getBtnAgregarCola() { return btnAgregarCola; }
    public JButton getBtnAsignarConsultorio() { return btnAsignarConsultorio; }
    public JButton getBtnRegistrarServicio() { return btnRegistrarServicio; }
    public JButton getBtnRegistrarPago() { return btnRegistrarPago; }
    public JButton getBtnReportes() { return btnReportes; }
    public JButton getBtnSalir() { return btnSalir; }
}