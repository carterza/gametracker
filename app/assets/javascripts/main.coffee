$ ->

  class window.Vote extends Backbone.RelationalModel
    urlRoot: '/vote'
    idAttribute: Backbone.Model.prototype.idAttribute

  class window.Game extends Backbone.RelationalModel
    urlRoot: '/game'
    idAttribute: Backbone.Model.prototype.idAttribute
    relations: [
      type: Backbone.HasMany
      key: 'votes'
      relatedModel: 'window.Vote'
      reverseRelation: {
        key: 'game'
        includeInJSON: Backbone.Model.prototype.idAttribute
      }
    ]

  class window.GameCollection extends Backbone.Collection
    url: '/game'
    model: window.Game
    
  class window.GameListView extends Backbone.View
    tagName: 'div'
    
    className: 'game_list_view'
    
    initialize: ->
      @model.bind 'all', @render, @
      @model.bind 'add', @renderGame, @
      
    template: Handlebars.compile($('#tpl_game_list').html())
    
    render: =>
      $(@el).html(@template())
      @renderGame game for game in @model.models
      $(@el).html()
      
    renderGame: (game) =>
      game_view = new window.GameView {model: game}
      @$('ul.game_list').prepend($(game_view.render()))
      
    events:
      'click input[type=submit]': 'onSubmit'
      
    onSubmit: (e) =>
      game = new window.Game {title: @$('.new_game_title').val()}
      game.save {}, {success: @onGameCreated, error: @onError}
      
    onGameCreated: (game, response) =>
      @model.add game, {at: 0}
      vote = new window.Vote {game: game.get('id')}
      vote.save {}, {error: @onError}
      
    onError: (model, response) =>
      error = $.parseJSON(response.responseText)
      @$('.error-message').html(error.message)

  class window.GameView extends Backbone.View
    tagName: 'li'
    
    className: 'game_view'
    
    initialize: ->
      @model.bind 'all', @render, @
      
    template: Handlebars.compile($('#tpl_game').html())
    
    render: =>
      $(@el).html(@template(@model.toJSON()))
      
  class window.Router extends Backbone.Router
    routes:
      '': 'showGameList'
    
    showGameList: ->
      game_collection = new window.GameCollection
      game_list_view = new window.GameListView {el: $('#content'), model: game_collection}
      
      game_collection.fetch()
      
  window.App = null
  window.App = new Router
  Backbone.history.start {pushState: true}