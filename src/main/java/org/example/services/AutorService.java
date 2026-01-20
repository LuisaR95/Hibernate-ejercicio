package org.example.services;

import org.example.dao.AutorDAO;
import org.example.models.Autor;
import java.util.List;

/**
 * Esta clase SÍ va en la carpeta 'services'.
 * Contiene la lógica de negocio y valida los datos antes de persistir.
 */
public class AutorService {
    private final AutorDAO autorDAO;

    public AutorService() {
        this.autorDAO = new AutorDAO();
    }

    /**
     * Obtiene todos los autores llamando al DAO.
     */
    public List<Autor> listarAutores() {
        return autorDAO.obtenerTodos();
    }

    /**
     * Busca un autor por su ID con validación básica.
     */
    public Autor buscarAutor(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID proporcionado no es válido.");
        }
        return autorDAO.obtenerPorId(id);
    }

    /**
     * Crea un autor asegurándose de que el nombre no sea nulo
     */
    public void registrarAutor(Autor autor) {
        if (autor.getNombre() == null || autor.getNombre().trim().isEmpty()) {
            throw new RuntimeException("No se puede registrar un autor sin nombre.");
        }
        autorDAO.crear(autor);
    }
}