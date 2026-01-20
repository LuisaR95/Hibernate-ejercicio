package org.example;

import org.example.models.Libro; // Asegúrate de importar tu modelo
import org.example.seeders.DatabaseSeeder;
import org.example.services.AutorService;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // 1. Poblado automático de la base de datos
            DatabaseSeeder.ejecutar();

            // 2. Uso del Servicio de Autores
            AutorService autorService = new AutorService();
            System.out.println("\n=== LISTADO DE AUTORES ===");
            autorService.listarAutores().forEach(a ->
                    System.out.println("Autor: " + a.getNombre())
            );

            // ---------------------------------------------------------
            // 3. CONSULTAS HQL AVANZADAS (Requisito del ejercicio)
            // --------------------------------------------------------
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {

                System.out.println("\n=== EJECUTANDO CONSULTAS HQL AVANZADAS ===");

                // CONSULTA 1: Filtro (WHERE) con parámetro nombrado
                String hql1 = "FROM Libro l WHERE l.precio > :precioMinimo";
                Query<Libro> query1 = session.createQuery(hql1, Libro.class);
                query1.setParameter("precioMinimo", 20.0);
                List<Libro> librosCaros = query1.getResultList();

                System.out.println("1. Libros con precio mayor a 20€:");
                librosCaros.forEach(l -> System.out.println("   - " + l.getTitulo() + " (" + l.getPrecio() + "€)"));

                // CONSULTA 2: Agregación (COUNT) - Cuántos libros hay
                String hql2 = "SELECT COUNT(l) FROM Libro l";
                Long totalLibros = session.createQuery(hql2, Long.class).getSingleResult();
                System.out.println("\n2. Total de libros en la base de datos: " + totalLibros);

                // CONSULTA 3: Búsqueda parcial (LIKE) y Ordenamiento (ORDER BY)
                String hql3 = "FROM Libro l WHERE l.titulo LIKE :patron ORDER BY l.titulo ASC";
                List<Libro> librosFiltrados = session.createQuery(hql3, Libro.class)
                        .setParameter("patron", "%de%") // Buscará "de la Mancha", "Soledad", etc.
                        .getResultList();

                System.out.println("\n3. Libros que contienen 'de' (Ordenados A-Z):");
                if (librosFiltrados.isEmpty()) {
                    System.out.println("   No se encontraron libros con ese patrón.");
                } else {
                    librosFiltrados.forEach(l -> System.out.println("   - " + l.getTitulo()));
                }
            }
            // ---------------------------------------------------------

        } catch (Exception e) {
            System.err.println("Error crítico en la aplicación: " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
            System.out.println("\n[SISTEMA] Sesión cerrada correctamente.");
        }
    }
}