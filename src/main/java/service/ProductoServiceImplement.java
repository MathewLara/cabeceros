package service;

import models.Producto;

import java.util.Arrays;
import java.util.List;
/*
 * autor: Mathew Lara
 * fecha y version: 06/11/2025 version: 1.0
 * descripcion: Implementacion concreta de la interfaz ProductoService.
 * Esta clase simula la obtencion de datos (como si fuera una
 * base de datos) y provee una lista fija de productos.
 */
public class ProductoServiceImplement implements ProductoService {
    /*
     * Implementacion del metodo abstracto listar().
     * Devuelve una lista fija de productos
     * usando el metodo Arrays.asList().
     * Retorna una Lista de objetos Producto con datos de ejemplo.
     */
    @Override
    public List<Producto> listar() {
        // Se retorna la lista de productos creada al momento
        return Arrays.asList(new Producto(1L, "laptop", "computacion", 523.21),
                new Producto(2L, "Mouse", "inalambrico", 15.25),
                new Producto(3L, "Impresora", "tinta continua", 256.25));
    }
}
