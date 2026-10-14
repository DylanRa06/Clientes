package com.krakedev.clientes;
import com.krakedev.clientes.services.ServicioCliente;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.krakedev.clientes.entidades.Cliente;

class ServicioClienteTest {

    private ServicioCliente servicio;

    @BeforeEach
    void preparar() {
        servicio = new ServicioCliente();
    }

    private Cliente crearCliente(String cedula, String nombre,
            String apellido) {

        Cliente cliente = new Cliente();
        cliente.setCedula(cedula);
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setEmail(nombre.toLowerCase() + "@example.com");

        return cliente;
    }

    @Test
    void crearClienteNuevo() {
        Cliente cliente = crearCliente("001", "Dylan", "Ramon");

        Cliente resultado = servicio.crear(cliente);

        assertSame(cliente, resultado);
        assertEquals("dylan@example.com", resultado.getEmail());
        assertEquals(1, servicio.listar().size());
        assertSame(cliente, servicio.buscarPorCedula("001"));
    }

    @Test
    void rechazarCedulaRepetida() {
        Cliente original = crearCliente("001", "Dylan", "Ramon");
        Cliente repetido = crearCliente("001", "Ana", "Perez");

        servicio.crear(original);
        Cliente resultado = servicio.crear(repetido);

        assertNull(resultado);
        assertEquals(1, servicio.listar().size());
        assertSame(original, servicio.buscarPorCedula("001"));
        assertEquals("dylan@example.com", original.getEmail());
    }

    @Test
    void buscarClienteExistente() {
        Cliente primero = crearCliente("001", "Dylan", "Ramon");
        Cliente segundo = crearCliente("002", "Ana", "Perez");

        servicio.crear(primero);
        servicio.crear(segundo);

        assertSame(segundo, servicio.buscarPorCedula("002"));
        assertEquals("ana@example.com", segundo.getEmail());
    }

    @Test
    void buscarClienteInexistente() {
        servicio.crear(crearCliente("001", "Dylan", "Ramon"));

        assertNull(servicio.buscarPorCedula("999"));
    }

    @Test
    void listarSinClientes() {
        assertTrue(servicio.listar().isEmpty());
    }

    @Test
    void listarClientesRegistrados() {
        Cliente primero = crearCliente("001", "Dylan", "Ramon");
        Cliente segundo = crearCliente("002", "Ana", "Perez");

        servicio.crear(primero);
        servicio.crear(segundo);

        assertEquals(2, servicio.listar().size());
        assertSame(primero, servicio.listar().get(0));
        assertSame(segundo, servicio.listar().get(1));
        assertEquals("dylan@example.com", servicio.listar().get(0).getEmail());
        assertEquals("ana@example.com", servicio.listar().get(1).getEmail());
    }

    @Test
    void actualizarClienteExistente() {
        Cliente original = crearCliente("001", "Dylan", "Ramon");
        Cliente otro = crearCliente("002", "Juan", "Lopez");
        Cliente cambios = crearCliente("999", "David", "Perez");

        servicio.crear(original);
        servicio.crear(otro);

        Cliente resultado = servicio.actualizar("001", cambios);

        assertSame(original, resultado);
        assertEquals("David", resultado.getNombre());
        assertEquals("Perez", resultado.getApellido());
        assertEquals("david@example.com", resultado.getEmail());

        // Actualizar modifica nombre, apellido y email, conserva la cedula.
        assertEquals("001", resultado.getCedula());
        assertSame(resultado, servicio.buscarPorCedula("001"));
        assertNull(servicio.buscarPorCedula("999"));
        assertEquals(2, servicio.listar().size());

        // El otro cliente conserva sus datos.
        assertEquals("Juan", otro.getNombre());
        assertEquals("Lopez", otro.getApellido());
        assertEquals("juan@example.com", otro.getEmail());
    }

    @Test
    void actualizarClienteInexistente() {
        Cliente original = crearCliente("001", "Dylan", "Ramon");
        servicio.crear(original);

        Cliente resultado = servicio.actualizar(
                "999", crearCliente("999", "Ana", "Perez"));

        assertNull(resultado);
        assertEquals(1, servicio.listar().size());
        assertEquals("Dylan", original.getNombre());
        assertEquals("Ramon", original.getApellido());
        assertEquals("dylan@example.com", original.getEmail());
    }

    @Test
    void eliminarClienteExistente() {
        Cliente primero = crearCliente("001", "Dylan", "Ramon");
        Cliente segundo = crearCliente("002", "Ana", "Perez");

        servicio.crear(primero);
        servicio.crear(segundo);

        assertTrue(servicio.eliminar("001"));
        assertNull(servicio.buscarPorCedula("001"));
        assertEquals(1, servicio.listar().size());
        assertSame(segundo, servicio.buscarPorCedula("002"));
        assertEquals("ana@example.com", segundo.getEmail());

        // Ya eliminado, un segundo intento devuelve false.
        assertFalse(servicio.eliminar("001"));
    }

    @Test
    void eliminarClienteInexistente() {
        Cliente original = crearCliente("001", "Dylan", "Ramon");
        servicio.crear(original);

        assertFalse(servicio.eliminar("999"));
        assertEquals(1, servicio.listar().size());
        assertSame(original, servicio.buscarPorCedula("001"));
        assertEquals("dylan@example.com", original.getEmail());
    }
    @Test
    void construirClienteConEmail() {
        Cliente cliente = new Cliente("003", "Paul", "Castillo", "paul@example.com");
        assertEquals("003", cliente.getCedula());
        assertEquals("Paul", cliente.getNombre());
        assertEquals("Castillo", cliente.getApellido());
        assertEquals("paul@example.com", cliente.getEmail());
        assertTrue(cliente.toString().contains("paul@example.com"));
    }
}
