package com.example.pecl_pcd_final;

import com.example.pecl_pcd_final.Entorno;

public class Ser extends Thread{

    private String identificador;
    private Entorno entorno;


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
}
