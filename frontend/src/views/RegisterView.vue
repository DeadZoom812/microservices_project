<template>
  <div class="register">
    <h1>Регистрация</h1>

    <form @submit.prevent="register">
      <div>
        <label>Логин:</label>
        <input v-model="form.login" required />
      </div>
      <div>
        <label>Пароль:</label>
        <input v-model="form.password" type="password" required />
      </div>
      <div>
        <label>Имя:</label>
        <input v-model="form.firstName" required />
      </div>
      <div>
        <label>Фамилия:</label>
        <input v-model="form.lastName" required />
      </div>
      <div>
        <label>Отчество:</label>
        <input v-model="form.middleName" />
      </div>

      <button type="submit" :disabled="loading">Зарегистрироваться</button>
    </form>

    <div v-if="error" class="error">{{ error }}</div>
    <div v-if="success" class="success">Пользователь создан! ID: {{ successId }}</div>

    <router-link to="/">← Назад</router-link>
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
.register {
  max-width: 500px;
  margin: 0 auto;
  padding: 20px;
}
.register label {
  display: block;
  margin: 10px 0 5px;
}
.register input {
  width: 100%;
  padding: 8px;
  margin-bottom: 15px;
}
.error {
  color: red;
  margin-top: 10px;
}
.success {
  color: green;
  margin-top: 10px;
}
</style>
