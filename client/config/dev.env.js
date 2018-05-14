'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  requestDestination: '"http://18.188.242.2:8080/api"',
  adminDestination: '"http://18.188.242.2:8080/admin"',
  loginDestination: '"http://18.188.242.2:8080/login"'
})
