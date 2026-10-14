package models;

import java.time.LocalDateTime;

public class Paciente {
    private String documento;
    private String nombre;
    private int edad;
    private String genero;
    private String telefono;
    private boolean tieneEps;
    private String tipoSangre;
    private LocalDateTime horaLlegadaCola;

    public Paciente(String documento, String nombre, int edad, String genero, String telefono, boolean tieneEps, String tipoSangre) {
        this.documento = documento;
        this.nombre = nombre;
        this.edad = edad;
        this.genero = genero;
        this.telefono = telefono;
        this.tieneEps = tieneEps;
        this.tipoSangre = tipoSangre;
        this.horaLlegadaCola = null;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public String getGenero() {
        return genero;
    }

    public String getTelefono() {
        return telefono;
    }

    public boolean isTieneEps() {
        return tieneEps;
    }

    public String getTipoSangre() {
        return tipoSangre;
    }

    public LocalDateTime getHoraLlegadaCola() {
        return horaLlegadaCola;
    }

    public void setHoraLlegadaCola(LocalDateTime horaLlegadaCola) {
        this.horaLlegadaCola = horaLlegadaCola;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paciente)) {
            return false;
        }
        Paciente p = (Paciente) o;
        return documento != null && documento.equalsIgnoreCase(p.documento);
    }

    @Override
    public int hashCode() {
        return documento == null ? 0 : documento.toUpperCase().hashCode();
    }
}
