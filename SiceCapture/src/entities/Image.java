/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author intec
 */
@Entity
@Table(name = "image")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Image.findAll", query = "SELECT i FROM Image i"),
    @NamedQuery(name = "Image.findByIdImage", query = "SELECT i FROM Image i WHERE i.idImage = :idImage"),
    @NamedQuery(name = "Image.findByName", query = "SELECT i FROM Image i WHERE i.name = :name"),
    @NamedQuery(name = "Image.findByDescription", query = "SELECT i FROM Image i WHERE i.description = :description"),
    @NamedQuery(name = "Image.findByPath", query = "SELECT i FROM Image i WHERE i.path = :path"),
    @NamedQuery(name = "Image.findBySize", query = "SELECT i FROM Image i WHERE i.size = :size"),
    @NamedQuery(name = "Image.findByExpedientDocumentClient", query = "SELECT i FROM Image i WHERE i.fkExpedient = :fkExpedient AND i.fkDocument = :fkDocument AND i.fkClient = :fkClient"),
    @NamedQuery(name = "Image.findByType", query = "SELECT i FROM Image i WHERE i.type = :type"),
    @NamedQuery(name = "Image.findByLastModification", query = "SELECT i FROM Image i WHERE i.lastModification = :lastModification"),
    @NamedQuery(name = "Image.findByRegistrationData", query = "SELECT i FROM Image i WHERE i.registrationData = :registrationData")})
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idImage")
    private Integer idImage;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "path")
    private String path;
    @Basic(optional = false)
    @Column(name = "size")
    private int size;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @Column(name = "last_modification")
    @Temporal(TemporalType.DATE)
    private Date lastModification;
    @Basic(optional = false)
    @Column(name = "registration_data")
    @Temporal(TemporalType.DATE)
    private Date registrationData;
    @JoinColumn(name = "fk_expedient", referencedColumnName = "idExpedient")
    @ManyToOne(optional = false)
    private Expedient fkExpedient;
    @JoinColumn(name = "fk_document", referencedColumnName = "idDocument")
    @ManyToOne(optional = false)
    private Document fkDocument;
    @JoinColumn(name = "fk_client", referencedColumnName = "idUser")
    @ManyToOne(optional = false)
    private Client fkClient;

    public Image() {
    }

    public Image(Integer idImage) {
        this.idImage = idImage;
    }

    public Image(Integer idImage, String name, String description, String path, int size, String type, Date lastModification, Date registrationData) {
        this.idImage = idImage;
        this.name = name;
        this.description = description;
        this.path = path;
        this.size = size;
        this.type = type;
        this.lastModification = lastModification;
        this.registrationData = registrationData;
    }

    public Integer getIdImage() {
        return idImage;
    }

    public void setIdImage(Integer idImage) {
        this.idImage = idImage;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getLastModification() {
        return lastModification;
    }

    public void setLastModification(Date lastModification) {
        this.lastModification = lastModification;
    }

    public Date getRegistrationData() {
        return registrationData;
    }

    public void setRegistrationData(Date registrationData) {
        this.registrationData = registrationData;
    }

    public Expedient getFkExpedient() {
        return fkExpedient;
    }

    public void setFkExpedient(Expedient fkExpedient) {
        this.fkExpedient = fkExpedient;
    }

    public Document getFkDocument() {
        return fkDocument;
    }

    public void setFkDocument(Document fkDocument) {
        this.fkDocument = fkDocument;
    }

    public Client getFkClient() {
        return fkClient;
    }

    public void setFkClient(Client fkClient) {
        this.fkClient = fkClient;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idImage != null ? idImage.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Image)) {
            return false;
        }
        Image other = (Image) object;
        if ((this.idImage == null && other.idImage != null) || (this.idImage != null && !this.idImage.equals(other.idImage))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sds.Image[ idImage=" + idImage + " ]";
    }

}
