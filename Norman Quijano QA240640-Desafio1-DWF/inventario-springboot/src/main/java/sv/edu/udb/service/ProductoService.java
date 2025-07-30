package sv.edu.udb.service;
import sv.edu.udb.model.Producto;
import sv.edu.udb.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository repository;

    public String registrarProducto(Producto producto) {
        repository.guardar(producto);
        return "Producto registrado correctamente.";
    }

    public List<Producto> listarProductos() {
        return repository.listar();
    }

    public Optional<Producto> buscarProductoPorNombre(String nombre) {
        return repository.buscarPorNombre(nombre);
    }
}
