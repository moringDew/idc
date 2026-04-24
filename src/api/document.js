import axios from './axios'

export const getDocuments = async () => {
  return axios.get('/documents')
}

export const getDocumentById = async (id) => {
  return axios.get(`/documents/${id}`)
}

export const createDocument = async (document) => {
  return axios.post('/documents', document)
}

export const updateDocument = async (id, document) => {
  return axios.put(`/documents/${id}`, document)
}

export const deleteDocument = async (id) => {
  return axios.delete(`/documents/${id}`)
}