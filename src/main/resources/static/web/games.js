    var app = new Vue({
      el: '#app',
      data: {
       games: [ ],
       scores: [ ],
       user: null,
       userName:"",
       password:""
      },

      mounted: function(){
        $.getJSON("/api/games", function(data){
                console.log(data)
                app.games = data.games
                app.scores = app.findPlayers(app.games)
                app.user = data.player
            });
      },
      methods: {
        login : function (){
            $.post("/api/login", { userName: app.userName, password: app.password })
            .done(function() { window.location.reload(true); console.log("logged in!");})
            .catch(function() { alert("user name or password invalid"); })

        },
        logout: function(){
            $.post("/api/logout").done(function() { console.log("logged out"); })
            .done(function() { window.location.reload(true); console.log("logged out!");})
        },
        signUp: function(){
            $.post("/api/players", { userName: app.userName, password: app.password})
            .done(function() {console.log("Sign up!"); app.login();})
            .catch(function() {alert("Username already in use")})
        },
        createAGame: function(){
        $.post("/api/games").done(function() {console.log("Game Created!"); })
        .done(function(data) { window.location.href = 'game.html?gp=' + data.GamePlayer; console.log("Game created!");})
        .catch(function() {alert("Ups! You´ve send a bad request")})
        },
        joinAGame: function(id){
        $.post('/api/game/' + id + '/players').done(function() {console.log("Game Joined"); })
        .done(function(data) { window.location.href = 'game.html?gp=' + data.id; console.log("Game Joined"); })
        .catch(function() {alert("Ups! You´ve send a bad request")})
        },

        findPlayers: function(games){
                var infoPlayers = [];
                for (var g = 0; g < games.length; g++){
                    for (var gp = 0; gp < games[g].gamePlayers.length; gp++){
                        var index = infoPlayers.findIndex(function (score){ return score.email == games[g].gamePlayers[gp].player.email});
                        if (index == -1 ) {
                            var score = {email: games[g].gamePlayers[gp].player.email, win : 0, lost: 0, tie : 0, total : 0}
                            if(games[g].gamePlayers[gp].score == 1)
                                score.win++;
                            else if(games[g].gamePlayers[gp].score == 0)
                                score.lost++;
                            else if(games[g].gamePlayers[gp].score == 0.5)
                                score.tie++;

                            if(games[g].gamePlayers[gp].score != null)
                                score.total += games[g].gamePlayers[gp].score;


                             infoPlayers.push(score);
                         }else{
                            if(games[g].gamePlayers[gp].score == 1)
                                infoPlayers[index].win++;
                            else if(games[g].gamePlayers[gp].score == 0)
                                infoPlayers[index].lost++;
                            else if(games[g].gamePlayers[gp].score == 0.5)
                                infoPlayers[index].tie++;

                            if(games[g].gamePlayers[gp].score != null)
                                infoPlayers[index].total += games[g].gamePlayers[gp].score;
                         }
                    }
                }
                    return infoPlayers;
           },
           }
      })




