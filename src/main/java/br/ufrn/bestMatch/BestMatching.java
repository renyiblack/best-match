package br.ufrn.bestMatch;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class BestMatching {
    public int levenshtein(int[][] matrix, String str1, String str2, int i, int j) {
        if (Math.min(i, j) == 0)
            return Math.max(i, j);

        if (matrix[i - 1][j - 1] != -1)
            return matrix[i - 1][j - 1];

        matrix[i - 1][j - 1] = Math.min(
                Math.min(levenshtein(matrix, str1, str2, i - 1, j) + 1, levenshtein(matrix, str1, str2, i, j - 1) + 1),
                levenshtein(matrix, str1, str2, i - 1, j - 1) + (str1.charAt(i - 1) != str2.charAt(j - 1) ? 1 : 0));

        return matrix[i - 1][j - 1];
    }

    public int calculate(String str1, String str2) {
        int[][] matrix = new int[str1.length()][str2.length()];

        for (int i = 0; i < str1.length(); i++)
            for (int j = 0; j < str2.length(); j++)
                matrix[i][j] = -1;

        return levenshtein(matrix, str1, str2, str1.length(), str2.length());
    }

    public List<String> read(String path) {

        try {
            Path paths = Path.of(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(path)).toURI());

            return Files.readAllLines(paths);

        } catch (IOException | URISyntaxException e) {
            System.err.println("Couldn't read \"" + path + "\" file.");
            return null;
        }
    }

    public Word start(String path, String text) {
        List<String> words = read(path);

        Word auxWord = new Word(Integer.MAX_VALUE, words.get(0));
        for (String word : words) {
            Word result = new Word(calculate(word, text), word);

            if (auxWord.getDistance().equals(result.getDistance())) {
                if (auxWord.getWord().compareTo(result.getWord()) > 0)
                    auxWord = result;
            } else if (auxWord.getDistance() > result.getDistance())
                auxWord = result;
        }

        return auxWord;
    }
}