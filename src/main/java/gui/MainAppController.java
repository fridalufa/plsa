package gui;

import entities.Song;
import entities.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import methods.ProbabilisticModelResult;
import methods.similarity.Result;
import methods.similarity.Similarity;
import methods.similarity.metrics.Metric;
import storage.ProbabilisticAnalysisResultRepository;
import storage.RunInfo;
import storage.SongRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 * @author fridalufa
 */
public class MainAppController {
    @FXML
    private ListView<String> lvArtists;

    @FXML
    private TableView<SongDataModel> tblSongs;

    @FXML
    private TableView<WordDataModel> tblWords;

    @FXML
    private TextField txtFilterArtists;

    @FXML
    private ComboBox<RunInfo> cmbProbAnalysisRunSelector;

    @FXML
    private PieChart chtTopicDistribution;

    @FXML
    private ListView<Result> lstSimilarSongs;

    @FXML
    private ComboBox<Metric> cmbSelectMetric;

    private SongRepository songRepository;
    private ProbabilisticAnalysisResultRepository probAnalysisRepository;

    private ProbabilisticModelResult selectedRun;

    private int numberOfDisplayedTopics = 5;

    private SongDataModel shouldBeSelectedSong;

    public MainAppController() {
        this.songRepository = new SongRepository();
        this.probAnalysisRepository = new ProbabilisticAnalysisResultRepository();
    }

    @FXML
    public void initialize() {

        List<RunInfo> runs = probAnalysisRepository.listPlsaRuns();
        runs.addAll(probAnalysisRepository.listLdaRuns());

        cmbProbAnalysisRunSelector.setItems(FXCollections.observableArrayList(runs));

        cmbProbAnalysisRunSelector.getSelectionModel().selectedItemProperty().addListener((comboBox, oldVal, newVal) -> {
            selectedRun = probAnalysisRepository.fetchResult(newVal.method, newVal.id);

            this.songRepository.selectCorpus(newVal.corpus.getId());

            tblSongs.setItems(FXCollections.observableArrayList());
            resetSongDetailsView();

            FilteredList<String> filteredList = new FilteredList<>(songRepository.fetchArtists(), s -> true);
            txtFilterArtists.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null || newValue.length() == 0) {
                    filteredList.setPredicate(s -> true);
                } else {
                    filteredList.setPredicate(s -> s.contains(newValue));
                }
            });

            lvArtists.setItems(filteredList);
        });

        lvArtists.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    resetSongDetailsView();
                    tblSongs.setItems(fetchSongsOfArtist(newValue));

                    if (shouldBeSelectedSong != null) {
                        tblSongs.getSelectionModel().select(shouldBeSelectedSong);
                        shouldBeSelectedSong = null;
                    }
                });

        tblSongs.getSelectionModel()
                .selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Song selectedSong = newValue.getSong();
                updateSongDetails(selectedSong);
            } else {
                tblWords.setItems(FXCollections.observableArrayList());
            }
        });

        lstSimilarSongs.setOnMouseClicked(event -> {
            if (event.getClickCount() >= 2) {
                Result selectedSong = lstSimilarSongs.getSelectionModel().getSelectedItem();
                if (selectedSong == null) {
                    return;
                }

                shouldBeSelectedSong = new SongDataModel(selectedSong.song);
                lvArtists.getSelectionModel().select(selectedSong.song.interpret);
                lvArtists.scrollTo(selectedSong.song.interpret);
            }
        });

        cmbSelectMetric.setItems(FXCollections.observableArrayList(Similarity.availableMetrics));
    }

    private ObservableList<SongDataModel> fetchSongsOfArtist(String artist) {
        List<SongDataModel> models = songRepository.fetchSongsOfArtist(artist).stream()
                .map(SongDataModel::new).collect(Collectors.toList());

        return FXCollections.observableArrayList(models);
    }

    private ObservableList<WordDataModel> prepareLyrics(List<Word> lyrics) {
        List<WordDataModel> wordDataModels = lyrics.stream()
                .map(WordDataModel::new).collect(Collectors.toList());

        return FXCollections.observableArrayList(wordDataModels);
    }

    private void resetSongDetailsView() {
        tblWords.setItems(FXCollections.observableArrayList());
        lstSimilarSongs.setItems(FXCollections.observableArrayList());
        chtTopicDistribution.setData(FXCollections.observableArrayList());
    }

    private void updateSongDetails(Song newSong) {
        tblWords.setItems(prepareLyrics(newSong.lyrics));
        lstSimilarSongs.setItems(FXCollections.observableArrayList());

        float[] topicProbForSong = selectedRun.topicProbForSong(newSong);
        int[] topElements = indexesOfTopElements(topicProbForSong, numberOfDisplayedTopics);

        DoubleStream topEntries = IntStream
                .range(0, numberOfDisplayedTopics)
                .mapToDouble(i -> topicProbForSong[topElements[i]]);

        double sum = topEntries.sum();

        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();

        IntStream.range(0, numberOfDisplayedTopics)
                .mapToObj(i -> createDataObject(topElements[i], topicProbForSong[topElements[i]]))
                .forEach(chartData::add);

        chartData.add(createRestObject(1 - (float) sum));

        chtTopicDistribution.setData(chartData);
    }

    private static int[] indexesOfTopElements(float[] orig, int nummax) {
        float[] copy = Arrays.copyOf(orig, orig.length);
        Arrays.sort(copy);
        float[] honey = Arrays.copyOfRange(copy, copy.length - nummax, copy.length);
        int[] result = new int[nummax];
        int resultPos = 0;
        for (int i = 0; i < orig.length; i++) {
            float onTrial = orig[i];
            int index = Arrays.binarySearch(honey, onTrial);
            if (index < 0) continue;
            result[resultPos++] = i;
        }
        return result;
    }

    private PieChart.Data createDataObject(int i, float prob) {
        String title = String.format("Topic %d (%.2f%%)", i, prob * 100);
        return new PieChart.Data(title, prob);
    }

    private PieChart.Data createRestObject(float prob) {
        String title = String.format("Rest (%.2f%%)", prob * 100);
        return new PieChart.Data(title, prob);
    }

    public void startSearch(ActionEvent actionEvent) {
        SongDataModel songDataModel = tblSongs.getSelectionModel().getSelectedItem();
        Metric selectedMetric = cmbSelectMetric.getSelectionModel().getSelectedItem();

        if (songDataModel == null) {
            return;
        }

        if (selectedMetric == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Keine Metrik gewählt");
            alert.setContentText("Bitte ein Ähnlichkeitsmaß auswählen!");

            alert.showAndWait();
            return;
        }

        Song selectedSong = songDataModel.getSong();

        Similarity similarity = new Similarity(selectedRun);

        lstSimilarSongs.setItems(
                FXCollections.observableArrayList(similarity.getSimilarSongs(selectedSong, 10, selectedMetric))
        );
    }

    public void showTopicWindow(ActionEvent actionEvent) {
        /*try {
            TopicViewController.open().setPlsa(selectedRun);
        } catch (IOException e) {
            e.printStackTrace();
        }*/ // TODO
    }
}
