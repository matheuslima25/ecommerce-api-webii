// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios'

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})

// Intercepta todas as requisições antes de serem enviadas
axios.interceptors.request.use((config) => {
  // Adiciona o cabeçalho Authorization com o token no formato Bearer
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
    console.log(`Token set to: ${token}`)
  }
  console.log('intercepted!')
  return config
})
