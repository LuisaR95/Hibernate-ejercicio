import jakarta.persistence.*;

@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private double total;

    public Pedido() {}
    public Pedido(Usuario usuario, double total) {
        this.usuario = usuario;
        this.total = total;
    }

    public int getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public double getTotal() { return total; }
}