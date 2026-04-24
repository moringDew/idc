import axios from './axios'

export const login = async (username, password) => {
  return axios.post('/auth/login', { username, password })
}