var app = new Vue({
  el: '#app',
  data: {
    gameView: {},
    player1: {},
    player2: {},
    user: null,
    gpid: null,
    arrayLocationsShip: [],
    arrayLocationsSalvo: []
  },
  methods: {

 logout: function(){
             $.post("/api/logout").done(function() { console.log("logged out"); })
             .done(function() { window.location="http://localhost:8080/web/games.html?userName=ntsd&Password="; console.log("logged out!");})
          },
 posicionateShips: function(){
    app.recorrerShips();
        $.post({
        url: '/api/games/players/'+app.gpid+'/ships',
        data: JSON.stringify( app.arrayLocationsShip ),
        dataType: "text",
        contentType: "application/json"
        })
        .done(function(){
        alert("Ship added:");
        window.location.reload(true);

        })
        .fail(function(){
        alert("Failed to add ship");
        })

 },

posicionateSalvos: function(){
        $.post({
        url: '/api/games/players/'+app.gpid+'/salvos',
        data: JSON.stringify({"turn":0, "locations": app.arrayLocationsSalvo }),
        dataType: "text",
        contentType: "application/json"
        })
        .done(function(){
        alert("salvo fired");
        window.location.reload(true);

        })
        .fail(function(){
        alert("something went wrong")})
 },

recorrerShips: function(){
    $("#Trompeta, #Baquetas, #Microfono, #Piano, #Guitarra").each(function(i, barco){
        var h = parseInt($(this).attr("data-gs-height"));
        var w = parseInt($(this).attr("data-gs-width"));
        var posX = parseInt($(this).attr("data-gs-x"));
        var posY = parseInt($(this).attr("data-gs-y"));

        var arrayLocations = [];
            if(w != 1){
                for (var i = 0; i < w; i++){
                arrayLocations.push(String.fromCharCode(65+posY)+(posX+1+i));
                }
            } else{
                for (var i = 0; i < h; i++){
                arrayLocations.push(String.fromCharCode(i+posY+65)+(posX+1));
                }
            }
       app.arrayLocationsShip.push({ "type":barco.id, "locations": arrayLocations})
})
console.log(app.arrayLocationsShip);
},

}
})




const urlParams = new URLSearchParams(window.location.search);
app.gpid = urlParams.get('gp');


fetch('/api/game_view/'+ app.gpid)
  .then(function(response) {
        return response.json();
   })
  .then(function(myJson) {
    console.log(myJson);
    app.gameView = myJson;
    app.salvoes = myJson.salvoes;
    app.ships = myJson.ships;
    players(myJson.gamePlayers);
    inicializarGrilla();
    playShips(myJson.ships);
    playSalvoes(app.salvoes, app.ships);
    addEvents();
    console.log(app.player1);
    console.log(app.player2);
    console.log(app.salvoes);
    console.log(app.ships);
  });



function players(gamePlayers){
    for (var i = 0; i < gamePlayers.length; i++){
       if (gamePlayers[i].id == app.gpid)
        app.player1 = gamePlayers[i].player;
       else
        app.player2 = gamePlayers[i].player;
         };
};

function playShips(ships){
    grid = $('#grid').data('gridstack');
    if(ships.length != 0){
    ships.forEach(function(ship){
       var searchChar = ship.locations[0].slice(0, 1);
       var secondChar = ship.locations[1].slice(0, 1);
       if ( searchChar === secondChar ) {
           ship.position = "Horizontal";
       } else {
           ship.position = "Vertical";
       }
       for (var i=0; i < ship.locations.length; i++) {
           ship.locations[i] = ship.locations[i].replace(/A/g, '0');
           ship.locations[i] = ship.locations[i].replace(/B/g, '1');
           ship.locations[i] = ship.locations[i].replace(/C/g, '2');
           ship.locations[i] = ship.locations[i].replace(/D/g, '3');
           ship.locations[i] = ship.locations[i].replace(/E/g, '4');
           ship.locations[i] = ship.locations[i].replace(/F/g, '5');
           ship.locations[i] = ship.locations[i].replace(/G/g, '6');
           ship.locations[i] = ship.locations[i].replace(/H/g, '7');
           ship.locations[i] = ship.locations[i].replace(/I/g, '8');
           ship.locations[i] = ship.locations[i].replace(/J/g, '9');
       }
       var yInGrid = parseInt(ship.locations[0].slice(0, 1));
       var xInGrid = parseInt(ship.locations[0].slice(1, 3)) - 1;

        if (ship.position === "Horizontal") {
             grid.addWidget($('<div id="'+ship.type+'"><div class="grid-stack-item-content ' + ship.type +'Horizontal"></div><div/>'),
             xInGrid, yInGrid, ship.locations.length, 1, false);
          } else if (ship.position === "Vertical") {
             grid.addWidget($('<div id="'+ship.type+'"><div class="grid-stack-item-content ' + ship.type +'Vertical"></div><div/>'),
             xInGrid, yInGrid, 1, ship.locations.length, false);
          }
    })}
    else {
        grid.addWidget($('<div id="Trompeta"><div class="grid-stack-item-content TrompetaHorizontal"></div><div/>'),
        0, 0, 2, 1, false),
        grid.addWidget($('<div id="Baquetas"><div class="grid-stack-item-content BaquetasHorizontal"></div><div/>'),
        0, 0, 3, 1, false),
        grid.addWidget($('<div id="Microfono"><div class="grid-stack-item-content MicrofonoHorizontal"></div><div/>'),
        0, 0, 3, 1, false),
        grid.addWidget($('<div id="Piano"><div class="grid-stack-item-content PianoHorizontal"></div><div/>'),
        0, 0, 5, 1, false),
        grid.addWidget($('<div id="Guitarra"><div class="grid-stack-item-content GuitarraHorizontal"></div><div/>'),
        0, 0, 4, 1, false)
    }
}




function addEvents(){
   $("#Trompeta, #Baquetas, #Microfono, #Piano, #Guitarra").click(function(){
       var h = parseInt($(this).attr("data-gs-height"));
       var w = parseInt($(this).attr("data-gs-width"));
       var posX = parseInt($(this).attr("data-gs-x"));
       var posY = parseInt($(this).attr("data-gs-y"));
       if (w>h) {
          if ( grid.isAreaEmpty(posX, posY+1, h, w-1) && posY+w<=10 ) {
              grid.update($(this), posX, posY, h, w);
              $(this).children('.grid-stack-item-content').removeClass($(this).attr('id')+"Horizontal");
              $(this).children('.grid-stack-item-content').addClass($(this).attr('id')+"Vertical");
          }
      }else {
          if ( grid.isAreaEmpty(posX+1, posY, h-1, w) && posX+h<=10 ) {
              grid.update($(this), posX, posY, h, w);
              $(this).children('.grid-stack-item-content').addClass($(this).attr('id')+"Horizontal");
              $(this).children('.grid-stack-item-content').removeClass($(this).attr('id')+"Vertical");
          }
      }
   });
}



function inicializarGrilla(){
    var options = {
            //grilla de 10 x 10
            width: 10,
            height: 10,
            //separacion entre elementos (les llaman widgets)
            verticalMargin: 0,
            //altura de las celdas
            cellHeight: 40,
            //desabilitando el resize de los widgets
            disableResize: true,
            //widgets flotantes
    		float: true,
            //removeTimeout: 100,
            //permite que el widget ocupe mas de una columna
            disableOneColumnMode: true,
            //false permite mover, true impide
            staticGrid: false,
            //activa animaciones (cuando se suelta el elemento se ve más suave la caida)
            animate: true
        }
      var options1 = {
                  //grilla de 10 x 10
                  width: 10,
                  height: 10,
                  //separacion entre elementos (les llaman widgets)
                  verticalMargin: 0,
                  //altura de las celdas
                  cellHeight: 40,
                  //desabilitando el resize de los widgets
                  disableResize: true,
                  //widgets flotantes
          		float: true,
                  //removeTimeout: 100,
                  //permite que el widget ocupe mas de una columna
                  disableOneColumnMode: true,
                  //false permite mover, true impide
                  staticGrid: true,
                  //activa animaciones (cuando se suelta el elemento se ve más suave la caida)
                  animate: true
              }
              if(app.gameView.ships.length != 5){
    //se inicializa el grid con las opciones
     $('.grid-stack').gridstack(options);
} else $('.grid-stack').gridstack(options1);

}

function playSalvoes(salvoes, ships){
       var shipLocations = []
       for (var s = 0; s < ships.length; s++){
            shipLocations = shipLocations.concat(ships[s].locations);
       }
       for (var i = 0; i < salvoes.length; i++) {
        for (var l = 0; l < salvoes[i].locations.length; l++) {
            if(app.player1.id == salvoes[i].player.id) {
               document.getElementById(salvoes[i].locations[l]).innerHTML +=  '<div class="myShot"</div>';
            }
            else{
                 salvoes[i].locations[l] = salvoes[i].locations[l].replace(/A/g, '0');
                 salvoes[i].locations[l] = salvoes[i].locations[l].replace(/B/g, '1');
                 salvoes[i].locations[l] = salvoes[i].locations[l].replace(/C/g, '2');
                 salvoes[i].locations[l] = salvoes[i].locations[l].replace(/D/g, '3');
                 salvoes[i].locations[l] = salvoes[i].locations[l].replace(/E/g, '4');
                 salvoes[i].locations[l] = salvoes[i].locations[l].replace(/F/g, '5');
                 salvoes[i].locations[l] = salvoes[i].locations[l].replace(/G/g, '6');
                 salvoes[i].locations[l] = salvoes[i].locations[l].replace(/H/g, '7');
                 salvoes[i].locations[l] = salvoes[i].locations[l].replace(/I/g, '8');
                 salvoes[i].locations[l] = salvoes[i].locations[l].replace(/J/g, '9');
                 if(shipLocations.indexOf(salvoes[i].locations[l]) != -1){
                    var yInGrid = (parseInt(salvoes[i].locations[l].slice(0, 1)))*40;
                    var xInGrid = (parseInt(salvoes[i].locations[l].slice(1, 3)) - 1)*40;
                        $("#grid").append('<div class="enemyShot" style="top:' + yInGrid + 'px; left:' + xInGrid + 'px;"></div>')
                 }
                 else {
                    var yInGrid = (parseInt(salvoes[i].locations[l].slice(0, 1)))*40;
                    var xInGrid = (parseInt(salvoes[i].locations[l].slice(1, 3)) - 1)*40;
                    $("#grid").append('<div class="enemyMiss" style="top:' + yInGrid + 'px; left:' + xInGrid + 'px;"></div>')
                 }
            }
        }
       }
}

$("#salvos td").click(function() {

    if(app.arrayLocationsSalvo.length <= 4 && app.arrayLocationsSalvo.indexOf(this.id) == -1){
        $(this).addClass("disparo-parcial")
        app.arrayLocationsSalvo.push((this).id);
  }
    else if(app.arrayLocationsSalvo.indexOf(this.id) != -1){
        (this).classList.remove("disparo-parcial")
        app.arrayLocationsSalvo = app.arrayLocationsSalvo.filter(l => l != this.id);
  }

    else(alert("you have already select your shots"))
        console.log(app.arrayLocationsSalvo);
 }

)

