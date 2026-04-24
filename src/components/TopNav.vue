<template>
  <header class="top-nav">
    <div class="top-nav-left">
      <h3>{{ title }}</h3>
    </div>
    <div class="top-nav-right">
      <div class="notification" @click="handleNotificationClickDropdown">
        <i class="fas fa-bell"></i>
        <span v-if="notificationCount > 0" class="badge">{{ notificationCount }}</span>
        <span v-if="isLoading" class="loading-dot"></span>
        
        <div v-if="showNotifications" class="notification-dropdown">
          <div class="dropdown-header">
            <span>通知消息</span>
            <button class="mark-all-btn" @click.stop="handleMarkAllAsRead">全部标为已读</button>
          </div>
          <div class="notification-list">
            <div v-if="notifications.length === 0" class="empty-state">
              <i class="fas fa-bell"></i>
              <p>暂无通知</p>
            </div>
            <div 
              v-for="notification in notifications" 
              :key="notification.id" 
              class="notification-item"
              :class="{ unread: !notification.read }"
            >
              <div class="notification-icon" :class="notification.type">
                <i class="fas fa-info-circle"></i>
              </div>
              <div class="notification-content">
                <h4>{{ notification.title }}</h4>
                <p>{{ notification.content }}</p>
                <span class="notification-time">{{ formatTime(notification.createdAt) }}</span>
              </div>
              <div class="notification-actions">
                <button 
                  v-if="!notification.read" 
                  class="mark-read-btn"
                  @click.stop="handleMarkAsRead(notification)"
                >
                  <i class="fas fa-check"></i>
                  <span>标为已读</span>
                </button>
                <span v-else class="read-status">
                  <i class="fas fa-check-circle"></i>
                  <span>已读</span>
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="user-info">
        <span>{{ username }}</span>
        <div class="user-avatar">{{ avatarText }}</div>
        <button class="logout-btn" @click="handleLogout">
          <i class="fas fa-sign-out-alt"></i>
          <span>退出</span>
        </button>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { getNotificationCount, getUnreadNotifications, markAsRead, markAllAsRead } from '@/api'

const props = defineProps({
  title: {
    type: String,
    default: ''
  },
  username: {
    type: String,
    default: '管理员'
  },
  userId: {
    type: String,
    default: '1'
  }
})

const router = useRouter()

const notificationCount = ref(0)
const notifications = ref([])
const showNotifications = ref(false)
const isLoading = ref(false)

const handleLogout = () => {
  localStorage.removeItem('user')
  router.push('/')
}

const avatarText = computed(() => {
  return props.username.charAt(0)
})

const loadNotificationCount = async () => {
  isLoading.value = true
  try {
    const response = await getNotificationCount(props.userId)
    if (response.code === 200) {
      notificationCount.value = response.data.count || 0
    }
  } catch (error) {
    console.error('Failed to load notification count:', error)
  } finally {
    isLoading.value = false
  }
}

const loadNotifications = async () => {
  try {
    const response = await getUnreadNotifications(props.userId)
    if (response.code === 200) {
      notifications.value = response.data
    }
  } catch (error) {
    console.error('Failed to load notifications:', error)
  }
}

const handleNotificationClickDropdown = () => {
  showNotifications.value = !showNotifications.value
  if (showNotifications.value) {
    loadNotifications()
  }
}

const handleNotificationItemClick = (notification) => {
  console.log('Notification clicked:', notification.title)
}

const handleMarkAsRead = async (notification) => {
  try {
    await markAsRead(notification.id)
    notification.read = true
    notificationCount.value = Math.max(0, notificationCount.value - 1)
    loadNotifications()
  } catch (error) {
    console.error('Failed to mark as read:', error)
  }
}

const handleMarkAllAsRead = async () => {
  await markAllAsRead(props.userId)
  notifications.value.forEach(n => n.read = true)
  notificationCount.value = 0
}

const formatTime = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  const now = new Date()
  const diff = now - date
  
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)
  
  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 7) return `${days}天前`
  
  return date.toLocaleDateString('zh-CN')
}

onMounted(() => {
  loadNotificationCount()
})

watch(() => props.userId, () => {
  loadNotificationCount()
})
</script>

<style scoped>
.top-nav {
  background: white;
  padding: 16px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.top-nav-left h3 {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.top-nav-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.notification {
  position: relative;
  cursor: pointer;
}

.notification i {
  font-size: 20px;
  color: #666;
}

.notification .badge {
  position: absolute;
  top: -4px;
  right: -6px;
  background: red;
  color: white;
  font-size: 10px;
  padding: 2px 5px;
  border-radius: 10px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logout-btn {
  background: #f5f5f5;
  border: 1px solid #ddd;
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 13px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
  color: #666;
  transition: all 0.2s;
}

.logout-btn:hover {
  background: #fff3e0;
  border-color: #ff9800;
  color: #ff9800;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  font-weight: 600;
}

.notification .loading-dot {
  position: absolute;
  top: -2px;
  right: -8px;
  width: 8px;
  height: 8px;
  background: #1e3c72;
  border-radius: 50%;
  animation: pulse 1s infinite;
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

.notification-dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: 8px;
  width: 360px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  overflow: hidden;
}

.dropdown-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #eee;
}

.dropdown-header span {
  font-weight: 600;
  color: #333;
}

.mark-all-btn {
  background: #1e3c72;
  color: white;
  border: none;
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
}

.mark-all-btn:hover {
  background: #2a5298;
}

.notification-list {
  max-height: 400px;
  overflow-y: auto;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  color: #999;
}

.empty-state i {
  font-size: 48px;
  margin-bottom: 12px;
}

.empty-state p {
  margin: 0;
}

.notification-item {
  display: flex;
  padding: 12px 16px;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
  transition: background 0.2s;
}

.notification-item:hover {
  background: #f9f9f9;
}

.notification-item.unread {
  background: #f5f8ff;
}

.notification-icon {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  flex-shrink: 0;
}

.notification-icon.info {
  background: #e3f2fd;
  color: #1976d2;
}

.notification-icon.warning {
  background: #fff3e0;
  color: #ff9800;
}

.notification-icon.error {
  background: #ffebee;
  color: #f44336;
}

.notification-icon.success {
  background: #e8f5e9;
  color: #4caf50;
}

.notification-content {
  flex: 1;
  min-width: 0;
}

.notification-content h4 {
  margin: 0 0 4px 0;
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.notification-content p {
  margin: 0 0 6px 0;
  font-size: 13px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.notification-time {
  font-size: 12px;
  color: #999;
}

.notification-actions {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: center;
  margin-left: 12px;
  gap: 4px;
}

.mark-read-btn {
  background: #1e3c72;
  color: white;
  border: none;
  padding: 6px 10px;
  border-radius: 4px;
  font-size: 11px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
  transition: background 0.2s;
}

.mark-read-btn:hover {
  background: #2a5298;
}

.mark-read-btn i {
  font-size: 10px;
}

.read-status {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 11px;
  color: #4caf50;
}

.read-status i {
  font-size: 12px;
}

@media (max-width: 768px) {
  .top-nav {
    padding: 12px 16px;
  }
  
  .notification-dropdown {
    right: 0;
    width: calc(100vw - 32px);
  }
}
</style>