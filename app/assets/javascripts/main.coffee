$ ->

  class window.Vote extends Backbone.RelationalModel
    urlRoot: '/votes'
    idAttribute: Backbone.Model.prototype.idAttribute

  class window.Game extends Backbone.RelationalModel
    urlRoot: '/games'
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
    url: '/games'
    model: window.Game
    initialize: ->
      @bind 'sync', @bindModelEvents, @
    bindModelEvents: =>
      @bindVoteAddedEvent game for game in @models
    bindVoteAddedEvent: (game) ->
      game.bind 'add:votes', @onVoteAdded, @
    onVoteAdded: =>
      @sort()
    
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
      @$('ul.game_list').append($(game_view.render()))
      
    events:
      'click .new_game_submit': 'onSubmit'
      
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
      @model.bind 'change', @sortCollection, @
      
    template: Handlebars.compile($('#tpl_game').html())
    
    render: =>
      $(@el).html(@template(@model.toJSON()))
      
    sortCollection: =>
      alert 'Sort!'
      
    events:
      'click .new_vote_submit': 'onSubmit'
      
    onSubmit: (e) =>
      vote = new window.Vote {game: @model}
      vote.save()
      
  class window.Router extends Backbone.Router
    routes:
      '': 'showGameLists'
    
    showGameLists: ->
      wanted_game_collection = new window.GameCollection
      wanted_game_collection.comparator = (game) ->
        -game.get('votes').length
      wanted_game_list_view = new window.GameListView {el: $('#wanted_games'), model: wanted_game_collection}
      wanted_game_collection.fetch {data: {owned: false}}
      
      owned_game_collection = new window.GameCollection
      owned_game_collection.comparator = (game) ->
        game.get('title')
      owned_game_list_view = new window.GameListView {el: $('#owned_games'), model: owned_game_collection}
      owned_game_collection.fetch {data: {owned: true}}       
      
  window.App = null
  window.App = new Router
  Backbone.history.start {pushState: true}