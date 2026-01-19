import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pedido> pedidos = new ArrayList<>();

    public Usuario() {}
    public Usuario(String email, String nombre) {
        this.email = email;
        this.nombre = nombre;
    }

    /**
     * Agrega un pedido manteniendo ambos lados sincronizados.
     */
    public void agregarPedido(Pedido pedido) {
        pedidos.add(pedido);
        pedido.setUsuario(this);
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public List<Pedido> getPedidos() { return pedidos; }
}