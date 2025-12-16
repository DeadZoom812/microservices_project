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

    <div
      v-if="isOccupied"
      class="p-4 mb-6 bg-red-50 border-l-4 border-red-500 text-red-700 rounded-lg"
    >
      ❗ Номер забронирован. Посмотрите в календаре свободные даты.
    </div>
    <div
      v-else
      class="p-4 mb-6 bg-green-50 border-l-4 border-green-500 text-green-700 rounded-lg"
    >
      ✅ Номер свободен.
    </div>

    <div
      v-if="!isOccupied && availability.nextBookingStart"
      class="p-4 mb-6 bg-blue-50 border-l-4 border-blue-500 text-blue-700 rounded-lg"
    >
      📅 Следующая бронь: с {{ formatDate(availability.nextBookingStart) }}
    </div>

    <div class="mb-6">
      <label class="block text-lg font-medium text-gray-700 mb-3">
        Выберите период бронирования:
      </label>
      <flat-pickr
        v-model="selectedRange"
        :config="flatpickrConfig"
        @on-change="onDateChange"
        class="w-full p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none"
        placeholder="Выберите даты"
      />
    </div>

    <!-- Кнопка -->
    <button
      @click="bookRoom"
      :disabled="!canBook"
      class="w-full py-3 px-6 bg-blue-600 hover:bg-blue-700 disabled:bg-gray-400 text-white font-semibold rounded-lg shadow transition"
    >
      Забронировать
    </button>

    <!-- Сообщения -->
    <div v-if="error" class="mt-4 p-3 bg-red-100 text-red-700 rounded">
      {{ error }}
    </div>
    <div v-if="success" class="mt-4 p-3 bg-green-100 text-green-700 rounded">
      ✅ Бронирование успешно создано!
    </div>

    <router-link to="/" class="mt-6 inline-block text-blue-600 hover:text-blue-800 font-medium">
      ← Назад к списку номеров
    </router-link>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import flatPickr from 'vue-flatpickr-component';
import 'flatpickr/dist/flatpickr.css';
import 'flatpickr/dist/themes/material_blue.css';
import { getRoomById, getBookingsByRoom, getRoomAvailability, createBooking } from '@/services/roomService';

const route = useRoute();
const roomId = parseInt(route.params.id);

const room = ref({});
const selectedRange = ref([]);
const error = ref('');
const success = ref(false);
const availability = ref({
  status: 'LOADING',
  nextAvailableDate: null,
  nextBookingStart: null
});

const disabledDates = ref([]);

const flatpickrConfig = ref({
  mode: 'range',
  dateFormat: 'Y-m-d',
  minDate: 'today',
  disable: [],
  onDayCreate: (dObj, dStr, fp, dayElem) => {
    if (disabledDates.value.includes(dStr)) {
      dayElem.classList.add('flatpickr-disabled-visual');
    }
  }
});

// watch(disabledDates, (newVal) => {
//   flatpickrConfig.value.disable = newVal;
// });

const isOccupied = computed(() => {
  return availability.value.status === 'OCCUPIED';
});

const canBook = computed(() => {
  return selectedRange.value && selectedRange.value.length === 2;
});

const loadRoomData = async () => {
  try {
    error.value = '';
    room.value = (await getRoomById(roomId)).data;
    const bookingsRes = await getBookingsByRoom(roomId);
    const bookings = bookingsRes.data;

    const availRes = await getRoomAvailability(roomId);
    availability.value = availRes.data;

    const dates = new Set();
    bookings.forEach(b => {

      const start = b.checkIn.split('-').map(Number);
      const end = b.checkOut.split('-').map(Number);
      let current = new Date(start[0], start[1] - 1, start[2]);
      const endDate = new Date(end[0], end[1] - 1, end[2]);
      while (current <= endDate) {

        const y = current.getFullYear();
        const m = String(current.getMonth() + 1).padStart(2, '0');
        const d = String(current.getDate()).padStart(2, '0');
        dates.add(`${y}-${m}-${d}`);
        current.setDate(current.getDate() + 1);
      }
    });
    disabledDates.value = Array.from(dates);
    flatpickrConfig.value.disable = Array.from(dates);
  } catch (err) {
    error.value = '❌ Ошибка загрузки данных';
  }
};

const onDateChange = (selectedDates) => {
  if (selectedDates && selectedDates.length === 2) {
    selectedRange.value = selectedDates;
  }
};

const bookRoom = async () => {
  if (!canBook.value) return;

  const [checkIn, checkOut] = selectedRange.value;
  const userId = 1;

  try {
    await createBooking(userId, roomId, checkIn, checkOut);
    success.value = true;
    error.value = '';

    setTimeout(() => {
      selectedRange.value = [];
      success.value = false;
      loadRoomData();
    }, 600);
  } catch (err) {
    error.value = '❌ ' + (err.response?.data || 'Не удалось забронировать');
    success.value = false;
  }
};

const formatDate = (isoDate) => {
  if (!isoDate) return '';
  return isoDate.split('-').reverse().join('.');
};

onMounted(() => {
  loadRoomData();
});
</script>

<style scoped>
.flatpickr-disabled-visual {
  background-color: #fee2e2 !important;
  color: #dc2626 !important;
  pointer-events: none !important;
  position: relative;
}
.flatpickr-disabled-visual::after {
  content: "✘";
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 10px;
  color: #b91c1c;
}
.room-image-wrapper {
  width: 700px;
  height: 360px;
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
</style>
