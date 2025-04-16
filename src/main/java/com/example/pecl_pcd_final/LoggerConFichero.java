package com.example.pecl_pcd_final;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class LoggerConFichero {

    private static Logger logger;

    static {
        try {
            // Creamos el logger
            logger = Logger.getLogger("Logger");

            FileHandler fileHandler = new FileHandler("fichero.txt", true);
            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false); //Para que no salga por consola

        } catch (IOException e) {
            System.err.println("Error configurando el logger: " + e.getMessage());
        }
    }


    public static Logger getLogger() {
        return logger;
    }
}

