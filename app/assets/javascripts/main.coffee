# -----------------------------------------------
# MAIN
# -----------------------------------------------
# DISCLAMER :
# If you're used to Backbone.js, you may be
# confused by the absence of models, but the goal
# of this sample is to demonstrate some features
# of Play including the template engine.
# I'm not using client-side templating nor models
# for this purpose, and I do not recommend this
# behavior for real life projects.
# -----------------------------------------------

class Games extends Backbone.View
    initialize: ->
        $("#newGame").click @addGame
    addGame: (e) ->
        r = jsRoutes.controllers.Games.add()
        $.ajax
            url: r.url
            type: r.type
            context: this
            success: (data) ->
                _view = new Game
                    el: $("#wantedGames").html(data)
            error: (err) ->
                $.error("Error: " + err)
                
class Game extends Backbone.View

$ ->

    games = new Games el : $("#wantedGames")