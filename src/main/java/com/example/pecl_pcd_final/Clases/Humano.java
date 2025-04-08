package com.example.pecl_pcd_final.Clases;

import com.example.pecl_pcd_final.Entorno;

public class Humano extends Ser{
    int numComida;
    boolean marcado;

    public Humano(String id, Entorno entorno){
        this.identificador= id;
        this.entorno=entorno;
        numComida=0;
        marcado=false;
    }

    @Override
    public void run(){
        //Zona común tiempo entre 1 y 2
        //entorno.LabelZonaComun.setText(identificador);

        //Seleccionar túnel
    }
}
