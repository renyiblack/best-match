package br.ufrn.bestMatch;

public class Main {
    public static void sequentialAlgorithm(String word, String path) {
        long start = System.currentTimeMillis();

        Word pair = new BestMatching(path, word).start();

        long end = System.currentTimeMillis();

        System.out.println("Palavra com a menor distância: " + pair.getWord());

        System.out.println("Com distância de: " + pair.getDistance());

        System.out.println("Duration: "
                + ((end - start) / 60000) + "min "
                + (((end - start) % 60000) / 1000) + "."
                + (((end - start) % 60000) % 1000) + "s");
    }

    public static void main(String[] args) {
        final String word = "test";
        sequentialAlgorithm(word, "big_file.txt");
    }
}