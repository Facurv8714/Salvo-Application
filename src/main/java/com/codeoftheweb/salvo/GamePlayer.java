package com.codeoftheweb.salvo;

import com.sun.tools.javac.file.Locations;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.stream.Location;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class GamePlayer {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "score")
    private Score score;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<Salvo> salvoes = new HashSet<>();



    public GamePlayer() {
    }

    public GamePlayer(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void addShip(Ship ship) {
        ship.setGamePlayer(this);
        ships.add(ship);
    }


    public void addSalvo(Salvo salvo) {
        salvo.setGamePlayer(this);
        salvoes.add(salvo);
    }



    public Set<Salvo> getSalvoes() {
        return salvoes;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public long getId() {
        return id;
    }

    public Score getScore() {
        return player.getScore(game);
    }

    public GamePlayer getOpponent() {
        return this.getGame().getGamePlayers().stream()
              .filter(gp -> gp.getId() != this.id)
                .findFirst()
                .orElse(null);
      }

      public List<Map<String, Object>> getHits(){
        if(getOpponent() == null){
            return null;
        }

        List<Map<String, Object>> listaReturn = new ArrayList<>();
        List<String> shipLocation = getOpponent().getShips().stream().flatMap(Ship -> Ship.getLocations().stream()).collect(toList());
        List<String> globalHits = new ArrayList<>();

        salvoes.stream().sorted(Comparator.comparing(Salvo::getTurn)).forEach(salvo -> {
            List<String> hitsLocation = new ArrayList<>();

            salvo.getLocations().stream().forEach(location -> {
               if(shipLocation.contains(location)){
                    hitsLocation.add(location);
                    globalHits.add(location);
               }
            });
            List<String> sinks = new ArrayList<>();
            List<String> unsinks = new ArrayList<>();

            getOpponent().getShips().stream().forEach(ship -> {
                if(globalHits.containsAll(ship.getLocations())){
                    sinks.add(ship.getType());
                }
                else unsinks.add(ship.getType());
            });



              Map<String, Object> dto = new LinkedHashMap<String, Object>();
                dto.put("turn", salvo.getTurn());
                dto.put("hits", hitsLocation);
                dto.put("sink", sinks);
                dto.put("left", unsinks);
                listaReturn.add(dto);

        });


          return listaReturn;
      }







}
