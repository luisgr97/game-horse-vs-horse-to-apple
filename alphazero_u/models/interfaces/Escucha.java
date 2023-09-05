package alphazero_u.models.interfaces;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import alphazero_u.models.Casilla;

public interface Escucha extends MouseListener{

        //Casilla management
        public void intercambiar(Casilla cs1, Casilla cs2);
        public void sumarAlTablero(Casilla manzana);

        //Events managment
        public void mouseClicked(MouseEvent me);
}
