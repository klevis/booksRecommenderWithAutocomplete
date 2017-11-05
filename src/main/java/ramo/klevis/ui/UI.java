package ramo.klevis.ui;

import ramo.klevis.ml.Book;
import ramo.klevis.ml.CollaborationFiltering;
import ramo.klevis.ml.PrepareData;
import ramo.klevis.ui.comp.AutocompleteJComboBox;
import ramo.klevis.ui.comp.StarRaterEditor;
import ramo.klevis.ui.comp.StarRaterRenderer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.TableColumn;
import java.awt.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by klevis.ramo on 10/29/2017.
 */
public class UI {
    private static final int FRAME_WIDTH = 698;
    private static final int FRAME_HEIGHT = 400;
    private static final int FEATURE_SIZE = 50;
    private static final int DATA_SIZE = 100000;

    private JFrame mainFrame;
    private JPanel mainPanel;
    private JProgressBar progressBar;
    private PrepareData prepareData;
    private RatingsTableModel ratingsTableModel;
    private JTable table;
    private final CollaborationFiltering collaborationFiltering;
    private JSpinner featureField;
    private final Font sansSerifBold = new Font("SansSerif", Font.BOLD, 14);
    private final Font sansSerifItalic = new Font("SansSerif", Font.ITALIC, 14);
    private BooksSearchable searchable;
    private JSpinner dataSizeField;

    public UI() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIManager.put("Table.font", new FontUIResource(new Font("Dialog", Font.ITALIC, 14)));
        UIManager.put("ProgressBar.font", new FontUIResource(new Font("Dialog", Font.BOLD, 16)));
        prepareData = new PrepareData(100000);
        initUI();
        collaborationFiltering = new CollaborationFiltering();
    }

    private void initUI() throws Exception {
        mainFrame = createMainFrame();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        addBookTableAndAutocomplete();
        addTopPanel();
        addSignature();

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    private void addBookTableAndAutocomplete() throws InvocationTargetException, InterruptedException {
        List<Book> bookList = prepareData.getBooks();
        ratingsTableModel = new RatingsTableModel();
        table = new JTable(ratingsTableModel);
        table.getTableHeader().setFont(sansSerifBold);
        table.getColumnModel().getColumn(0).setPreferredWidth(190);
        TableColumn col = table.getColumnModel().getColumn(1);
        col.setCellEditor(new StarRaterEditor(ratingsTableModel));
        col.setCellRenderer(new StarRaterRenderer(ratingsTableModel));
        JPanel tableAndComboPanel = new JPanel(new BorderLayout());
        JScrollPane ratingScrollPane = new JScrollPane(table);
        setPanelTitle(ratingScrollPane, "Tell the Algorithm What you like");
        tableAndComboPanel.add(ratingScrollPane, BorderLayout.CENTER);

        ratingsTableModel.resetAndAddNewMovies(bookList);
        ratingsTableModel.fireTableDataChanged();

        GridLayout gridLayout = new GridLayout(2, 1);
        JPanel panelSuggestion = new JPanel(gridLayout);
        JLabel label = new JLabel("Find Books Starting With(please rate books and train before)");
        label.setFont(sansSerifItalic);
        label.setForeground(Color.BLUE);
        panelSuggestion.add(label);
        searchable = new BooksSearchable(prepareData.getBooks(), collaborationFiltering);
        AutocompleteJComboBox combo = new AutocompleteJComboBox(searchable);
        panelSuggestion.add(combo);
        tableAndComboPanel.add(panelSuggestion, BorderLayout.NORTH);
        mainPanel.add(tableAndComboPanel, BorderLayout.CENTER);

    }

    private void setPanelTitle(JScrollPane suggestedScrollPane, String title) {
        suggestedScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                title,
                TitledBorder.CENTER,
                TitledBorder.TOP, sansSerifItalic, Color.BLUE));
    }

    private void addTopPanel() throws IOException {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton reset = new JButton("Reset with Data Size");
        reset.addActionListener(e -> {
            resetBooksRate();
        });
        reset.setFont(sansSerifBold);
        topPanel.add(reset);
        SpinnerModel model = new SpinnerNumberModel(DATA_SIZE, 100000, 2700000, 5000);
        dataSizeField = new JSpinner(model);
        dataSizeField.setFont(sansSerifBold);
        topPanel.add(dataSizeField);

        JButton train = new JButton("Train Algorithm with");
        train.setFont(sansSerifBold);
        train.addActionListener(e -> {
            showProgressBar();
            Runnable runnable = () -> {
                try {
                    collaborationFiltering.train(prepareData.getBooks(), (Integer) featureField.getValue(), prepareData.getRatings());
                    searchable.setCollaborationFiltering(collaborationFiltering);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                } finally {
                    progressBar.setVisible(false);
                }
            };
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            thread.start();
        });
        topPanel.add(train);

        JLabel genreSize = new JLabel("Genres size");
        genreSize.setFont(sansSerifBold);
        topPanel.add(genreSize);
        model = new SpinnerNumberModel(FEATURE_SIZE, 10, 150, 5);
        featureField = new JSpinner(model);
        featureField.setFont(sansSerifBold);
        topPanel.add(featureField);

        mainPanel.add(topPanel, BorderLayout.NORTH);
    }

    private void resetBooksRate() {
        try {
            prepareData = new PrepareData((Integer) dataSizeField.getValue());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        ratingsTableModel.resetAndAddNewMovies(prepareData.getBooks());
        ratingsTableModel.fireTableDataChanged();
    }

    private JFrame createMainFrame() {
        JFrame mainFrame = new JFrame();
        mainFrame.setTitle("Book Recommender");
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
        ImageIcon imageIcon = new ImageIcon("video.png");
        mainFrame.setIconImage(imageIcon.getImage());

        return mainFrame;
    }


    private void showProgressBar() {
        SwingUtilities.invokeLater(() -> {
            progressBar = createProgressBar(mainFrame);
            progressBar.setString("Training Algorithm!Please wait it may take one or two minutes");
            progressBar.setStringPainted(true);
            progressBar.setIndeterminate(true);
            progressBar.setVisible(true);
            mainFrame.repaint();
        });
    }

    private JProgressBar createProgressBar(JFrame mainFrame) {
        JProgressBar jProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);
        jProgressBar.setVisible(false);
        mainFrame.add(jProgressBar, BorderLayout.NORTH);
        return jProgressBar;
    }

    private void addSignature() {
        JLabel signature = new JLabel("ramok.tech", JLabel.HORIZONTAL);
        signature.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 20));
        signature.setForeground(Color.BLUE);
        mainPanel.add(signature, BorderLayout.SOUTH);
    }
}
