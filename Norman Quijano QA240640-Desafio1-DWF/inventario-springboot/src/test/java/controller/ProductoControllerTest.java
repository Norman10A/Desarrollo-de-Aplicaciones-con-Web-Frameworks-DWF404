package controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sv.edu.udb.model.Producto;
import sv.edu.udb.service.ProductoService;
import sv.edu.udb.controller.ProductoController;

import java.util.Arrays;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService service;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testRegistrarProducto() throws Exception {
        Producto producto = new Producto("Mouse", 10.5, 20);
        when(service.registrarProducto(producto)).thenReturn("Producto registrado correctamente.");

        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Producto registrado correctamente."));
    }

    @Test
    public void testListarProductos() throws Exception {
        Producto producto = new Producto("Mouse", 10.5, 20);
        when(service.listarProductos()).thenReturn(Arrays.asList(producto));

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Mouse"));
    }

    @Test
    public void testBuscarProductoExistente() throws Exception {
        Producto producto = new Producto("Teclado", 25.0, 15);
        when(service.buscarProductoPorNombre("Teclado")).thenReturn(Optional.of(producto));

        mockMvc.perform(get("/productos/Teclado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Teclado"));
    }

    @Test
    public void testBuscarProductoNoExistente() throws Exception {
        when(service.buscarProductoPorNombre("Monitor")).thenReturn(Optional.empty());

        mockMvc.perform(get("/productos/Monitor"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Producto no encontrado"));
    }
}
