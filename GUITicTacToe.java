/* This project is a version of TicTacToe that utilizes a Java GUI (allowing for 
 * a more user-friendly interface than just the console. 
 * Programmer: Jason Randolph
   Last Modified: 10-29-21 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

// "implements ActionListener" is inserted to allow for action listeners in the code.
public class GUITicTacToe implements ActionListener {

	/* Elements of the game are created (e.g. the JFrame and other components,
	as well as strings and integers that are needed for the game. */
	JFrame frame = new JFrame();
	JButton[][] button = new JButton[3][3];
	int board[][] = new int[3][3];
	final int BLANK = 0;
	final int X_MOVE = 1;
	final int O_MOVE = 2;
	final int X_TURN = 0;
	final int O_TURN = 1;
	int turn = X_TURN;
	int playerXWins = 0;
	int playerOWins = 0;
	boolean legalChangeName = true;
	Container center = new Container();
	Container north = new Container();
	
	// This container wasn't in the guidance video. It'll be used to state who won each game.
	Container south = new Container();
	String playerXName = "X";
	String playerOName = "O";
	JLabel xName = new JLabel(playerXName + "'s wins: " + playerXWins);
	JLabel oName = new JLabel(playerOName + "'s wins: " + playerOWins);
	JLabel noteArea = new JLabel("Notes will appear here.");
	JButton xChangeName = new JButton("Change Player X's name:");
	JButton oChangeName = new JButton("Change Player O's name:");
	JTextField xChangeField = new JTextField();
	JTextField oChangeField = new JTextField();
	
	public GUITicTacToe() {
		// Setting the characteristics of the frame.
		frame.setSize(400, 400);
		frame.setLayout(new BorderLayout());
		
		// Making the grid layout in the center of the window (i.e. the actual board).
		center.setLayout(new GridLayout(3,3));
		for (int i = 0; i < button.length; i++) {
			for (int j = 0; j < button[0].length; j++) {
				button[j][i] = new JButton();
				center.add(button[j][i]);
				button[j][i].addActionListener(this);
			}
		}
		frame.add(center, BorderLayout.CENTER);
		/* Adding and editing elements of the frame. This includes the labels, buttons, grid, 
		 * and note section at the bottom of the frame. The defaultCloseOperation is also set to 
		 * "exit on close" (meaning that the program will terminate upon closing the window)
		   and the frame is set to visible. */
		north.setLayout(new GridLayout(3,2));
		north.add(xName);
		north.add(oName);
		north.add(xChangeName);
		xChangeName.addActionListener(this);
		north.add(oChangeName);
		oChangeName.addActionListener(this);
		north.add(xChangeField);
		north.add(oChangeField);
		south.setLayout(new BorderLayout());
		south.add(noteArea);
		frame.add(south, BorderLayout.SOUTH);
		frame.add(north, BorderLayout.NORTH);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	// Main method. 
	public static void main(String[] args) {
		new GUITicTacToe();
	}

	// Method for the manipulation of the grid.
	@Override
	public void actionPerformed(ActionEvent event) {
		JButton current;
		
		/* This boolean is set to false initially. If the actionPerformed is to a grid button,
		 * causing the following code to be executed. If not, gridButton stays false and 
		   it continues to the next if statement. */
		boolean gridButton = false;
		for (int i = 0; i < button.length; i++) {
			for (int j = 0; j < button[0].length; j++) {
				if (event.getSource().equals(button[j][i])) {
					gridButton = true;
					current = button[j][i];
					
					/* If the board spot is blank, and it's that player's turn, the button text
					 * is set to that player's variable, the button is disabled, the array value 
					   is set to that player's number, and the turn shifts to the other player. */
					if (board[j][i] == BLANK) {
						if (turn == X_TURN) {
							current.setText("X");
							current.setEnabled(false);
							board[j][i] = X_MOVE;
							turn = O_TURN;
						} else {
							current.setText("O");
							current.setEnabled(false);
							board[j][i] = O_MOVE;
							turn = X_TURN;
						}
					}
					
					/* After each button is pressed, the checkWin method will be evaluated. If it's
					 * true, the win counter for that player increases, that player's label updates to
					 * reflect the new win count, and the board is cleared to prepare for another match.
					   I also added a new label that tells the user which player just won. */
					if (checkWin(X_MOVE) == true) {
						playerXWins ++;
						xName.setText(playerXName + "'s wins: " + playerXWins);
						noteArea.setText(playerXName + " has won!");
						clearBoard();
					} else if (checkWin(O_MOVE) == true) {
						playerOWins ++;
						oName.setText(playerOName + "'s wins: " + playerOWins);
						noteArea.setText(playerOName + " has won!");
						clearBoard();
						
					// If a tie occurs, the label mentions it and the board is reset.
					} else if (checkTie() == true) {
						noteArea.setText("That was a tie. Nobody won.");
						clearBoard();
					}
				}
			}
		}
		
		/* This if statement checks to see if either of the name change buttons were clicked. If 
		 * so, it'll set the player name to whatever was in the text field. "getText()" method taken 
		 * from https://docs.oracle.com/javase/tutorial/uiswing/components/textfield.html. */
		if(gridButton == false) {
			if(event.getSource().equals(xChangeName) == true) {
				
				/* This code runs the name-change. I used a boolean to check whether the text field
				 * is empty or not. If it is empty, the boolean is set to false. This skips the next if 
				 * statement, which contains the name change code. If the text field isn't empty, the boolean
				   remains true and the if statement containing the name change code will activate. */
				if (xChangeField.getText().equals("")) {
					legalChangeName = false;
				}
				
				/* If the boolean is true, the player's name is changed to whatever code is in the 
				 * text field. The label is updated accordingly. I also added some code to reset the 
				 * content of the text field. This changes the name in the "change name" button so that 
				   it reflects the new name and doesn't just say "change Player X's name". */
				if (legalChangeName == true) {
					playerXName = xChangeField.getText();
					xName.setText(playerXName + "'s wins: " + playerXWins);
					xChangeField.setText("");
					xChangeName.setText("Change " + playerXName + "'s name:");
				}
				
				/* The boolean is reset to true. If I didn't do this and the player tried to change their name
				 * with an empty text field, the boolean would stay false and there would be no way to get the 
				   value back to true, thus preventing them from changing their name again. */
				legalChangeName = true;
			} else if(event.getSource().equals(oChangeName) == true) {
				if (oChangeField.getText().equals("")) {
					legalChangeName = false;
				}
				if (legalChangeName == true) {
					playerOName = oChangeField.getText();
					oName.setText(playerOName + "'s wins: " + playerOWins);
					oChangeField.setText("");
					oChangeName.setText("Change " + playerOName + "'s name:");
				}
				legalChangeName = true;
			}
		}
	}
	
	// checkWin method - fairly simple. Checks for three identical values in a row and returns a boolean value.
	public boolean checkWin(int player) {
		if (board[0][0] == player && board[0][1] == player && board[0][2] == player) {
			return true;
		}
		if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
			return true;
		}
		if (board[0][0] == player && board[1][0] == player && board[2][0] == player) {
			return true;
		}
		if (board[1][0] == player && board[1][1] == player && board[1][2] == player) {
			return true;
		}
		if (board[2][0] == player && board[2][1] == player && board[2][2] == player) {
			return true;
		}
		if (board[0][1] == player && board[1][1] == player && board[2][1] == player) {
			return true;
		}
		if (board[0][2] == player && board[1][2] == player && board[2][2] == player) {
			return true;
		}
		if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
			return true;
		}
		return false;
	}
	
	// checkTie method - checks for blanks and will return true if there are no blanks and no one has won.
	public boolean checkTie() {
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[0].length; column++) {
				if (board[row][column] == BLANK) {
					return false;
				}
			}
		}
		return true;
	}
	
	/* This method clears the board. The array value is reset to BLANK, the button text is reset
	 * to a blank argument, and the button's status is set to enabled (so that it doesn't stay greyed out).
	   I also chose to reset the turn here as well. */
	public void clearBoard() { 
		for (int a = 0; a < board.length; a++) {
			for (int b = 0; b < board[0].length; b++) {
				board[a][b] = BLANK;
				button[a][b].setText("");
				button[a][b].setEnabled(true);
			}
		}
		turn = X_TURN;
	}

}