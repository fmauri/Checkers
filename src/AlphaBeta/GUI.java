package AlphaBeta;

import java.awt.*;
import java.applet.*;

/**
 * Created by Francesco Mauri on 5/14/2017.
 */
public class GUI extends Applet{
    public void init() {

        setLayout(null);
        setBackground(new Color(0,100,150));
        CheckersCanvas game = new CheckersCanvas();
        add(game);

        game.newGameButton.setBackground(Color.lightGray);
        add(game.newGameButton);

        game.resignButton.setBackground(Color.lightGray);
        add(game.resignButton);

        game.message.setForeground(new Color(5,188,23));
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
