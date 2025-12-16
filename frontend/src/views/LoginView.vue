<template>
  <div class="login-container">
    <h2>Вход в аккаунт</h2>
    <form @submit.prevent="login" class="login-form">
      <div class="form-group">
        <label for="login">Логин</label>
        <input
          id="login"
          v-model="login"
          type="text"
          required
          placeholder="Введите логин"
        />
      </div>
      <div class="form-group">
        <label for="password">Пароль</label>
        <input
          id="password"
          v-model="password"
          type="password"
          required
          placeholder="Введите пароль"
        />
      </div>
      <form @submit.prevent="handleLogin">
        <button type="submit" :disabled="loading">
          {{ loading ? 'Вход...' : 'Войти' }}
        </button>
      </form>
      <router-link to="/register" class="btn btn-outline">
        Зарегистрироваться
      </router-link>
      <div v-if="error" class="error">{{ error }}</div>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const login = ref('')
const password = ref('')
const loading = ref(false)
const error = ref(null)

const router = useRouter()

const handleLogin = async () => {
  loading.value = true
  error.value = null

  try {
    const response = await axios.post('http://localhost:8081/api/users/auth/login', {
      login: login.value,
      password: password.value
    })

    localStorage.setItem('user', JSON.stringify(response.data))
    await router.push('/profile')
  } catch (err) {
    console.error('Login error:', err)
    error.value = 'Неверный логин или пароль'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: #f9f9f9;
}
.login-form h2 {
  text-align: center;
  margin-bottom: 20px;
}
.form-group {
  margin-bottom: 15px;
}
.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}
.form-group input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}
.btn {
  width: 100%;
  padding: 10px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}
.error {
  color: red;
  margin-top: 10px;
  text-align: center;
}
</style>
