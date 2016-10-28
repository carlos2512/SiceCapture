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
public class ExpedientClientPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "fk_expedient")
    private int fkExpedient;
    @Basic(optional = false)
    @Column(name = "fk_client")
    private int fkClient;

    public ExpedientClientPK() {
    }

    public ExpedientClientPK(int fkExpedient, int fkClient) {
        this.fkExpedient = fkExpedient;
        this.fkClient = fkClient;
    }

    public int getFkExpedient() {
        return fkExpedient;
    }

    public void setFkExpedient(int fkExpedient) {
        this.fkExpedient = fkExpedient;
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
        hash += (int) fkExpedient;
        hash += (int) fkClient;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpedientClientPK)) {
            return false;
        }
        ExpedientClientPK other = (ExpedientClientPK) object;
        if (this.fkExpedient != other.fkExpedient) {
            return false;
        }
        if (this.fkClient != other.fkClient) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ExpedientClientPK[ fkExpedient=" + fkExpedient + ", fkClient=" + fkClient + " ]";
    }
    
}
