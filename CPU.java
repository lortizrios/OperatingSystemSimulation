/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lowebservices.os_simulator;

import com.lowebservices.os_simulator.Process;
import com.lowebservices.os_simulator.PCBTableModel;
import com.lowebservices.os_simulator.Process.ProcessState;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.Instant;
import javax.swing.DefaultListModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;

/**
 *
 * @author leroyortizrios
 */
public class CPU implements ActionListener{

    //Atrubutos
    private int cpuId;
    private int programCounter;
    private Process execProcess;
    private RandomProcessGenerator randomProcessGenerator;
    private Process currentProcess;
    private int processSize;
    private List<Process> processes;
    private int pId;
    
    //Boton de new
    JButton clocktickButton = new JButton("ClockTick");
     //Boton de Ready
    JButton pcbButton = new JButton("PCB");

    // Listas para almacenar los procesos en cada estado
    private List<Process> newProcesses;
    private List<Process> readyProcesses;
    private List<Process> runningProcesses;
    private List<Process> blockedProcesses;
    private List<Process> finishedProcesses;

    // Referencia al GUI para actualizar visualmente los cambios
    public DefaultListModel<String> dispatcher;
    
    private PCBTableModel pcbTableModel;
    private JTable pcbTable;

    //Default constructor
    public CPU(int id) {
        Process process = new Process();
        
        this.cpuId = id;
        execProcess = null;
        programCounter = 0;
        dispatcher = new DefaultListModel<>();
        processSize = (int) (Math.random() * 10) + 1;
        pId = process.getPid();
        
        randomProcessGenerator = new RandomProcessGenerator();
        pcbTableModel = new PCBTableModel(processes);
        
        //System.out.println("Se creo un cpu nuevo");
        //Inicializar las listas de procesos en cada estado
        newProcesses = new ArrayList<>();
        readyProcesses = new ArrayList<>();
        runningProcesses = new ArrayList<>();
        blockedProcesses = new ArrayList<>();
        finishedProcesses = new ArrayList<>();
        
        processes = new ArrayList<>();  // Inicializa la lista de procesos
        
    }
    
    // Agrega un proceso a la lista
    public void addProcess(Process process) {
        processes.add(process);  
    }
 
    //Verifica si hay algun estado en ejecucion
    public String getCurrentState() {
        if (currentProcess != null) {
            return "Proceso en ejecución: " + currentProcess;
        } else {
            return "Sin proceso en ejecución";
        }
    }
    
    //Mueve los procesos a estado new
    public void moveToNew(Process process) {
        if (newProcesses.isEmpty() || process.getState() == ProcessState.NEW)  {
            Instant start = Instant.now();
            
            newProcesses.add(process);
            process.setState(ProcessState.NEW);
            System.out.println("Proceso "+process.getPid()+" se añadio a disco duro."+" State: " +process.getState());                       
                
            Instant end = Instant.now();
            //System.out.println("Total time de ready a new: " + Duration.between(start, end)); 
            //Duration totalTimeReadytoNew = Duration.between(start, end);
        
        }else{
            System.out.println("Proceso"+process.getPid()+" no se añadio la lista de New");
        } 
        
        
        //Imprime los procesos que esten en la lista new
//        for(Process moved : newProcesses) {
//            System.out.println(moved);
//        }
        
        pcbTableModel.fireTableDataChanged();
    }
    
    //mover un proceso de la lista de procesos nuevos ("New") a la lista de procesos listos ("Ready"), 
    //actualizando el estado del proceso y realizando las acciones necesarias para reflejar los cambios 
    //en la interfaz de usuario.
    public void moveToReady(Process process) {
        //Verifica si getState es igual a new
        if (Process.ProcessState.NEW == process.getState()) {
            //readyProcesses.remove(process);
            newProcesses.remove(process);
            process.setState(ProcessState.READY);
            readyProcesses.add(process);

            System.out.println("Proceso "+process.getPid()+" se movio a memoria ram. State: " +process.getState());
            //System.out.println("Proceso "+process.getPid()+" se ha movido a Estado Ready");
            
            pcbTableModel.fireTableDataChanged();
            pcbTableModel.addProcess(process);
            
        } else {
            System.out.println("Proceso " +process.getPid()+ " no se pudo añadir a la memoria ram.");
            System.out.println(execProcess);
        }
        
    }

    public void moveToRunning(Process process) {
        if (!readyProcesses.isEmpty() && process.getState()== Process.ProcessState.READY) {
            readyProcesses.remove(process); // Remover de la lista de "Ready"
            process.setState(ProcessState.RUNNING);
            runningProcesses.add(process);
            System.out.println("Proceso " + process.getPid() + " se ha movido a CPU" +cpuId+" Estado: " +process.getState());
            System.out.println("Proceso "+process.getPid()+" procesando en cpu "+cpuId+"...");

        }else {
            System.out.println("Proceso sin estado (Sin procesar).");
        }
        pcbTableModel.fireTableDataChanged();
        pcbTableModel.addProcess(process);  
    }

    public void moveToBlocked(Process process) {
        runningProcesses.remove(process);
        blockedProcesses.add(process);
        process.setState(ProcessState.BLOCK);
        System.out.println("Proceso se ha movido a Blocked");
        
        pcbTableModel.fireTableDataChanged();
    }

    public void moveToFinish(Process process) {
        if (Process.ProcessState.RUNNING == process.getState()) {
            runningProcesses.remove(process); // Remover de la lista de "Runing"
            process.setState(ProcessState.FINISH);
            finishedProcesses.add(process);
            System.out.println("Proceso " + process.getPid() + " se ha movido a Estado: " +process.getState());
        }else {
            System.out.println("Proceso sin estado (Sin procesar).");
        }
        pcbTableModel.fireTableDataChanged();
        pcbTableModel.addProcess(process);  
    }
      
    //Métodos se utilizan para manipular y administrar los procesos en diferentes estados.
    public boolean hasNewProcesses() {
        return !newProcesses.isEmpty();
    }

    public void moveToReadyFromNew() {
        if (!newProcesses.isEmpty()) {
            Process process = newProcesses.remove(0);
            process.setState(ProcessState.READY);
            readyProcesses.add(process);
        }
    }

    public int getReadyProcessesSize() {
        return readyProcesses.size();
    }
    
    public JPanel createTablePanel() {
        JScrollPane scrollPane = new JScrollPane(pcbTable);
        JPanel panel = new JPanel();
        panel.add(scrollPane);
        return panel;
    }
    
    private PCBTableModel pcbTableGUI;
    
    public void setupPCBTable(List<Process> processes) {
        pcbTableGUI = new PCBTableModel(processes);
        JPanel tablePanel = pcbTableGUI.createTablePanel();
        // Agrega el panel en la ubicación deseada dentro del GUI
        // ...
    }
    
    public void updateTable() {
    // Implementa la lógica para actualizar la tabla aquí
    }

    public boolean shouldFinish() {
        int timeInRunning = 0;
        
        if (timeInRunning >= 10) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean shouldBlock() {
        int size = 0;
        if (size > 5) {
            //Inicia el timer 
            Instant start = Instant.now();
            //Termina el timer
            Instant end = Instant.now();
            System.out.println("Thread Execution Time: " + Duration.between(start, end)); 
            Duration totalTimeBlock = Duration.between(start, end);

            return true;
        } else {
            return false;
        }
    }

    // Método para simular la ejecución de procesos y actualizar el contador de programa
    public void executeProcess() {
        if (execProcess != null) {
            System.out.println("PCB: " + execProcess);
            programCounter++;
            
//            // Simulación de bloqueo de proceso
//            if (shouldBlock()) {
//                moveToBlocked(execProcess);
//                System.out.println("Proceso bloqueado: " + execProcess);
//                
//            } else if (shouldFinish()) {
//                // Simulación de finalización de proceso
//                moveToFinish(execProcess);
//                System.out.println("Proceso finalizado: " + execProcess);
//            } else {
//                // Mover el proceso a READY para seguir ejecutando
//                moveToReady(execProcess);
//                System.out.println("Proceso se movio de Ready a New " + execProcess);
//            }

            execProcess = null;
        } else {
            System.out.println("No hay proceso para ejecutar en el CPU " + cpuId);
        }
        pcbTableModel.updateTable();  // Llama al método para actualizar la tabla
        
    }

    //Verifica si se ha generado un nuevo proceso. Devuelve true o false
    public boolean clockTick() {

        Process validaNuevoproceso = randomProcessGenerator.getProcessInstance();

        if (validaNuevoproceso != null) {
            System.out.println("Se generó un nuevo proceso: " + validaNuevoproceso);
            return true;

        } else {
            System.out.println("No se generó ningún proceso.");
            return false;
        }
    }

    public boolean isAvailable() {
        if (execProcess == null) {
            return true;
        }
        return false;
    }

    //Se anade proceso al cpu si el cpu esta disponible 
    public boolean setProcess(Process p) {
        if (isAvailable()) {
//            this.execProcess = p;
//            System.out.println("PCB "+execProcess);
            return true;
        }else{
            return false;
        }
    }

    public void clear() {
        this.execProcess = null;
        this.programCounter = 1;
    }

    public boolean hasProcess() {
        if (this.execProcess == null) {
            return false;
        }
        return true;
    }

    public Process getExeProcess() {
        return this.execProcess;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    // Método para generar procesos aleatorios y agregarlos a la lista de nuevos procesos
    public Process generateRandomProcess() {
        Process process = randomProcessGenerator.getProcessInstance();
        if (process != null) {
            newProcesses.add(process);
            
            //process.setState(ProcessState.NEW);
            dispatcher.addElement(process.toString());
            System.out.println("Generando nuevo proceso: " + process);
        } else {
            System.out.println("No se generó un nuevo proceso.");
        }
        return process;
    }  
    
    //Verifica si tiene procesos
    @Override
    public String toString() {
        if (hasProcess()) {
            return "CPU: " + cpuId + " tiene: " + this.execProcess + " | PC: " + getProgramCounter();
        }
        return " CPU: " + cpuId + " No thiene ningun proceso... | PC: " + getProgramCounter();
    }
    
    public void recibirNuevoProceso(Process proceso) {
        if (proceso.getState().equals("new")) {
            pcbTableModel.addProcess(proceso);
            pcbTableModel.fireTableDataChanged();
        }
    }
    
    public void GUI(){
        //GUI
        JFrame frame = new JFrame();
        
        JPanel panel = new JPanel();
        
        pcbTableModel = new PCBTableModel(processes);
        pcbTable = new JTable(pcbTableModel);

        JScrollPane scrollPane = new JScrollPane(pcbTable);
        panel.add(scrollPane);
        
        clocktickButton.addActionListener(this);
        
       
        pcbButton.addActionListener(this);

        
        JLabel label = new JLabel("Number of clicks: 0");
        
        //Set Tamaño del panel
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel.setLayout(new GridLayout(0,1));
        
        //añade boton al jpanel^
        panel.add(clocktickButton);
        panel.add(pcbButton);
        
        //añade label al jpanel
        panel.add(label);
        
        frame.add(panel,BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("OS Simulator");
        frame.pack();
        frame.setVisible(true);
        
        // Llama al método para configurar la tabla
        setupPCBTable(processes);  

        
    }
   
    public void clockTickRun(){
        
        CPU cpu = new CPU(1);
        
        List<Process> processList = new ArrayList<>();
        
        Random random = new Random();

        // Generar la lista de procesos aleatorios
        int numProcesses = random.nextInt(10) + 1;
        Duration totalTimeNew = null;
        Duration totalProcessTime = Duration.ZERO; // Inicializa la variable acumuladora con una duración de cero
  
        for (int i = 1; i < numProcesses; i++) {
            //Inicia el timer para el proceso actual
            Instant start = Instant.now();                
            
            //Genera un número aleatorio de iteraciones para el proceso actual
            for (int j = 0; j < processSize; j++) {
                // Crea el proceso actual
                Process process = cpu.generateRandomProcess();
                processList.add(process);
                
                Instant end = Instant.now();
                Duration processTime = Duration.between(start, end);
                
                // Agregar el tiempo del proceso a la duración total
                totalProcessTime = totalProcessTime.plus(processTime);
                
                System.out.println("Process Time: " + processTime);
                System.out.println("");  
            }
            
        }
        
        // Imprimir la duración total de los procesos
        System.out.println("Total Process Time in New List: " + totalProcessTime);
        System.out.println("-------------------------------------------------");

        // Imprimir la lista de procesos
        System.out.println(" ");
        System.out.println("Lista de procesos:");
        
        for (Process process : processList) {
            System.out.println(process);
        }
        
        // Imprimir la duración total de los procesos
        System.out.println("Total Process Time in New List: " + totalProcessTime);
        System.out.println("-------------------------------------------------");

        
        // Procesar los procesos de la lista en el cpu
        for (Process process : processList) {
            
            
            //Si tine 10 procesos
            cpu.getReadyProcessesSize();              
            //De new se mueve a ready
            cpu.moveToNew(process);
            cpu.moveToReady(process);
            cpu.moveToRunning(process);
            cpu.moveToFinish(process);


            //Ejecuta hasta que el cpu no tenga procesos
            while (cpu.hasNewProcesses()) {
                cpu.moveToReadyFromNew();
            }
            
            //Verifica si cpu esta disponible y procesa el proceso
            if(cpu.setProcess(process)!= true){
                //Se anadio e proceso al cpu
//                cpu.setProcess(process);
                //el cpu ha procesado los datos
                cpu.executeProcess();
            }
            
            System.out.println("Estado actual del CPU: " + cpu.getCurrentState());
            System.out.println("---------------------------------------------");
        }  
        
            // Imprimir la lista de procesos
            System.out.println(" ");
            System.out.println("Process Control Block:");

            for (Process process : processList) {
                System.out.println(process);
            }

            // Imprimir la duración total de los procesos
            System.out.println("Total Process Time: " + totalProcessTime);
            System.out.println("-------------------------------------------------");
    }

    public static void main(String[] args) {
        
        CPU cpu = new CPU(1);
        //Llamamos a la interfas grafica para que 
        cpu.GUI();
   
        
//        cpu.clockTick();

    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        //verifica que boton imprimimos crear nuevos procesos 
        if (e.getSource() == clocktickButton) {
            clockTickRun();
        } else if (e.getSource() == pcbButton) {
            
        }        
    } 

}
