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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
@Table(name = "document")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Document.findAll", query = "SELECT d FROM Document d"),
    @NamedQuery(name = "Document.findByIdDocument", query = "SELECT d FROM Document d WHERE d.idDocument = :idDocument"),
    @NamedQuery(name = "Document.findByName", query = "SELECT d FROM Document d WHERE UPPER(d.name) = :name"),
    @NamedQuery(name = "Document.findByDescription", query = "SELECT d FROM Document d WHERE d.description = :description")})
public class Document implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDocument")
    private Integer idDocument;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "maxImageSize")
    private int maxImageSize;
    @Basic(optional = false)
    @Column(name = "canRepeat")
    private int canRepeat;
    @Basic(optional = false)
    @Column(name = "isRequired")
    private int isRequired;
    @Basic(optional = false)
    @Column(name = "canExpire")
    private int canExpire;
    @JoinTable(name = "document_expedient", joinColumns = {
        @JoinColumn(name = "fk_document", referencedColumnName = "idDocument")}, inverseJoinColumns = {
        @JoinColumn(name = "fk_expedient", referencedColumnName = "idExpedient")})
    @ManyToMany
    private Collection<Expedient> expedientCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkDocument")
    private Collection<DocumentParameter> documentParameterCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkDocument")
    private Collection<DocumentData> documentDataCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "document")
    private Collection<Image> imageCollection;

    public Document() {
    }

    public Document(Integer idDocument) {
        this.idDocument = idDocument;
    }

    public Document(Integer idDocument, String name, int maxImageSize, int canRepeat, int isRequired, int canExpire) {
        this.idDocument = idDocument;
        this.name = name;
        this.maxImageSize = maxImageSize;
        this.canRepeat = canRepeat;
        this.isRequired = isRequired;
        this.canExpire = canExpire;
    }

  public Integer getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(Integer idDocument) {
        this.idDocument = idDocument;
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

    public int getMaxImageSize() {
        return maxImageSize;
    }

    public void setMaxImageSize(int maxImageSize) {
        this.maxImageSize = maxImageSize;
    }

    public int getCanRepeat() {
        return canRepeat;
    }

    public void setCanRepeat(int canRepeat) {
        this.canRepeat = canRepeat;
    }

    public int getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(int isRequired) {
        this.isRequired = isRequired;
    }

    public int getCanExpire() {
        return canExpire;
    }

    public void setCanExpire(int canExpire) {
        this.canExpire = canExpire;
    }

    @XmlTransient
    public Collection<Expedient> getExpedientCollection() {
        return expedientCollection;
    }

    public void setExpedientCollection(Collection<Expedient> expedientCollection) {
        this.expedientCollection = expedientCollection;
    }

    @XmlTransient
    public Collection<DocumentParameter> getDocumentParameterCollection() {
        return documentParameterCollection;
    }

    public void setDocumentParameterCollection(Collection<DocumentParameter> documentParameterCollection) {
        this.documentParameterCollection = documentParameterCollection;
    }

    @XmlTransient
    public Collection<DocumentData> getDocumentDataCollection() {
        return documentDataCollection;
    }

    public void setDocumentDataCollection(Collection<DocumentData> documentDataCollection) {
        this.documentDataCollection = documentDataCollection;
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
        hash += (idDocument != null ? idDocument.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Document)) {
            return false;
        }
        Document other = (Document) object;
        if ((this.idDocument == null && other.idDocument != null) || (this.idDocument != null && !this.idDocument.equals(other.idDocument))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
