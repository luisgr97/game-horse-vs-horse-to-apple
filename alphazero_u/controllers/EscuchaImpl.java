package alphazero_u.controllers;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import alphazero_u.models.Casilla;
import alphazero_u.models.interfaces.Escucha;

public class EscuchaImpl implements Escucha {

        private ArrayList<int[]> movimientos;

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
        @Override
        public void intercambiar(Casilla cs1, Casilla cs2) {

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
                tablero_logico[cs2.getFila()][cs2.getColumna()].setIcon(new ImageIcon("juego-Alpha-zero\\Imagenes\\caballo_blanco.gif"));//cambio el caballo de posicion.
                tablero_logico[cs2.getFila()][cs2.getColumna()].setYosoy("caballoB");
                tablero_logico[cs2.getFila()][cs2.getColumna()].setTurno(true);
                blanco = tablero_logico[cs2.getFila()][cs2.getColumna()];

                //pone el cuadro del tablero correspondiente.         
                if ((cs1.getFila() % 2 == 0) || (cs1.getFila() == 0)) {
                    if ((cs1.getColumna() % 2 == 0) || (cs1.getColumna() == 0)) {
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setIcon(new ImageIcon("juego-Alpha-zero\\Imagenes\\cuadro_claro.png"));
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setYosoy("casilla");
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setTurno(false);

                    } else {
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setIcon(new ImageIcon("juego-Alpha-zero\\Imagenes\\cuadro_oscuro.png"));
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setYosoy("casilla");
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setTurno(false);
                    }
                }//cierra if
                else {
                    if ((cs1.getColumna() % 2 == 0) || (cs1.getColumna() == 0)) {
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setIcon(new ImageIcon("juego-Alpha-zero\\Imagenes\\cuadro_oscuro.png"));
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setYosoy("casilla");
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setTurno(false);
                    } else {
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setIcon(new ImageIcon("juego-Alpha-zero\\Imagenes\\cuadro_claro.png"));
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setYosoy("casilla");
                        tablero_logico[cs1.getFila()][cs1.getColumna()].setTurno(false);
                    }//cierra else interno
                }//cierra else externo

                if (esManzana == true) {

                    System.out.println("Me comi una manzana");
                    IncrementarManzanasBlanco();
                    Casilla c = new Casilla();
                    c.setIcon(new ImageIcon("juego-Alpha-zero\\Imagenes\\manzana.gif"));
                    SumarAlTablero(c);

                }

            } catch (Exception ex) {
                System.err.println("Error en el metodo Intercambio" + ex);
            };
        }//cierra el metodo

        //método para sumarle las manzanas a los tableros.
        @Override
        public void sumarAlTablero(Casilla manzana) {
            panelder.add(manzana, CENTER);
            RestarManzanas();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            throw new UnsupportedOperationException("Unimplemented method 'mousePressed'");
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            throw new UnsupportedOperationException("Unimplemented method 'mouseReleased'");
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            throw new UnsupportedOperationException("Unimplemented method 'mouseEntered'");
        }

        @Override
        public void mouseExited(MouseEvent e) {
            throw new UnsupportedOperationException("Unimplemented method 'mouseExited'");
        }

    }
