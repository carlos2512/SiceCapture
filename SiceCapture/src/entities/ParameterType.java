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
@Table(name = "parameter_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParameterType.findAll", query = "SELECT p FROM ParameterType p"),
    @NamedQuery(name = "ParameterType.findByIdParametertype", query = "SELECT p FROM ParameterType p WHERE p.idParametertype = :idParametertype"),
    @NamedQuery(name = "ParameterType.findByCode", query = "SELECT p FROM ParameterType p WHERE p.code = :code"),
    @NamedQuery(name = "ParameterType.findByName", query = "SELECT p FROM ParameterType p WHERE p.name = :name"),
    @NamedQuery(name = "ParameterType.findByDescription", query = "SELECT p FROM ParameterType p WHERE p.description = :description"),
    @NamedQuery(name = "ParameterType.findByEstate", query = "SELECT p FROM ParameterType p WHERE p.estate = :estate")})
public class ParameterType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idParameter_type")
    private Integer idParametertype;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "estate")
    private int estate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkParameterType")
    private Collection<DocumentParameter> documentParameterCollection;

    public ParameterType() {
    }

    public ParameterType(Integer idParametertype) {
        this.idParametertype = idParametertype;
    }

    public ParameterType(Integer idParametertype, String code, String name, String description, int estate) {
        this.idParametertype = idParametertype;
        this.code = code;
        this.name = name;
        this.description = description;
        this.estate = estate;
    }

    public Integer getIdParametertype() {
        return idParametertype;
    }

    public void setIdParametertype(Integer idParametertype) {
        this.idParametertype = idParametertype;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
    public Collection<DocumentParameter> getDocumentParameterCollection() {
        return documentParameterCollection;
    }

    public void setDocumentParameterCollection(Collection<DocumentParameter> documentParameterCollection) {
        this.documentParameterCollection = documentParameterCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idParametertype != null ? idParametertype.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParameterType)) {
            return false;
        }
        ParameterType other = (ParameterType) object;
        if ((this.idParametertype == null && other.idParametertype != null) || (this.idParametertype != null && !this.idParametertype.equals(other.idParametertype))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ParameterType[ idParametertype=" + idParametertype + " ]";
    }
    
}
