package lab;

@FunctionalInterface
public interface ScoreListener {
    void onScoreChanged(int newScore);
}
