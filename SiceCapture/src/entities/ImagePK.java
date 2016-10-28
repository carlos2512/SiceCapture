/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author intec
 */
@Embeddable
public class ImagePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idImage")
    private int idImage;
    @Basic(optional = false)
    @Column(name = "fk_expedient")
    private int fkExpedient;
    @Basic(optional = false)
    @Column(name = "fk_document")
    private int fkDocument;
    @Basic(optional = false)
    @Column(name = "fk_client")
    private int fkClient;

    public ImagePK() {
    }

    public ImagePK(int idImage, int fkExpedient, int fkDocument, int fkClient) {
        this.idImage = idImage;
        this.fkExpedient = fkExpedient;
        this.fkDocument = fkDocument;
        this.fkClient = fkClient;
    }

    public int getIdImage() {
        return idImage;
    }

    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }

    public int getFkExpedient() {
        return fkExpedient;
    }

    public void setFkExpedient(int fkExpedient) {
        this.fkExpedient = fkExpedient;
    }

    public int getFkDocument() {
        return fkDocument;
    }

    public void setFkDocument(int fkDocument) {
        this.fkDocument = fkDocument;
    }

    public int getFkClient() {
        return fkClient;
    }

    public void setFkClient(int fkClient) {
        this.fkClient = fkClient;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idImage;
        hash += (int) fkExpedient;
        hash += (int) fkDocument;
        hash += (int) fkClient;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ImagePK)) {
            return false;
        }
        ImagePK other = (ImagePK) object;
        if (this.idImage != other.idImage) {
            return false;
        }
        if (this.fkExpedient != other.fkExpedient) {
            return false;
        }
        if (this.fkDocument != other.fkDocument) {
            return false;
        }
        if (this.fkClient != other.fkClient) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ImagePK[ idImage=" + idImage + ", fkExpedient=" + fkExpedient + ", fkDocument=" + fkDocument + ", fkClient=" + fkClient + " ]";
    }
    
}
