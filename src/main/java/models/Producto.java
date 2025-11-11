package models;
/*
 * autor: Mathew Lara
 * fecha y version: 06/11/2025 version: 1.0
 * descripcion: Clase que modela un Producto
 * Contiene los atributos de un producto (id, nombre, tipo, precio),
 * constructores para crear instancias, y sus metodos getters y setters
 * para acceder y modificar los atributos.
 */
public class Producto {
    private Long id;
    private String nombre;
    private String tipo;
    private double precio;
    /*
     * Constructor por defecto (vacío).
     * Permite crear una instancia de Producto sin inicializar sus atributos.
     */
    public Producto() {
    }
    /*
     * Constructor con parámetros.
     * Permite crear una instancia de Producto e inicializar todos sus atributos.
     * El parametro id define el identificador único del producto.
     * El parametro nombre define el nombre del producto.
     * El parametro tipo define la categoria o tipo del producto.
     * El parametro precio define el precio del producto.
     */
    public Producto(Long id, String nombre, String tipo, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.precio = precio;
    }

    /*
     * Metodo para obtener el ID del producto.
     * Retorna el id (Long) del producto.
     */
    public Long getId() {
        return id;
    }

    /*
     * Metodo para establecer el ID del producto.
     * El parametro id define el id a establecer.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /*
     * Metodo para obtener el nombre del producto.
     * Retorna el nombre (String) del producto.
     */
    public String getNombre() {
        return nombre;
    }
    /*
     * Metodo para establecer el nombre del producto.
     * El parametro nombre define el nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /*
     * Metodo para obtener el tipo del producto.
     * Retorna el tipo (String) del producto.
     */
    public String getTipo() {
        return tipo;
    }

    /*
     * Metodo para establecer el tipo del producto.
     * El parametro tipo define el tipo a establecer.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /*
     * Metodo para obtener el precio del producto.
     * Retorna el precio (double) del producto.
     */
    public double getPrecio() {
        return precio;
    }

    /*
     * Metodo para establecer el precio del producto.
     * El parametro precio define el precio a establecer.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
