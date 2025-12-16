<template>
  <div class="room-detail max-w-4xl mx-auto p-6 bg-white rounded-xl shadow-lg">
    <h1 class="text-3xl font-bold text-gray-800 mb-4">Номер {{ room.roomNumber }}</h1>

    <div class="room-image-wrapper">
      <img
        v-if="room.imageUrl"
        :src="room.imageUrl"
        alt="Номер"
        class="room-image"
      />
      <div v-else class="room-placeholder">Нет изображения</div>
    </div>

    <div class="p-4 mb-6 bg-green-50 border-l-4 border-green-500 text-green-700 rounded-lg">
      ✅ Просмотрите доступные даты в календаре ниже.
    </div>

    <div class="mb-6">
      <label class="block text-lg font-medium text-gray-700 mb-3">
        Выберите период бронирования:
      </label>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div class="date-input-container">
          <label class="block mb-2">Заезд:</label>
          <input
            type="date"
            v-model="checkIn"
            :min="today"
            :class="{ 'bg-gray-200': isDateDisabled(checkIn) }"
            @change="validateDates"
          />
        </div>
        <div class="date-input-container">
          <label class="block mb-2">Выезд:</label>
          <input
            type="date"
            v-model="checkOut"
            :min="checkIn || today"
            :class="{ 'bg-gray-200': isDateDisabled(checkOut) }"
            @change="validateDates"
          />
        </div>
      </div>

      <div class="mt-6">
        <p class="text-sm font-medium text-gray-700 mb-2 flex items-center gap-2">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" class="text-gray-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
          </svg>
          Занятые даты:
        </p>

        <div
          v-if="disabledDates.length > 0"
          class="flex flex-wrap gap-2 p-3 bg-gray-50 rounded-lg border border-gray-200"
        >
          <span
            v-for="date in disabledDates"
            :key="date"
            class="px-2.5 py-1.5 bg-white text-gray-800 rounded text-xs font-medium flex items-center gap-1.5 shadow-sm border border-gray-200"
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" class="text-red-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
            {{ formatDate(date) }}
          </span>
        </div>

        <div v-else class="flex items-center gap-2 p-3 bg-green-50 text-green-700 rounded-lg border border-green-200">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          <span class="text-sm font-medium">Нет бронирований — все даты свободны!</span>
        </div>
      </div>
    </div>

    <button
      @click="bookRoom"
      :disabled="!canBook"
      class="auth-btn manage-btn w-full"
    >
      Забронировать
    </button>

    <div v-if="error" class="mt-4 p-3 bg-red-100 text-red-700 rounded">
      {{ error }}
    </div>
    <div v-if="success" class="mt-4 p-3 bg-green-100 text-green-700 rounded">
      ✅ Бронирование успешно создано!
    </div>

    <router-link to="/" class="auth-btn back-btn mt-6">
      ← Назад к списку номеров
    </router-link>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { getRoomById, getBookingsByRoom, createBooking } from '@/services/roomService';

const route = useRoute();
const roomId = parseInt(route.params.id);

const room = ref({});
const bookings = ref([]);
const checkIn = ref('');
const checkOut = ref('');
const error = ref('');
const success = ref(false);

const today = new Date().toISOString().split('T')[0];
const disabledDates = ref([]);

const loadRoomData = async () => {
  try {
    room.value = (await getRoomById(roomId)).data;
    const res = await getBookingsByRoom(roomId);
    bookings.value = res.data;

    const dates = new Set();
    bookings.value.forEach(b => {
      const start = new Date(b.checkIn);
      const end = new Date(b.checkOut);
      let current = new Date(start);
      while (current <= end) {
        dates.add(current.toISOString().split('T')[0]);
        current.setDate(current.getDate() + 1);
      }
    });
    disabledDates.value = Array.from(dates).sort();
  } catch (err) {
    error.value = '❌ Ошибка загрузки данных';
  }
};

const isDateDisabled = (date) => {
  if (!date) return false;
  return disabledDates.value.includes(date);
};

const validateDates = () => {
  if (isDateDisabled(checkIn.value) || isDateDisabled(checkOut.value)) {
    error.value = 'Выбраны занятые даты';
  } else {
    error.value = '';
  }
};

const canBook = computed(() => {
  return (
    checkIn.value &&
    checkOut.value &&
    checkOut.value > checkIn.value &&
    !isDateDisabled(checkIn.value) &&
    !isDateDisabled(checkOut.value)
  );
});

const bookRoom = async () => {
  if (!canBook.value) return;

  try {
    await createBooking(1, roomId, checkIn.value, checkOut.value);
    success.value = true;
    error.value = '';

    setTimeout(() => {
      checkIn.value = '';
      checkOut.value = '';
      success.value = false;
      loadRoomData(); // обновить занятые даты
    }, 1000);
  } catch (err) {
    error.value = '❌ ' + (err.response?.data || 'Не удалось забронировать');
  }
};

const formatDate = (isoDate) => {
  if (!isoDate) return '';
  return isoDate.split('-').slice(1).reverse().join('.');
};

onMounted(() => {
  loadRoomData();
});
</script>

<style scoped>
.auth-btn {
  padding: 10px 20px;
  border: 2px solid #007bff;
  border-radius: 6px;
  color: #007bff;
  text-decoration: none;
  font-weight: bold;
  transition: all 0.2s;
  background: white;
  display: inline-block;
  text-align: center;
  width: 100%;
  box-sizing: border-box;
}

.auth-btn:hover {
  background: #007bff;
  color: white;
}

.manage-btn {
  background: #007bff;
  color: white;
  border-color: #007bff;
}

.back-btn {
  border-color: #6c757d;
  color: #6c757d;
  width: auto !important;
  margin-left: auto;
  margin-right: 0;
}

.back-btn:hover {
  background: #6c757d;
  color: white;
}

/* Остальные стили без изменений */
.room-image-wrapper {
  width: 700px;
  height: 320px;
  border-radius: 12px;
  overflow: hidden;
  background: #f8f9fa;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 24px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.room-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.room-placeholder {
  color: #999;
  font-style: italic;
}
.date-input-container {
  width: 200px;
}

input[type="date"] {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 16px;
}
</style>
