package util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import models.ClinicaGestor;
import models.Paciente;

/**
 * TEMPORAL - Solo para pruebas.
 * Lee datos_prueba_pacientes.json (si existe) y registra los pacientes en el gestor.
 * Para eliminarlo: borrar este archivo y el JSON. No afecta al resto del codigo.
 */
public class CargadorDatosPruebaTmp {

    private static final String ARCHIVO = "datos_prueba_pacientes.json";

    public static int cargar(ClinicaGestor gestor) {
        if (gestor == null) {
            return 0;
        }
        try {
            Path ruta = Paths.get(ARCHIVO);
            if (!Files.exists(ruta)) {
                return 0;
            }
            String contenido = Files.readString(ruta);
            int inicio = contenido.indexOf('[');
            int fin = contenido.lastIndexOf(']');
            if (inicio < 0 || fin < 0 || fin <= inicio) {
                return 0;
            }
            String arreglo = contenido.substring(inicio + 1, fin);
            int cargados = 0;
            int i = 0;
            while (i < arreglo.length()) {
                int abre = arreglo.indexOf('{', i);
                if (abre < 0) {
                    break;
                }
                int cierra = buscarCierre(arreglo, abre);
                if (cierra < 0) {
                    break;
                }
                String bloque = arreglo.substring(abre + 1, cierra);
                if (registrarBloque(gestor, bloque)) {
                    cargados++;
                }
                i = cierra + 1;
            }
            if (cargados > 0) {
                System.out.println("[PRUEBA] Pacientes cargados desde " + ARCHIVO + ": " + cargados);
            }
            return cargados;
        } catch (Exception ex) {
            System.out.println("[PRUEBA] No se pudieron cargar datos de prueba: " + ex.getMessage());
            return 0;
        }
    }

    private static int buscarCierre(String texto, int abre) {
        boolean enComillas = false;
        for (int i = abre + 1; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == '"') {
                enComillas = !enComillas;
            } else if (!enComillas && c == '}') {
                return i;
            }
        }
        return -1;
    }

    private static boolean registrarBloque(ClinicaGestor gestor, String bloque) {
        try {
            String documento = extraerTexto(bloque, "documento");
            String nombre = extraerTexto(bloque, "nombre");
            int edad = extraerEntero(bloque, "edad");
            String genero = extraerTexto(bloque, "genero");
            String telefono = extraerTexto(bloque, "telefono");
            boolean tieneEps = extraerBooleano(bloque, "tieneEps");
            String tipoSangre = extraerTexto(bloque, "tipoSangre");
            if (documento.isEmpty() || nombre.isEmpty()) {
                return false;
            }
            Paciente paciente = new Paciente(documento, nombre, edad, genero, telefono, tieneEps, tipoSangre);
            return gestor.registrarPaciente(paciente);
        } catch (Exception ex) {
            return false;
        }
    }

    private static String extraerValorCrudo(String bloque, String clave) {
        String marca = "\"" + clave + "\"";
        int pos = bloque.indexOf(marca);
        if (pos < 0) {
            return "";
        }
        int dosPuntos = bloque.indexOf(':', pos + marca.length());
        if (dosPuntos < 0) {
            return "";
        }
        int j = dosPuntos + 1;
        while (j < bloque.length() && Character.isWhitespace(bloque.charAt(j))) {
            j++;
        }
        if (j >= bloque.length()) {
            return "";
        }
        if (bloque.charAt(j) == '"') {
            int fin = bloque.indexOf('"', j + 1);
            if (fin < 0) {
                return "";
            }
            return bloque.substring(j + 1, fin);
        }
        int fin = j;
        while (fin < bloque.length() && bloque.charAt(fin) != ',' && bloque.charAt(fin) != '}') {
            fin++;
        }
        return bloque.substring(j, fin).trim();
    }

    private static String extraerTexto(String bloque, String clave) {
        return extraerValorCrudo(bloque, clave);
    }

    private static int extraerEntero(String bloque, String clave) {
        try {
            return Integer.parseInt(extraerValorCrudo(bloque, clave));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private static boolean extraerBooleano(String bloque, String clave) {
        return "true".equalsIgnoreCase(extraerValorCrudo(bloque, clave));
    }
}
