import Vue from 'vue';
import Resource from 'vue-resource';

Vue.use(Resource);

export default {

  register(state, userObject) {
    const date = new Date;

    const alreadyExistingUser = state.registrations.find(registration => {
      return registration.name === userObject.name;
    });
    if (alreadyExistingUser === undefined) {
      const registration = {
        name: userObject.name,
        gameCode: userObject.gameCode
      };
      state.registrations.push(registration);
    } else {
    }
  },

  unregister(state, payload) {
    const registration = state.registrations.find(registration => {
      return registration.name === payload.name;
    });
    state.registrations.splice(state.registrations.indexOf(registration), 1);
  },

  updatePlayers(state, players) {
    state.registrations = players;
  },

  setGameCode(state, gameCode) {
    state.gameCode = gameCode;
  },

  setMyUsername(state, name) {
    state.username = name;
  },
};
