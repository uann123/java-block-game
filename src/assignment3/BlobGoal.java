package assignment3;

import java.awt.Color;

public class BlobGoal extends Goal{

	public BlobGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		/*
		 * ADD YOUR CODE HERE
		 */
		Color[][] array = board.flatten();
		boolean[][] visited = new boolean[array.length][array[0].length];

		int score = 0;
		for (int i = 0; i < array.length; i++){
			for (int j = 0; j < array[0].length; j++){
				if (array[i][j].equals(targetGoal)){
					if (!visited[i][j]){ //if has not been visited yet, if it is false
						score = Math.max(score, undiscoveredBlobSize(i,j,array,visited));
					}
				}

			}
		}
		return score;
	}

	@Override
	public String description() {
		return "Create the largest connected blob of " + GameColors.colorToString(targetGoal) 
		+ " blocks, anywhere within the block";
	}


	public int undiscoveredBlobSize(int i, int j, Color[][] unitCells, boolean[][] visited) {
		/*
		 * ADD YOUR CODE HERE
		 */


		//make sure bounds are within, already been visited or not target color
		if (i < 0 || i >= unitCells.length || j < 0 || j >= unitCells[0].length
		|| visited[i][j] || !unitCells[i][j].equals(targetGoal)){
			return 0;
		}

		visited[i][j] = true;
		int tot = 1;

		tot += undiscoveredBlobSize(i+1, j, unitCells, visited); //down
		tot += undiscoveredBlobSize(i-1, j, unitCells, visited); //up
		tot += undiscoveredBlobSize(i, j+1, unitCells, visited); //right
		tot += undiscoveredBlobSize(i, j-1, unitCells, visited); //left

		return tot;

	}

}
