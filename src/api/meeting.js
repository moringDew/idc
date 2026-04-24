import axios from './axios'

export const getMeetings = async () => {
  return axios.get('/meetings')
}

export const getMeetingById = async (id) => {
  return axios.get(`/meetings/${id}`)
}

export const createMeeting = async (meeting) => {
  return axios.post('/meetings', meeting)
}

export const updateMeeting = async (id, meeting) => {
  return axios.put(`/meetings/${id}`, meeting)
}

export const deleteMeeting = async (id) => {
  return axios.delete(`/meetings/${id}`)
}