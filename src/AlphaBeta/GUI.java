package AlphaBeta;

import java.awt.*;
import java.applet.*;

/**
 * Created by Francesco Mauri on 5/14/2017.
 */
public class GUI extends Applet{
    public void init() {

        setLayout(null);  // I will do the layout myself.

        setBackground(new Color(0,150,0));  // Dark green background.

      /* Create the components and add them to the applet. */

        CheckersCanvas game = new CheckersCanvas();
        // Note: The constructor creates the buttons game.resignButton
        // and game.newGameButton and the Label game.message.
        add(game);

        game.newGameButton.setBackground(Color.lightGray);
        add(game.newGameButton);

        game.resignButton.setBackground(Color.lightGray);
        add(game.resignButton);

        game.message.setForeground(Color.green);
        game.message.setFont(new Font("Serif", Font.BOLD, 14));
        add(game.message);

      /* Set the position and size of each component by calling
         its setBounds() method. */

        game.setBounds(20,20,164,164); // Note:  size MUST be 164-by-164 !
        game.newGameButton.setBounds(210, 60, 100, 30);
        game.resignButton.setBounds(210, 120, 100, 30);
        game.message.setBounds(0, 200, 330, 30);
    }
}
