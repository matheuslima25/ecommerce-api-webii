<template>
  <div class="hello">
    <img v-bind:src="image" :alt="`${name}`" class="picture" />
      <h5>ID: {{ id }}</h5>
      <h1>Product: {{ name }}</h1>
      <h3>Description: {{ description }}</h3>
      <h3>Price: {{ price }}</h3>
      <h3>Category: {{ category }}</h3>
      <button class="btn-random" @click="getProduct()">Get Next</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      id: '3643aad7-6a82-4767-b52a-9542f1456439',
      name: 'Keyboard',
      description: 'Gamer Kayboard',
      price: '59.99',
      category: 'Peripheral',
      image: 'https://images.tcdn.com.br/img/img_prod/740836/teclado_mecanico_razer_blackwidow_v3_tenkeyless_razer_chroma_rgb_us_switch_yellow_12535_1_10e721931a23430dae9288fe66c122bb.jpg',
    }
  },
  methods: {
    async getProduct() {
      const res = await fetch('http://localhost:8080/api/products')
      const data = await res.json()
      const firstProduct = data[0]

      console.log(data)
      console.log(firstProduct)

      this.id = firstProduct.id
      this.name = firstProduct.name
      this.description = firstProduct.description
      this.price = firstProduct.price
      this.category = firstProduct.category.name
      this.image = 'http://localhost:8080' + firstProduct.image

      console.log(this.image)
    },
  },
  created() {
    this.getProduct();
  },
};
</script>

<style>
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

html,
body {
  font-family: Arial, Helvetica, sans-serif;
  background: #1a1a1a;
  color: #fcfcfc;
}

#app {
  width: 400px;
  height: 100vh;
  margin: auto;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

h1,
h3 {
  margin-bottom: 1rem;
  font-weight: normal;
}

h1 {
  font-weight: bold;
  text-decoration: underline;
}

h5 {
  margin-bottom: 1rem;
}

img {
  border: double 5px transparent;
  border-radius: 50%;
  background-image: linear-gradient(white, white),
    radial-gradient(
      circle at top left,
      rgba(255, 0, 0, 1) 0%,
      rgba(255, 154, 0, 1) 10%,
      rgba(208, 222, 33, 1) 20%,
      rgba(79, 220, 74, 1) 30%,
      rgba(63, 218, 216, 1) 40%,
      rgba(47, 201, 226, 1) 50%,
      rgba(28, 127, 238, 1) 60%,
      rgba(95, 21, 242, 1) 70%,
      rgba(186, 12, 248, 1) 80%,
      rgba(251, 7, 217, 1) 90%,
      rgba(255, 0, 0, 1) 100%
    );
  background-origin: border-box;
  background-clip: content-box, border-box;
  display: inline-block;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.19), 0 6px 6px rgba(0, 0, 0, 0.23);
  margin-bottom: 1rem;
}

button {
  cursor: pointer;
  display: inline-block;
  background: #333;
  color: white;
  font-size: 18px;
  border: 0;
  padding: 1rem 1.5rem;
}

button:focus {
  outline: none;
}

button:hover {
  transform: scale(0.98);
}

.btn-random {
  background-color: white;
  border-radius: 5px;
  color: #1a1a1a;
  font-weight: bold;
  margin: 15px;
}

.picture {
  height: 500px;
}
</style>
