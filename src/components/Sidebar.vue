<template>
  <aside class="sidebar">
    <div class="sidebar-header">
      <h2>协同管理平台</h2>
    </div>
    <nav class="sidebar-nav">
      <li v-for="item in menuItems" :key="item.name">
        <router-link
          :to="item.path"
          :class="{ active: currentPath === item.path }"
        >
          <i :class="item.icon"></i>
          <span>{{ item.label }}</span>
        </router-link>
      </li>
    </nav>
  </aside>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const currentPath = computed(() => route.path)

const menuItems = [
  { name: 'Dashboard', path: '/dashboard', label: '工作台', icon: 'fas fa-home' },
  { name: 'Document', path: '/document', label: '公文管理', icon: 'fas fa-file-alt' },
  { name: 'Approval', path: '/approval', label: '流程审批', icon: 'fas fa-file-check' },
  { name: 'Knowledge', path: '/knowledge', label: '知识文档', icon: 'fas fa-book' },
  { name: 'Meeting', path: '/meeting', label: '会议管理', icon: 'fas fa-calendar' },
  { name: 'Monitor', path: '/monitor', label: '督办监控', icon: 'fas fa-chart-bar' },
  { name: 'Settings', path: '/settings', label: '系统设置', icon: 'fas fa-cog' }
]
</script>

<style scoped>
.sidebar {
  width: 250px;
  background: linear-gradient(180deg, #1e3c72 0%, #2a5298 100%);
  color: white;
  padding: 20px;
  flex-shrink: 0;
  min-height: 100vh;
}

.sidebar-header {
  padding: 20px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  margin-bottom: 20px;
}

.sidebar-header h2 {
  font-size: 18px;
  font-weight: 600;
}

.sidebar-nav {
  list-style: none;
}

.sidebar-nav li {
  margin-bottom: 8px;
}

.sidebar-nav a {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.sidebar-nav a:hover {
  background: rgba(255, 255, 255, 0.1);
}

.sidebar-nav a.active {
  background: rgba(255, 255, 255, 0.2);
  color: white;
}

.sidebar-nav i {
  margin-right: 10px;
  font-size: 16px;
}

@media (max-width: 768px) {
  .sidebar {
    width: 60px;
    padding: 10px;
  }
  .sidebar-header h2 {
    display: none;
  }
  .sidebar-nav a span {
    display: none;
  }
  .sidebar-nav a {
    justify-content: center;
  }
}
</style>
