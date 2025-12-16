<template>
  <div class="register-container">
    <h2>Регистрация</h2>
    <form @submit.prevent="register" class="register-form">
      <div class="form-group">
        <label>Логин:</label>
        <input v-model="form.login" type="text" required />
      </div>
      <div class="form-group">
        <label>Пароль:</label>
        <input v-model="form.password" type="password" required />
      </div>
      <div class="form-group">
        <label>Имя:</label>
        <input v-model="form.firstName" type="text" required />
      </div>
      <div class="form-group">
        <label>Фамилия:</label>
        <input v-model="form.lastName" type="text" required />
      </div>
      <div class="form-group">
        <label>Отчество:</label>
        <input v-model="form.middleName" type="text" />
      </div>

      <div class="form-actions">
        <button type="submit" :disabled="loading" class="auth-btn login-btn">
          Зарегистрироваться
        </button>
        <router-link to="/" class="auth-btn back-btn">← Назад</router-link>
      </div>

      <div v-if="error" class="error">{{ error }}</div>
      <div v-if="success" class="success">Регистрация успешна!</div>

    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { registerUser } from '@/services/userService';

const form = ref({
  login: '',
  password: '',
  firstName: '',
  lastName: '',
  middleName: '',
});

const loading = ref(false);
const error = ref('');
const success = ref(false);
const successId = ref(null);

const register = async () => {
  loading.value = true;
  error.value = '';
  success.value = false;

  try {
    const res = await registerUser(
      form.value.login,
      form.value.password,
      form.value.firstName,
      form.value.lastName,
      form.value.middleName
    );
    success.value = true;

  } catch (err) {
    error.value = 'Ошибка: ' + (err.response?.data || err.message);
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.register-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: #f9f9f9;
}
.register-form h2 {
  text-align: center;
  margin-bottom: 20px;
}
.form-group {
  margin-bottom: 15px;
}
.form-actions {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  margin-top: 20px;
}

.auth-btn {
  padding: 10px 20px;
  border: 2px solid #007bff;
  border-radius: 6px;
  color: #007bff;
  text-decoration: none;
  font-weight: bold;
  background: white;
  transition: all 0.2s;
}

.auth-btn:hover {
  background: #007bff;
  color: white;
}

.login-btn {
  background: #007bff;
  color: white;
  border-color: #007bff;
}

.back-btn {
  border-color: #6c757d;
  color: #6c757d;
}

.back-btn:hover {
  background: #6c757d;
  color: white;
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
.error {
  color: red;
  margin-top: 10px;
  text-align: center;
}
.success {
  color: green;
  margin-top: 10px;
  text-align: center;
}
.back-btn {
  border-color: #6c757d;
  color: #6c757d;
}
.back-btn:hover {
  background: #6c757d;
  color: white;
}
</style>
