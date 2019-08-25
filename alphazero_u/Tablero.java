/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alphazero_u;

import java.awt.*;
import static java.awt.Label.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import javax.swing.*;
import org.w3c.dom.events.*;
import org.w3c.dom.views.*;

/**
 *
 * @author ASUS-LGR23
 */
public class Tablero extends javax.swing.JFrame {

    //atributos:
    private Casilla tablero_logico[][] = new Casilla[6][6];
    private Random aleatoreo = new Random();
    private JPanel panelTablero = new JPanel();
    private JPanel panelizq = new JPanel();
    private JPanel panelder = new JPanel();
    public int numeroManzanas = 0;
    private Label etiquetaCPU, etiquetaJ1;
    private Casilla blanco, negro;
    public int manzanasBlancas = 0, manzanasNegras = 0;
    public static final int MAX = 1, SECONDS = 2, INF_MEN = (-1000 * 9999999);
    private Arbol arbol_de_decision;

    //clase para manejar los escuchas:
    private class Escucha implements MouseListener {

        ArrayList<int[]> movimientos;

        @Override
        public void mouseClicked(java.awt.event.MouseEvent me) {
            try {
                //for para el evento de tocar a caballoBlanco y ver a donde se puede mover.
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 6; j++) {
                        if ((me.getSource() == tablero_logico[i][j]) && (tablero_logico[i][j].getYosoy() == "caballoB") && (tablero_logico[i][j].getTurno() == true)) {
                            movimientos = ValidarMovimientos_Tablero(MovimientosCaballos(tablero_logico[i][j]));
                            blanco = tablero_logico[i][j];
                            System.out.println("Estoy en: " + tablero_logico[i][j].getFila() + "," + tablero_logico[i][j].getColumna());
                        }//cierra if
                        else if ((me.getSource() == tablero_logico[i][j])) {
                            for (int k = 0; k < movimientos.size(); k++) {
                                if ((movimientos.get(k)[0] == i) && (movimientos.get(k)[1] == j)) {

                                    System.out.println("Me movi a: " + i + "," + j);
                                    Intercambiar(blanco, tablero_logico[i][j]);
                                    blanco = null;
                                    ReunirInformacion_Maquina();
                                }
                            }
                        }//cierra if
                        else {
                            //System.out.println("No hace nada");
                        }
                    }//cierra for interno
                }//cierra for externo
            } catch (Exception e) {
                System.err.println("Error en el escucha: " + e);
            };
        }//cerrar metodo mauseClicked.

        //método para cambiar el caballo de posicion.
        public void Intercambiar(Casilla cs1, Casilla cs2) {

            //variable logica:
            boolean esManzana = false;

            try {
                ///quita los bordes subrayados por que ya selecciono a donde ir.
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 6; j++) {
                        tablero_logico[i][j].setBorder(null);
                    }//cierra for interno
                }//cierra for externo

                //validar que a donde voy sea una manzana.
                if (tablero_logico[cs2.getFila()][cs2.getColumna()].getYosoy() == "manzana") {
                    esManzana = true;
                }

                //intercambia la posicion
                tablero_logico[cs2.getFila()][cs2.getColumna()].setIcon(new ImageIcon("src\\Imagenes\\caballo_blanco.gif"));//cambio el caballo de posicion.
                tablero_logico[cs2.getFila()][cs2.getColumna()].setYosoy("caballoB");
                tablero_logico[cs2.getFila()][cs2.getColumna()].setTurno(true);
                blanco = tablero_logico[cs2.getFila()][cs2.getColumna()];

                //pone el cuadro del tablero correspondiente.         
                if ((cs1.getFila() % 2 == 0) || (cs1.getFila() == 0)) {
                    if ((cs1.getColumna() % 2 == 0) || (cs1.getColumna() == 0)) {
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setIcon(new ImageIcon("src\\Imagenes\\cuadro_claro.png"));
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setYosoy("casilla");
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setTurno(false);

                    } else {
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setIcon(new ImageIcon("src\\Imagenes\\cuadro_oscuro.png"));
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setYosoy("casilla");
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setTurno(false);
                    }
                }//cierra if
                else {
                    if ((cs1.getColumna() % 2 == 0) || (cs1.getColumna() == 0)) {
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setIcon(new ImageIcon("src\\Imagenes\\cuadro_oscuro.png"));
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setYosoy("casilla");
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setTurno(false);
                    } else {
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setIcon(new ImageIcon("src\\Imagenes\\cuadro_claro.png"));
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setYosoy("casilla");
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setTurno(false);
                    }//cierra else interno
                }//cierra else externo

                if (esManzana == true) {

                    System.out.println("Me comi una manzana");
                    IncrementarManzanasBlanco();
                    Casilla c = new Casilla();
                    c.setIcon(new ImageIcon("src\\Imagenes\\manzana.gif"));
                    SumarAlTablero(c);

                }

            } catch (Exception ex) {
                System.err.println("Error en el metodo Intercambio" + ex);
            };
        }//cierra el metodo

        //método para sumarle las manzanas a los tableros.
        public void SumarAlTablero(Casilla manzana) {
            panelder.add(manzana, CENTER);
            RestarManzanas();
        }

        @Override
        public void mousePressed(java.awt.event.MouseEvent me) {
            //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseReleased(java.awt.event.MouseEvent me) {
            // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseEntered(java.awt.event.MouseEvent me) {
            //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent me) {
            //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }
    private Escucha escucha = new Escucha();

    //metodos://////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Tablero() {
        initComponents();
        this.setTitle("ALPHA ZERO UNIVALLE");
        ConstruirTablero();
        System.out.println("voy a jugar con:" + numeroManzanas + " Manzanas");
        panelizq.setLayout(new GridLayout(numeroManzanas + 1, 1));
        panelizq.setBackground(Color.BLACK);
        panelizq.setBorder(BorderFactory.createRaisedBevelBorder());
        this.etiquetaCPU = new Label("CPU");
        this.etiquetaCPU.setAlignment(CENTER);
        this.etiquetaCPU.setForeground(Color.WHITE);
        panelizq.add(this.etiquetaCPU);
        this.add(panelizq);
        this.add(panelTablero);
        panelder.setLayout(new GridLayout(numeroManzanas + 1, 1));
        panelder.setBackground(Color.WHITE);
        panelder.setBorder(BorderFactory.createRaisedBevelBorder());
        this.etiquetaJ1 = new Label("JUGADOR");
        this.etiquetaJ1.setAlignment(CENTER);
        this.etiquetaJ1.setForeground(Color.BLACK);
        panelder.add(this.etiquetaJ1);
        this.add(panelder);
    }//cierra constructor

    //metrodo que me construye la interfaz.
    public void ConstruirTablero() {
        this.panelTablero.setLayout(new GridLayout(6, 6));
        this.panelTablero.setSize(500, 500);

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                Casilla casilla = new Casilla();
                if ((i % 2 == 0) || (i == 0)) {
                    if ((j % 2 == 0) || (j == 0)) {
                        casilla.setIcon(new ImageIcon("src\\Imagenes\\cuadro_claro.png"));
                        casilla.setFila(i);
                        casilla.setColumna(j);
                        casilla.addMouseListener(escucha);
                        casilla.setYosoy("casilla");
                        this.tablero_logico[i][j] = casilla;

                    } else {
                        casilla.setIcon(new ImageIcon("src\\Imagenes\\cuadro_oscuro.png"));
                        casilla.setFila(i);
                        casilla.setColumna(j);
                        casilla.addMouseListener(escucha);
                        casilla.setYosoy("casilla");
                        this.tablero_logico[i][j] = casilla;

                    }
                }//cierra if
                else {
                    if ((j % 2 == 0) || (j == 0)) {
                        casilla.setIcon(new ImageIcon("src\\Imagenes\\cuadro_oscuro.png"));
                        casilla.setFila(i);
                        casilla.setColumna(j);
                        casilla.addMouseListener(escucha);
                        casilla.setYosoy("casilla");
                        this.tablero_logico[i][j] = casilla;

                    } else {
                        casilla.setIcon(new ImageIcon("src\\Imagenes\\cuadro_claro.png"));
                        casilla.setFila(i);
                        casilla.setColumna(j);
                        casilla.addMouseListener(escucha);
                        casilla.setYosoy("casilla");
                        this.tablero_logico[i][j] = casilla;

                    }//cierra else interno
                }//cierra else externo
            }//cierra for interno  
        }//cierra for externo

        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 6; y++) {
                this.panelTablero.add(tablero_logico[x][y]);
            }
        }//cierra for externo

        //poner los caballos
        int filaCB = aleatoreo.nextInt(6);
        int columnaCB = aleatoreo.nextInt(6);
        int filaCN = aleatoreo.nextInt(6);
        int columnaCN = aleatoreo.nextInt(6);
        tablero_logico[filaCN][columnaCN].setIcon(new ImageIcon("src\\Imagenes\\Caballo_negro.gif"));
        tablero_logico[filaCN][columnaCN].setYosoy("caballoN");
        tablero_logico[filaCB][columnaCB].setIcon(new ImageIcon("src\\Imagenes\\caballo_blanco.gif"));
        tablero_logico[filaCB][columnaCB].setYosoy("caballoB");
        tablero_logico[filaCB][columnaCB].setTurno(true);
        //poner manzanas
        int respuesta = Integer.parseInt(JOptionPane.showInputDialog("Con cuantas manzanas desea jugar: (3) (5) (7)"));
        if (respuesta <= 1) {
            respuesta = 1;
        }
        if ((respuesta <= 3) && (respuesta > 1)) {
            respuesta = 3;
        }
        if ((respuesta <= 5) && (respuesta > 3)) {
            respuesta = 5;
        }
        if ((respuesta <= 7) && (respuesta > 5)) {
            respuesta = 7;
        }
        if (respuesta > 7) {
            respuesta = 7;
        }
        this.numeroManzanas = respuesta;
        if (respuesta == 3) {
            int fila = 0;
            int filaAnt = 0;
            int columna = 0;
            int columnaAnt = 0;
            for (int i = 0; i < 3; i++) {

                while (((filaCB == fila) && (columnaCB == columna)) || ((filaCN == fila) && (columnaCN == columna))
                        || ((filaAnt == fila) && (columnaAnt == columna))) {
                    fila = aleatoreo.nextInt(6);
                    columna = aleatoreo.nextInt(6);

                }
                tablero_logico[fila][columna].setIcon(new ImageIcon("src\\Imagenes\\manzana.gif"));
                tablero_logico[fila][columna].setYosoy("manzana");
                filaAnt = fila;
                columnaAnt = columna;
            }//cierra for
        }//cierra if 
        if (respuesta == 5) {
            int fila = 0;
            int filaAnt = 0;
            int columna = 0;
            int columnaAnt = 0;
            for (int i = 0; i < 5; i++) {

                while (((filaCB == fila) && (columnaCB == columna)) || ((filaCN == fila) && (columnaCN == columna))
                        || ((filaAnt == fila) && (columnaAnt == columna))) {
                    fila = aleatoreo.nextInt(6);
                    columna = aleatoreo.nextInt(6);

                }
                tablero_logico[fila][columna].setIcon(new ImageIcon("src\\Imagenes\\manzana.gif"));
                tablero_logico[fila][columna].setYosoy("manzana");
                filaAnt = fila;
                columnaAnt = columna;
            }//cierra for
        }
        if (respuesta == 7) {
            int fila = 0;
            int filaAnt = 0;
            int columna = 0;
            int columnaAnt = 0;
            for (int i = 0; i < 7; i++) {

                while (((filaCB == fila) && (columnaCB == columna)) || ((filaCN == fila) && (columnaCN == columna))
                        || ((filaAnt == fila) && (columnaAnt == columna))) {
                    fila = aleatoreo.nextInt(6);
                    columna = aleatoreo.nextInt(6);

                }
                tablero_logico[fila][columna].setIcon(new ImageIcon("src\\Imagenes\\manzana.gif"));
                tablero_logico[fila][columna].setYosoy("manzana");
                filaAnt = fila;
                columnaAnt = columna;
            }//cierra for
        }

        this.panelder.setSize(500, 500);
        this.panelizq.setSize(500, 500);
        this.setSize(1500, 580);
        //ReunirInformacion_Maquina();
    }

    //método para quitar las manzanas que se comieron ya.
    public void RestarManzanas() {
        //método que resta las manzanas y determina si se acabo el juego.
        if (numeroManzanas > 0) {
            this.numeroManzanas--;
            System.out.println("Quedan: " + numeroManzanas + " manzanas");
        }//muestra el mensaje de ganaste o perdiste.
        if (numeroManzanas == 0) {
            if (this.manzanasBlancas > this.manzanasNegras) {
                JOptionPane.showMessageDialog(null, " Ganaste");
                System.exit(-1);
            }//cierra if
            else {
                JOptionPane.showInternalMessageDialog(null, "Perdiste!");
                System.exit(-1);
            }
        }//cierra if
        if ((numeroManzanas == 0) && (this.manzanasBlancas == this.manzanasNegras)) {
            JOptionPane.showInternalMessageDialog(null, "Empate!");
            System.exit(-1);

        }
    }

    //metodo para sumar las manzanas que se come el caballo blanco
    public void IncrementarManzanasBlanco() {
        this.manzanasBlancas++;
        System.out.println("Blanco tiene: " + manzanasBlancas + " Manzanas.");
    }

    //metodo para sumar la manzanas que se come el caballo negro
    public void IncrementarManzanasNegro() {
        this.manzanasNegras++;
        System.out.println("Negro tiene: " + manzanasNegras + " Manzanas.");
    }

    //metodo que calcula los movimientos de los caballos
    public ArrayList<int[]> MovimientosCaballos(Casilla csla) {
        //metodo que construye los movimientos del caballo en el tablero

        //bloque de variables locales:
        int fila = csla.getFila();
        int columna = csla.getColumna();
        ArrayList<int[]> movimientos = new ArrayList<int[]>();
        int posicion[] = new int[2];
        int posicion2[] = new int[2];
        int posicion3[] = new int[2];
        int posicion4[] = new int[2];
        int posicion5[] = new int[2];
        int posicion6[] = new int[2];
        int posicion7[] = new int[2];
        int posicion8[] = new int[2];

        //Movimientos cuadro sup izq:
        //movimiento 1:
        posicion[0] = fila - 2;
        posicion[1] = columna - 1;
        movimientos.add(posicion);
        //movimiento 2:
        posicion2[0] = fila - 1;
        posicion2[1] = columna - 2;
        movimientos.add(posicion2);

        //Movimientos cuadro sup der:
        //movimiento 1:
        posicion3[0] = fila - 2;
        posicion3[1] = columna + 1;
        movimientos.add(posicion3);
        //movimiento 2:
        posicion4[0] = fila - 1;
        posicion4[1] = columna + 2;
        movimientos.add(posicion4);

        //Movimientos inf sup der:
        //movimiento 1:
        posicion5[0] = fila + 1;
        posicion5[1] = columna + 2;
        movimientos.add(posicion5);
        //movimiento 2:
        posicion6[0] = fila + 2;
        posicion6[1] = columna + 1;
        movimientos.add(posicion6);

        //Movimientos cuadro inf izq:
        //movimiento 1:
        posicion7[0] = fila + 1;
        posicion7[1] = columna - 2;
        movimientos.add(posicion7);
        //movimiento 2:
        posicion8[0] = fila + 2;
        posicion8[1] = columna - 1;
        movimientos.add(posicion8);

        return movimientos;

    }//cierra método.

    //método para validar movimientos de los caballos.
    public ArrayList<int[]> ValidarMovimientos_Tablero(ArrayList<int[]> movimientosCaballos) {
        //variables locales:
        ArrayList<int[]> movValidos = new ArrayList<int[]>();

        //ciclo que valida que los saltos del caballo no se salga del tablero.
        for (int i = 0; i < movimientosCaballos.size(); i++) {
            //si no se sale del tablero mire que no le salte encima al otro caballo.
            if ((movimientosCaballos.get(i)[0] >= 0) && (movimientosCaballos.get(i)[0] <= 5)
                    && (movimientosCaballos.get(i)[1] >= 0) && (movimientosCaballos.get(i)[1] <= 5)) {
                if (this.tablero_logico[movimientosCaballos.get(i)[0]][movimientosCaballos.get(i)[1]].getYosoy() != "caballoN") {
                    movValidos.add(movimientosCaballos.get(i));
                    this.tablero_logico[movimientosCaballos.get(i)[0]][movimientosCaballos.get(i)[1]].setBorder(BorderFactory.createLineBorder(Color.green, 5));
                }//cierra if.
            }//cierra el if. 
        }//cierra el for.

        return movValidos;
    }//cierra el método.

    //método para cambiar el caballo de posicion.
    public void IntercambiarCaballoNegro(Casilla cs1, Casilla cs2) {

        //variable logica:
        boolean esManzana = false;

        try {

            //validar que a donde voy sea una manzana.
            if (tablero_logico[cs2.getFila()][cs2.getColumna()].getYosoy() == "manzana") {
                esManzana = true;
            }

            //intercambia la posicion
            tablero_logico[cs2.getFila()][cs2.getColumna()].setIcon(new ImageIcon("src\\Imagenes\\caballo_negro.gif"));//cambio el caballo de posicion.
            tablero_logico[cs2.getFila()][cs2.getColumna()].setYosoy("caballoN");
            blanco = tablero_logico[cs2.getFila()][cs2.getColumna()];

            //pone el cuadro del tablero correspondiente.         
            if ((cs1.getFila() % 2 == 0) || (cs1.getFila() == 0)) {
                if ((cs1.getColumna() % 2 == 0) || (cs1.getColumna() == 0)) {
                    tablero_logico[cs1.getFila()][cs1.getColumna()].setIcon(new ImageIcon("src\\Imagenes\\cuadro_claro.png"));
                    tablero_logico[cs1.getFila()][cs1.getColumna()].setYosoy("casilla");
                } else {
                    tablero_logico[cs1.getFila()][cs1.getColumna()].setIcon(new ImageIcon("src\\Imagenes\\cuadro_oscuro.png"));
                    tablero_logico[cs1.getFila()][cs1.getColumna()].setYosoy("casilla");
                }
            }//cierra if
            else {
                if ((cs1.getColumna() % 2 == 0) || (cs1.getColumna() == 0)) {
                    tablero_logico[cs1.getFila()][cs1.getColumna()].setIcon(new ImageIcon("src\\Imagenes\\cuadro_oscuro.png"));
                    tablero_logico[cs1.getFila()][cs1.getColumna()].setYosoy("casilla");
                } else {
                    tablero_logico[cs1.getFila()][cs1.getColumna()].setIcon(new ImageIcon("src\\Imagenes\\cuadro_claro.png"));
                    tablero_logico[cs1.getFila()][cs1.getColumna()].setYosoy("casilla");
                }//cierra else interno
            }//cierra else externo

            if (esManzana == true) {

                System.out.println("Me comi una manzana");
                IncrementarManzanasNegro();
                Casilla c = new Casilla();
                c.setIcon(new ImageIcon("src\\Imagenes\\manzana.gif"));
                SumarAlTableroCaballoNegro(c);

            }

        } catch (Exception ex) {
            System.err.println("Error en el metodo Intercambio" + ex);
        };
    }//cierra el metodo

    //método para sumarle las manzanas a los tableros.
    public void SumarAlTableroCaballoNegro(Casilla manzana) {
        panelizq.add(manzana, CENTER);
        RestarManzanas();
    }

    //método que ayuda a identificar a la maquina donde esta situado su rival, las manzanas y el mismo.
    public void ReunirInformacion_Maquina() {
        //variables globales para almacenar el estado del juego y representarlo como 
        Nodo estado_juego = new Nodo();
        int posicionCaballoBlanco[] = new int[2];
        int posicionCaballoNegro[] = new int[2];
        ArrayList<int[]> posicionManzanas = new ArrayList<int[]>();

        //FOR PARA ACTIVAR Y DESACTVAR EL TURNO DEL CABALLO BLANCO HASTA QUE LA MAQUINA MUEVA.
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (this.tablero_logico[i][j].getYosoy() == "caballoB") {
                    this.tablero_logico[i][j].setTurno(false);//despues de haberme movido se puede mover mi adversario.
                }
            }
        }

        //recorro el tablero y miro donde estan los caballos y las manzanas. 
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (this.tablero_logico[i][j].getYosoy() == "caballoB") {
                    posicionCaballoBlanco[0] = tablero_logico[i][j].getFila();
                    posicionCaballoBlanco[1] = tablero_logico[i][j].getColumna();
                    System.out.println("El caballo Blanco esta en: " + posicionCaballoBlanco[0] + "," + posicionCaballoBlanco[1]);
                } else if (this.tablero_logico[i][j].getYosoy() == "caballoN") {
                    posicionCaballoNegro[0] = tablero_logico[i][j].getFila();
                    posicionCaballoNegro[1] = tablero_logico[i][j].getColumna();
                    System.out.println("El caballo Negro esta en: " + posicionCaballoNegro[0] + "," + posicionCaballoNegro[1]);
                } else if (this.tablero_logico[i][j].getYosoy() == "manzana") {
                    int posicion[] = new int[2];
                    posicion[0] = tablero_logico[i][j].getFila();
                    posicion[1] = tablero_logico[i][j].getColumna();
                    posicionManzanas.add(posicion);
                    System.out.println("Una manzana en: " + posicion[0] + "," + posicion[1]);
                }//cierra cadena de if y else
            }//cierra for1        
        }//cierra for2
        //se introducen los valores al estado sobre el cual el arbol se construira y tomara la decision de que movimiento realizar
        estado_juego.setManzanas(this.numeroManzanas);
        estado_juego.setManzanasBlancas(this.manzanasBlancas);
        estado_juego.setManzansNegras(manzanasNegras);
        estado_juego.setPadre(null);
        estado_juego.setPosicionCaballoBlanco(posicionCaballoBlanco);
        estado_juego.setPosicionCaballoNegro(posicionCaballoNegro);
        estado_juego.setPosicion_Manzanas(posicionManzanas);
        estado_juego.setProfundidad(0);
        estado_juego.setTipo(MAX);
        estado_juego.setUtilidad(INF_MEN);
        //intancia el arbol que tomara la decision del juego. 
        arbol_de_decision = new Arbol(estado_juego);
        int decision[] = arbol_de_decision.getDecision();
        System.out.println("El caballo negro se movera a: " + decision[0] + "," + decision[1]);
        Casilla destino = this.tablero_logico[decision[0]][decision[1]];
        Casilla caballo = new Casilla();
        //activa el caballo blanco
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (this.tablero_logico[i][j].getYosoy() == "caballoN") {
                    caballo = this.tablero_logico[i][j];
                }
            }
        }
        IntercambiarCaballoNegro(caballo, destino);

        //activa el caballo blanco
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (this.tablero_logico[i][j].getYosoy() == "caballoB") {
                    this.tablero_logico[i][j].setTurno(true);//despues de haberme movido se puede mover mi adversario.
                }
            }
        }

    }//cierra el metodo que reune la informacion del juego, y se la pasa al arbol para que decida a donde ir.

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout());

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Tablero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tablero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tablero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tablero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tablero().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
