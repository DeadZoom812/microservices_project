import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import RoomView from '../views/RoomView.vue';
import RegisterView from '../views/RegisterView.vue';

const routes = [
  { path: '/', component: HomeView },
  { path: '/room/:id', component: RoomView, props: true },
  { path: '/register', component: RegisterView },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
