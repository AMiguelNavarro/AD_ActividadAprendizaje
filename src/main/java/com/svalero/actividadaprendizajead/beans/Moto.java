package com.svalero.actividadaprendizajead.beans;

public class Moto {

    private String matricula, marca, modelo, tipo;

    /**
     * Constructor vacío
     */
    public Moto(){}

    /**
     * Constructor con parámetros
     * @param matricula
     * @param marca
     * @param modelo
     * @param tipo
     */
    public Moto(String matricula, String marca, String modelo, String tipo) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
    }


    /* MÉTODOS GETTERS Y SETTERS DE LA CLASE */

    public String getMatricula() {return matricula;}

    public void setMatricula(String matricula) {this.matricula = matricula;}

    public String getMarca() {return marca;}

    public void setMarca(String marca) { this.marca = marca;}

    public String getModelo() { return modelo;}

    public void setModelo(String modelo) { this.modelo = modelo;}

    public String getTipo() {return tipo;}

    public void setTipo(String tipo) {this.tipo = tipo;}
}
