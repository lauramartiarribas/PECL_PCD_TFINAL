package com.example.pecl_pcd_final;


public class Ser extends Thread{

    private String identificador;
    private Entorno entorno;
    private boolean estaMuerto=false;


    public String toString() {
        return identificador;
    }


    //Getter y setter
    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Entorno getEntorno() {
        return entorno;
    }

    public void setEntorno(Entorno entorno) {
        this.entorno = entorno;
    }

    public boolean isEstaMuerto() {
        return estaMuerto;
    }

    public void setEstaMuerto(boolean estaMuerto) {
        this.estaMuerto = estaMuerto;
    }
}
