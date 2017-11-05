package ramo.klevis.ml;

import org.apache.spark.mllib.recommendation.Rating;
import scala.tools.scalap.Classfile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by klevis.ramo on 10/29/2017.
 */
public class PrepareData {

    private static final String ML_LATEST_SMALL_MOVIES_CSV = "data/books.csv";
    private static final String ML_LATEST_SMALL_RATINGS_CSV = "data/ratings.csv";
    private static final int LIMIT_SIZE = 100000;
    private final List<Book> books;
    private final List<Rating> ratings;

    public PrepareData() throws Exception {
        books = readAllBooks();
        ratings = readUserRatings();
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public List<Book> readAllBooks() throws Exception {
        return Files.readAllLines(getPath(ML_LATEST_SMALL_MOVIES_CSV), StandardCharsets.ISO_8859_1)
                .stream().parallel().skip(1).limit(LIMIT_SIZE).map(line -> {
                    String[] values = line.split(";");
                    int id = 0;
                    try {
                        id = Integer.parseInt(values[0].replaceAll("\"", ""));
                    } catch (NumberFormatException e) {
                        return null;
                    }
                    return new Book(id, values[1].replaceAll("\"", ""), 0d);
                }).filter(e -> e != null).collect(Collectors.toList());
    }

    public List<Rating> readUserRatings() throws Exception {
        final int[] count = {0};
        List<Rating> collect = Files.readAllLines(getPath(ML_LATEST_SMALL_RATINGS_CSV), StandardCharsets.ISO_8859_1)
                .stream().parallel().skip(1).limit(LIMIT_SIZE).map(line -> {
                    String[] values = line.split(";");

                    int product;
                    try {
                        product = Integer.parseInt(values[1].replaceAll("\"", ""));
                    } catch (NumberFormatException e) {
                        count[0]++;
                        return null;
                    }
                    return new Rating(Integer.parseInt(values[0].replaceAll("\"", "")),
                            product,
                            Double.parseDouble(values[2].replaceAll("\"", "")));
                }).filter(e -> e != null).collect(Collectors.toList());

        System.out.println(count[0]);
        System.out.println(collect.size());
        return collect;
    }

    public List<Book> getBooks() {
        return books;
    }

    private Path getPath(String path) throws IOException, URISyntaxException {
        return getPath(this.getClass().getResource("/" + path).toURI());
    }

    private Path getPath(URI uri) throws IOException {
        Path start = null;
        try {
            start = Paths.get(uri);
        } catch (FileSystemNotFoundException e) {
            Map<String, String> env = new HashMap<>();
            env.put("create", "true");
            FileSystems.newFileSystem(uri, env);
            start = Paths.get(uri);
        }
        return start;
    }
}
