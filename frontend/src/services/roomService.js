import { hotelApi } from './api';

export const getAllRooms = () => {
  return hotelApi.get('/rooms/all');
};

export const getAvailableRooms = (checkIn, checkOut) => {
  return hotelApi.get('/rooms', { params: { checkIn, checkOut } });
};

export const getRoomById = (id) => {
  return hotelApi.get(`/rooms/${id}`);
};

export const getRoomAvailability = (id) => {
  return hotelApi.get(`/rooms/${id}/availability`);
};

export const getBookingsByRoom = (roomId) => {
  return hotelApi.get(`/bookings/room/${roomId}`);
};



export const createBooking = (userId, roomId, checkIn, checkOut) => {
  return hotelApi.post('/bookings', {
    userId,
    roomId,
    checkIn,
    checkOut,
  });
};
