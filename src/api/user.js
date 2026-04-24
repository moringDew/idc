import axios from './axios'

export const getUsers = async () => {
  return axios.get('/users')
}

export const getUserById = async (id) => {
  return axios.get(`/users/${id}`)
}

export const createUser = async (user) => {
  return axios.post('/users', user)
}

export const updateUser = async (id, user) => {
  return axios.put(`/users/${id}`, user)
}

export const deleteUser = async (id) => {
  return axios.delete(`/users/${id}`)
}

export const toggleUserStatus = async (id) => {
  return axios.put(`/users/${id}/status`)
}