$ ->
  class Game extends Backbone.Model
    defaults:
      id: null
      title: ''
      owned: false
      votes: []
    
    initialize: ->
      if !@get('title')
          @set({'title': @defaults.title})
          
    markOwned: ->
      @save({owned: true})
      
  class WantedGamesList extends Backbone.Collection
    model: Game
    url: jsRoutes.controllers.Application.games().url
    
  class OwnedGamesList extends Backbone.Collection
    model: Game
    comparator: 'title'
    url: jsRoutes.controllers.Application.ownedGames().url
    
  class GameView extends Backbone.View
    tagName: 'li'
    
    template: _.template($('#item-template').html())
    
    events:
      'click .check'          : 'markOwned'
      'click  .vote'          : 'vote'
    
    initialize: ->
      @model.bind('change', @render, @)
      @model.bind('remove', @remove, @)
      @model.bind('sync', @updateCollection, @)
      
    render: =>
      $(@el).html(@template(@model.toJSON()))
      @setTitle()
      return this
      
    remove: =>
      $(@el).remove()
      
    updateCollection: (model, resp, options) =>
      if @model.get('owned') and @model.collection instanceof WantedGamesList
        WantedGames.remove @model
        OwnedGames.add @model
        
    setTitle: ->
      title = @model.get('title')
      @$('game-text').text(title)
      @titleSpan = @$('.game-title')
      @titleSpan.html(title)
      
    markOwned: ->
      @model.markOwned()
      
    vote: ->
      
  
  class AppView extends Backbone.View
    el: $('#gameapp')
    
    events:
      'keypress #new-game'  : 'createOnEnter',
      'keyup #new-game'     : 'showTooltip'
      
    initialize: =>
      @input = @$('#new-game')
      
      WantedGames.bind('add', @addOneWanted, @)
      WantedGames.bind('reset', @addAllWanted, @)
      WantedGames.bind('all', @renderWanted, @)
      
      OwnedGames.bind('add', @addOneOwned, @)
      OwnedGames.bind('reset', @addAllOwned, @)
      OwnedGames.bind('all', @renderOwned, @)
      
      WantedGames.fetch()
      OwnedGames.fetch()
      
    renderWanted: =>
      @$('#wanted-games').empty()
      for game in WantedGames.models
        do (game) =>
          view = new GameView({model: game})
          @$('#wanted-games').append(view.render().el)
          
    renderOwned: =>
      @$('#owned-games').empty()
      for game in OwnedGames.models
        do (game) =>
          view = new GameView({model: game})
          @$('#owned-games').append(view.render().el)
    
    addOneWanted: (game) =>
      view = new GameView({model: game})
      @$('#wanted-games').append(view.render().el)
      
    addAllWanted: =>
      WantedGames.each(@addOneWanted)
      
    addOneOwned: (game) =>
      view = new GameView({model: game})
      @$('#owned-games').append(view.render().el)
      
    addAllOwned: =>
      OwnedGames.each(@addOneOwned)
      
    newAttributes: ->
      return {
        title: @input.val(),
        owned: false,
        votes: []
      }
      
    createOnEnter: (e) ->
      return if (e.keyCode != 13)
      WantedGames.create(@newAttributes())
      @input.val('')
      
    showTooltip: (e) ->
      tooltip = this.$('.ui-tooltip-top')
      val = @input.val()
      tooltip.fadeOut()
      clearTimeout(@tooltipTimeout) if (@tooltipTimeout)
      return if (val is '' || val is @input.attr('placeholder'))
      
      show = () ->
        tooltip.show().fadeIn()
      @tooltipTimeout = _.delay(show, 1000)

  WantedGames = new WantedGamesList()
  OwnedGames = new OwnedGamesList()
  App = new AppView()
    