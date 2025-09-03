package assignment3;

import java.awt.Color;

public class PerimeterGoal extends Goal{

	public PerimeterGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		/*
		 * ADD YOUR CODE HERE
		 */
		int scored = 0;
		Color [][] array = board.flatten(); //make it into an array
		int size = array.length;

		for (int i = 0; i < size; i++){
			if (array[i][0].equals(targetGoal)){ //left column
				scored++;
			}
			if (array[i][size-1].equals(targetGoal)){ //right column
				scored++;
			}
		}
		for (int j = 0; j < size; j++){
			if (array[0][j].equals(targetGoal)){ //first row
				scored++;
			}
			if (array[size-1][j].equals(targetGoal)){ //last row
				scored++;
			}
		}
		return scored;
	}

	@Override
	public String description() {
		return "Place the highest number of " + GameColors.colorToString(targetGoal) 
		+ " unit cells along the outer perimeter of the board. Corner cell count twice toward the final score!";
	}

}
