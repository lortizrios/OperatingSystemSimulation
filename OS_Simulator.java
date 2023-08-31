/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.lowebservices.os_simulator;

import java.util.Random;

public class OS_Simulator {
    
    private int id;
    private int time;
    private int processTime;
    private CPU pcb;
    private int programCounter;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getProcessTime() {
        return processTime;
    }

    public void setProcessTime(int processTime) {
        this.processTime = processTime;
    }
    
    private int processTime(){
        Random random = new Random();
        
        //Obtine un nuero entre 1-10
        int n = random.nextInt(10) + 1;
        return n;
        
    }
    
    private void cpu(){
        
    }
    
//    public ProcessId(int id){
//        id = -1;
//        tiempoProceso = getProcessTime();
//        pcb = new ProcessControlBlock();
//    } 

    
    public static void main(String[] args) {
        System.out.println();         
    }
}
