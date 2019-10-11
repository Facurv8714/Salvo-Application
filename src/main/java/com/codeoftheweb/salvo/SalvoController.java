package com.codeoftheweb.salvo;

import org.hibernate.query.criteria.internal.predicate.IsEmptyPredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.Location;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;


@RestController

@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/players")
    public ResponseEntity<Object> register(
            @RequestParam String userName, @RequestParam String password) {

        if (userName.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByUserName(userName) != null) {
            return new ResponseEntity<>(makeMap("error", "Name already in use"), HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(userName, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PostMapping("/games")
        public ResponseEntity<Map<String, Object>> gamePlayer (Authentication authentication) {
        if ( isGuest(authentication)){
            return new ResponseEntity(makeMap("error", "Not log in"), HttpStatus.UNAUTHORIZED);
        }
        else {
            Player player = playerRepository.findByUserName(authentication.getName());
            Game newGame = gameRepository.save(new Game(LocalDateTime.now()));
            GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(newGame, player));
            return new ResponseEntity(makeMap("GamePlayer", newGamePlayer.getId()), HttpStatus.CREATED);
        }

}

    @GetMapping("/games")
    public ResponseEntity<Map<String, Object>> getAll(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        if (isGuest(authentication)) {
            dto.put("player", null);
        } else {
            Player player = playerRepository.findByUserName(authentication.getName());
            dto.put("player", makePlayerDTO(player));
        }
        dto.put("games", gameRepository.findAll().stream()
                .map(this::makeGameDTO)
                .collect(Collectors.toList()));

        return new ResponseEntity(dto, HttpStatus.OK);
    }

    @GetMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String, Object>> getGameView (@PathVariable Long gamePlayerId, Authentication authentication){
        Optional<GamePlayer> optionalGamePlayer = gamePlayerRepository.findById(gamePlayerId);
        GamePlayer gamePlayer = optionalGamePlayer.get();
        Player player = playerRepository.findByUserName(authentication.getName());

        if(player.getId() != gamePlayer.getPlayer().getId()){
            return new ResponseEntity(makeMap("error", "You don´t have acces"), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(makeGameViewDTO(gamePlayer), HttpStatus.OK);
    }

    @PostMapping("/game/{gameId}/players")

    public ResponseEntity<Object> joinAGame (@PathVariable Long gameId, Authentication authentication) {

        Player player = playerRepository.findByUserName(authentication.getName());

        if (isGuest(authentication)) {
            return new ResponseEntity(makeMap("error", "Not log in"), HttpStatus.UNAUTHORIZED);
        }

        Optional<Game> optionalGame = gameRepository.findById(gameId);
        if (optionalGame.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "Not such game"), HttpStatus.FORBIDDEN);
        }
        Game game = optionalGame.get();

        if (game.getGamePlayers().size() > 1) {
            return new ResponseEntity<>("Game is full", HttpStatus.FORBIDDEN);
        }

        GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(game, player));

        return new ResponseEntity<>(makeMap("id", gamePlayer.getId()), HttpStatus.CREATED);
    }


    @PostMapping("/games/players/{gamePlayerId}/ships")
    public ResponseEntity<String> addShips (@PathVariable Long gamePlayerId, Authentication authentication, @RequestBody Set<Ship> ships){

        if (isGuest(authentication)) {
            return new ResponseEntity(makeMap("error", "Not log in"), HttpStatus.UNAUTHORIZED);
        }

        Optional<GamePlayer> gamePlayerOptional = gamePlayerRepository.findById(gamePlayerId);
        if (gamePlayerOptional.isEmpty()){
            return new ResponseEntity(makeMap("error", "You don´t have acces"), HttpStatus.UNAUTHORIZED);
        }

        GamePlayer gamePlayer = gamePlayerOptional.get();

        Player player = playerRepository.findByUserName(authentication.getName());

       if(gamePlayer.getShips().size() >= 5){
           return new ResponseEntity<>("there are ships", HttpStatus.FORBIDDEN);
       }

       ships.forEach(ship -> gamePlayer.addShip(ship));
       gamePlayerRepository.save(gamePlayer);

       return new ResponseEntity<>("ship added", HttpStatus.CREATED);
    }

    @PostMapping("/games/players/{gamePlayerId}/salvos")
        public ResponseEntity<String> addSalvos (@PathVariable Long gamePlayerId, Authentication authentication, @RequestBody Salvo salvo){
        if (isGuest(authentication)) {
            return new ResponseEntity(makeMap("error", "Not log in"), HttpStatus.UNAUTHORIZED);
        }

        Optional<GamePlayer> gamePlayerOptional = gamePlayerRepository.findById(gamePlayerId);
        if (gamePlayerOptional.isEmpty()){
            return new ResponseEntity(makeMap("error", "You don´t have acces"), HttpStatus.UNAUTHORIZED);
        }

        GamePlayer gamePlayer = gamePlayerOptional.get();

        Player player = playerRepository.findByUserName(authentication.getName());

        if(salvo.getLocations().size() > 5){
            return new ResponseEntity<>("you have more salvos than permitted", HttpStatus.FORBIDDEN);
        }

        salvo.setTurn(gamePlayer.getSalvoes().size() + 1);


            GamePlayer gamePlayerx = gamePlayer.getOpponent();

            if(gamePlayer.getOpponent() == null){
                return new ResponseEntity<>("Falta 1 player", HttpStatus.FORBIDDEN);
            }

            if(gamePlayer.getOpponent() != null && (gamePlayer.getSalvoes().size()) > (gamePlayer.getOpponent().getSalvoes().size())){
                return new ResponseEntity<>("Not your turn", HttpStatus.FORBIDDEN);
            }




        gamePlayer.addSalvo(salvo);
        gamePlayerRepository.save(gamePlayer);


        return new ResponseEntity<>("salvo added", HttpStatus.CREATED);
    }

        private Map<String, Object> makeGameViewDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getGame().getId());
        dto.put("created", gamePlayer.getGame().getCreationDate());
        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers().stream()
                .map(this::makeGamePlayerDTO)
                .collect(Collectors.toList()));
        dto.put("ships", gamePlayer.getShips().stream()
                .map(this::makeShipDTO)
                .collect(Collectors.toList()));
        dto.put("salvoes", gamePlayer.getGame().getGamePlayers().stream()
                .flatMap(gp -> gp.getSalvoes().stream()
                        .map(this::makeSalvoDTO))
                .collect(Collectors.toList()));
        dto.put("hits", gamePlayer.getHits());
        if(gamePlayer.getOpponent() != null) {
            dto.put("opponentHits", gamePlayer.getOpponent().getHits());
        }
        return dto;


    }


    private Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gamePlayers", game.getGamePlayers().stream()
                .map(this::makeGamePlayerDTO)
                .collect(Collectors.toList()));
        return dto;
    }


    private Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getId());
        dto.put("player", makePlayerDTO(gamePlayer.getPlayer()));
        if (gamePlayer.getScore() != null)
            dto.put("score", gamePlayer.getScore().getScore());
        else
            dto.put("score", null);
        return dto;

    }

    private Map<String, Object> makePlayerDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", player.getId());
        dto.put("email", player.getUserName());
        return dto;
    }

    private Map<String, Object> makeShipDTO(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("type", ship.getType());
        dto.put("locations", ship.getLocations());
        return dto;
    }

    private Map<String, Object> makeSalvoDTO(Salvo salvo) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("player", makePlayerDTO(salvo.getGamePlayer().getPlayer()));
        dto.put("turn", salvo.getTurn());
        dto.put("locations", salvo.getLocations());
        return dto;
    }

    private Map<String, Object> makeScoreDTO(Score score) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("player", makePlayerDTO(score.getPlayer()));
        dto.put("score", score.getScore());
        return dto;
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}



