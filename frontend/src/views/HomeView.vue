<template>
  <div class="home">
    <h1>Отель Miracle</h1>
    <div class="search-box">
      <div class="date-inputs">
        <label>Заезд:</label>
        <input v-model="searchCheckIn" type="date" />
        <label>Выезд:</label>
        <input v-model="searchCheckOut" type="date" />
      </div>
      <button @click="search" :disabled="!searchCheckIn || !searchCheckOut">Найти</button>
      <button v-if="isFiltered" @click="showAll">Показать все</button>

      <router-link to="/register" class="btn btn-outline">
        Зарегистрироваться
      </router-link>
      <router-link to="/profile" class="btn btn-outline">
        Профиль
      </router-link>
    </div>

    <div v-if="loading" class="loading">Загрузка...</div>

    <div v-else class="rooms-grid">
      <div v-for="room in displayedRooms" :key="room.id" class="room-card">
        <img v-if="room.imageUrl" :src="room.imageUrl" :alt="`Номер ${room.roomNumber}`" />
        <h3>Номер {{ room.roomNumber }}</h3>
        <span class="status" :class="room.status.toLowerCase()">{{ room.status }}</span>
        <button
          @click="$router.push(`/room/${room.id}`)"
          class="details-btn"
        >
          Подробнее →
        </button>
      </div>
      <div v-if="displayedRooms.length === 0 && isFiltered" class="empty">
        Нет свободных номеров на выбранные даты.
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { getAllRooms, getAvailableRooms } from '@/services/roomService';

const allRooms = ref([]);
const filteredRooms = ref([]);
const searchCheckIn = ref('');
const searchCheckOut = ref('');
const loading = ref(false);
const isFiltered = ref(false);

const displayedRooms = computed(() =>
  isFiltered.value ? filteredRooms.value : allRooms.value
);

const loadAll = async () => {
  const res = await getAllRooms();
  allRooms.value = res.data;
};

const search = async () => {
  if (!searchCheckIn.value || !searchCheckOut.value) return;
  loading.value = true;
  try {
    const res = await getAvailableRooms(searchCheckIn.value, searchCheckOut.value);
    filteredRooms.value = res.data;
    isFiltered.value = true;
  } finally {
    loading.value = false;
  }
};

const showAll = () => {
  isFiltered.value = false;
  searchCheckIn.value = '';
  searchCheckOut.value = '';
};

loadAll();
</script>

<style scoped>
.home {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}
.search-box {
  display: flex;
  gap: 15px;
  margin-bottom: 25px;
  align-items: end;
}
.date-inputs {
  display: flex;
  gap: 10px;
  align-items: center;
}
.date-inputs label {
  font-weight: bold;
}
button {
  padding: 8px 16px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
button:disabled {
  background: #ccc;
}
.rooms-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 25px;
}
.room-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  padding: 16px;
  transition: transform 0.2s;
}
.room-card:hover {
  transform: translateY(-4px);
}
.room-card img {
  width: 100%;
  height: 160px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 12px;
}
.status {
  display: inline-block;
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: bold;
  margin-bottom: 10px;
}
.status.available {
  background: #d4edda;
  color: #155724;
}
.status.occupied {
  background: #f8d7da;
  color: #721c24;
}
.empty {
  grid-column: 1 / -1;
  text-align: center;
  color: #666;
  font-style: italic;
}

.details-btn {
  background: #007bff;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}
.details-btn:hover {
  background: #0056b3;
  transform: translateX(2px);
}
</style>
