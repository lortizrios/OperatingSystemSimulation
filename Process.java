/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lowebservices.os_simulator;

import java.time.Duration;

/**
 *
 * @author leroyortizrios
 */
public class Process {
    private static int nextPid = 0;
    
    private final int pid;
    private final int size;
    private int pc; 
    private ProcessState state;
    private int timeInNew;
    private int timeInReady;
    private int timeInRunning;
    private int timeInBlock;
    private int totalTime;
    private int priority;
    private int programCounter;

    
    //Constructor
    public Process() {      
        pid = nextPid++;
        size = (int) (Math.random() * 10) + 1;
        state = ProcessState.NEW;
        timeInNew = 0;
        timeInReady = 0;
        timeInRunning = 0;
        timeInBlock = 0;
        totalTime = 0;
        priority = (int) (Math.random() * 10) + 1;
        programCounter = 0;
        
    }
  
    public int getPid(){
        return pid;
    }
    
    public int getSize() {
        return size;
    }
    
    //CPU cpu = new CPU(pc);
    
    public ProcessState getState() {
        return state;
    }
    
    public void setState(ProcessState newState) {
        state = newState;
    }
    
    public int getTimeInNew() {
        return timeInNew;
    }
    
    public int getTimeInReady() {
        return timeInReady;
    }
    
    public int getTimeInRunning() {
        return timeInRunning;
    }
    
    public int getTimeInBlock() {
        return timeInBlock;
    }
    
    public int getTotalTime() {
        return totalTime;
    }
    
    public void incrementTimeInNew() {
        timeInNew++;
        totalTime++;
    }
    
    public void incrementTimeInReady() {
        timeInReady++;
        totalTime++;
    }
    
    public void incrementTimeInRunning() {
        timeInRunning++;
        totalTime++;
    }
    
    public void incrementTimeInBlock() {
        timeInBlock++;
        totalTime++;
    }
   
    @Override
    public String toString() {
        return "PID: " + pid + " | SIZE: " + size + " | STATE: " + state+ " | PRIORITY: " + priority;
    }

    public enum ProcessState {
        NEW, READY, RUNNING, BLOCK, FINISH
    }
    
}
