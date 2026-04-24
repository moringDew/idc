import axios from './axios'

export const getKnowledge = async () => {
  return axios.get('/knowledge')
}

export const getKnowledgeById = async (id) => {
  return axios.get(`/knowledge/${id}`)
}

export const createKnowledge = async (knowledge) => {
  return axios.post('/knowledge', knowledge)
}

export const updateKnowledge = async (id, knowledge) => {
  return axios.put(`/knowledge/${id}`, knowledge)
}

export const deleteKnowledge = async (id) => {
  return axios.delete(`/knowledge/${id}`)
}