import axios from './axios'

export const getTasks = async () => {
  return axios.get('/monitor')
}

export const getTaskById = async (id) => {
  return axios.get(`/monitor/${id}`)
}

export const createTask = async (task) => {
  return axios.post('/monitor', task)
}

export const updateTask = async (id, task) => {
  return axios.put(`/monitor/${id}`, task)
}

export const deleteTask = async (id) => {
  return axios.delete(`/monitor/${id}`)
}