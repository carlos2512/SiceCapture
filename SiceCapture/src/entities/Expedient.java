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
import javax.persistence.ManyToMany;
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
@Table(name = "expedient")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Expedient.findAll", query = "SELECT e FROM Expedient e"),
    @NamedQuery(name = "Expedient.findByIdExpedient", query = "SELECT e FROM Expedient e WHERE e.idExpedient = :idExpedient"),
    @NamedQuery(name = "Expedient.findByName", query = "SELECT e FROM Expedient e WHERE e.name = :name and e.estate = 1"),
    @NamedQuery(name = "Expedient.findByDescription", query = "SELECT e FROM Expedient e WHERE e.description = :description"),
    @NamedQuery(name = "Expedient.findByEstate", query = "SELECT e FROM Expedient e WHERE e.estate = :estate")})
public class Expedient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idExpedient")
    private Integer idExpedient;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "estate")
    private int estate;
    @ManyToMany(mappedBy = "expedientCollection")
    private Collection<Document> documentCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expedient")
    private Collection<ExpedientClient> expedientClientCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkExpedient")
    private Collection<Image> imageCollection;

    public Expedient() {
    }

    public Expedient(Integer idExpedient) {
        this.idExpedient = idExpedient;
    }

    public Expedient(Integer idExpedient, String name, String description, int estate) {
        this.idExpedient = idExpedient;
        this.name = name;
        this.description = description;
        this.estate = estate;
    }

    public Integer getIdExpedient() {
        return idExpedient;
    }

    public void setIdExpedient(Integer idExpedient) {
        this.idExpedient = idExpedient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEstate() {
        return estate;
    }

    public void setEstate(int estate) {
        this.estate = estate;
    }

    @XmlTransient
    public Collection<Document> getDocumentCollection() {
        return documentCollection;
    }

    public void setDocumentCollection(Collection<Document> documentCollection) {
        this.documentCollection = documentCollection;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idExpedient != null ? idExpedient.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Expedient)) {
            return false;
        }
        Expedient other = (Expedient) object;
        if ((this.idExpedient == null && other.idExpedient != null) || (this.idExpedient != null && !this.idExpedient.equals(other.idExpedient))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Expedient[ idExpedient=" + idExpedient + " ]";
    }

}
