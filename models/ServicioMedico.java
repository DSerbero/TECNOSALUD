package models;

public class ServicioMedico {
    private String codigo;
    private String nombre;
    private double precioUnitario;
    private boolean esConsulta;
    private boolean esHospitalizacion;
    private boolean esCirugiaEmergencia;

    public ServicioMedico(String codigo, String nombre, double precioUnitario, boolean esConsulta, boolean esHospitalizacion, boolean esCirugiaEmergencia) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.esConsulta = esConsulta;
        this.esHospitalizacion = esHospitalizacion;
        this.esCirugiaEmergencia = esCirugiaEmergencia;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public double getPrecioUnitario() { return precioUnitario; }
    public boolean isEsConsulta() { return esConsulta; }
    public boolean isEsHospitalizacion() { return esHospitalizacion; }
    public boolean isEsCirugiaEmergencia() { return esCirugiaEmergencia; }
}