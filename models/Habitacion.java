package models;

public class Habitacion {
    private String numero;
    private String tipo;
    private boolean ocupado;

    public Habitacion(String numero, String tipo) {
        this.numero = numero;
        this.tipo = tipo;
        this.ocupado = false;
    }

    public String getNumero() { return numero; }
    public String getTipo() { return tipo; }
    public boolean isOcupado() { return ocupado; }
    public void setOcupado(boolean ocupado) { this.ocupado = ocupado; }
}