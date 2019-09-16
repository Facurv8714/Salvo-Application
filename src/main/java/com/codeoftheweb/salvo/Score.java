package com.codeoftheweb.salvo;

import com.sun.tools.javac.file.Locations;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;


    private LocalDateTime finishDate;

    private Double score;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "score", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<GamePlayer> gamePlayers = new HashSet<>();

    public Score(){ }

    public Score (LocalDateTime finishDate, Game game, Player player, Double score) {
        this.finishDate = finishDate;
        this.game = game;
        this.player = player;
        this.score = score;
    };


    public Score(LocalDateTime finishDate){
        this.finishDate = finishDate;
    }

    public Game getGame(){
        return game;
    }

    public Player getPlayer(){
        return player;
    }

    public Double getScore(){
        return score;
    }

    public long getId() {
        return id;
    }

}
