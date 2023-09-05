package alphazero_u.controllers;

import java.awt.*;
import static java.awt.Label.*;
import java.awt.event.MouseListener;
import java.util.*;
import javax.swing.*;
import alphazero_u.models.Arbol;
import alphazero_u.models.Casilla;
import alphazero_u.models.Nodo;
import alphazero_u.models.constants.ResourceImagesPath;
import alphazero_u.models.interfaces.Escucha;

/**
 *
 * @author ASUS-LGR23
 */
public class Tablero extends JFrame {

    //atributos del juego
    private static final int MAX = 1, SECONDS = 2, INF_MEN = (-1000 * 9999999);
    private Random aleatoreo = new Random();
    private int numeroManzanas = 0;
    private int manzanasBlancas = 0, manzanasNegras = 0;
    private Casilla blanco, negro;

    //Atributos para la logica del juego
    private Arbol arbol_de_decision;
    private Casilla tablero_logico[][] = new Casilla[6][6];

    //Atributo para el manejo de eventos
    private Escucha escucha = new EscuchaImpl();

    //Atributos de los elementos graficos
    private JPanel panelTablero = new JPanel();
    private JPanel panelizq = new JPanel();
    private JPanel panelder = new JPanel();
    private Label etiquetaCPU, etiquetaJ1;

    public Tablero() {
        this.setTitle("ARBOLES DE DECISÓN - Capturar Manzanas");
        makeLeftPanel();
        makeTable();
        makeRigthPanel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout());
        pack();
    }

    //Methos Getter and Setters
    public static int getMax() {
        return MAX;
    }

    public static int getSeconds() {
        return SECONDS;
    }

    public static int getInfMen() {
        return INF_MEN;
    }

    public Random getAleatoreo() {
        return aleatoreo;
    }

    public void setAleatoreo(Random aleatoreo) {
        this.aleatoreo = aleatoreo;
    }

    public int getNumeroManzanas() {
        return numeroManzanas;
    }

    public void setNumeroManzanas(int numeroManzanas) {
        this.numeroManzanas = numeroManzanas;
    }

    public int getManzanasBlancas() {
        return manzanasBlancas;
    }

    public void setManzanasBlancas(int manzanasBlancas) {
        this.manzanasBlancas = manzanasBlancas;
    }

    public int getManzanasNegras() {
        return manzanasNegras;
    }

    public void setManzanasNegras(int manzanasNegras) {
        this.manzanasNegras = manzanasNegras;
    }

    public Casilla getBlanco() {
        return blanco;
    }

    public void setBlanco(Casilla blanco) {
        this.blanco = blanco;
    }

    public Casilla getNegro() {
        return negro;
    }

    public void setNegro(Casilla negro) {
        this.negro = negro;
    }

    public Arbol getArbol_de_decision() {
        return arbol_de_decision;
    }

    public void setArbol_de_decision(Arbol arbol_de_decision) {
        this.arbol_de_decision = arbol_de_decision;
    }

    public Casilla[][] getTablero_logico() {
        return tablero_logico;
    }

    public void setTablero_logico(Casilla[][] tablero_logico) {
        this.tablero_logico = tablero_logico;
    }

    public Escucha getEscucha() {
        return escucha;
    }

    public void setEscucha(Escucha escucha) {
        this.escucha = escucha;
    }

    public JPanel getPanelTablero() {
        return panelTablero;
    }

    public void setPanelTablero(JPanel panelTablero) {
        this.panelTablero = panelTablero;
    }

    public JPanel getPanelizq() {
        return panelizq;
    }

    public void setPanelizq(JPanel panelizq) {
        this.panelizq = panelizq;
    }

    public JPanel getPanelder() {
        return panelder;
    }

    public void setPanelder(JPanel panelder) {
        this.panelder = panelder;
    }

    public Label getEtiquetaCPU() {
        return etiquetaCPU;
    }

    public void setEtiquetaCPU(Label etiquetaCPU) {
        this.etiquetaCPU = etiquetaCPU;
    }

    public Label getEtiquetaJ1() {
        return etiquetaJ1;
    }

    public void setEtiquetaJ1(Label etiquetaJ1) {
        this.etiquetaJ1 = etiquetaJ1;
    }

    //Methods to make the view.
     private void makeLeftPanel(){
        panelizq.setLayout(new GridLayout(numeroManzanas + 1, 1));
        panelizq.setBackground(Color.BLACK);
        panelizq.setBorder(BorderFactory.createRaisedBevelBorder());
        this.etiquetaCPU = new Label("CPU");
        this.etiquetaCPU.setAlignment(CENTER);
        this.etiquetaCPU.setForeground(Color.WHITE);
        panelizq.add(this.etiquetaCPU);
        this.add(panelizq);
    }
    
    private void makeRigthPanel(){
        panelder.setLayout(new GridLayout(numeroManzanas + 1, 1));
        panelder.setBackground(Color.WHITE);
        panelder.setBorder(BorderFactory.createRaisedBevelBorder());
        this.etiquetaJ1 = new Label("JUGADOR");
        this.etiquetaJ1.setAlignment(CENTER);
        this.etiquetaJ1.setForeground(Color.BLACK);
        panelder.add(this.etiquetaJ1);
        this.add(panelder);
    }

    private void makeTable() {
        this.panelTablero.setLayout(new GridLayout(6, 6));
        this.panelTablero.setSize(500, 500);
        this.fullFilledTableSquares();
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 6; y++) {
                this.panelTablero.add(tablero_logico[x][y]);
            }
        }
        this.indexElements();
        this.panelder.setSize(500, 500);
        this.panelizq.setSize(500, 500);
        this.setSize(1500, 580);
        this.add(panelTablero);
    }

    private void indexElements(){
        Map<String, Integer> positions = this.indexHourses();
        this.indexApples(Integer.parseInt(JOptionPane.showInputDialog(
            "Con cuantas manzanas desea jugar: (3) (5) (7)")));
    }

    private Map<String, Integer> indexHourses() {
        Map <String, Integer> mapositionsp = new HashMap<>();
        mapositionsp.put("filaCB", aleatoreo.nextInt(6));
        mapositionsp.put("columnaCB", aleatoreo.nextInt(6));
        mapositionsp.put("filaCN", aleatoreo.nextInt(6));
        final String CABALLON = "caballoN";
        final String CABALLOB = "caballoB";
        tablero_logico[map.get("filaCN")][map.get("columnaCN")].setIcon(new ImageIcon(ResourceImagesPath.DARK_HORSE.getPath()));
        tablero_logico[map.get("filaCN")][map.get("columnaCN")].setYosoy(CABALLON);
        tablero_logico[map.get("filaCB")][map.get("columnaCB")].setIcon(new ImageIcon(ResourceImagesPath.LIGHT_HORSE.getPath()));
        tablero_logico[map.get("filaCB")][map.get("columnaCB")].setYosoy(CABALLOB);
        tablero_logico[map.get("filaCB")][map.get("columnaCB")].setTurno(true);
        return mapositionsp;
    }

    private void indexApples(int respuesta){
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
                tablero_logico[fila][columna].setIcon(new ImageIcon("juego-Alpha-zero\\Imagenes\\manzana.gif"));
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
                tablero_logico[fila][columna].setIcon(new ImageIcon("juego-Alpha-zero\\Imagenes\\manzana.gif"));
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
                tablero_logico[fila][columna].setIcon(new ImageIcon("juego-Alpha-zero\\Imagenes\\manzana.gif"));
                tablero_logico[fila][columna].setYosoy("manzana");
                filaAnt = fila;
                columnaAnt = columna;
            }//cierra for
        }
    }

    private void fullFilledTableSquares(){
        final String CASILLA = "casilla";
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                Casilla casilla = new Casilla();
                if ((i % 2 == 0) || (i == 0)) {
                    if ((j % 2 == 0) || (j == 0)) {
                        casilla.setIcon(new ImageIcon(ResourceImagesPath.LIGHT_SQUARE.getPath()));
                        casilla.setFila(i);
                        casilla.setColumna(j);
                        casilla.addMouseListener(escucha);
                        casilla.setYosoy(CASILLA);
                        this.tablero_logico[i][j] = casilla;

                    } else {
                        casilla.setIcon(new ImageIcon(ResourceImagesPath.DARK_SQUARE.getPath()));
                        casilla.setFila(i);
                        casilla.setColumna(j);
                        casilla.addMouseListener(escucha);
                        casilla.setYosoy(CASILLA);
                        this.tablero_logico[i][j] = casilla;
                    }
                }
                else {
                    if ((j % 2 == 0) || (j == 0)) {
                        casilla.setIcon(new ImageIcon(ResourceImagesPath.DARK_SQUARE.getPath()));
                        casilla.setFila(i);
                        casilla.setColumna(j);
                        casilla.addMouseListener(escucha);
                        casilla.setYosoy("casilla");
                        this.tablero_logico[i][j] = casilla;
                    } else {
                        casilla.setIcon(new ImageIcon(ResourceImagesPath.LIGHT_SQUARE.getPath()));
                        casilla.setFila(i);
                        casilla.setColumna(j);
                        casilla.addMouseListener(escucha);
                        casilla.setYosoy("casilla");
                        this.tablero_logico[i][j] = casilla;
                    }
                }
            }  
        }
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
            tablero_logico[cs2.getFila()][cs2.getColumna()].setIcon(new ImageIcon("juego-Alpha-zero\\Imagenes\\caballo_negro.gif"));//cambio el caballo de posicion.
            tablero_logico[cs2.getFila()][cs2.getColumna()].setYosoy("caballoN");
            blanco = tablero_logico[cs2.getFila()][cs2.getColumna()];

            //pone el cuadro del tablero correspondiente.         
            if ((cs1.getFila() % 2 == 0) || (cs1.getFila() == 0)) {
                if ((cs1.getColumna() % 2 == 0) || (cs1.getColumna() == 0)) {
                    tablero_logico[cs1.getFila()][cs1.getColumna()].setIcon(new ImageIcon("juego-Alpha-zero\\Imagenes\\cuadro_claro.png"));
                    tablero_logico[cs1.getFila()][cs1.getColumna()].setYosoy("casilla");
                } else {
                    tablero_logico[cs1.getFila()][cs1.getColumna()].setIcon(new ImageIcon("juego-Alpha-zero\\Imagenes\\cuadro_oscuro.png"));
                    tablero_logico[cs1.getFila()][cs1.getColumna()].setYosoy("casilla");
                }
            }//cierra if
            else {
                if ((cs1.getColumna() % 2 == 0) || (cs1.getColumna() == 0)) {
                    tablero_logico[cs1.getFila()][cs1.getColumna()].setIcon(new ImageIcon("juego-Alpha-zero\\Imagenes\\cuadro_oscuro.png"));
                    tablero_logico[cs1.getFila()][cs1.getColumna()].setYosoy("casilla");
                } else {
                    tablero_logico[cs1.getFila()][cs1.getColumna()].setIcon(new ImageIcon("juego-Alpha-zero\\Imagenes\\cuadro_claro.png"));
                    tablero_logico[cs1.getFila()][cs1.getColumna()].setYosoy("casilla");
                }//cierra else interno
            }//cierra else externo

            if (esManzana == true) {

                System.out.println("Me comi una manzana");
                IncrementarManzanasNegro();
                Casilla c = new Casilla();
                c.setIcon(new ImageIcon("juego-Alpha-zero\\Imagenes\\manzana.gif"));
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
    
}