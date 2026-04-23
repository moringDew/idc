<template>
  <header class="top-nav">
    <div class="top-nav-left">
      <h3>{{ title }}</h3>
    </div>
    <div class="top-nav-right">
      <div class="notification">
        <i class="fas fa-bell"></i>
        <span v-if="notificationCount > 0" class="badge">{{ notificationCount }}</span>
      </div>
      <div class="user-info">
        <span>{{ username }}</span>
        <div class="user-avatar">{{ avatarText }}</div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  title: {
    type: String,
    default: ''
  },
  username: {
    type: String,
    default: '管理员'
  },
  notificationCount: {
    type: Number,
    default: 3
  }
})

const avatarText = computed(() => {
  return props.username.charAt(0)
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

@media (max-width: 768px) {
  .top-nav {
    padding: 12px 16px;
  }
}
</style>
