<template>
  <div class="layout-container">
    <Sidebar />
    <main class="main-content">
      <TopNav title="会议管理" />
      <div class="content-area">
        <div class="breadcrumb">
          <router-link to="/dashboard">首页</router-link>
          <span><i class="fas fa-angle-right"></i></span>
          <span>会议管理</span>
        </div>

        <div class="card">
          <div class="card-header">
            <div class="card-title">会议列表</div>
            <div class="card-actions">
              <button class="btn btn-primary" @click="showCreateModal = true">
                <i class="fas fa-plus"></i> 新建会议
              </button>
            </div>
          </div>

          <div class="filter-bar">
            <select v-model="filterStatus">
              <option value="">全部状态</option>
              <option value="pending">待召开</option>
              <option value="processing">进行中</option>
              <option value="completed">已结束</option>
            </select>
            <select v-model="filterType">
              <option value="">全部类型</option>
              <option value="党委会议">党委会议</option>
              <option value="行政会议">行政会议</option>
              <option value="业务会议">业务会议</option>
              <option value="培训会议">培训会议</option>
            </select>
            <input type="date" v-model="filterDate" placeholder="会议日期">
            <button class="btn btn-secondary" @click="handleFilter">筛选</button>
          </div>

          <div class="meeting-grid">
            <div 
              class="meeting-card" 
              v-for="meeting in filteredMeetings" 
              :key="meeting.id"
            >
              <div class="meeting-header">
                <div>
                  <div class="meeting-title">{{ meeting.title }}</div>
                  <div class="meeting-type">会议类型: {{ meeting.type }}</div>
                </div>
                <span :class="['status', meeting.status]">{{ getStatusText(meeting.status) }}</span>
              </div>
              <div class="meeting-info">
                <span><i class="fas fa-calendar"></i> {{ meeting.date }}</span>
                <span><i class="fas fa-clock"></i> {{ meeting.time }}</span>
                <span><i class="fas fa-map-marker-alt"></i> {{ meeting.location }}</span>
              </div>
              <div class="meeting-content">
                <div class="content-label">会议主题:</div>
                <div class="content-text">{{ meeting.topic }}</div>
              </div>
              <div class="meeting-actions">
                <button class="btn btn-secondary" style="padding: 4px 10px; font-size: 12px; margin-right: 5px;">查看详情</button>
                <button 
                  class="btn" 
                  :class="getActionBtnClass(meeting.status)"
                  style="padding: 4px 10px; font-size: 12px;"
                  @click="handleMeetingAction(meeting)"
                >{{ getActionBtnText(meeting.status) }}</button>
              </div>
            </div>
          </div>

          <div class="pagination">
            <button @click="prevPage" :disabled="currentPage <= 1">上一页</button>
            <button 
              v-for="page in totalPages" 
              :key="page"
              :class="{ active: currentPage === page }"
              @click="currentPage = page"
            >{{ page }}</button>
            <button @click="nextPage" :disabled="currentPage >= totalPages">下一页</button>
          </div>
        </div>
      </div>
    </main>
  </div>

  <div v-if="showCreateModal" class="modal-overlay" @click.self="showCreateModal = false">
    <div class="modal">
      <div class="modal-header">
        <h3>新建会议</h3>
        <button class="modal-close" @click="showCreateModal = false">×</button>
      </div>
      <div class="modal-body">
        <div class="form-group">
          <label>会议主题</label>
          <input type="text" v-model="form.title" placeholder="请输入会议主题">
        </div>
        <div class="form-group">
          <label>会议类型</label>
          <select v-model="form.type">
            <option>党委会议</option>
            <option>行政会议</option>
            <option>业务会议</option>
            <option>培训会议</option>
          </select>
        </div>
        <div class="form-group">
          <label>会议时间</label>
          <div class="time-input">
            <input type="date" v-model="form.date">
            <input type="time" v-model="form.startTime">
            <span>至</span>
            <input type="time" v-model="form.endTime">
          </div>
        </div>
        <div class="form-group">
          <label>会议地点</label>
          <select v-model="form.location">
            <option>一号会议室</option>
            <option>二号会议室</option>
            <option>三号会议室</option>
            <option>培训室</option>
          </select>
        </div>
        <div class="form-group">
          <label>参会人员</label>
          <input type="text" v-model="form.participants" placeholder="请选择参会人员">
        </div>
        <div class="form-group">
          <label>会议议程</label>
          <textarea v-model="form.agenda" placeholder="请输入会议议程"></textarea>
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn btn-secondary" @click="showCreateModal = false">取消</button>
        <button class="btn btn-primary" @click="handleCreateMeeting">创建会议</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, reactive, onMounted } from 'vue'
import Sidebar from '@/components/Sidebar.vue'
import TopNav from '@/components/TopNav.vue'
import { getMeetings, createMeeting } from '@/api'

const filterStatus = ref('')
const filterType = ref('')
const filterDate = ref('')
const currentPage = ref(1)
const showCreateModal = ref(false)

const form = reactive({
  title: '',
  type: '行政会议',
  date: '',
  startTime: '',
  endTime: '',
  location: '一号会议室',
  participants: '',
  agenda: ''
})

const meetings = ref([])

const filteredMeetings = computed(() => {
  return meetings.value.filter(meeting => {
    const matchStatus = !filterStatus.value || meeting.status === filterStatus.value
    const matchType = !filterType.value || meeting.type === filterType.value
    const matchDate = !filterDate.value || meeting.date === filterDate.value
    return matchStatus && matchType && matchDate
  })
})

const totalPages = computed(() => Math.ceil(filteredMeetings.value.length / 4))

const getStatusText = (status) => {
  const statusMap = {
    pending: '待召开',
    processing: '进行中',
    completed: '已结束'
  }
  return statusMap[status] || status
}

const getActionBtnClass = (status) => {
  if (status === 'processing') return 'btn-primary'
  if (status === 'pending') return 'btn-primary'
  return 'btn-secondary'
}

const getActionBtnText = (status) => {
  if (status === 'processing') return '参会签到'
  if (status === 'pending') return '发送通知'
  return '查看纪要'
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

const handleMeetingAction = (meeting) => {
  console.log('Action:', meeting.title, getActionBtnText(meeting.status))
}

const handleCreateMeeting = async () => {
  try {
    const response = await createMeeting({
      title: form.title,
      type: form.type,
      date: form.date,
      time: `${form.startTime} - ${form.endTime}`,
      location: form.location,
      participants: form.participants,
      agenda: form.agenda,
      status: 'pending'
    })
    if (response.code === 200) {
      meetings.value.unshift(response.data)
    }
    showCreateModal.value = false
    Object.keys(form).forEach(key => form[key] = '')
    form.type = '行政会议'
    form.location = '一号会议室'
  } catch (error) {
    console.error('Failed to create meeting:', error)
  }
}

const loadMeetings = async () => {
  try {
    const response = await getMeetings()
    if (response.code === 200) {
      meetings.value = response.data
    }
  } catch (error) {
    console.error('Failed to load meetings:', error)
  }
}

onMounted(loadMeetings)
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

.filter-bar {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.filter-bar select,
.filter-bar input {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

.meeting-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.meeting-card {
  border: 1px solid #eee;
  border-radius: 12px;
  padding: 20px;
}

.meeting-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.meeting-title {
  font-weight: 600;
  font-size: 16px;
  margin-bottom: 4px;
}

.meeting-type {
  font-size: 13px;
  color: #666;
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

.status.processing {
  background: #cce5ff;
  color: #004085;
}

.status.completed {
  background: #d4edda;
  color: #155724;
}

.meeting-info {
  display: flex;
  gap: 20px;
  font-size: 13px;
  color: #666;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.meeting-info i {
  margin-right: 4px;
}

.meeting-content {
  padding: 12px;
  background: #f9fafb;
  border-radius: 8px;
  margin-bottom: 16px;
}

.content-label {
  font-size: 13px;
  color: #666;
  margin-bottom: 4px;
}

.content-text {
  font-size: 14px;
}

.meeting-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.pagination {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-top: 24px;
}

.pagination button {
  padding: 8px 16px;
  border: 1px solid #ddd;
  background: white;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
}

.pagination button:hover:not(:disabled) {
  background: #f5f7fa;
}

.pagination button.active {
  background: #1e3c72;
  color: white;
  border-color: #1e3c72;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  background: white;
  border-radius: 12px;
  width: 90%;
  max-width: 600px;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-header {
  padding: 20px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  font-size: 18px;
  font-weight: 600;
}

.modal-close {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #999;
}

.modal-body {
  padding: 20px;
}

.modal-footer {
  padding: 20px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  font-weight: 500;
}

.form-group input,
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 10px 14px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

.form-group textarea {
  resize: vertical;
  min-height: 100px;
}

.time-input {
  display: flex;
  align-items: center;
  gap: 10px;
}

.time-input input {
  flex: 1;
}

@media (max-width: 768px) {
  .meeting-grid {
    grid-template-columns: 1fr;
  }
}
</style>
