package models;

public class Habitacion {

    public enum TipoHabitacion {
        CONSULTA_EXTERNA,
        OBSERVACION,
        HOSPITALIZACION,
        QUIROFANO
    }

    private String numero;
    private TipoHabitacion tipo;
    private boolean ocupado;
    private double tarifaDiaria;

    public Habitacion(String numero, TipoHabitacion tipo) {
        this(numero, tipo, 0.0);
    }

    public Habitacion(String numero, TipoHabitacion tipo, double tarifaDiaria) {
        this.numero = numero;
        this.tipo = tipo;
        this.ocupado = false;
        this.tarifaDiaria = tarifaDiaria;
    }

    public String getNumero() {
        return numero;
    }

    public TipoHabitacion getTipo() {
        return tipo;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    public double getTarifaDiaria() {
        return tarifaDiaria;
    }

    public void setTarifaDiaria(double tarifaDiaria) {
        this.tarifaDiaria = tarifaDiaria;
    }
}
