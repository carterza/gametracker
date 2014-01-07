$ ->
  class window.Game extends Backbone.Model
    defaults: ->
      id: null
      title: ''
      owned: false
      votes: []
    initialize: =>
      @set({"title": @defaults.title}) if not @get("title")
    markAsOwned: =>
      @save({owned: true})
      
  class window.Vote extends Backbone.Model
    defaults: ->
      id: null
      
  class window.WantedGameList extends Backbone.Collection
    model: Game
    url: jsRoutes.controllers.Application.games().url
      
  class window.OwnedGameList extends Backbone.Collection
    model: Game
    url: jsRoutes.controllers.Application.ownedGames().url
      
  class window.GameView extends Backbone.View
    tagname: 'li'
    template: _.template($('#item-template').html())
    events:
      'click .check': 'markAsOwned'
    initialize: =>
      @model.bind('change', @render, @)
    render: =>
      if 'owned' of @model.changed
        @updateCollections()
      $(@el).html(@template(@model.toJSON()))
      @setText()
      @
    updateCollections: =>
      if @model.get 'owned'
        WantedGames.remove @model
        OwnedGames.add @model
      else
        OwnedGames.remove @model
        WantedGames.add @model
    setText: =>
      text = @model.get('title')
      @$('.game-text').text(text)
      @input = @$('.game-input')
      @input.bind('blur', _.bind(@close, @)).val(text)
    markAsOwned: =>
      @model.markAsOwned()
    edit: =>
      $(@el).addClass 'editing'
      @input.focus
    close: =>
      @model.save {title: @input.val}
      $(@el).removeClass 'editing'
    updateOnEnter: (e) ->
      @close if e.keyCode is 13
      
  class window.AppView extends Backbone.View
    el: $('#gameapp')
    collection: window.WantedGames
    events:
      'keypress #new-game': 'createOnEnter'
      'keyup #new-game': 'showTooltip'
    initialize: =>
      @input = @$('#new-game')
      WantedGames.bind('add', @addOneWanted, @)
      OwnedGames.bind('add', @addOneOwned, @)
      WantedGames.fetch()
      OwnedGames.fetch()
    addOneWanted: (game) ->
      view = new GameView({model: game})
      console.log(game)
      $('#wanted-game-list').append(view.render().el)
    addOneOwned: (game) ->
      view = new GameView({model: game})
      $('#owned-game-list').append(view.render().el)
    createOnEnter: (e) =>
      text = @input.val()
      return if not text or e.keyCode isnt 13
      
      jsRoutes.controllers.Application.add().ajax
        type: 'POST'
        context: this
        data:
          title: text
          owned: false
        success: (tpl) ->
          newGame = new Game(tpl)
          WantedGames.add(newGame)
        err: (err) ->
          alert "Something went wrong:" + err
          
      @input.val('')
      false
    showTooltip: (e) =>
      tooltip = @$('.ui-tooltip-top')
      val = @input.val()
      tooltip.fadeOut()
      clearTimeout(@tooltipTimeout) if @tooltipTimeout
      return if val is '' or val is @input.attr('placeholder')
      show = -> tooltip.show().fadeIn()
      @tooltipTimeout = _.delay(show, 1000)
      
  window.WantedGames = new WantedGameList
  window.OwnedGames = new OwnedGameList
  window.App = new AppView
  