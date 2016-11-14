/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import entities.Client;
import entities.ClientData;
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
public class TableModelCustomClientData extends AbstractTableModel {

    private List<ClientData> clientDataList;
    private String[] columnNames
            = {
                "Tipo de dato", "Nombre", "Valor"
            };

    public TableModelCustomClientData() {
        clientDataList = new ArrayList<>();
    }

    public TableModelCustomClientData(List<ClientData> clientDatList) {
        this.clientDataList = clientDatList;
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
        return clientDataList.size();
    }

    public ClientData getClientData(int row) {
        return this.clientDataList.get(row);
    }

    @Override
    public Object getValueAt(int row, int column) {
        ClientData clientData = getClientData(row);
        switch (column) {
            case 0:
                return clientData.getDocumentData().getFkDataType().getName();
            case 1:
                return clientData.getDocumentData().getName();
            case 2:
                return clientData.getValue();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        ClientData clientData = getClientData(row);
        switch (column) {
            case 0:
                clientData.getDocumentData().getFkDataType().setName((String) value);
                break;
            case 1:
                clientData.getDocumentData().setName((String) value);
                break;
            case 2:
                clientData.setValue((String) value);
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
            default:
                return String.class;
        }
    }

    public void addClientData(ClientData clientData) {
        insertClientData(getRowCount(), clientData);
    }

    public void insertClientData(int row, ClientData clientData) {
        clientDataList.add(row, clientData);
        fireTableRowsInserted(row, row);
    }

    public void removeClientData(int row) {
        clientDataList.remove(row);
        fireTableRowsDeleted(row, row);
    }
}
