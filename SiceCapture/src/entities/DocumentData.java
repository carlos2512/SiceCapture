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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "document_data")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentData.findAll", query = "SELECT d FROM DocumentData d"),
    @NamedQuery(name = "DocumentData.findByIdDocumentdata", query = "SELECT d FROM DocumentData d WHERE d.idDocumentdata = :idDocumentdata"),
    @NamedQuery(name = "DocumentData.findByCode", query = "SELECT d FROM DocumentData d WHERE d.code = :code"),
    @NamedQuery(name = "DocumentData.findByName", query = "SELECT d FROM DocumentData d WHERE d.name = :name"),
    @NamedQuery(name = "DocumentData.findByDescription", query = "SELECT d FROM DocumentData d WHERE d.description = :description"),
    @NamedQuery(name = "DocumentData.findByRequired", query = "SELECT d FROM DocumentData d WHERE d.required = :required")})
public class DocumentData implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idDocument_data")
    private Integer idDocumentdata;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "required")
    private int required;
    @JoinColumn(name = "fk_document", referencedColumnName = "idDocument")
    @ManyToOne(optional = false)
    private Document fkDocument;
    @JoinColumn(name = "fk_data_type", referencedColumnName = "idData_type")
    @ManyToOne(optional = false)
    private DataType fkDataType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documentData")
    private Collection<ClientData> clientDataCollection;

    public DocumentData() {
    }

    public DocumentData(Integer idDocumentdata) {
        this.idDocumentdata = idDocumentdata;
    }

    public DocumentData(Integer idDocumentdata, String code, int required) {
        this.idDocumentdata = idDocumentdata;
        this.code = code;
        this.required = required;
    }

    public Integer getIdDocumentdata() {
        return idDocumentdata;
    }

    public void setIdDocumentdata(Integer idDocumentdata) {
        this.idDocumentdata = idDocumentdata;
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

    public int getRequired() {
        return required;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    public Document getFkDocument() {
        return fkDocument;
    }

    public void setFkDocument(Document fkDocument) {
        this.fkDocument = fkDocument;
    }

    public DataType getFkDataType() {
        return fkDataType;
    }

    public void setFkDataType(DataType fkDataType) {
        this.fkDataType = fkDataType;
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
        hash += (idDocumentdata != null ? idDocumentdata.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentData)) {
            return false;
        }
        DocumentData other = (DocumentData) object;
        if ((this.idDocumentdata == null && other.idDocumentdata != null) || (this.idDocumentdata != null && !this.idDocumentdata.equals(other.idDocumentdata))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.DocumentData[ idDocumentdata=" + idDocumentdata + " ]";
    }
    
}
