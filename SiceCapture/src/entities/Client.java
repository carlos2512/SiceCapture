/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author intec
 */
@Entity
@Table(name = "client")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Client.findAll", query = "SELECT c FROM Client c"),
    @NamedQuery(name = "Client.findByIdUser", query = "SELECT c FROM Client c WHERE c.idUser = :idUser"),
    @NamedQuery(name = "Client.findByName", query = "SELECT c FROM Client c WHERE c.name = :name"),
    @NamedQuery(name = "Client.findByIdentification", query = "SELECT c FROM Client c WHERE c.identification = :identification"),
    @NamedQuery(name = "Client.findByIdentificationType", query = "SELECT c FROM Client c WHERE c.identificationType = :identificationType"),
    @NamedQuery(name = "Client.findByCountry", query = "SELECT c FROM Client c WHERE c.country = :country")})
public class Client implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idUser")
    private Integer idUser;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "identification")
    private Integer identification;
    @Basic(optional = false)
    @Column(name = "identification_type")
    private String identificationType;
    @Basic(optional = false)
    @Column(name = "country")
    private String country;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client")
    private Collection<ExpedientClient> expedientClientCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkClient")
    private Collection<Image> imageCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client")
    private Collection<ClientData> clientDataCollection;

    public Client() {
    }

    public Client(Integer idUser) {
        this.idUser = idUser;
    }

    public Client(Integer idUser, String name, Integer identification, String identificationType, String country) {
        this.idUser = idUser;
        this.name = name;
        this.identification = identification;
        this.identificationType = identificationType;
        this.country = country;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIdentification() {
        return identification;
    }

    public void setIdentification(Integer identification) {
        this.identification = identification;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @XmlTransient
    public Collection<ExpedientClient> getExpedientClientCollection() {
        return expedientClientCollection;
    }

    public void setExpedientClientCollection(Collection<ExpedientClient> expedientClientCollection) {
        this.expedientClientCollection = expedientClientCollection;
    }

    @XmlTransient
    public Collection<Image> getImageCollection() {
        return imageCollection;
    }

    public void setImageCollection(Collection<Image> imageCollection) {
        this.imageCollection = imageCollection;
    }

    @XmlTransient
    public Collection<ClientData> getClientDataCollection() {
        return clientDataCollection;
    }

    public void setClientDataCollection(Collection<ClientData> clientDataCollection) {
        this.clientDataCollection = clientDataCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUser != null ? idUser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Client)) {
            return false;
        }
        Client other = (Client) object;
        if ((this.idUser == null && other.idUser != null) || (this.idUser != null && !this.idUser.equals(other.idUser))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Client[ idUser=" + idUser + " ]";
    }
    
}
