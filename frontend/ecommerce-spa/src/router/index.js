import Vue from 'vue'
import Router from 'vue-router'
import ProductList from '@/components/ProductList.vue'
import ProductDetail from '@/components/ProductDetail.vue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'ProductList',
      component: ProductList
    },
    {
      path: '/products/:id',
      name: 'ProductDetail',
      component: ProductDetail,
      props: true
    }
  ]
})
