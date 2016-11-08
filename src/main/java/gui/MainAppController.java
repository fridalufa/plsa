package gui;

import entities.Corpus;
import entities.Song;
import entities.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import plsa.PLSA;
import storage.PlsaRepository;
import storage.PlsaRunInfo;
import storage.SongRepository;

import java.util.ArrayList;
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
    private ComboBox<PlsaRunInfo> cmbPlsaRunSelector;

    @FXML
    private PieChart chtTopicDistribution;

    private SongRepository songRepository;
    private PlsaRepository plsaRepository;

    private PLSA selectedPLSA;

    private int numberOfDisplayedTopics = 5;

    public MainAppController() {
        this.songRepository = new SongRepository();
        this.plsaRepository = new PlsaRepository();
    }

    @FXML
    public void initialize() {

        cmbPlsaRunSelector.setItems(FXCollections.observableArrayList(plsaRepository.listPlsaRuns()));

        cmbPlsaRunSelector.getSelectionModel().selectedItemProperty().addListener((comboBox, oldVal, newVal) -> {
            selectedPLSA = plsaRepository.fetchPLSA(newVal.id);

            this.songRepository.selectCorpus(newVal.corpus.getId());

            tblSongs.setItems(FXCollections.observableArrayList());
            tblWords.setItems(FXCollections.observableArrayList());

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
                    tblWords.setItems(FXCollections.observableArrayList());
                    tblSongs.setItems(fetchSongsOfArtist(newValue));
                });

        tblSongs.getSelectionModel()
                .selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Song selectedSong = newValue.getSong();
                //tblWords.setItems(prepareLyrics(selectedSong.lyrics));
                updateSongDetails(selectedSong);
            } else {
                tblWords.setItems(FXCollections.observableArrayList());
            }
        });
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

    private void updateSongDetails(Song newSong) {
        tblWords.setItems(prepareLyrics(newSong.lyrics));

        float[] topicProbForSong = selectedPLSA.topicProbForSong(newSong);
        int[] topElements = indexesOfTopElements(topicProbForSong, numberOfDisplayedTopics);

        DoubleStream topEntries = IntStream
                .range(0,numberOfDisplayedTopics)
                .mapToDouble(i -> topicProbForSong[topElements[i]]);

        double sum = topEntries.sum();

        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();

        IntStream.range(0, numberOfDisplayedTopics)
                .mapToObj(i -> createDataObject(topElements[i], topicProbForSong[topElements[i]]))
                .forEach(chartData::add);
        
        chartData.add(createRestObject(1-(float)sum));

        chtTopicDistribution.setData(chartData);
    }

    private static int[] indexesOfTopElements(float[] orig, int nummax) {
        float[] copy = Arrays.copyOf(orig,orig.length);
        Arrays.sort(copy);
        float[] honey = Arrays.copyOfRange(copy,copy.length - nummax, copy.length);
        int[] result = new int[nummax];
        int resultPos = 0;
        for(int i = 0; i < orig.length; i++) {
            float onTrial = orig[i];
            int index = Arrays.binarySearch(honey,onTrial);
            if(index < 0) continue;
            result[resultPos++] = i;
        }
        return result;
    }

    private PieChart.Data createDataObject(int i, float prob) {
        String title = String.format("Topic %d (%.2f%%)", i, prob*100);
        return new PieChart.Data(title, prob);
    }

    private PieChart.Data createRestObject(float prob) {
        String title = String.format("Rest (%.2f%%)", prob*100);
        return new PieChart.Data(title, prob);
    }

}
