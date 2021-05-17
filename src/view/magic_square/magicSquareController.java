package view.magic_square;

import module.Condition;
import module.magic_square.MagicSquare;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Optional;

public class magicSquareController {

    @FXML
    private FlowPane Board;
    @FXML
    private TextField SumField;
    @FXML
    private TextField DimField;
    @FXML
    private Slider sizeSlider;
    @FXML
    private RadioButton ConstraintYes;
    @FXML
    private RadioButton ConstraintNo;
    @FXML
    private MenuBar Menu;
    @FXML
    private Label ConstraintTip;
    @FXML
    private Button ConditionButton;
    @FXML
    private TextField ErrorField;
    @FXML
    private TextField GenerationField;

    private Stage MS_stage;
    private Timeline timeline;


    private Label[][] grid;
    private int size = 20;
    private double board_height;
    private double board_width;
    private double grid_length ;

    private boolean clock[][];
    private boolean swapJudge;
    private int clock_x;
    private int clock_y;

    private boolean constraint[][];
    private boolean ifConstraint;
    private boolean errorOnceOrGet;//to reduce error calculate
//    private Condition state;

    private MagicSquare ms;

    public void setMS_stage(Stage MS_stage) {
        this.MS_stage = MS_stage;
    }

    @FXML
    private void initialize() {
//        Board.heightProperty().addListener((observable, oldValue, newValue) -> board_height = (double)newValue);
//        Board.widthProperty().addListener((observable, oldValue, newValue) -> board_width = (double)newValue);
        putBoard();
        sizeSlider.valueProperty().addListener((observable, oldValue, newValue) ->{
            size = (int)Math.floor((double)newValue);
            Board.getChildren().clear();
            putBoard();
        });
        Menu.setOnMouseEntered(e -> Menu.setOpacity(1));
        Menu.setOnMouseExited(e -> Menu.setOpacity(0));
        ConstraintNo.setSelected(true);
    }


    private void putBoard(){
        ms = new MagicSquare(size);
        clock = new boolean[size][size];
        constraint = new boolean[size][size];
        grid = new Label[size+2][size+2];
        board_width = Board.getPrefWidth();
        board_height= Board.getPrefHeight();
        grid_length = 680.0/(size+2);

        GenerationField.setText("0");
        SumField.setText(String.valueOf((1+size*size)*size/2));
        DimField.setText(String.valueOf(size));


        double preLength = (size+2) * (Math.ceil(grid_length));
        Board.setPrefSize(preLength,preLength);

        for(int i=0;i<size+2;i++){
            for (int j=0;j<size+2;j++){
                Label one_grid = new Label("0");

                one_grid.setAlignment(Pos.CENTER);
                grid[i][j] = one_grid;
//                grid[i][j].setMaxSize(grid_length, grid_length);
                grid[i][j].setPrefSize(grid_length, grid_length);
                Board.getChildren().add(one_grid);

//                grid[i][j].setStyle("-fx-border-color: #7000D6");
//
                if(i==0||j==0||i==size+1||j==size+1) {
                    grid[i][j].setStyle("-fx-border-color: #f2eada;-fx-font-family: 'FangSong';" +
                            "-fx-font-size: 10;-fx-font-weight: bold;-fx-text-fill: Blue;-fx-background-color: #EE7AE9");
                    continue;
                }

                if(size<=20)  grid[i][j].setStyle("-fx-border-color: #f2eada;-fx-font-family: 'FangSong';" +
                        "-fx-font-size: 15;-fx-font-weight: bold;-fx-text-fill: #f6f5ec");
                else if(size<=30) grid[i][j].setStyle("-fx-border-color: #f2eada;-fx-font-family: 'FangSong';" +
                        "-fx-font-size: 10;-fx-font-weight: bold;-fx-text-fill: #f6f5ec");
//                grid[i][j].setOpacity(0.5);

                int finalI = i;
                int finalJ = j;

                grid[i][j].textProperty().addListener((observable, oldValue, newValue) -> {
                    ms.getBoard()[finalI-1][finalJ-1] = Integer.parseInt(newValue);
                    ErrorField.setText(String.valueOf(ms.evl(ms.board)));

                    int value = Integer.parseInt(grid[0][finalJ].getText())
                            +Integer.parseInt((String)newValue)-Integer.parseInt(oldValue);
                    grid[0][finalJ].setText(String.valueOf(value));
                    grid[size+1][finalJ].setText(grid[0][finalJ].getText());

                    grid[finalI][0].setText(String.valueOf(Integer.parseInt(grid[finalI][0].getText())
                            +Integer.parseInt((String)newValue)-Integer.parseInt(oldValue)));
                    grid[finalI][size+1].setText(grid[finalI][0].getText());

                    if(finalI==finalJ) {
                        grid[0][0].setText(String.valueOf(Integer.parseInt(grid[0][0].getText())
                                +Integer.parseInt((String)newValue)-Integer.parseInt(oldValue)));
                        grid[size+1][size+1].setText(grid[0][0].getText());
                    }else if(finalI+finalJ==size+1) {
                        grid[0][size+1].setText(String.valueOf(Integer.parseInt(grid[0][size+1].getText())
                                +Integer.parseInt((String)newValue)-Integer.parseInt(oldValue)));
                        grid[size+1][0].setText(grid[0][size+1].getText());
                    }
                });

                grid[i][j].setOnMouseClicked(e -> {
                    if(e.getButton()== MouseButton.PRIMARY) ClickGrid(finalI, finalJ);
                    else if(e.getButton()==MouseButton.SECONDARY) {
                        if(ConstraintYes.isSelected()) {
                            if(!constraint[finalI-1][finalJ-1]) {
                                ms.getCnst()[finalI-1][finalJ-1] = 1;
                                constraint[finalI - 1][finalJ - 1] = true;
                                grid[finalI][finalJ].setBackground(new Background(new BackgroundFill(Color.valueOf("#EEB422"), null, null)));
                            }else {
                                ms.getCnst()[finalI-1][finalJ-1] = 0;
                                constraint[finalI - 1][finalJ - 1] = false;
                                grid[finalI][finalJ].setBackground(new Background(new BackgroundFill(null, null, null)));
                            }
                        }else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.initOwner(MS_stage);
                            alert.setTitle("Constraint Information");
                            alert.setHeaderText("Please Select Constraint Yes!!");
                            alert.showAndWait();
                        }
                    }
                });

                grid[i][j].setOnMouseEntered(e ->{
                    if(!constraint[finalI-1][finalJ-1])
                        grid[finalI][finalJ].setBackground(new Background(new BackgroundFill(Color.valueOf("#EE7AE9"),null,null)));
                        });
                grid[i][j].setOnMouseExited(e -> {
//                    System.out.println(clock[finalI-1][finalJ-1]);
                    if(!clock[finalI-1][finalJ-1]&&!constraint[finalI-1][finalJ-1])
                        grid[finalI][finalJ].setBackground(new Background(new BackgroundFill(null,null,null)));
                });
            }
        }
        putGrid(null);
//        System.out.println(grid[0][0].getWidth()*size);
    }

    public void putGrid(int [][] array){
        if(array==null) {
            for (int i = 1; i < size + 1; i++) {
                for (int j = 1; j < size + 1; j++) {
                    grid[i][j].setText(String.valueOf((i - 1) * size + j));
                }
            }
        }else {
            for (int i = 0; i < size ; i++) {
                for (int j = 0; j < size ; j++) {
                    grid[i][j].setText(String.valueOf(array[i][j]));
                }
            }
        }
    }

    public void ClickGrid(int i, int j) {
        if(!clock[i-1][j-1]&&!constraint[i-1][j-1]){
            if(swapJudge){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initOwner(MS_stage);
                alert.setTitle("Swap value");
                alert.setHeaderText("Do you want to change this two value");
                alert.setContentText("the value "+grid[clock_x][clock_y].getText()+ " with "+grid[i][j].getText());
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    String temp = grid[i][j].getText();
                    grid[i][j].setText(grid[clock_x][clock_y].getText());
                    grid[clock_x][clock_y].setText(temp);
                    swapJudge = false;
                    clock[clock_x-1][clock_y-1] = false;
                    grid[clock_x][clock_y].setBackground(new Background(new BackgroundFill(null,null,null)));
                }
            }else {
                swapJudge = true;
                clock_x = i; clock_y = j;
                grid[i][j].setBackground(new Background(new BackgroundFill(Color.valueOf("#EE7AE9"), null, null)));
                clock[i - 1][j - 1] = true;
            }
        }else if(!constraint[i-1][j-1]){
            grid[i][j].setBackground(new Background(new BackgroundFill(null,null,null)));
            clock[clock_x-1][clock_y-1] = false;
            swapJudge = false;
        }
    }

    public void GameCondition(){
        if(ConditionButton.getText().equals(Condition.START.toString())){
            ConditionButton.setText(Condition.PAUSED.toString());
            timeline = new Timeline(new KeyFrame(Duration.seconds(0.05), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    ms.nextGeneration();
                    GenerationField.setText(String.valueOf(Integer.parseInt(GenerationField.getText())+1));
                    if(Integer.parseInt(GenerationField.getText())%100==0){
                        putGrid(ms.board);
                    }
                }
            }));
            timeline.setCycleCount(100000);
            timeline.play();
        }else if(ConditionButton.getText().equals(Condition.PAUSED.toString())){
            ConditionButton.setText(Condition.CONTINUE.toString());
            if(timeline!=null&&timeline.getStatus()== Animation.Status.RUNNING) timeline.pause();
        }else {
            ConditionButton.setText(Condition.PAUSED.toString());
            if(timeline!=null&&timeline.getStatus()== Animation.Status.PAUSED) timeline.play();
        }
    }
}
