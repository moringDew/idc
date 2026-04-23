<template>
  <div class="layout-container">
    <Sidebar />
    <main class="main-content">
      <TopNav title="督办监控" />
      <div class="content-area">
        <div class="breadcrumb">
          <router-link to="/dashboard">首页</router-link>
          <span><i class="fas fa-angle-right"></i></span>
          <span>督办监控</span>
        </div>

        <div class="stats-grid">
          <div class="stat-card" v-for="stat in stats" :key="stat.label">
            <div :class="['stat-icon', stat.color]">
              <i :class="stat.icon"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stat.value }}</div>
              <div class="stat-label">{{ stat.label }}</div>
            </div>
          </div>
        </div>

        <div class="card">
          <div class="card-header">
            <div class="card-title">督办任务列表</div>
            <div class="card-actions">
              <button class="btn btn-primary">
                <i class="fas fa-plus"></i> 新增督办
              </button>
            </div>
          </div>

          <div class="filter-bar">
            <select v-model="filterStatus">
              <option value="">全部状态</option>
              <option value="pending">未开始</option>
              <option value="processing">进行中</option>
              <option value="completed">已完成</option>
              <option value="warning">超时预警</option>
            </select>
            <select v-model="filterPriority">
              <option value="">全部优先级</option>
              <option value="urgent">紧急</option>
              <option value="important">重要</option>
              <option value="normal">一般</option>
            </select>
            <input type="date" v-model="startDate" placeholder="开始日期">
            <input type="date" v-model="endDate" placeholder="结束日期">
            <button class="btn btn-secondary" @click="handleFilter">筛选</button>
          </div>

          <table class="table">
            <thead>
              <tr>
                <th>任务标题</th>
                <th>督办单位</th>
                <th>责任人</th>
                <th>截止时间</th>
                <th>优先级</th>
                <th>进度</th>
                <th>状态</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="task in filteredTasks" :key="task.id">
                <td>{{ task.title }}</td>
                <td>{{ task.department }}</td>
                <td>{{ task.person }}</td>
                <td>{{ task.deadline }}</td>
                <td><span :class="['priority', task.priority]">{{ getPriorityText(task.priority) }}</span></td>
                <td>
                  <div class="progress-container">
                    <div class="progress-bar" :style="{ width: task.progress + '%' }"></div>
                  </div>
                  <span class="progress-text">{{ task.progress }}%</span>
                </td>
                <td><span :class="['status', task.status]">{{ getStatusText(task.status) }}</span></td>
                <td>
                  <button class="btn btn-secondary" style="padding: 4px 10px; font-size: 12px;" @click="viewDetail(task)">查看</button>
                  <button v-if="task.status !== 'completed'" class="btn btn-primary" style="padding: 4px 10px; font-size: 12px; margin-left: 5px;" @click="urgeTask(task)">催办</button>
                </td>
              </tr>
            </tbody>
          </table>

          <div class="pagination">
            <button @click="prevPage" :disabled="currentPage <= 1">上一页</button>
            <button v-for="page in totalPages" :key="page" :class="{ active: currentPage === page }" @click="currentPage = page">{{ page }}</button>
            <button @click="nextPage" :disabled="currentPage >= totalPages">下一页</button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import Sidebar from '@/components/Sidebar.vue'
import TopNav from '@/components/TopNav.vue'

const filterStatus = ref('')
const filterPriority = ref('')
const startDate = ref('')
const endDate = ref('')
const currentPage = ref(1)

const stats = ref([
  { value: '45', label: '督办任务总数', icon: 'fas fa-tasks', color: 'blue' },
  { value: '32', label: '已完成', icon: 'fas fa-check-circle', color: 'green' },
  { value: '10', label: '进行中', icon: 'fas fa-clock', color: 'orange' },
  { value: '3', label: '超时预警', icon: 'fas fa-exclamation-triangle', color: 'red' }
])

const tasks = ref([
  { id: 1, title: '完成年度训练计划编制', department: '司令部', person: '李XX', deadline: '2024-01-20', priority: 'urgent', progress: 70, status: 'processing' },
  { id: 2, title: '完善安全管理制度', department: '政治处', person: '王XX', deadline: '2024-01-18', priority: 'important', progress: 100, status: 'completed' },
  { id: 3, title: '采购办公设备', department: '后勤处', person: '张XX', deadline: '2024-01-16', priority: 'urgent', progress: 30, status: 'pending' },
  { id: 4, title: '组织年度考核', department: '司令部', person: '赵XX', deadline: '2024-01-14', priority: 'normal', progress: 100, status: 'completed' }
])

const filteredTasks = computed(() => {
  return tasks.value.filter(task => {
    const matchStatus = !filterStatus.value || task.status === filterStatus.value
    const matchPriority = !filterPriority.value || task.priority === filterPriority.value
    return matchStatus && matchPriority
  })
})

const totalPages = computed(() => Math.ceil(filteredTasks.value.length / 5))

const getPriorityText = (priority) => {
  const map = { urgent: '紧急', important: '重要', normal: '一般' }
  return map[priority] || priority
}

const getStatusText = (status) => {
  const map = { pending: '未开始', processing: '进行中', completed: '已完成', warning: '超时预警' }
  return map[status] || status
}

const prevPage = () => {
  if (currentPage.value > 1) currentPage.value--
}

const nextPage = () => {
  if (currentPage.value < totalPages.value) currentPage.value++
}

const handleFilter = () => {
  currentPage.value = 1
}

const viewDetail = (task) => {
  console.log('View task:', task.title)
}

const urgeTask = (task) => {
  console.log('Urge task:', task.title)
}
</script>

<style scoped>
.layout-container {
  display: flex;
  min-height: 100vh;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.content-area {
  padding: 24px;
  overflow-y: auto;
  background: #f5f7fa;
  flex: 1;
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 20px;
  font-size: 14px;
}

.breadcrumb a {
  color: #1e3c72;
  text-decoration: none;
}

.breadcrumb a:hover {
  text-decoration: underline;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 24px;
}

.stat-icon.blue { background: rgba(30, 60, 114, 0.1); color: #1e3c72; }
.stat-icon.green { background: rgba(40, 167, 69, 0.1); color: #28a745; }
.stat-icon.orange { background: rgba(255, 152, 0, 0.1); color: #ff9800; }
.stat-icon.red { background: rgba(220, 53, 69, 0.1); color: #dc3545; }

.stat-info { flex: 1; }
.stat-value { font-size: 28px; font-weight: 700; color: #333; }
.stat-label { font-size: 14px; color: #666; margin-top: 4px; }

.card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.card-title { font-size: 18px; font-weight: 600; color: #333; }

.card-actions { display: flex; gap: 10px; }

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-primary {
  background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
  color: white;
}

.btn-primary:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(30, 60, 114, 0.3); }

.btn-secondary { background: #f5f7fa; color: #666; }
.btn-secondary:hover { background: #eee; }

.filter-bar {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.filter-bar select, .filter-bar input {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

.table { width: 100%; border-collapse: collapse; }

.table th, .table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.table th {
  background: #f5f7fa;
  font-weight: 600;
  color: #666;
  font-size: 14px;
}

.table tr:hover { background: #fafbfc; }

.priority, .status {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.priority.urgent { background: #fff3cd; color: #856404; }
.priority.important { background: #cce5ff; color: #004085; }
.priority.normal { background: #d4edda; color: #155724; }

.status.pending { background: #fff3cd; color: #856404; }
.status.processing { background: #cce5ff; color: #004085; }
.status.completed { background: #d4edda; color: #155724; }
.status.warning { background: #f8d7da; color: #721c24; }

.progress-container {
  width: 100px;
  height: 8px;
  background: #eee;
  border-radius: 4px;
  overflow: hidden;
  display: inline-block;
  vertical-align: middle;
}

.progress-bar {
  height: 100%;
  background: linear-gradient(90deg, #28a745, #20c997);
  border-radius: 4px;
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 12px;
  color: #666;
  margin-left: 8px;
}

.pagination {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-top: 20px;
}

.pagination button {
  padding: 8px 16px;
  border: 1px solid #ddd;
  background: white;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
}

.pagination button:hover:not(:disabled) { background: #f5f7fa; }
.pagination button.active { background: #1e3c72; color: white; border-color: #1e3c72; }
.pagination button:disabled { opacity: 0.5; cursor: not-allowed; }
</style>
