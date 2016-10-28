/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author intec
 */
@Entity
@Table(name = "document_parameter")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentParameter.findAll", query = "SELECT d FROM DocumentParameter d"),
    @NamedQuery(name = "DocumentParameter.findByIdParameter", query = "SELECT d FROM DocumentParameter d WHERE d.idParameter = :idParameter"),
    @NamedQuery(name = "DocumentParameter.findByCode", query = "SELECT d FROM DocumentParameter d WHERE d.code = :code"),
    @NamedQuery(name = "DocumentParameter.findByName", query = "SELECT d FROM DocumentParameter d WHERE d.name = :name"),
    @NamedQuery(name = "DocumentParameter.findByDescription", query = "SELECT d FROM DocumentParameter d WHERE d.description = :description"),
    @NamedQuery(name = "DocumentParameter.findByEstate", query = "SELECT d FROM DocumentParameter d WHERE d.estate = :estate"),
    @NamedQuery(name = "DocumentParameter.findByValue", query = "SELECT d FROM DocumentParameter d WHERE d.value = :value")})
public class DocumentParameter implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idParameter")
    private Integer idParameter;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "estate")
    private int estate;
    @Basic(optional = false)
    @Column(name = "value")
    private String value;
    @JoinColumn(name = "fk_document", referencedColumnName = "idDocument")
    @ManyToOne(optional = false)
    private Document fkDocument;
    @JoinColumn(name = "fk_parameter_type", referencedColumnName = "idParameter_type")
    @ManyToOne(optional = false)
    private ParameterType fkParameterType;

    public DocumentParameter() {
    }

    public DocumentParameter(Integer idParameter) {
        this.idParameter = idParameter;
    }

    public DocumentParameter(Integer idParameter, String code, String name, int estate, String value) {
        this.idParameter = idParameter;
        this.code = code;
        this.name = name;
        this.estate = estate;
        this.value = value;
    }

    public Integer getIdParameter() {
        return idParameter;
    }

    public void setIdParameter(Integer idParameter) {
        this.idParameter = idParameter;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Document getFkDocument() {
        return fkDocument;
    }

    public void setFkDocument(Document fkDocument) {
        this.fkDocument = fkDocument;
    }

    public ParameterType getFkParameterType() {
        return fkParameterType;
    }

    public void setFkParameterType(ParameterType fkParameterType) {
        this.fkParameterType = fkParameterType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idParameter != null ? idParameter.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentParameter)) {
            return false;
        }
        DocumentParameter other = (DocumentParameter) object;
        if ((this.idParameter == null && other.idParameter != null) || (this.idParameter != null && !this.idParameter.equals(other.idParameter))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.DocumentParameter[ idParameter=" + idParameter + " ]";
    }
    
}
