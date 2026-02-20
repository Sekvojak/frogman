package cz.vsb.fei.projekt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/scores")
public class ScoreController {

    @Autowired
    private final ScoreRepository scoreRepository;

    public ScoreController(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    @GetMapping
    public List<Score> getAllScores() {
        return scoreRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Score> getScoreById(@PathVariable Long id) {
        return scoreRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Score> createScore(@RequestBody Score score) {
        score.setTimestamp(LocalDateTime.now());
        Score savedScore = scoreRepository.save(score);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedScore);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Score> updateScore(@PathVariable Long id, @RequestBody Score score) {
        if (!scoreRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        score.setId(id);
        Score updatedScore = scoreRepository.save(score);
        return new ResponseEntity<>(updatedScore, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScore(@PathVariable Long id) {
        if (!scoreRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        scoreRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
