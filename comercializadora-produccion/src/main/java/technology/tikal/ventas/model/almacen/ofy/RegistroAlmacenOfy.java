package technology.tikal.ventas.model.almacen.ofy;

import java.util.Date;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import technology.tikal.ventas.model.almacen.RegistroAlmacen;
import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.producto.Producto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

public class RegistroAlmacenOfy implements RegistroAlmacen {

    @Parent @JsonIgnore
    private Ref<Pedido> owner;
    @Id
    private Long id;
    @Index
    private Long idProveedor;
    @Index
    private Ref<Producto> producto;
    private Long cantidad;
    private Date fechaDeCreacion;
    @Index
    private Date fechaRegistro;
    @Length(max=360)
    @Pattern(regexp="^[\\w\\p{IsLatin}\\p{Punct}]+( [\\w\\p{IsLatin}\\p{Punct}]+)*")
    private String descripcion;
    @Length(max=40)
    @Pattern(regexp="^[\\w\\p{IsLatin}\\p{Punct}]+( [\\w\\p{IsLatin}\\p{Punct}]+)*")
    private String tag;
    
    protected RegistroAlmacenOfy() {
        this.fechaDeCreacion = new Date();
        this.fechaRegistro = new Date();
        this.descripcion = "";
        this.tag = "entrada";
    }
    
    public RegistroAlmacenOfy(RegistroAlmacenOfy base) {
        this();
        if (base.owner == null) {
            throw new NullPointerException();
        }
        this.owner = base.owner;
        if (base.producto == null) {
            throw new NullPointerException();
        }
        this.producto = base.producto;
        this.idProveedor = base.idProveedor;
    }
    
    public RegistroAlmacenOfy(Pedido owner, Producto producto, Long idProveedor) {
        this();
        if (owner == null) {
            throw new NullPointerException();
        }
        if (owner.getId() == null) {
            throw new IllegalArgumentException();
        }
        this.owner = Ref.create(owner);
        if (producto == null) {
            throw new NullPointerException();
        }
        if (producto.getId() == null) {
            throw new IllegalArgumentException();
        }
        this.producto = Ref.create(producto);
        if (idProveedor == null) {
            throw new NullPointerException();
        }
        this.idProveedor = idProveedor;
    }
    
    @Override
    public Long getPedidoId() {
        return this.owner.getKey().getId();
    }   
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    @Override
    public Producto getProducto() {
        return producto.get();
    }
    
    @Override
    public Long getIdProveedor() {
        return idProveedor;
    }
    
    @Override
    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    @Override
    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
