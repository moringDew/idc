<template>
  <div class="layout-container">
    <Sidebar />
    <main class="main-content">
      <TopNav title="流程审批" />
      <div class="content-area">
        <div class="breadcrumb">
          <router-link to="/dashboard">首页</router-link>
          <span><i class="fas fa-angle-right"></i></span>
          <span>流程审批</span>
        </div>

        <div class="action-bar">
          <button class="btn btn-primary" @click="showCreateModal = true">
            <i class="fas fa-plus"></i> 发起流程
          </button>
          <button class="btn btn-secondary">流程监控</button>
          <button class="btn btn-secondary">流程报表</button>
        </div>

        <div class="stats-row">
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
            <div class="card-title">待我审批</div>
          </div>

          <div class="approval-list">
            <div class="approval-item" v-for="item in pendingApprovals" :key="item.id">
              <div class="approval-header">
                <div>
                  <div class="approval-title">{{ item.title }}</div>
                  <div class="approval-type">流程类型: {{ item.type }}</div>
                </div>
                <span :class="['status', item.status]">{{ getStatusText(item.status) }}</span>
              </div>
              <div class="approval-meta">
                <span><i class="fas fa-user"></i> {{ item.applicant }}</span>
                <span><i class="fas fa-building"></i> {{ item.department }}</span>
                <span><i class="fas fa-clock"></i> {{ item.time }}</span>
              </div>
              <div class="approval-content">
                <div class="content-label">申请内容:</div>
                <div class="content-text">{{ item.content }}</div>
              </div>
              <div class="approval-actions">
                <button class="btn btn-secondary" @click="viewDetail(item)">查看详情</button>
                <button class="btn btn-danger" @click="handleReject(item)">驳回</button>
                <button class="btn btn-success" @click="handleApprove(item)">同意</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>

  <div v-if="showCreateModal" class="modal-overlay" @click.self="showCreateModal = false">
    <div class="modal">
      <div class="modal-header">
        <h3>发起流程</h3>
        <button class="modal-close" @click="showCreateModal = false">×</button>
      </div>
      <div class="modal-body">
        <div class="form-group">
          <label>流程类型</label>
          <select v-model="form.type">
            <option>采购审批</option>
            <option>请假审批</option>
            <option>报销审批</option>
            <option>用章审批</option>
            <option>外出审批</option>
            <option>其他</option>
          </select>
        </div>
        <div class="form-group">
          <label>流程标题</label>
          <input type="text" v-model="form.title" placeholder="请输入流程标题">
        </div>
        <div class="form-group">
          <label>申请内容</label>
          <textarea v-model="form.content" placeholder="请输入申请内容"></textarea>
        </div>
        <div class="form-group">
          <label>附件</label>
          <input type="file" multiple>
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn btn-secondary" @click="showCreateModal = false">取消</button>
        <button class="btn btn-primary" @click="submitApproval">提交</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import Sidebar from '@/components/Sidebar.vue'
import TopNav from '@/components/TopNav.vue'
import { getApprovals, createApproval, approve, reject } from '@/api'

const showCreateModal = ref(false)

const form = reactive({
  type: '采购审批',
  title: '',
  content: ''
})

const stats = ref([
  { value: '0', label: '待我审批', icon: 'fas fa-clock', color: 'blue' },
  { value: '0', label: '我已审批', icon: 'fas fa-check-circle', color: 'green' },
  { value: '0', label: '我发起的', icon: 'fas fa-file-invoice', color: 'orange' },
  { value: '0', label: '知会我', icon: 'fas fa-eye', color: 'red' }
])

const pendingApprovals = ref([])

const getStatusText = (status) => {
  const statusMap = {
    pending: '待审批',
    processing: '处理中',
    approved: '已通过',
    rejected: '已驳回'
  }
  return statusMap[status] || status
}

const viewDetail = (item) => {
  console.log('View detail:', item.title)
}

const handleReject = async (item) => {
  try {
    const response = await reject(item.id)
    if (response.code === 200) {
      item.status = 'rejected'
    }
  } catch (error) {
    console.error('Failed to reject:', error)
  }
}

const handleApprove = async (item) => {
  try {
    const response = await approve(item.id)
    if (response.code === 200) {
      item.status = 'approved'
    }
  } catch (error) {
    console.error('Failed to approve:', error)
  }
}

const submitApproval = async () => {
  try {
    const response = await createApproval({
      title: form.title,
      type: form.type,
      applicant: '管理员',
      applicantDepartment: '办公室',
      content: form.content,
      status: 'pending'
    })
    if (response.code === 200) {
      pendingApprovals.value.unshift({
        ...response.data,
        department: response.data.applicantDepartment,
        time: response.data.createdAt
      })
    }
    showCreateModal.value = false
    form.type = '采购审批'
    form.title = ''
    form.content = ''
  } catch (error) {
    console.error('Failed to create approval:', error)
  }
}

const loadApprovals = async () => {
  try {
    const response = await getApprovals()
    if (response.code === 200) {
      pendingApprovals.value = response.data.map(item => ({
        ...item,
        department: item.applicantDepartment,
        time: item.createdAt
      }))
      stats.value[0].value = pendingApprovals.value.filter(a => a.status === 'pending').length.toString()
      stats.value[1].value = pendingApprovals.value.filter(a => a.status === 'approved').length.toString()
    }
  } catch (error) {
    console.error('Failed to load approvals:', error)
  }
}

onMounted(loadApprovals)
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

.action-bar {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
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

.btn-success {
  background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
  color: white;
}

.btn-danger {
  background: linear-gradient(135deg, #dc3545 0%, #e63946 100%);
  color: white;
}

.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
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

.approval-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.approval-item {
  border: 1px solid #eee;
  border-radius: 12px;
  padding: 20px;
}

.approval-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.approval-title {
  font-weight: 600;
  font-size: 16px;
  margin-bottom: 4px;
}

.approval-type {
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

.status.approved {
  background: #d4edda;
  color: #155724;
}

.status.rejected {
  background: #f8d7da;
  color: #721c24;
}

.approval-meta {
  display: flex;
  gap: 24px;
  font-size: 13px;
  color: #666;
  margin-bottom: 16px;
}

.approval-meta i {
  margin-right: 4px;
}

.approval-content {
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

.approval-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.approval-actions .btn {
  padding: 6px 14px;
  font-size: 13px;
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

@media (max-width: 768px) {
  .stats-row {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
