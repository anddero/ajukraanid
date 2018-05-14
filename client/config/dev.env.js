'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  requestDestination: '"http://localhost:8080/api"',
  adminDestination: '"http://localhost:8080/admin"',
  loginDestination: '"http://localhost:8080/login"'
})
