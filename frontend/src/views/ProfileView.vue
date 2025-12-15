<template>
  <div class="profile-container" v-if="user">
    <h2>Мой профиль</h2>
    <p><strong>Логин:</strong> {{ user.login }}</p>
    <p><strong>Имя:</strong> {{ user.firstName }}</p>
    <p><strong>Фамилия:</strong> {{ user.lastName }}</p>
    <p><strong>Отчество:</strong> {{ user.middleName || '—' }}</p>
    <button @click="logout" class="btn">Выйти</button>
  </div>
  <div v-else>
    <p>Вы не авторизованы. <router-link to="/login">Войти</router-link></p>
  </div>
</template>

<script>
export default {
  name: 'ProfileView',
  data() {
    return {
      user: null
    };
  },
  mounted() {
    this.user = JSON.parse(localStorage.getItem('user'));
    if (!this.user) {
      this.$router.push('/login');
    }
  },
  methods: {
    logout() {
      localStorage.removeItem('user');
      this.user = null;
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
.profile-container {
  max-width: 500px;
  margin: 50px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: #f9f9f9;
}
.btn {
  margin-top: 20px;
  padding: 8px 16px;
  background: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>
