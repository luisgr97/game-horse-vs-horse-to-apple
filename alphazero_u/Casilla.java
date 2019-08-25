/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alphazero_u;

import javax.swing.*;

/**
 *
 * @author ASUS-LGR23
 */
public class Casilla extends JLabel {

    //atributos:
    private String yosoy = "casilla"; 
    private int x=0;
    private int y=0;
    private boolean turno = false;
    
    
    //meetodos:
    public Casilla() {
        this.setVisible(true);
        this.setSize(100, 90);
    }

    public boolean getTurno() {
        return turno;
    }

    public void setTurno(boolean turno) {
        this.turno = turno;
    }

    public String getYosoy() {
        return yosoy;
    }

    public void setYosoy(String yosoy) {
        this.yosoy = yosoy;
    }

    public int getFila() {
        return x;
    }

    public void setFila(int x) {
        this.x = x;
    }

    public int getColumna() {
        return y;
    }

    public void setColumna(int y) {
        this.y = y;
    }
    
    
    
    
    
}
