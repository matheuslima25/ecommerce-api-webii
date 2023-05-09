<template>
  <div>
    <h2>Product Registation</h2>
    <form @submit.prevent="save">
      <div class="form-group">
        <label>Product name</label>
        <input type="text" v-model="product.name" class="form-control" placeholder="Product name">
      </div>
      <div class="form-group">
        <label>Product description</label>
        <input type="text" v-model="product.description" class="form-control" placeholder="Product description">
      </div>
      <div class="form-group">
        <label>Product price</label>
        <input type="number" step=".01" v-model="product.price" class="form-control" placeholder="0.0">
      </div>
      <div class="form-group">
        <label>Product category</label>
        <select id="category-select" v-model="selectedCategory">
          <option v-for="category in categories" :key="category.id" :value="category.id">{{ category.name }}</option>
        </select>
      </div>
      <button type="submit" class="btn btn-primary">Save</button>
    </form>
    <h2>Product View</h2>
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
            <button type="button" class="btn btn-warning" @click="editProduct(product)">Edit</button>
            <button type="button" class="btn btn-danger" @click="deleteProduct(product)">Delete</button>
          </td>
        </tr>

      </tbody>
    </table>

  </div>
</template>

<script>
import Vue from 'vue'
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
  },
  methods: {
    cloneProduct (product) {
      return JSON.parse(JSON.stringify(product))
    },
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
    save () {
      if (this.product.id) {
        this.updateProduct()
      } else {
        this.saveProduct()
      }
    },
    saveProduct () {
      const formData = new FormData()
      formData.append('name', this.product.name)
      formData.append('description', this.product.description)
      formData.append('price', this.product.price)
      formData.append('category', this.selectedCategory)

      axios
        .post('http://localhost:8080/api/products', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        .then(({ data }) => {
          alert('Product saved successfully!')
          this.resetForm()
          this.getProductList()
        })
        .catch((error) => {
          alert('Error saving product!')
          console.log(error)
        })
    },
    updateProduct () {
      const oldProduct = this.originalList.find(p => p.id === this.product.id)
      const formData = new FormData()
      const updatedFields = {}

      if (this.product.name !== oldProduct.name) {
        formData.append('name', this.product.name)
        updatedFields.name = this.product.name
        console.log('name updated')
        console.log(this.product.name)
        console.log(updatedFields)
      }

      if (this.product.description !== oldProduct.description) {
        formData.append('description', this.product.description)
        updatedFields.description = this.product.description
      }

      if (this.product.price !== oldProduct.price) {
        formData.append('price', this.product.price)
        updatedFields.price = this.product.price
      }

      if (this.selectedCategory !== oldProduct.category.id) {
        formData.append('category', this.selectedCategory)
        updatedFields.category = this.selectedCategory
      }

      const index = this.result.findIndex((p) => p.id === this.product.id)
      if (index !== -1) {
        const updatedProduct = { ...this.result[index] }
        updatedProduct.name = updatedFields.name || updatedProduct.name
        updatedProduct.description = updatedFields.description || updatedProduct.description
        updatedProduct.price = updatedFields.price || updatedProduct.price
        updatedProduct.category = this.categories.find(category => category.id === updatedFields.category) || updatedProduct.category
        this.result.splice(index, 1, updatedProduct)
      }

      axios
        .patch(`http://localhost:8080/api/products/${oldProduct.id}`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        .then(({ data }) => {
          alert('Product updated successfully!')
          this.resetForm()

          // atualize a variável 'result' com os dados atualizados
          const index = this.result.findIndex((p) => p.id === this.product.id)
          if (index !== -1) {
            const updatedProduct = { ...this.result[index], ...updatedFields }
            Vue.set(this.result, index, updatedProduct)
          }

          this.getProductList()
        })
        .catch((error) => {
          alert('Error updating product!')
          console.log(error)
        })
    },
    deleteProduct (product) {
      const confirmed = confirm('Are you sure you want to delete this product?')
      if (confirmed) {
        axios
          .delete(`http://localhost:8080/api/products/${product.id}`)
          .then(({ data }) => {
            alert('Product deleted successfully!')
            this.getProductList()
          })
          .catch((error) => {
            alert('Error deleting product!')
            console.log(error)
          })
      }
    },
    editProduct (product) {
      this.product = this.cloneProduct(product)
      this.selectedCategory = this.product.category.id
    },
    resetForm () {
      this.product = {
        id: '',
        name: '',
        description: '',
        price: '',
        category: null
      }
      this.selectedCategory = null
    },
    submitForm () {
      if (this.product.id) {
        this.updateProduct()
      } else {
        this.saveProduct()
      }
    }
  }
}

</script>
