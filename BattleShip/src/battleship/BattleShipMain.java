package battleship;

import java.util.Random;

import battleship.Board.Cell;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BattleShipMain extends Application {
	
	private boolean running = false;
	private Board enemyBoard, playerBoard;
	private int shipsToPlace = 5;
	private boolean enemyTurn = false;
	private Random random = new Random();
	
	private Parent createContent() {
		BorderPane root = new BorderPane();
		root.setPrefSize(600, 800);
		root.setRight(new Text("RIGHT SIDEBAR - CONTROLS"));
		
		enemyBoard = new Board(true, event ->{
			if(!running)
				return;
			
			Cell cell = (Cell) event.getSource();
			if(cell.wasShot)
				return;
			
			enemyTurn = !cell.shoot();
			
			if(enemyBoard.ships == 0) {
				System.out.println("YOU WIN");
				System.exit(0);
			}
			
			if(enemyTurn)
				enemyMove();
		});
		
		playerBoard = new Board(false, event ->{
			if(running)
				return;
			
			Cell cell = (Cell) event.getSource();
			
			if(playerBoard.placeShip(new Ship(shipsToPlace, event.getButton()== MouseButton.PRIMARY), cell.x, cell.y)) {
				if(--shipsToPlace == 0) {
					startGame();
				}
			}
			
		});
		
		VBox vbox = new VBox(50, enemyBoard, playerBoard);
		vbox.setAlignment(Pos.CENTER);
		
		root.setCenter(vbox);
		return root;
	}
	
	private void startGame() {
		int type = 5;
		
		while(type > 0) {
			
			int x = random.nextInt(10);
			int y = random.nextInt(10);
			
			if(enemyBoard.placeShip(new Ship(type, Math.random() < 0.5), x, y))
			type--;
		}
		
		running = true;
	}
	
	private void enemyMove() {
		while(enemyTurn) {
			int x = random.nextInt(10);
			int y = random.nextInt(10);
			
			Cell cell = playerBoard.getCell(x, y);
			
			if(cell.wasShot)
				continue;
			
			enemyTurn = cell.shoot();
			
			if(playerBoard.ships == 0) {
				System.out.println("YOU LOSE");
				System.exit(0);
			}
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(createContent());
		primaryStage.setTitle("Battleship");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
