import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import RoomView from '../views/RoomView.vue';
import RegisterView from '../views/RegisterView.vue';
import LoginView  from '@/views/LoginView.vue';
import ProfileView from '@/views/ProfileView.vue';

const routes = [
  { path: '/', component: HomeView },
  { path: '/room/:id', component: RoomView, props: true },
  { path: '/register', component: RegisterView },
  { path: '/login', name: 'login', component: LoginView },
  { path: '/profile', name: 'profile', component: ProfileView }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
