package alphazero_u;

import alphazero_u.Casilla;
import alphazero_u.Nodo;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.BorderFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ASUS-LGR23
 */
public class Arbol {

    //atributos de la clase arbol
    private Nodo nodo;
    private int decision[] = new int[2];
    private static final int MIN = 0, MAX = 1, INF_MAS = (1000 * 9999999), INF_MEN = (-1000 * 9999999);
    private ArrayList<Nodo> pila = new ArrayList<Nodo>();
    private ArrayList<Nodo> pilaAuxiliar = new ArrayList<Nodo>();
    private Comparator<Nodo> comparador = new Comparator<Nodo>() {
        @Override
        public int compare(Nodo p1, Nodo p2) {
            // Aqui esta el truco, ahora comparamos p2 con p1 y no al reves como antes
            return new Integer(p2.getProfundidad()).compareTo(new Integer(p1.getProfundidad()));
        }
    };

    //bloque de métodos para la clase:
    //constructor del arbol, apartir de un estado incial dado
    public Arbol(Nodo nodo) {
        this.nodo = nodo;
    }

    //metodo que calcula los movimientos de los caballos
    public ArrayList<int[]> MovimientosCaballos(int[] coordenada) {
        //metodo que construye los movimientos del caballo en el tablero

        //bloque de variables locales:
        int fila = coordenada[0];
        int columna = coordenada[1];
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
    public ArrayList<int[]> ValidarMovimientos_Tablero(ArrayList<int[]> movimientosCaballos, int adversario[]) {
        //variables locales:
        ArrayList<int[]> movValidos = new ArrayList<int[]>();

        //ciclo que valida que los saltos del caballo no se salga del tablero.
        for (int i = 0; i < movimientosCaballos.size(); i++) {
            //si no se sale del tablero mire que no le salte encima al otro caballo.
            if ((movimientosCaballos.get(i)[0] >= 0) && (movimientosCaballos.get(i)[0] <= 5)
                    && (movimientosCaballos.get(i)[1] >= 0) && (movimientosCaballos.get(i)[1] <= 5)) {
                if ((movimientosCaballos.get(i)[1] != adversario[0]) || (movimientosCaballos.get(i)[1] != adversario[1])) {
                    movValidos.add(movimientosCaballos.get(i));
                }//cierra if.                
            }//cierra el if. 
        }//cierra el for.

        return movValidos;
    }//cierra el método.

    //método para expandir los nodos del juego dado un punto incial.
    public ArrayList<Nodo> Expandir(Nodo nodo) {
        System.out.println("voy a expandir: ---------------" + "Tipo: " + nodo.getTipo());
        ArrayList<Nodo> expansion = new ArrayList<Nodo>();
        ArrayList<int[]> posicionesposibles;
        int adversario[];
        if (nodo.getTipo() == MAX) {
            adversario = nodo.getPosicionCaballoBlanco();
            posicionesposibles = ValidarMovimientos_Tablero(MovimientosCaballos(nodo.getPosicionCaballoNegro()), adversario);
        } else {
            adversario = nodo.getPosicionCaballoNegro();
            posicionesposibles = ValidarMovimientos_Tablero(MovimientosCaballos(nodo.getPosicionCaballoBlanco()), adversario);
        }//cierra if else.

        //ciclo for que se encarga de para expancion de un movimiento del caballo, determinar el estado en que queda el juego, y
        //convertir esa informacion en objetos nodo.
        for (int i = 0; i < posicionesposibles.size(); i++) {
            int profun = nodo.getProfundidad() + 1;
            int manzanasN = nodo.getManzansNegras();
            int manzanasB = nodo.getManzanasBlancas();
            int manzanas = nodo.getManzanas();

            if (nodo.getTipo() == MAX) {
                ArrayList<int[]> pmanzanas = nodo.getPosicion_Manzanas();
                //con este ciclo elimino las manzanas que se vaya comiendo en una linea de juego.
                for (int j = 0; j < pmanzanas.size(); j++) {
                    if ((posicionesposibles.get(i)[0] == pmanzanas.get(j)[0]) && (posicionesposibles.get(i)[1] == pmanzanas.get(j)[1])) {
                        pmanzanas.remove(j);
                        if (nodo.getTipo() == MAX) {
                            manzanasN += 1;
                            manzanas -= 1;
                        } else if (nodo.getTipo() == MIN) {
                            manzanasB += 1;
                            manzanas -= 1;
                        }//cierra else if.
                    }//cierra if
                }//cierra for
                Nodo aux = new Nodo();
                aux.setManzanas(manzanas);
                aux.setManzanasBlancas(manzanasB);
                aux.setManzansNegras(manzanasN);
                aux.setPadre(nodo);
                aux.setPosicionCaballoBlanco(adversario);
                aux.setPosicionCaballoNegro(posicionesposibles.get(i));
                aux.setPosicion_Manzanas(pmanzanas);
                aux.setProfundidad(profun);
                aux.setTipo(MIN);
                aux.setUtilidad(INF_MAS);
                expansion.add(aux);
                System.out.println("--manzanas en: ");
                for (int l = 0; l < pmanzanas.size(); l++) {
                    System.out.println(pmanzanas.get(l)[0] + "," + pmanzanas.get(l)[1]);
                }
                System.out.println("Expandi un nodo: " + aux.getTipo() + "\n"
                        + "con profundidad: " + aux.getProfundidad() + "\n"
                        + "Utilidad: " + aux.getUtilidad() + "\n"
                        + "Items en juego: " + aux.getManzanas() + "\n"
                        + "El caballo negro esta en: " + aux.getPosicionCaballoNegro()[0] + "," + aux.getPosicionCaballoNegro()[1] + "\n"
                        + "El caballo blanco esta en: " + aux.getPosicionCaballoBlanco()[0] + "," + aux.getPosicionCaballoBlanco()[1] + "\n"
                        + "Items caballo balnco: " + aux.getManzanasBlancas() + "\n"
                        + "Items caballo negro: " + aux.getManzansNegras() + "\n"
                        + "El nodo padre es: " + aux.getPadre().getPosicionCaballoNegro()[0] + "," + aux.getPadre().getPosicionCaballoNegro()[1] + "\n");
               

            } else if (nodo.getTipo() == MIN) {
                ArrayList<int[]> pmanzanas = nodo.getPosicion_Manzanas();
                //con este ciclo elimino las manzanas que se vaya comiendo en una linea de juego.
                for (int j = 0; j < pmanzanas.size(); j++) {
                    if ((posicionesposibles.get(i)[0] == pmanzanas.get(j)[0]) && (posicionesposibles.get(i)[1] == pmanzanas.get(j)[1])) {
                        pmanzanas.remove(j);
                        if (nodo.getTipo() == MAX) {
                            manzanasN += 1;
                            manzanas -= 1;
                        } else if (nodo.getTipo() == MIN) {
                            manzanasB += 1;
                            manzanas -= 1;
                        }//cierra else if.
                    }//cierra if
                }//cierra for
                Nodo aux = new Nodo();
                aux.setManzanas(manzanas);
                aux.setManzanasBlancas(manzanasB);
                aux.setManzansNegras(manzanasN);
                aux.setPadre(nodo);
                aux.setPosicionCaballoBlanco(posicionesposibles.get(i));
                aux.setPosicionCaballoNegro(adversario);
                aux.setPosicion_Manzanas(pmanzanas);
                aux.setProfundidad(profun);
                aux.setTipo(MAX);
                aux.setUtilidad(INF_MEN);
                expansion.add(aux);
                System.out.println("--manzanas en: ");
                for (int l = 0; l < pmanzanas.size(); l++) {
                    System.out.println(pmanzanas.get(l)[0] + "," + pmanzanas.get(l)[1]);
                }
                System.out.println("Expandi un nodo: " + aux.getTipo() + "\n"
                        + "con profundidad: " + aux.getProfundidad() + "\n"
                        + "Utilidad: " + aux.getUtilidad() + "\n"
                        + "Items en juego: " + aux.getManzanas() + "\n"
                        + "El caballo negro esta en: " + aux.getPosicionCaballoNegro()[0] + "," + aux.getPosicionCaballoNegro()[1] + "\n"
                        + "El caballo blanco esta en: " + aux.getPosicionCaballoBlanco()[0] + "," + aux.getPosicionCaballoBlanco()[1] + "\n"
                        + "Items caballo balnco: " + aux.getManzanasBlancas() + "\n"
                        + "Items caballo negro: " + aux.getManzansNegras() + "\n"
                        + "El nodo padre es: " + aux.getPadre().getPosicionCaballoBlanco()[0] + "," + aux.getPadre().getPosicionCaballoBlanco()[1] + "\n");
                
            }//cierra else if de asignaciones a volores de los nodos MAX y MIN
        }//cierra el for que se encarga de registrar para cada movmiento posible, los datos que generaria en el juego
        //y los asigna a un objeto nodo,    que representa el estado del juego tras ese movimiento.
        return expansion;
    }//cierra el metodo expandir

    //método para construir un arbol de profundidad 8 tal que se pueda tomar una decision a partir de este.
    public void ConstruirArbol() {

        //el primer elemento de la pila es el nodo Max de profundidad 0 que nos pasan
        pila.add(nodo);
        ArrayList<Nodo> aux = Expandir(nodo);
        for (int x = 0; x < aux.size(); x++) {
            pila.add(aux.get(x));//añade los nodos a la pila
        }
        pilaAuxiliar.add(pila.get(0));
        pila.remove(0);
        //ordena la pila de mayor a menor para el proceso de expansion.
       // Collections.sort(pila, comparador);

        while (pila.size() > 0) {

            if (pila.get(0).getProfundidad() == 6) {
                pilaAuxiliar.add(pila.get(0));
                pila.remove(0);
                //ordena la pila de mayor a menor para el proceso de expansion.
                //Collections.sort(pila, comparador);
            } else {
                ArrayList<Nodo> aux2 = Expandir(pila.get(0));
                for (int i = 0; i < aux2.size(); i++) {
                    pila.add(aux2.get(i));//añade lo nodos expandidos a la pila.
                }
                pilaAuxiliar.add(pila.get(0));
                pila.remove(0);
                //ordena la pila de mayor a menor para el proceso de expansion.
                Collections.sort(pila, comparador);
            }
            //pila.remove(0);
        }

        Collections.sort(pilaAuxiliar, comparador);


        /* 
         for (int i=0;i<8;i++){
           if (i==0){
             ArrayList<Nodo> aux = Expandir(pila.get(k));
             for (int j=0;j<aux.size();j++){
                    pila.add(aux.get(0));//añade los nodos a la pila
             }
             //ordena la pila de mayor a menor para el proceso de expansion.
             Collections.sort(pila, comparador);
           }else if (i>0){
                ArrayList<Nodo> aux = Expandir(pila.get(k));
                for (int j=0;j<aux.size();j++){
                    pila.add(aux.get(j));//añade lo nodos expandidos a la pila.
                 }
             //ordena la pila de mayor a menor para el proceso de expansion.
             Collections.sort(pila, comparador);
             
           }//cierra if else
         }//cierra for.
        
         */
    }//cierra el método construir arbol.

    //método para calcular la utilidad de un estado del juego.
    public int CalcularUtilidad(Nodo nodoFinal) {
        //la utilidad es la diferencia de manzanas entre los dos adversarios.
        int utiliti = nodoFinal.getManzansNegras() - nodoFinal.getManzanasBlancas();
        ArrayList<int []> manza =  nodoFinal.getPosicion_Manzanas();
       int cercania = 0;
        for (int i=0;i<manza.size();i++){
             int fila = manza.get(i)[0];
             int columna = manza.get(i)[1];
             int cabaFila = nodoFinal.getPosicionCaballoNegro()[0];
             int cabaColu = nodoFinal.getPosicionCaballoNegro()[1];
             int  euristica = Math.abs(fila - cabaFila) + Math.abs(columna - cabaColu);
             if (euristica == 3){
                 cercania = euristica;
             }
        }

        return utiliti+cercania;
    }

    //método para asignar la Utilidada todos los nodos hoja.
    public void UtilidadParaLasHojas() {
        //hace un ciclo para ponerle la utilidad a todos los nodos hojas. 
        for (int i = 0; i < pilaAuxiliar.size(); i++) {
            if (pilaAuxiliar.get(i).getProfundidad() == 6) {
                pilaAuxiliar.get(i).setUtilidad(CalcularUtilidad(pilaAuxiliar.get(i)));
                // System.out.println("Tipo: " + pilaAuxiliar.get(i).getTipo() + " Utilidad: " + pilaAuxiliar.get(i).getUtilidad() + " Profundidad: " + pilaAuxiliar.get(i).getProfundidad());
            }//cierra if
        }//cierra for.
    }

    //método para calcular el movimiento basado en la decision minimax
    public void AlgoritmoMinMax() {

        UtilidadParaLasHojas();
        Collections.sort(pilaAuxiliar, comparador);

        System.out.println("Tomando decisión MinMax:---------------------------------------------------" + "\n");
        //recorro la pila ajustando la utilidad.
        System.out.println("Expansiones del arbol: " + pilaAuxiliar.size() + " Nodos.");
        int i = 0;
        while (i < pilaAuxiliar.size() - 1) {

            
            if (pilaAuxiliar.get(i).getPadre().getTipo() == MAX) {
                if (pilaAuxiliar.get(i).getPadre().getUtilidad() < pilaAuxiliar.get(i).getUtilidad()) {
                    // System.out.println("Utilidad del padre antes: "+pilaAuxiliar.get(i).getPadre().getUtilidad());
                    pilaAuxiliar.get(i).getPadre().setUtilidad(pilaAuxiliar.get(i).getUtilidad());
                    //System.out.println("Utilidad del padre Ahora: "+pilaAuxiliar.get(i).getPadre().getUtilidad());
                    decision = pilaAuxiliar.get(i).getPosicionCaballoNegro();
                    pilaAuxiliar.remove(i);
                }
            } else {
                if (pilaAuxiliar.get(i).getPadre().getUtilidad() > pilaAuxiliar.get(i).getUtilidad()) {
                    //System.out.println("Utilidad del padre antes: "+pilaAuxiliar.get(i).getPadre().getUtilidad());
                    pilaAuxiliar.get(i).getPadre().setUtilidad(pilaAuxiliar.get(i).getUtilidad());
                    //System.out.println("Utilidad del padre Ahora: "+pilaAuxiliar.get(i).getPadre().getUtilidad());
                    decision = pilaAuxiliar.get(i).getPosicionCaballoNegro();
                    pilaAuxiliar.remove(i);
                }

            }

            i++;
        }

        System.out.println("--------------Utilidad final:---------------------" + "\n"
                + "Utilidad: " + pilaAuxiliar.get(pilaAuxiliar.size() - 1).getUtilidad() + "\n");

    }

    //retorna la decision final
    public int[] getDecision() {
        //este método construye el arbol de decision y aplica minmax para obetner el movimiento a realizar.
        ConstruirArbol();
        AlgoritmoMinMax();
        this.pilaAuxiliar.removeAll(pilaAuxiliar);
        return decision;
    }

}
