package com.psn;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.psn.engine.DecoderService;
import com.psn.engine.EncoderService;
import com.psn.models.FileStatus;
import com.psn.models.PSNFile;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Launcher extends Application {

	final static Logger logger = Logger.getLogger(Launcher.class);
	
	private Context context;
	
    private ObservableList<PSNFile> observableFileList = FXCollections.observableArrayList(p -> new Observable[] {
            new SimpleStringProperty(p.getAbsolutePath()), new SimpleObjectProperty<FileStatus>(p.getStatus()) });

    private TableView<PSNFile> table;

    private Group root = new Group();

    private ProgressIndicator encodingProgressIndicator = new ProgressIndicator(0);
    
    private ProgressIndicator decodingProgressIndicator = new ProgressIndicator(0);

    private StackPane stack = new StackPane();

    private VBox encodingVBox = new VBox(encodingProgressIndicator);

    private VBox decodingVBox = new VBox(decodingProgressIndicator);

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void start(Stage primaryStage) {

    	logger.info("Start application");
    	
    	/* Load initial context */
    	context = Context.getInstance();
    	
    	/* Check the running usb drive */
    	try{
    		context.checkUsbVidPid();
    	} catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	logger.info("Running on drive '"+context.getRunningDriveLetter()+"'");
    	
        final EncoderService encoderService = new EncoderService(observableFileList);
        final DecoderService decoderService = new DecoderService(observableFileList);

        observableFileList.addListener(createListener("observableFileList"));

        /* Set the title of the application */
        primaryStage.setTitle(Configuration.APPLICATION_NAME);
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(false);

        Label label = new Label(Configuration.UI_STAGE_TITLE_LABEL);
        label.setFont(new Font("Arial", 20));

        /* Define the table structure */
        table = new TableView<PSNFile>();
        table.setPlaceholder(new Label(Configuration.UI_MAIN_LABEL_TEXT));
        TableColumn<PSNFile, String> absolutePathCol = new TableColumn<PSNFile, String>(Configuration.UI_TABLE_COLUMN_FILE);
        TableColumn<PSNFile, FileStatus> statusCol = new TableColumn<PSNFile, FileStatus>(Configuration.UI_TABLE_COLUMN_STATUS);

        /*
         * Define how to fill data for each cell. Get value from property of PSNFile
         */
        absolutePathCol.setCellValueFactory(new PropertyValueFactory<>("absolutePath"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        absolutePathCol.setMinWidth(500);
        statusCol.setSortable(false);
        statusCol.setResizable(false);

        absolutePathCol.setCellFactory(column -> {
            return new TableCell<PSNFile, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        setStyle("-fx-font-weight: bold; -fx-alignment: CENTER-LEFT;");
                    }
                }
            };
        });

        statusCol.setCellFactory(column -> {
            return new TableCell<PSNFile, FileStatus>() {
                @Override
                protected void updateItem(FileStatus item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                    	setText(item.getLabel());
                    	String cellStyle = "-fx-alignment: CENTER;";
                    	String rowStyle = item.getRowUiStyle();
                    	setStyle(cellStyle);
                    	getTableRow().setStyle(rowStyle);
                    }
                }
            };
        });

        /* Create the button on row */
        TableColumn actionCol = new TableColumn<>("");
        actionCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PSNFile, Boolean>, ObservableValue<Boolean>>() {

            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<PSNFile, Boolean> p) {
                return new SimpleBooleanProperty(p.getValue() != null);
            }
            
        });
        actionCol.setSortable(false);
        actionCol.setResizable(false);
        actionCol.setStyle("-fx-alignment: CENTER;");

        /* Add the button to the cell */
        actionCol.setCellFactory(new Callback<TableColumn<PSNFile, Boolean>, TableCell<PSNFile, Boolean>>() {

            @Override
            public TableCell<PSNFile, Boolean> call(TableColumn<PSNFile, Boolean> p) {
                return new ButtonCell(table, Configuration.UI_DELETE_BUTTON);
            }

        });

        /* Add all the columns to the table */
        table.getColumns().addAll(absolutePathCol, statusCol, actionCol);

        /* Add the list for containing the files */
        table.setItems(observableFileList);

        /* Create the button 'Codifica' */
        Button encodeButton = new Button(Configuration.UI_ENCODE_BUTTON);
        encodeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                stack.setDisable(true);
                encodingVBox.setVisible(true);
                decodingVBox.setVisible(false);
                encoderService.restart();
                encoderService.setOnSucceeded(w -> {
                    encodingVBox.setVisible(false);
                    decodingVBox.setVisible(false);
                    stack.setDisable(false);
                });
            }
        });
        encodeButton.setDisable(true);
        encodeButton.setStyle("-fx-font-size:20; -fx-font-weight: bold;");

        /* Create the button 'Decodifica' */
        Button decodeButton = new Button(Configuration.UI_DECODE_BUTTON);
        decodeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	stack.setDisable(true);
            	decodingVBox.setVisible(true);
                encodingVBox.setVisible(false);
                decoderService.restart();
                decoderService.setOnSucceeded(w -> {
                    decodingVBox.setVisible(false);
                    encodingVBox.setVisible(false);
                    stack.setDisable(false);
                });
            }
        });
        decodeButton.setDisable(true);
        decodeButton.setStyle("-fx-font-size:20; -fx-font-weight: bold;");

        /* Handle the drag over on the table */
        table.setOnDragOver(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != table && event.getDragboard().hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                }
                event.consume();
            }
        });

        /* Handle the drag drop on the table */
        table.setOnDragDropped(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    List<File> fileList = new ArrayList<>();
                    List<File> files = db.getFiles();
                    for (File file : files) {
                        if (file.isDirectory()) {
                            fileList = Utils.getFiles(file.getAbsolutePath());
                        } else {
                            fileList.add(file);
                        }
                    }
                    for (File f : fileList) {
                        PSNFile psnFile = new PSNFile(f.getAbsolutePath());
                        observableFileList.add(psnFile);
                        //LOGGER.info("Add file '"+f.getAbsolutePath()+"'");
                    }
                    encodeButton.setDisable(false);
                    decodeButton.setDisable(false);
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
        
        /* Create the horizontal box containing the encode/decode buttons */
        HBox hBox = new HBox(10, encodeButton, decodeButton);
        hBox.setAlignment(Pos.CENTER);
        
        Region encodingVeil = new Region();
        encodingVeil.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4)");
        encodingVeil.setMaxWidth(800);
        encodingVeil.setMaxHeight(500);
        encodingProgressIndicator.setMaxSize(800, 500);
        encodingProgressIndicator.progressProperty().bind(encoderService.progressProperty());
        encodingVeil.visibleProperty().bind(encoderService.runningProperty());
        encodingProgressIndicator.visibleProperty().bind(encoderService.runningProperty());
        // table.itemsProperty().bind(service.valueProperty());

        Region decodingVeil = new Region();
        decodingVeil.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4)");
        decodingVeil.setMaxWidth(800);
        decodingVeil.setMaxHeight(500);
        decodingProgressIndicator.setMaxSize(800, 500);
        decodingProgressIndicator.progressProperty().bind(decoderService.progressProperty());
        decodingVeil.visibleProperty().bind(decoderService.runningProperty());
        decodingProgressIndicator.visibleProperty().bind(decoderService.runningProperty());
        
        /* Create the vertical box containing the title, the table and the buttons */
        VBox vBox = new VBox(0, label, table, hBox);
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 10, 10, 10));

        stack.setMinSize(800, 500);
        stack.setMaxSize(800, 500);
        // stack.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
        // BorderWidths.DEFAULT)));
        stack.getChildren().addAll(vBox);

        encodingVBox.setMinSize(800, 500);
        encodingVBox.setMaxSize(800, 500);
        encodingVBox.setAlignment(Pos.CENTER);
        encodingVBox.setVisible(false);

        decodingVBox.setMinSize(800, 500);
        decodingVBox.setMaxSize(800, 500);
        decodingVBox.setAlignment(Pos.CENTER);
        decodingVBox.setVisible(false);
        
        root.getChildren().addAll(stack, encodingVBox, decodingVBox);

        /* Create the scene */
        Scene scene = new Scene(root, 800, 500);

        /* Add the scene to the stage */
        primaryStage.setScene(scene);

        /* Display the application */
        primaryStage.show();
        
        /* Show confirmation dialog on application exit */
        primaryStage.setOnCloseRequest(evt -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.NO, ButtonType.YES);
            alert.setTitle("Uscita applicazione");
            alert.setHeaderText("Sei sicuro di chiudere l'applicazione?");
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);

            if (ButtonType.NO.equals(result)) {
              // no choice or no clicked -> don't close
              evt.consume();
            } else{
            	System.exit(0);
            }
        });
    }

    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

//    private void processRows() {
//        try {
//            EncoderTask task = new EncoderTask(observableFileList);
//
//            task.setOnRunning((succeesesEvent) -> {
//                System.out.println(task.getMessage());
//                System.out.println("RUNNING");
//            });
//
//            task.setOnSucceeded((succeededEvent) -> {
//                observableFileList = task.getValue();
//                System.out.println("SUCCESS");
//            });
//
//            Thread th = new Thread(task);
//            th.setDaemon(false);
//            th.start();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private static ListChangeListener<PSNFile> createListener(String listId) {
        return (Change<? extends PSNFile> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    // System.out.println(listId + " added: " + c.getAddedSubList());
                }
                if (c.wasRemoved()) {
                    // System.out.println(listId + " removed: " + c.getRemoved());
                }
                if (c.wasUpdated()) {
                    // System.out.println(listId + " updated");
                }
            }
        };
    }

    private class ButtonCell extends TableCell<PSNFile, Boolean> {
        Button cellButton;

        ButtonCell(final TableView<PSNFile> tableView, String buttonLabel) {
            cellButton = new Button(buttonLabel);

            cellButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    int selectedIndex = getTableRow().getIndex();
                    PSNFile fileToRemove = tableView.getItems().get(selectedIndex);
                    observableFileList.remove(fileToRemove);
                }
            });
        }

        // Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            } else {
                setGraphic(null);
            }
        }
    }

}
