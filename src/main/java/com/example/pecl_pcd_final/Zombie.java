package com.example.pecl_pcd_final;

import com.example.pecl_pcd_final.Entorno;
import com.example.pecl_pcd_final.Ser;

public class Zombie extends Ser {

    public Zombie (String id, Entorno entorno){
        this.identificador=id;
        this.entorno=entorno;
    }


    @Override
    public void run(){
        //Elige zona

        //Busca humano para atacar

            //Ataca un tiempo

            //Permanece un tiempo
    }
}
