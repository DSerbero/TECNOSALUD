package models;

public class Paciente {
    private String documento;
    private String nombre;
    private int edad;
    private String genero;
    private String telefono;
    private boolean tieneEps;
    private String tipoSangre;

    public Paciente(String documento, String nombre, int edad, String genero, String telefono, boolean tieneEps, String tipoSangre) {
        this.documento = documento;
        this.nombre = nombre;
        this.edad = edad;
        this.genero = genero;
        this.telefono = telefono;
        this.tieneEps = tieneEps;
        this.tipoSangre = tipoSangre;
    }

    public String getDocumento() { return documento; }
    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }
    public String getGenero() { return genero; }
    public String getTelefono() { return telefono; }
    public boolean isTieneEps() { return tieneEps; }
    public String getTipoSangre() { return tipoSangre; }
}