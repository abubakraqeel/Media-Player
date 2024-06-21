package studiplayer.ui;


import java.io.File;

import java.net.URL;

import javafx.scene.control.TextField;
import javafx.application.Application;
import javafx.application.Platform;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.ChoiceBox;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;

import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import studiplayer.audio.AudioFile;

import studiplayer.audio.NotPlayableException;
import studiplayer.audio.PlayList;
import studiplayer.audio.SortCriterion;


public class Player extends Application{
	
	private boolean useCertPlayList = false;
	public static final String DEFAULT_PLAYLIST = "playlists/DefaultPlayList.m3u";
	private static final String PLAYLIST_DIRECTORY = "playlists";
	private static final String INITIAL_PLAY_TIME_LABEL = "00:00";
	private static final String NO_CURRENT_SONG = "-";
	private PlayList playList = new PlayList();
	private Button playButton;
	private Button pauseButton;
	private Button stopButton;
	private Button nextButton;
	private Label playListLabel = new Label(PLAYLIST_DIRECTORY);
	private Label playTimeLabel = new Label(INITIAL_PLAY_TIME_LABEL);
	private Label currentSongLabel = new Label(NO_CURRENT_SONG);
	private ChoiceBox<SortCriterion> sortChoiceBox = new ChoiceBox<>();
	private TextField searchTextField = new TextField();
	private Button filterButton = new Button("display");
	private boolean songPaused;
	PlayerThread playerThread;
	TimerThread timer;
	
	SongTable songTable = new SongTable(playList);

	public void setUseCertPlayList(boolean val) {
		this.useCertPlayList = val;
		
	}
	
	public void loadPlayList(String pathname) {
		if (pathname == null || pathname.isEmpty()) {
			setPlayList(DEFAULT_PLAYLIST);
        } else {
        	setPlayList(pathname);
        	
        }
		songTable.refreshSongs(playList);
		
	}
	
	public void setPlayList(String pathname) {
		playList = new PlayList(pathname);
		songTable.refreshSongs(playList);
		
	}
	
	public Button createButton(String iconfile) {
		Button button = null;
		try {
			URL url = getClass().getResource("/icons/" + iconfile); 
			Image icon = new Image(url.toString());
			ImageView imageView = new ImageView(icon); 
			imageView.setFitHeight(20);
			imageView.setFitWidth(20);
			button = new Button("", imageView); button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY); 
			button.setStyle("-fx-background-color: #fff;");
		} catch (Exception e) { 
			System.out.println("Image " + "icons/"
					+ iconfile + " not found!"); System.exit(-1);
		}
		return button;
	}

	public static void main(String[] args) {
		
		launch(args);

	}
	
	
	@Override
	public void start(Stage stage) throws Exception {
		   BorderPane root = new BorderPane();
	        stage.setTitle("APA Player");
		if (useCertPlayList) {
			
			loadPlayList(null);
		} else {
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showOpenDialog(stage);
			if (file != null) {
				loadPlayList(file.getPath());
			} else {
				loadPlayList(null);
			}
		}
		
        //controls in grid pane
		GridPane filterGrid = new GridPane();
        filterGrid.setPadding(new Insets(10));
        filterGrid.setHgap(5);
        filterGrid.setVgap(10);
        sortChoiceBox.setValue(SortCriterion.DEFAULT);
        sortChoiceBox.getItems().addAll(SortCriterion.values());
        filterGrid.add(new Label("Search text:"), 0, 0);
        filterGrid.add(searchTextField, 1, 0);
        filterGrid.add(new Label("Sort by:"), 0, 1);
        filterGrid.add(sortChoiceBox, 1, 1);
        filterGrid.add(filterButton, 2, 1);

        //TOP PART of the player title pane
		TitledPane filterBox = new TitledPane();
		filterBox.setContent(filterGrid);
		filterBox.setText("Filter");
		
		
		
		//Center Song Table
		
		
		//BOTTOM PART of the player V box
		VBox bottomPart = new VBox();
		bottomPart.setPadding(new Insets(10));
        bottomPart.setSpacing(5);
        
		//Current Song data in gridPane
		GridPane songData = new GridPane();
		songData.setHgap(20);
		songData.setVgap(5);
		songData.add(new Label("Playlist"), 0, 1);
		songData.add(playListLabel, 1, 1);
		songData.add(new Label("Current Song"), 0, 2);
		songData.add(currentSongLabel, 1, 2);
		songData.add(new Label("Playtime"), 0, 3);
		songData.add(playTimeLabel, 1, 3);
			
		//control buttons HBox
		HBox buttonRow = new HBox();
		buttonRow.setAlignment(Pos.CENTER);
		buttonRow.setPadding(new Insets(0, 10, 15, 10));
		buttonRow.setSpacing(10);
		stopButton = createButton("stop.jpg");
		playButton = createButton("play.jpg");
		pauseButton = createButton("pause.jpg");
		nextButton = createButton("next.jpg");

		
		buttonRow.getChildren().addAll(playButton, stopButton, pauseButton, nextButton);
		setButtonStates(false, true, true, false);
		bottomPart.getChildren().addAll(songData, buttonRow);
		
		filterButton.setOnAction(e -> {
			playList.setSearch(searchTextField.getText());
			playList.setSortCriterion(sortChoiceBox.getValue());
			songTable.refreshSongs(playList);
		});
		
		playButton.setOnAction(e -> {
	         playCurrentSong();
	     });
		stopButton.setOnAction(e -> {
	         stopCurrentSong();
	     });
		pauseButton.setOnAction(e -> {
	         pauseCurrentSong();
	     });
		nextButton.setOnAction(e -> {
	         nextSong();
	         songTable.selectSong(playList.currentAudioFile());
	     });
		
		songTable.setRowSelectionHandler(e -> {
			stopCurrentSong();
			
			
		});
        // Root Pane
     
        root.setBottom(bottomPart);
        root.setTop(filterBox);
        
        root.setCenter(songTable);
        songTable.refreshSongs(playList);
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
	}
	private void setButtonStates(boolean playButtonState, boolean pauseButtonState, boolean stopButtonState, boolean nextButtonState) {
		Platform.runLater(() -> {
			playButton.setDisable(playButtonState);
			pauseButton.setDisable(pauseButtonState);
			nextButton.setDisable(nextButtonState);
			stopButton.setDisable(stopButtonState);
		});

		
	}
	private void updateSongInfo(AudioFile af) { Platform.runLater(() -> {
		if (af == null) {
		// set currentSongLabel and playTimeLabel
			currentSongLabel.setText(NO_CURRENT_SONG);
			playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL);
			
		} else {
			currentSongLabel.setText(af.getTitle());
			playTimeLabel.setText(af.formatPosition());

			
		}
	});
	}
	private void nextSong() {
		if (playerThread != null) {
			playerThread.terminate();
			playerThread = null;
		}
		stopCurrentSong();
		playList.nextSong();
		setButtonStates(true, false, false, false);
		updateSongInfo(playList.currentAudioFile());
		playCurrentSong();
		
	}
	private void pauseCurrentSong() {
		if(songPaused) {
			songPaused= false;
			if (playerThread == null || !playerThread.isAlive()) {
				playerThread = new PlayerThread();
				playerThread.start();
			}
			if (timer == null || !timer.isAlive()) {
				timer = new TimerThread();
				timer.start();
			}
			
			setButtonStates(true, false, false, false);
			
		} else {
			songPaused = true;
			
			if (timer != null) {
				timer.terminate();
				timer = null;
			}
			setButtonStates(true, false, false, false);
			
			
		}
		playList.currentAudioFile().togglePause();
		setButtonStates(true, false, false, false);
		
	}
	private void stopCurrentSong() {
		if (timer != null) {
			timer.terminate();
			timer = null;
		}
		if (playerThread != null) {
			playerThread.terminate();
			playerThread = null;
		}
		songPaused = false;
		
		playList.currentAudioFile().stop();
		setButtonStates(false, true, true, false);
		updateSongInfo(null);
		
	}
	private void playCurrentSong() {
		if (!songPaused) {
			playerThread = new PlayerThread();
			playerThread.start();
			timer = new TimerThread();
			timer.start();
		} else {
			songPaused = false;
			timer.start();
			playList.currentAudioFile().togglePause();
		}
		
		
		
		songTable.selectSong(playList.currentAudioFile());
		setButtonStates(true, false, false, false);
		updateSongInfo(playList.currentAudioFile());
		

	}
	
	
	class PlayerThread extends Thread {
	 private boolean stopped = false;

	 public void terminate() {
	     stopped = true;
	 }

	 @Override
	 public void run() {
	     while (!stopped) {
	         AudioFile currentSong = playList.currentAudioFile();
	         if (currentSong != null) {
	          
	             Platform.runLater(() -> songTable.selectSong(currentSong));
	             
	             try {
	                 currentSong.play();
	             } catch (NotPlayableException e) {
	                 e.printStackTrace();
	             }
	             
	         
	             if (!stopped) {
	                 playList.nextSong(); 
	                 updateSongInfo(playList.currentAudioFile());
	             }
	         }
	     }
	 }
	}
	class TimerThread extends Thread {
	    private volatile boolean stopped = false;

	    public void terminate() {
	        stopped = true;
	    }

	    @Override
	    public void run() {
	        while (!stopped) {
	            Platform.runLater(() -> {
	                AudioFile currentSong = playList.currentAudioFile();
	                if (currentSong != null) {
	                    playTimeLabel.setText(currentSong.formatPosition());
	                    currentSongLabel.setText(currentSong.getTitle());
	                } else {
	                    playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL);
	                    currentSongLabel.setText(NO_CURRENT_SONG);
	                }
	            });

	            try {
	                Thread.sleep(1000); 
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}




}





