package cz.vsb.fei.projekt;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Setter
@ToString
@EqualsAndHashCode
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    @OneToMany(mappedBy = "player")
    private List<GameResult> gameResults = new ArrayList<>();
}
