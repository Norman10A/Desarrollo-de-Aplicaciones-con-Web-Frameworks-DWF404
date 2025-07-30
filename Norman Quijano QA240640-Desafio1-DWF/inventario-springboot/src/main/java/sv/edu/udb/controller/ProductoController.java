package sv.edu.udb.controller;
import sv.edu.udb.model.Producto;
import sv.edu.udb.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")

public class ProductoController {
    @Autowired
    private ProductoService service;

    @PostMapping
    public ResponseEntity<String> registrarProducto(@RequestBody Producto producto) {
        String mensaje = service.registrarProducto(producto);
        return ResponseEntity.ok(mensaje);
    }

    @GetMapping
    public List<Producto> listarProductos() {
        return service.listarProductos();
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<?> buscarProducto(@PathVariable String nombre) {
        return service.buscarProductoPorNombre(nombre)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("Producto no encontrado"));
    }
}
