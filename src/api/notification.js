import axios from './axios'

export const getNotificationCount = async (userId) => {
  return axios.get(`/notifications/count?userId=${userId}`)
}

export const getNotifications = async (userId) => {
  return axios.get(`/notifications?userId=${userId}`)
}

export const getUnreadNotifications = async (userId) => {
  return axios.get(`/notifications/unread?userId=${userId}`)
}

export const markAsRead = async (id) => {
  return axios.put(`/notifications/${id}/read`)
}

export const markAllAsRead = async (userId) => {
  return axios.put(`/notifications/read-all?userId=${userId}`)
}

export const deleteNotification = async (id) => {
  return axios.delete(`/notifications/${id}`)
}