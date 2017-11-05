package ramo.klevis.ui;

import ramo.klevis.ml.Book;
import ramo.klevis.ml.CollaborationFiltering;
import ramo.klevis.ml.PrepareData;
import ramo.klevis.ui.comp.StarRaterEditor;
import ramo.klevis.ui.comp.StarRaterRenderer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.TableColumn;
import java.awt.*;

import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by klevis.ramo on 10/29/2017.
 */
public class UI {
    private static final int FRAME_WIDTH = 698;
    private static final int FRAME_HEIGHT = 400;
    private static final int FEATURE_SIZE = 50;

    private JFrame mainFrame;
    private JPanel mainPanel;
    private JProgressBar progressBar;
    private PrepareData prepareData;
    private RatingsTableModel ratingsTableModel;
    private JTable table;
    private final CollaborationFiltering collaborationFiltering;
    private JSpinner featureField;
    private final Font sansSerifBold = new Font("SansSerif", Font.BOLD, 14);
    private final Font sansSerifItalic = new Font("SansSerif", Font.ITALIC, 13);

    public UI() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIManager.put("Button.font", new FontUIResource(new Font("Dialog", Font.BOLD, 16)));
        UIManager.put("Table.font", new FontUIResource(new Font("Dialog", Font.ITALIC, 14)));
        UIManager.put("ProgressBar.font", new FontUIResource(new Font("Dialog", Font.BOLD, 16)));
        prepareData = new PrepareData();
        initUI();
        collaborationFiltering = new CollaborationFiltering();
    }

    private void initUI() throws IOException {
        mainFrame = createMainFrame();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        addMovieTables();
        addTopPanel();
        addSignature();

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    private void addMovieTables() {
        ratingsTableModel = new RatingsTableModel();
        table = new JTable(ratingsTableModel);
        table.getTableHeader().setFont(sansSerifBold);
        table.getColumnModel().getColumn(0).setPreferredWidth(190);
        GridLayout gridLayout = new GridLayout(2, 1);
        JPanel tablePanel = new JPanel(new BorderLayout());

        JScrollPane ratingScrollPane = new JScrollPane(table);
        setPanelTitle(ratingScrollPane, "Tell the Algorithm What you like");
        tablePanel.add(ratingScrollPane,BorderLayout.CENTER);

        List<Book> bookList = prepareData.getBooks();
        ratingsTableModel.restAndAddNewMovies(bookList);
        ratingsTableModel.fireTableDataChanged();
        TableColumn col = table.getColumnModel().getColumn(1);
        col.setCellEditor(new StarRaterEditor(ratingsTableModel));
        col.setCellRenderer(new StarRaterRenderer(ratingsTableModel));

        mainPanel.add(tablePanel, BorderLayout.CENTER);
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
        JLabel genres = new JLabel("Genres");
        genres.setFont(sansSerifBold);
        topPanel.add(genres);

        JButton reset = new JButton("Reset Ratings");
        reset.addActionListener(e -> {
            resetMoviesRate();
        });
        topPanel.add(reset);

        JButton train = new JButton("Suggest Movies");
        train.addActionListener(e -> {
            showProgressBar();
            Runnable runnable = () -> {
                try {
                    List<Book> topTenRated = collaborationFiltering.train(prepareData.getBooks(), (Integer) featureField.getValue());
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
        SpinnerModel model = new SpinnerNumberModel(FEATURE_SIZE, 10, 150, 5);
        featureField = new JSpinner(model);
        featureField.setFont(sansSerifBold);
        topPanel.add(featureField);
        mainPanel.add(topPanel, BorderLayout.NORTH);
    }

    private void resetMoviesRate() {
        try {
            prepareData = new PrepareData();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        ratingsTableModel.restAndAddNewMovies(new ArrayList<>());
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
