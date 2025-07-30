package sv.edu.udb.repository;

import sv.edu.udb.model.Producto;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProductoRepository {

    private final Map<String, Producto> productos = new HashMap<>();


    public ProductoRepository() {
        guardar(new Producto("Mouse", 10.5, 20));
        guardar(new Producto("Teclado", 25.0, 15));
        guardar(new Producto("Monitor", 99.9, 5));

    }

    public void guardar(Producto producto) {
        productos.put(producto.getNombre().toLowerCase(), producto);
    }

    public List<Producto> listar() {
        return new ArrayList<>(productos.values());
    }

    public Optional<Producto> buscarPorNombre(String nombre) {
        return Optional.ofNullable(productos.get(nombre.toLowerCase()));
    }
}
