package cz.vsb.fei.projekt;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ToString.Include
    @Column(name = "score_value")
    private int scoreValue;
    @ToString.Include
    private LocalDateTime timestamp;

    @OneToOne(mappedBy = "score")
    private GameResult gameResult;


    public int getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(int scoreValue) {
        this.scoreValue = scoreValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
