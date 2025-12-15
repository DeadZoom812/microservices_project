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
      <button type="submit" :disabled="loading" class="btn">
        {{ loading ? 'Вход...' : 'Войти' }}
      </button>
      <div v-if="error" class="error">{{ error }}</div>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'LoginView',
  data() {
    return {
      login: '',
      password: '',
      loading: false,
      error: null
    };
  },
  methods: {
    async login() {
      this.loading = true;
      this.error = null;
      try {
        const response = await axios.post('http://localhost:8081/api/auth/login', {
          login: this.login,
          password: this.password
        });

        localStorage.setItem('user', JSON.stringify(response.data));
        this.$router.push('/profile');
      } catch (err) {
        console.error('Login error:', err);
        this.error = 'Неверный логин или пароль';
      } finally {
        this.loading = false;
      }
    }
  }
};
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
