package ticTacToe;

import java.util.*;
public class aiTicTacToe {

	public int player; //1 for player 1 and 2 for player 2
	public int depth;
	private int evals; //number of nodes evaluated
	private int step; //keep track of number of turns
	private List<List<positionTicTacToe>> winningLines;
	private int lookAheadCounter;  //counter to keep track of lookahead depth
	private int getStateOfPositionFromBoard(positionTicTacToe position, List<positionTicTacToe> board)
	{
		//a helper function to get state of a certain position in the Tic-Tac-Toe board by given position TicTacToe
		int index = position.x*16+position.y*4+position.z;
		return board.get(index).state;
	}
	
	public positionTicTacToe randomnessAIAlgorithm(List<positionTicTacToe> board, int player)
	{
		positionTicTacToe myNextMove = new positionTicTacToe(0,0,0);
		do
			{
				Random rand = new Random();
				int x = rand.nextInt(4);
				int y = rand.nextInt(4);
				int z = rand.nextInt(4);
				myNextMove = new positionTicTacToe(x,y,z);
			}while(getStateOfPositionFromBoard(myNextMove,board)!=0);
		return myNextMove;
	}
	public positionTicTacToe myMinimaxAlgorithm(List<positionTicTacToe> board, int player)
	{
		long startTime = System.currentTimeMillis();
		positionTicTacToe myNextMove = null;
		positionTicTacToe potentialMove = null;
		evals = 0;
		int bestScore;
		int score;
		int bestScorex = -1;
		int bestScorey = -1;
		int bestScorez = -1;
		int opponent = player == 1?2:1;
		
		bestScore = -1000000;
		
		//this is where the agent checks for the outcome of every possible move
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
				for(int k = 0; k<4;k++)
				{
					potentialMove = new positionTicTacToe(i,j,k);
					if(getStateOfPositionFromBoard(potentialMove,board)==0)
					{	
						//it makes a copy of the board to make sure it doesn't affect the actual board
						List<positionTicTacToe> copiedBoard = deepCopyATicTacToeBoard(board);
						makeMove(potentialMove, player, copiedBoard);
						score = minimax(opponent, copiedBoard);	//call the alpha-beta pruning function
						lookAheadCounter = 0;
						if(score > bestScore)
						{
							bestScore = score;
							bestScorex = i;
							bestScorey = j;
							bestScorez = k;
						}
					}
				}
		myNextMove = new positionTicTacToe(bestScorex,bestScorey,bestScorez,player);
		long endTime = System.currentTimeMillis();
		System.out.println("Player "+player+" evaluated "+evals+" nodes in turn "+step);
		long duration = (endTime - startTime);
		System.out.println("Player "+player+" took "+duration+" milliseconds for turn "+step);
		System.out.println();
		step++;
		return myNextMove;
	}
	public positionTicTacToe myAlphabetaAlgorithm(List<positionTicTacToe> board, int player)
	{
		long startTime = System.currentTimeMillis();
		positionTicTacToe myNextMove = null;
		positionTicTacToe potentialMove = null;
		evals = 0;
		int bestScore;
		int score;
		int bestScorex = -1;
		int bestScorey = -1;
		int bestScorez = -1;
		int opponent = player == 1?2:1;
		
		bestScore = -1000000;
		
		//this is where the agent checks for the outcome of every possible move
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
				for(int k = 0; k<4;k++)
				{
					potentialMove = new positionTicTacToe(i,j,k);
					if(getStateOfPositionFromBoard(potentialMove,board)==0)
					{	
						//it makes a copy of the board to make sure it doesn't affect the actual board
						List<positionTicTacToe> copiedBoard = deepCopyATicTacToeBoard(board);
						makeMove(potentialMove, player, copiedBoard);
						score = alphabeta(opponent, 1000, -1000, copiedBoard);	//call the alpha-beta pruning function
						lookAheadCounter = 0;
						if(score > bestScore)
						{
							bestScore = score;
							bestScorex = i;
							bestScorey = j;
							bestScorez = k;
						}
					}
				}
		myNextMove = new positionTicTacToe(bestScorex,bestScorey,bestScorez,player);
		long endTime = System.currentTimeMillis();
		System.out.println("Player "+player+" evaluated "+evals+" nodes in turn "+step);
		long duration = (endTime - startTime);
		System.out.println("Player "+player+" took "+duration+" milliseconds for turn "+step);
		System.out.println();
		step++;
		return myNextMove;
	}
	private int minimax(int turn, List<positionTicTacToe> board)
	{
		int opponent = 0;
		if(turn==1)
			opponent = 2;
		if(turn==2)
			opponent = 1;
		positionTicTacToe potentialMove = null;
		int value = 0;
		evals++;
		if(lookAheadCounter >= depth)
		{
			return heuristic(board, turn);
		}
		lookAheadCounter++;
		if(turn == player)
		{
			value = -1000000;
			for(int i = 0; i<4; i++)
				for(int j = 0; j<4;j++)
					for(int k = 0; k<4;k++)
					{
						potentialMove = new positionTicTacToe(i,j,k);
						if(getStateOfPositionFromBoard(potentialMove,board)==0)
						{	
							List<positionTicTacToe> copiedBoard = deepCopyATicTacToeBoard(board);
							makeMove(potentialMove, turn, copiedBoard);
							if(isEnded(turn, copiedBoard))
								return 1000000;
							//maximize score and set alpha value
							else
								value = Math.max(value, minimax(opponent, copiedBoard));
						}
					}
		}
		else
		{
			value = 1000000;
			for(int i = 0; i<4; i++)
				for(int j = 0; j<4;j++)
					for(int k = 0; k<4;k++)
					{
						potentialMove = new positionTicTacToe(i,j,k);
						if(getStateOfPositionFromBoard(potentialMove,board)==0)
						{	
							List<positionTicTacToe> copiedBoard = deepCopyATicTacToeBoard(board);
							makeMove(potentialMove, turn, copiedBoard);
							if(isEnded(turn, copiedBoard))
								return 1000000;
							//maximize score and set alpha value
							else
								value = Math.min(value, minimax(opponent, copiedBoard));
						}
					}
		}
		return value;
	}
	private int alphabeta(int turn, int alpha, int beta, List<positionTicTacToe> board)
	{
		int opponent = turn == 1?2:1;
		int a = alpha;
		int b = beta;
		positionTicTacToe potentialMove = null;
		int value = 0;
		evals++;
		if(lookAheadCounter >= depth)
		{
			return heuristic(board, turn);
		}
		lookAheadCounter++;
		check:
		//maximize the player's score
		if(turn == player)
		{
			value = -1000000;
			for(int i = 0; i<4; i++)
				for(int j = 0; j<4;j++)
					for(int k = 0; k<4;k++)
					{
						potentialMove = new positionTicTacToe(i,j,k);
						if(getStateOfPositionFromBoard(potentialMove,board)==0)
						{	
							List<positionTicTacToe> copiedBoard = deepCopyATicTacToeBoard(board);
							makeMove(potentialMove, turn, copiedBoard);
							if(isEnded(turn, copiedBoard))
								return 1000000;
							//maximize score and set alpha value
							else
								value = Math.max(value, alphabeta(opponent, a, b, copiedBoard));
								a = Math.max(value, a);
								if(a >= b)
									break check;
						}
					}
		}
		//minimize the opponent's score
		else
		{
			value = 1000000;
			for(int i = 0; i<4; i++)
				for(int j = 0; j<4;j++)
					for(int k = 0; k<4;k++)
					{
						potentialMove = new positionTicTacToe(i,j,k);
						if(getStateOfPositionFromBoard(potentialMove,board)==0)
						{	
							List<positionTicTacToe> copiedBoard = deepCopyATicTacToeBoard(board);
							makeMove(potentialMove, turn, copiedBoard);
							if(isEnded(turn, copiedBoard))
								return 1000000;
							//maximize score and set alpha value
							else
								value = Math.min(value, alphabeta(opponent, a, b, copiedBoard));
								b = Math.min(value, b);
								if(a >= b)
									break check;
						}
					}
		}
		return value;
	}
	//heuristic function to assign a value to node
	private int heuristic(List<positionTicTacToe> copiedBoard, int player)
	{
		int opponent = player == 1?2:1;
		int playerScore = 0;
		int opponentScore = 0;
		positionTicTacToe p;
		int posState;
		int value = 0;
		//calculate the player score for each line filled exclusively by the player
		for (int i=0;i<winningLines.size();i++)
		{
			int countPlayer = 0;
			for(int j=0;j<4;j++)
			{
				p = winningLines.get(i).get(j);
				posState = getStateOfPositionFromBoard(p,copiedBoard);
				if(posState==player)
					countPlayer++;
				if(posState==opponent)
					break;
			}
			switch (countPlayer) 
			{
				case 4:
					playerScore += 1000;
					break;
				case 3:
					playerScore += 100;
					break;
				case 2:
					playerScore += 10;
					break;
				case 1:
					playerScore += 1;
					break;
			}
		}
		//calculate the opponent score for each line filled exclusively by the opponent
		for (int i=0;i<winningLines.size();i++)
		{
			int countOpponent = 0;
			for(int j=0;j<4;j++)
			{
				p = winningLines.get(i).get(j);
				posState = getStateOfPositionFromBoard(p,copiedBoard);
				if(posState==opponent)
					countOpponent++;
				if(posState==player)
					break;
			}
			switch (countOpponent) 
			{
				case 4:
					opponentScore += 5000;
					break;
				case 3:
					opponentScore += 500;
					break;
				case 2:
					opponentScore += 50;
					break;
				case 1:
					opponentScore += 5;
					break;
			}
		}
		//penalize for opponent successes
		value = playerScore - opponentScore;
		return value;
	}
	private List<List<positionTicTacToe>> initializeWinningLines()
	{
		//create a list of winning line so that the game will "brute-force" check if a player satisfied any winning condition(s).
		List<List<positionTicTacToe>> winningLines = new ArrayList<List<positionTicTacToe>>();
		
		//48 straight winning lines
		//z axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,j,0,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//y axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,j,-1));
				winningLines.add(oneWinCondtion);
			}
		//x axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,j,-1));
				winningLines.add(oneWinCondtion);
			}
		
		//12 main diagonal winning lines
		//xz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,0,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,1,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,2,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//yz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,0,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//xy plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,0,i,-1));
				oneWinCondtion.add(new positionTicTacToe(1,1,i,-1));
				oneWinCondtion.add(new positionTicTacToe(2,2,i,-1));
				oneWinCondtion.add(new positionTicTacToe(3,3,i,-1));
				winningLines.add(oneWinCondtion);
			}
		
		//12 anti diagonal winning lines
		//xz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,3,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,2,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,1,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,0,-1));
				winningLines.add(oneWinCondtion);
			}
		//yz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,3,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,0,-1));
				winningLines.add(oneWinCondtion);
			}
		//xy plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,3,i,-1));
				oneWinCondtion.add(new positionTicTacToe(1,2,i,-1));
				oneWinCondtion.add(new positionTicTacToe(2,1,i,-1));
				oneWinCondtion.add(new positionTicTacToe(3,0,i,-1));
				winningLines.add(oneWinCondtion);
			}
		
		//4 additional diagonal winning lines
		List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,0,0,-1));
		oneWinCondtion.add(new positionTicTacToe(1,1,1,-1));
		oneWinCondtion.add(new positionTicTacToe(2,2,2,-1));
		oneWinCondtion.add(new positionTicTacToe(3,3,3,-1));
		winningLines.add(oneWinCondtion);
		
		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,0,3,-1));
		oneWinCondtion.add(new positionTicTacToe(1,1,2,-1));
		oneWinCondtion.add(new positionTicTacToe(2,2,1,-1));
		oneWinCondtion.add(new positionTicTacToe(3,3,0,-1));
		winningLines.add(oneWinCondtion);
		
		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(3,0,0,-1));
		oneWinCondtion.add(new positionTicTacToe(2,1,1,-1));
		oneWinCondtion.add(new positionTicTacToe(1,2,2,-1));
		oneWinCondtion.add(new positionTicTacToe(0,3,3,-1));
		winningLines.add(oneWinCondtion);
		
		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,3,0,-1));
		oneWinCondtion.add(new positionTicTacToe(1,2,1,-1));
		oneWinCondtion.add(new positionTicTacToe(2,1,2,-1));
		oneWinCondtion.add(new positionTicTacToe(3,0,3,-1));
		winningLines.add(oneWinCondtion);	
		
		return winningLines;
		
	}
	//helper function to copy the game board
	private List<positionTicTacToe> deepCopyATicTacToeBoard(List<positionTicTacToe> board)
	{
		List<positionTicTacToe> copiedBoard = new ArrayList<positionTicTacToe>();
		for (positionTicTacToe positionTicTacToe : board) {
			copiedBoard.add(new positionTicTacToe(positionTicTacToe.x, positionTicTacToe.y, positionTicTacToe.z, positionTicTacToe.state));
		}
		return copiedBoard;
	}
	//helper function from runTicTacToe
	private void makeMove(positionTicTacToe position, int player, List<positionTicTacToe> targetBoard)
	{
		for(int i=0;i<targetBoard.size();i++)
		{
			if(targetBoard.get(i).x==position.x && targetBoard.get(i).y==position.y && targetBoard.get(i).z==position.z) //if this is the position
			{
				if(targetBoard.get(i).state==0)
				{
					targetBoard.get(i).state = player;
					return;
				}
				else
				{
					System.out.println("Error: this is not a valid move.");
				}
			}
			
		}
	}
	//minimax with alpha beta pruning function
	private boolean isEnded(int player, List<positionTicTacToe> board)
	{
		//test whether the current game is ended
		
		//brute-force
		boolean win = false;
		for(int i=0;i<winningLines.size();i++)
		{
			
			positionTicTacToe p0 = winningLines.get(i).get(0);
			positionTicTacToe p1 = winningLines.get(i).get(1);
			positionTicTacToe p2 = winningLines.get(i).get(2);
			positionTicTacToe p3 = winningLines.get(i).get(3);
			
			int state0 = getStateOfPositionFromBoard(p0,board);
			int state1 = getStateOfPositionFromBoard(p1,board);
			int state2 = getStateOfPositionFromBoard(p2,board);
			int state3 = getStateOfPositionFromBoard(p3,board);
			
			//if they have the same state (marked by same player) and they are not all marked.
			if(state0 == state1 && state1 == state2 && state2 == state3 && state0==player)
			{
				//someone wins				
				//print the satisfied winning line (one of them if there are several)
				win = true;
			}
		}
		return win;
	}
	public aiTicTacToe(int setPlayer, int lookaheadDepth)
	{
		player = setPlayer;
		winningLines = initializeWinningLines();
		depth = lookaheadDepth;
		step = 1;
	}

}