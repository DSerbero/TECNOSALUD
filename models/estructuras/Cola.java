package models.estructuras;

public class Cola<T> {
    private Nodo<T> frente;
    private Nodo<T> fin;
    private int tamano;
    private int capacidadMaxima;

    public Cola() {
        this(-1);
    }

    public Cola(int capacidadMaxima) {
        this.frente = null;
        this.fin = null;
        this.tamano = 0;
        this.capacidadMaxima = capacidadMaxima;
    }

    public boolean encolar(T dato) {
        if (capacidadMaxima != -1 && tamano >= capacidadMaxima) {
            return false;
        }
        Nodo<T> nuevo = new Nodo<>(dato);
        if (fin != null) {
            fin.setSiguiente(nuevo);
        }
        fin = nuevo;
        if (frente == null) {
            frente = nuevo;
        }
        tamano++;
        return true;
    }

    public T desencolar() {
        if (estaVacia()) {
            return null;
        }
        T dato = frente.getDato();
        frente = frente.getSiguiente();
        if (frente == null) {
            fin = null;
        }
        tamano--;
        return dato;
    }

    public T verFrente() {
        if (estaVacia()) {
            return null;
        }
        return frente.getDato();
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public boolean estaLlena() {
        return capacidadMaxima != -1 && tamano >= capacidadMaxima;
    }

    public int getTamano() {
        return tamano;
    }

    public boolean contiene(T dato) {
        if (dato == null) {
            return false;
        }
        Nodo<T> actual = frente;
        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }
}
