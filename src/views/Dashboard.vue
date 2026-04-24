<template>
  <div class="layout-container">
    <Sidebar />
    <main class="main-content">
      <TopNav title="工作台" :username="currentUser.name" :userId="currentUser.id" />
      <div class="content-area">
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
            <div class="card-title">待处理公文</div>
            <div class="card-actions">
              <button class="btn btn-primary">查看全部</button>
            </div>
          </div>
          <table class="table">
            <thead>
              <tr>
                <th>标题</th>
                <th>类型</th>
                <th>来源</th>
                <th>时间</th>
                <th>状态</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="doc in documents" :key="doc.id">
                <td>{{ doc.title }}</td>
                <td>{{ doc.type }}</td>
                <td>{{ doc.source }}</td>
                <td>{{ doc.date }}</td>
                <td><span :class="['status', doc.status]">{{ getStatusText(doc.status) }}</span></td>
                <td>
                  <button 
                    class="btn" 
                    :class="doc.status === 'pending' ? 'btn-primary' : 'btn-secondary'"
                    style="padding: 4px 10px; font-size: 12px;"
                    @click="handleDocAction(doc)"
                  >
                    {{ doc.status === 'pending' ? '处理' : '查看' }}
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="card">
          <div class="card-header">
            <div class="card-title">待办事项</div>
          </div>
          <div class="todo-list">
            <div 
              class="todo-item" 
              v-for="item in todos" 
              :key="item.id"
              @click="toggleTodo(item)"
            >
              <input type="checkbox" v-model="item.completed" />
              <div class="todo-content">
                <div class="todo-title" :class="{ completed: item.completed }">{{ item.title }}</div>
                <div class="todo-time">{{ item.time }}</div>
              </div>
              <span :class="['status', item.status]">{{ getStatusText(item.status) }}</span>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Sidebar from '@/components/Sidebar.vue'
import TopNav from '@/components/TopNav.vue'
import { getDocuments, getApprovals, getTasks } from '@/api'

const currentUser = ref({ id: '1', name: '管理员' })

const stats = ref([
  { value: '0', label: '待处理公文', icon: 'fas fa-file-text', color: 'blue' },
  { value: '0', label: '已完成审批', icon: 'fas fa-check-circle', color: 'green' },
  { value: '0', label: '待办事项', icon: 'fas fa-clock', color: 'orange' },
  { value: '0', label: '超时预警', icon: 'fas fa-exclamation-triangle', color: 'red' }
])

const documents = ref([])
const todos = ref([
  { id: 1, title: '审批后勤处采购申请', time: '截止时间: 2024-01-16', status: 'pending', completed: false },
  { id: 2, title: '参加党委扩大会议', time: '时间: 2024-01-15 14:30', status: 'processing', completed: false },
  { id: 3, title: '审阅年度工作报告', time: '截止时间: 2024-01-18', status: 'pending', completed: false }
])

const getStatusText = (status) => {
  const statusMap = {
    pending: '待审批',
    processing: '处理中',
    approved: '已通过',
    rejected: '已驳回'
  }
  return statusMap[status] || status
}

const toggleTodo = (item) => {
  item.completed = !item.completed
}

const handleDocAction = (doc) => {
  console.log('Action on:', doc.title)
}

const loadCurrentUser = () => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    const user = JSON.parse(userStr)
    currentUser.value = { id: user.id.toString(), name: user.name }
  }
}

const loadData = async () => {
  loadCurrentUser()
  try {
    const docResponse = await getDocuments({ status: 'pending' })
    if (docResponse.code === 200) {
      documents.value = docResponse.data.slice(0, 4)
      stats.value[0].value = docResponse.data.length.toString()
    }
    
    const approvalResponse = await getApprovals({ status: 'approved' })
    if (approvalResponse.code === 200) {
      stats.value[1].value = approvalResponse.data.length.toString()
    }
    
    const taskResponse = await getTasks({ status: 'pending' })
    if (taskResponse.code === 200) {
      stats.value[2].value = taskResponse.data.length.toString()
    }
  } catch (error) {
    console.error('Failed to load data:', error)
  }
}

onMounted(loadData)
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
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
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

.stat-icon.blue {
  background: rgba(30, 60, 114, 0.1);
  color: #1e3c72;
}

.stat-icon.green {
  background: rgba(40, 167, 69, 0.1);
  color: #28a745;
}

.stat-icon.orange {
  background: rgba(255, 152, 0, 0.1);
  color: #ff9800;
}

.stat-icon.red {
  background: rgba(220, 53, 69, 0.1);
  color: #dc3545;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #666;
  margin-top: 4px;
}

.card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  padding: 20px;
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.card-actions {
  display: flex;
  gap: 10px;
}

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

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(30, 60, 114, 0.3);
}

.btn-secondary {
  background: #f5f7fa;
  color: #666;
}

.btn-secondary:hover {
  background: #eee;
}

.table {
  width: 100%;
  border-collapse: collapse;
}

.table th,
.table td {
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

.table tr:hover {
  background: #fafbfc;
}

.status {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.status.pending {
  background: #fff3cd;
  color: #856404;
}

.status.approved {
  background: #d4edda;
  color: #155724;
}

.status.rejected {
  background: #f8d7da;
  color: #721c24;
}

.status.processing {
  background: #cce5ff;
  color: #004085;
}

.todo-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.todo-item {
  display: flex;
  align-items: center;
  padding: 12px;
  background: #f9fafb;
  border-radius: 8px;
  cursor: pointer;
}

.todo-item input {
  margin-right: 12px;
}

.todo-content {
  flex: 1;
}

.todo-title {
  font-weight: 500;
}

.todo-title.completed {
  text-decoration: line-through;
  color: #999;
}

.todo-time {
  font-size: 12px;
  color: #666;
  margin-top: 2px;
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
}
</style>
