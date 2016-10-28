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
@Table(name = "data_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DataType.findAll", query = "SELECT d FROM DataType d"),
    @NamedQuery(name = "DataType.findByIdDatatype", query = "SELECT d FROM DataType d WHERE d.idDatatype = :idDatatype"),
    @NamedQuery(name = "DataType.findByCode", query = "SELECT d FROM DataType d WHERE d.code = :code"),
    @NamedQuery(name = "DataType.findByName", query = "SELECT d FROM DataType d WHERE d.name = :name"),
    @NamedQuery(name = "DataType.findByDescription", query = "SELECT d FROM DataType d WHERE d.description = :description"),
    @NamedQuery(name = "DataType.findByEstate", query = "SELECT d FROM DataType d WHERE d.estate = :estate")})
public class DataType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idData_type")
    private Integer idDatatype;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "estate")
    private int estate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkDataType")
    private Collection<DocumentData> documentDataCollection;

    public DataType() {
    }

    public DataType(Integer idDatatype) {
        this.idDatatype = idDatatype;
    }

    public DataType(Integer idDatatype, String code, int estate) {
        this.idDatatype = idDatatype;
        this.code = code;
        this.estate = estate;
    }

    public Integer getIdDatatype() {
        return idDatatype;
    }

    public void setIdDatatype(Integer idDatatype) {
        this.idDatatype = idDatatype;
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
    public Collection<DocumentData> getDocumentDataCollection() {
        return documentDataCollection;
    }

    public void setDocumentDataCollection(Collection<DocumentData> documentDataCollection) {
        this.documentDataCollection = documentDataCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDatatype != null ? idDatatype.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DataType)) {
            return false;
        }
        DataType other = (DataType) object;
        if ((this.idDatatype == null && other.idDatatype != null) || (this.idDatatype != null && !this.idDatatype.equals(other.idDatatype))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.DataType[ idDatatype=" + idDatatype + " ]";
    }
    
}
