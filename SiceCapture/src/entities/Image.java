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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
    @NamedQuery(name = "Image.findByIdImage", query = "SELECT i FROM Image i WHERE i.imagePK.idImage = :idImage"),
    @NamedQuery(name = "Image.findByCode", query = "SELECT i FROM Image i WHERE i.code = :code"),
    @NamedQuery(name = "Image.findByName", query = "SELECT i FROM Image i WHERE i.name = :name"),
    @NamedQuery(name = "Image.findByDescription", query = "SELECT i FROM Image i WHERE i.description = :description"),
    @NamedQuery(name = "Image.findByPath", query = "SELECT i FROM Image i WHERE i.path = :path"),
    @NamedQuery(name = "Image.findByFkExpedient", query = "SELECT i FROM Image i WHERE i.imagePK.fkExpedient = :fkExpedient"),
    @NamedQuery(name = "Image.findByFkDocument", query = "SELECT i FROM Image i WHERE i.imagePK.fkDocument = :fkDocument"),
    @NamedQuery(name = "Image.findByFkClient", query = "SELECT i FROM Image i WHERE i.imagePK.fkClient = :fkClient"),
    @NamedQuery(name = "Image.findBySize", query = "SELECT i FROM Image i WHERE i.size = :size"),
    @NamedQuery(name = "Image.findByType", query = "SELECT i FROM Image i WHERE i.type = :type"),
    @NamedQuery(name = "Image.findByLastModification", query = "SELECT i FROM Image i WHERE i.lastModification = :lastModification"),
    @NamedQuery(name = "Image.findByRegistrationData", query = "SELECT i FROM Image i WHERE i.registrationData = :registrationData")})
public class Image implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ImagePK imagePK;
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
    @JoinColumn(name = "fk_client", referencedColumnName = "idUser", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Client client;
    @JoinColumn(name = "fk_document", referencedColumnName = "idDocument", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Document document;
    @JoinColumn(name = "fk_expedient", referencedColumnName = "idExpedient", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Expedient expedient;

    public Image() {
    }

    public Image(ImagePK imagePK) {
        this.imagePK = imagePK;
    }

    public Image(ImagePK imagePK, String code, String name, String description, String path, int size, String type, Date lastModification, Date registrationData) {
        this.imagePK = imagePK;
        this.code = code;
        this.name = name;
        this.description = description;
        this.path = path;
        this.size = size;
        this.type = type;
        this.lastModification = lastModification;
        this.registrationData = registrationData;
    }

    public Image(int idImage, int fkExpedient, int fkDocument, int fkClient) {
        this.imagePK = new ImagePK(idImage, fkExpedient, fkDocument, fkClient);
    }

    public ImagePK getImagePK() {
        return imagePK;
    }

    public void setImagePK(ImagePK imagePK) {
        this.imagePK = imagePK;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Expedient getExpedient() {
        return expedient;
    }

    public void setExpedient(Expedient expedient) {
        this.expedient = expedient;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (imagePK != null ? imagePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Image)) {
            return false;
        }
        Image other = (Image) object;
        if ((this.imagePK == null && other.imagePK != null) || (this.imagePK != null && !this.imagePK.equals(other.imagePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Image[ imagePK=" + imagePK + " ]";
    }
    
}
