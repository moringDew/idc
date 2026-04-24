import axios from './axios'

export const getApprovals = async () => {
  return axios.get('/approvals')
}

export const getApprovalById = async (id) => {
  return axios.get(`/approvals/${id}`)
}

export const createApproval = async (approval) => {
  return axios.post('/approvals', approval)
}

export const approve = async (id) => {
  return axios.put(`/approvals/${id}/approve`)
}

export const reject = async (id) => {
  return axios.put(`/approvals/${id}/reject`)
}