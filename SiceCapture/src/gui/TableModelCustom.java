/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import entities.Client;
import entities.Expedient;
import entities.ExpedientClient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author intec
 */
public class TableModelCustom extends AbstractTableModel {

    private List<ExpedientClient> expedientClientList;
    private String[] columnNames
            = {
                "Tipo Identificación", "No. Identificación", "Nombre", "Nacionalidad", "Expediente", "Fecha Ult. Movimiento"
            };

    public TableModelCustom() {
        expedientClientList = new ArrayList<>();
    }

    public TableModelCustom(List<ExpedientClient> expedientClientList) {
        this.expedientClientList = expedientClientList;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public int getRowCount() {
        return expedientClientList.size();
    }

    public ExpedientClient getExpedientClient(int row) {
        return this.expedientClientList.get(row);
    }

    @Override
    public Object getValueAt(int row, int column) {
        ExpedientClient expedientClient = getExpedientClient(row);
        switch (column) {
            case 0:
                return expedientClient.getClient().getIdentificationType();
            case 1:
                return expedientClient.getClient().getIdentification();
            case 2:
                return expedientClient.getClient().getName();
            case 3:
                return expedientClient.getClient().getCountry();
            case 4:
                return expedientClient.getExpedient().getName();
            case 5:
                return expedientClient.getLastModification();
            default:
                return null;
        }

    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        ExpedientClient expedientClient = getExpedientClient(row);
        switch (column) {
            case 0:
                expedientClient.getClient().setIdentificationType((String) value);
                break;
            case 1:
                expedientClient.getClient().setIdentification((Integer) value);
                break;
            case 2:
                expedientClient.getClient().setName((String) value);
                break;
            case 3:
                expedientClient.getClient().setCountry((String) value);
                break;
            case 4:
                expedientClient.getExpedient().setName((String) value);
                break;
            case 5:
                expedientClient.setLastModification((Date) value);
                break;
        }
        fireTableCellUpdated(row, column);
    }

    @Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            case 4:
                return String.class;
            case 5:
                return Date.class;
            default:
                return String.class;
        }
    }

    public void addExpedientClient(ExpedientClient expedientClient) {
        insertExpedientClient(getRowCount(), expedientClient);
    }

    public void insertExpedientClient(int row, ExpedientClient expedientClient) {
        expedientClientList.add(row, expedientClient);
        fireTableRowsInserted(row, row);
    }

    public void removePerson(int row) {
        expedientClientList.remove(row);
        fireTableRowsDeleted(row, row);
    }
}
