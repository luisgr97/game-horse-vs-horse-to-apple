package alphazero_u;

import javax.swing.UIManager.LookAndFeelInfo;

import alphazero_u.controllers.Tablero;

public class StartGame {

     private static String LOOK_AND_FEEL = "Nimbus";

     /**
     *  Start App
     */
    public static void main() {
        try {
            for (LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (LOOK_AND_FEEL.equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Tablero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tablero().setVisible(true);
            }
        });
    }
}
