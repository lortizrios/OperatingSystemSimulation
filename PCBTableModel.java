/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lowebservices.os_simulator;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;


/**
 *
 * @author leroyortizrios
 */

public class PCBTableModel extends AbstractTableModel {
    private List<Process> processes;
    private String[] columnNames = {"ID", "Estado", "Tiempo en NEW", "Tiempo en READY", "Tiempo en RUNNING", "Tiempo en BLOCK", "Tiempo total"};
    private JTable pcbTable;
    
    public PCBTableModel(List<Process> processes) {
        this.processes = processes;
        var pcbTable = new JTable();
        this.pcbTable = new JTable(this);  // Inicializa la lista process
    }
    @Override
    public int getRowCount() {
        return processes.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Process process = processes.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return process.getPid();
            case 1:
                return process.getState();
            case 2:
                return process.getTimeInNew();
            case 3:
                return process.getTimeInReady();
            case 4:
                return process.getTimeInRunning();
            case 5:
                return process.getTimeInBlock();
            case 6:
                return process.getTotalTime();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    public JPanel createTablePanel() {
        JScrollPane scrollPane = new JScrollPane(pcbTable);
        JPanel panel = new JPanel();
        panel.add(scrollPane);
        return panel;
    }
    
    public void updateTable() {
        fireTableDataChanged();
    }
    
    public void addProcess(Process process) {
        
        if (processes == null) {
        processes = new ArrayList<>(); // Inicializa la lista si es nula
    }
        processes.add(process);
        fireTableRowsInserted(processes.size() - 1, processes.size() - 1);
    }
   
}

