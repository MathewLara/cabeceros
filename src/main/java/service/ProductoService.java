package service;

import models.Producto;

import java.util.List;
/*
 * autor: Mathew Lara
 * fecha y version: 06/11/2025 version: 1.0
 * descripcion: Interfaz que define el contrato para los servicios
 * relacionados con Producto. Establece los metodos que
 * cualquier clase de servicio de productos debe implementar.
 */
public interface ProductoService {
    /*
     * Metodo abstracto para listar todos los productos.
     * Toda clase que implemente esta interfaz debe definir este metodo.
     * Retorna una Lista (List<Producto>) de objetos Producto.
     */
    List<Producto> listar();
}
