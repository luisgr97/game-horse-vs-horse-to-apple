package alphazero_u.models;

import java.util.ArrayList;

/**
 *
 * @author ASUS-LGR23
 */
public class Nodo {
    //clase nodo, representa un estado determinado del juego, por tanto tiene todas sus caracternisticas como parametros
    
    //bloque de variables globales:
    private int tipo;
    private int profundidad=0;
    private int utilidad;
    private int posicionCaballoNegro[];
    private int posicionCaballoBlanco[];
    private int manzanas=0;
    private int manzanasBlancas=0;
    private int manzansNegras=0;
    private ArrayList <int [] > posicion_Manzanas;
    private Nodo padre;
    
 
     //metodos de la clase:
    public Nodo() {
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(int profundidad) {
        this.profundidad = profundidad;
    }

    public int getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(int utilidad) {
        this.utilidad = utilidad;
    }

    public int[] getPosicionCaballoNegro() {
        return posicionCaballoNegro;
    }

    public void setPosicionCaballoNegro(int[] posicionCaballoNegro) {
        this.posicionCaballoNegro = posicionCaballoNegro;
    }

    public int[] getPosicionCaballoBlanco() {
        return posicionCaballoBlanco;
    }

    public void setPosicionCaballoBlanco(int[] posicionCaballoBlanco) {
        this.posicionCaballoBlanco = posicionCaballoBlanco;
    }

    public int getManzanas() {
        return manzanas;
    }

    public void setManzanas(int manzanas) {
        this.manzanas = manzanas;
    }

    public int getManzanasBlancas() {
        return manzanasBlancas;
    }

    public void setManzanasBlancas(int manzanasBlancas) {
        this.manzanasBlancas = manzanasBlancas;
    }

    public int getManzansNegras() {
        return manzansNegras;
    }

    public void setManzansNegras(int manzansNegras) {
        this.manzansNegras = manzansNegras;
    }

    public ArrayList<int[]> getPosicion_Manzanas() {
        return posicion_Manzanas;
    }

    public void setPosicion_Manzanas(ArrayList<int[]> posicion_Manzanas) {
        this.posicion_Manzanas = posicion_Manzanas;
    }

    public Nodo getPadre() {
        return padre;
    }

    public void setPadre(Nodo padre) {
        this.padre = padre;
    }
    
}