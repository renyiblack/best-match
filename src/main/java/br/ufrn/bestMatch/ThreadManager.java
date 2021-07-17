package br.ufrn.bestMatch;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.infra.results.I_Result;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@JCStressTest
@Outcome(id = "0", expect = Expect.ACCEPTABLE, desc = "less distance should be 0")
@org.openjdk.jcstress.annotations.State
@org.openjdk.jmh.annotations.State(Scope.Benchmark)
public class ThreadManager {
    @Param({ "small_file.txt" })
    private String path;
    @Param({ "test" })
    String word;
    private final static int THREADS_NUMBER = 8;

    public ThreadManager() {
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public ThreadManager(String path, String word) {
        this.path = path;
        this.word = word;
    }

    @org.openjdk.jmh.annotations.Benchmark
    @Fork(value = 3, warmups = 2)
    public Word start() {
        try {
            List<Thread> threads = new ArrayList<>();
            List<Word> closestWords = new ArrayList<>();
            Path paths = Paths.get("./", path);
            long linesCount = Files.lines(paths).count();
            long offset = linesCount / THREADS_NUMBER;
            long start = 0;
            long end = offset;

            List<String> lines = Files.readAllLines(paths);

            for (int i = 1; i <= THREADS_NUMBER; i++) {
                closestWords.add(new Word(0, ""));

                threads.add(new Thread(new Levenshtein(lines.subList((int) start, (int) end - 1), word, closestWords.get(i - 1))));
                threads.get(i - 1).start();

                start = offset * i;
                end = offset * (i + 1);
                if (i == THREADS_NUMBER - 1) {
                    end = linesCount;
                }
            }

            do {
            } while (threads.stream().anyMatch(Thread::isAlive));

            closestWords.sort((p1, p2) -> {
                if (p1.getDistance().equals(p2.getDistance()))
                    return p1.getWord().compareTo(p2.getWord());
                return p1.getDistance() - p2.getDistance();
            });

            return closestWords.get(0);
        } catch (IOException e) {
            System.err.println("Couldn't read \"" + path + "\" file.");
            return new Word(0, "");
        }
    }

    @Actor
    public void getDistanceFromSmallFile(I_Result r) {
        setPath("small_file.txt");
        setWord("test");

        r.r1 = start().getDistance();
    }
}