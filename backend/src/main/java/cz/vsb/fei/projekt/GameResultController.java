package cz.vsb.fei.projekt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game-results")
public class GameResultController {
    @Autowired
    private GameResultRepository gameResultRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @GetMapping
    public List<GameResult> getAllResults() {
        return gameResultRepository.findAll();
    }


    @PostMapping("/{playerId}/{scoreId}")
    public ResponseEntity<GameResult> createGameResult(@PathVariable Long playerId, @PathVariable Long scoreId) {
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Score score = scoreRepository.findById(scoreId).orElse(null);
        if (score == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        GameResult gameResult = new GameResult();
        gameResult.setPlayer(player);
        gameResult.setScore(score);

        GameResult savedGameResult = gameResultRepository.save(gameResult);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedGameResult);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGameResult(@PathVariable Long id) {
        return gameResultRepository.findById(id).map(gameResult -> {
            Score score = gameResult.getScore();
            gameResultRepository.deleteById(id);
            if (score != null) {
                scoreRepository.deleteById(score.getId());
            }
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }


}
