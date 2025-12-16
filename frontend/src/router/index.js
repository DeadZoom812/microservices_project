import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import RoomView from '../views/RoomView.vue';
import RegisterView from '../views/RegisterView.vue';
import LoginView  from '../views/LoginView.vue';
import ProfileView from '../views/ProfileView.vue';

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/home',
    name: 'Home',
    component: HomeView
  },
  {
    path: '/room/:id',
    name: 'Room',
    component: RoomView,
    beforeEnter: (to, from, next) => {
      const user = localStorage.getItem('user')
      if (user) {
        next()
      } else {
        next('/login')
      }
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginView
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterView
  },
  {
    path: '/profile',
    name: 'Profile',
    component: ProfileView,
    beforeEnter: (to, from, next) => {
      const user = localStorage.getItem('user')
      if (user) {
        next()
      } else {
        next('/login')
      }
    }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
