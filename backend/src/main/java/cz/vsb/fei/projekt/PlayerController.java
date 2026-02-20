package cz.vsb.fei.projekt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    private final PlayerRepository playerRepository;

    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @GetMapping
    public List<Player> getPlayers() {
        List<Player> players = playerRepository.findAll();
        System.out.println("All players: " + players);
        return players;
    }

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        Player savedPlayer = playerRepository.save(player);
        System.out.println("Created player: " + savedPlayer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlayer);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Player> getPlayerByName(@PathVariable String name) {
        return playerRepository.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @GetMapping("/{id}")
    public Player getPlayer(@PathVariable Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerRepository.deleteById(id);
    }
}
