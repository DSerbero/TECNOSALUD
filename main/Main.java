package main;

import views.VistaPrincipal;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("No se pudo cargar el diseño nativo. Usando el diseño por defecto de Java.");
        }

        java.awt.EventQueue.invokeLater(() -> {
            
            
            VistaPrincipal vista = new VistaPrincipal();
            
            
            vista.setVisible(true);
        });
    }
}