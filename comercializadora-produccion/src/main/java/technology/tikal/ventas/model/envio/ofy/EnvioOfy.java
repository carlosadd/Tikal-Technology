/**
 *   Copyright 2015 Tikal-Technology
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */
package technology.tikal.ventas.model.envio.ofy;

import java.util.Date;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import technology.tikal.ventas.model.OfyEntity;
import technology.tikal.ventas.model.envio.Envio;
import technology.tikal.ventas.model.pedido.Pedido;

@JsonTypeName("EnvioOfy")
@Entity
@Cache
public class EnvioOfy implements Envio, OfyEntity<Envio> {

    @Parent @JsonIgnore
    private Ref<Pedido> owner;
    @Id
    private Long id;
    private String name;
    @Index
    private String status;
    @Index
    private Long idTransportista;
    @Index
    private Date fechaSalida;
    @Index
    private Date fechaEntrega;
    @Length(max=360)
    @Pattern(regexp="^[\\w\\p{IsLatin}\\p{Punct}]+( [\\w\\p{IsLatin}\\p{Punct}]+)*")
    private String nota;
    
    protected EnvioOfy() {
        super();
    }
    
    public EnvioOfy(EnvioOfy base) {
        this();
        if (base.owner == null) {
            throw new NullPointerException();
        }
        this.owner = base.owner;
    }
    
    public EnvioOfy(Pedido owner) {
        this();
        if (owner == null) {
            throw new NullPointerException();
        }
        if (owner.getId() == null) {
            throw new IllegalArgumentException();
        }
        this.owner = Ref.create(owner);
    }
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Long getPedidoId() {
        return owner.getKey().getId();
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getIdTransportista() {
        return idTransportista;
    }

    public void setIdTransportista(Long idTransportista) {
        this.idTransportista = idTransportista;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    @Override
    public void update(Envio source) {
        BeanUtils.copyProperties(source, this, "owner");
    }    
}
