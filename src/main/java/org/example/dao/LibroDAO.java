package org.example.dao;

import org.example.models.Libro;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

/**
 * DAO para la gestión de Libros.
 * Incluye consultas HQL avnzadas con filtros y agregaciones.
 */
public class LibroDAO {

    public void crear(Libro libro) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(libro);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al crear libro: " + e.getMessage());
        }
    }

    /**
     * Busca libros por título usando parámetros nombrados (HQL 2 - LIKE).
     */
    public List<Libro> buscarPorTitulo(String texto) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Libro l WHERE l.titulo LIKE :busqueda";
            Query<Libro> query = session.createQuery(hql, Libro.class);
            query.setParameter("busqueda", "%" + texto + "%");
            return query.list();
        }
    }

    /**
     * Obtiene el promedio de precio de los libros (HQL 3 - Agregación).
     */
    public Double obtenerPrecioPromedio() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT AVG(l.precio) FROM Libro l";
            return session.createQuery(hql, Double.class).uniqueResult();
        }
    }

    /**
     * Filtra libros que superan un precio específico (HQL 4 - Filtro).
     */
    public List<Libro> filtrarPorPrecioMinimo(Double precioMin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Libro l WHERE l.precio > :precio";
            return session.createQuery(hql, Libro.class)
                    .setParameter("precio", precioMin)
                    .list();
        }
    }

    public List<Libro> obtenerTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Libro", Libro.class).list();
        }
    }
}