export default {
  register ({commit}, user) {
    commit('register', user)
  },
  startGame ({commit}) {
    commit('startGame')
  },
  updatePlayers ({commit}, reservatiosn) {
    commit('updatePlayers', reservatiosn)
  },
  setGameCode ({commit}, gamecode) {
    commit('setGameCode', gamecode)
  },
  setMyUsername ({commit}, name) {
    commit('setMyUsername', name)
  },
  setLastQuestion ({commit}, question) {
    commit('setLastQuestion', question)
  },
  loadState ({commit}) {
    commit('loadState')
  }
}
