import { userApi } from './api';

export const registerUser = (login, password, firstName, lastName, middleName) => {
  return userApi.post('/users/register', {
    login,
    password,
    firstName,
    lastName,
    middleName,
  });
};
