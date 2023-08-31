/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lowebservices.os_simulator;

import java.util.Random;

/**
 *
 * @author leroyortizrios
 */
public class RandomProcessGenerator {
    private int probabilidad;
    
    public RandomProcessGenerator (){
        probabilidad = 100;
    }

    public Process getProcessInstance(){
 
        if (Math.random() <= probabilidad / 100.0) {
            return new Process();
        } else {
            return null;
        }   
    }
    
    public int getProbabilidad(){
        return probabilidad;
    }
    
    public void setProbabilidad(int probabilidad) {
        this.probabilidad = probabilidad;
    }
}
