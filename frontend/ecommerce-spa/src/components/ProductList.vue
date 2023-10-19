<template>
  <div>
    <h2>Product View <button type="button" class="btn btn-primary"><router-link :to="{ name: 'ProductDetail', params: { id: null } }" tag="div">New</router-link></button></h2>
    <table class="table table-dark">
      <thead>
        <tr>
          <th scope="col">ID</th>
          <th scope="col">Name</th>
          <th scope="col">Description</th>
          <th scope="col">Price</th>
          <th scope="col">Category</th>
          <th scope="col">Option</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="product in result" v-bind:key="product.id">

          <td>{{ product.id }}</td>
          <td>{{ product.name }}</td>
          <td>{{ product.description }}</td>
          <td>{{ product.price }}</td>
          <td>{{ product.category.name }}</td>
          <td>
            <button type="button" class="btn btn-warning"><router-link :to="{ name: 'ProductDetail', params: { id: product.id } }" tag="div">Edit</router-link></button>
            <button type="button" class="btn btn-danger" @click="deleteProduct(product)">Delete</button>
          </td>
        </tr>

      </tbody>
    </table>

  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Product',
  data () {
    return {
      result: [],

      categories: [],
      selectedCategory: null,

      product: {
        id: '',
        name: '',
        description: '',
        price: '',
        category: null
      }
    }
  },
  created () {
    this.getProductList()
  },
  mounted () {
    this.getCategoryList()
    // this.getProductList()
  },
  methods: {
    getProductList () {
      axios.get('http://localhost:8080/api/products')
        .then(({ data }) => {
          this.result = data
          this.originalList = data.map(p => ({ ...p }))
        })
        .catch((error) => {
          alert('Error loading products!')
          console.log(error)
        })
    },
    async getCategoryList () {
      const response = await fetch('http://localhost:8080/api/categories')
      const data = await response.json()
      this.categories = data
    },
    deleteProduct (product) {
      const confirmed = confirm('Are you sure you want to delete this product?')
      if (confirmed) {
        axios
          .delete(`http://localhost:8080/api/products/soft-delete/${product.id}`)
          .then(({ data }) => {
            alert('Product deleted successfully!')
            this.getProductList()
          })
          .catch((error) => {
            alert('Error deleting product!')
            console.log(error)
          })
      }
    }
  }
}

</script>
