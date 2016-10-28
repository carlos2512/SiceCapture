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
public class ClientDataPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "fk_client")
    private int fkClient;
    @Basic(optional = false)
    @Column(name = "fk_document_data")
    private int fkDocumentData;

    public ClientDataPK() {
    }

    public ClientDataPK(int fkClient, int fkDocumentData) {
        this.fkClient = fkClient;
        this.fkDocumentData = fkDocumentData;
    }

    public int getFkClient() {
        return fkClient;
    }

    public void setFkClient(int fkClient) {
        this.fkClient = fkClient;
    }

    public int getFkDocumentData() {
        return fkDocumentData;
    }

    public void setFkDocumentData(int fkDocumentData) {
        this.fkDocumentData = fkDocumentData;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) fkClient;
        hash += (int) fkDocumentData;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientDataPK)) {
            return false;
        }
        ClientDataPK other = (ClientDataPK) object;
        if (this.fkClient != other.fkClient) {
            return false;
        }
        if (this.fkDocumentData != other.fkDocumentData) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ClientDataPK[ fkClient=" + fkClient + ", fkDocumentData=" + fkDocumentData + " ]";
    }
    
}
