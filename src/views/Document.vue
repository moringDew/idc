<template>
  <div class="layout-container">
    <Sidebar />
    <main class="main-content">
      <TopNav title="公文管理" />
      <div class="content-area">
        <div class="breadcrumb">
          <router-link to="/dashboard">首页</router-link>
          <span><i class="fas fa-angle-right"></i></span>
          <span>公文管理</span>
        </div>

        <div class="card">
          <div class="card-header">
            <div class="card-title">公文列表</div>
            <div class="card-actions">
              <button class="btn btn-primary" @click="showCreateModal = true">
                <i class="fas fa-plus"></i> 新建公文
              </button>
            </div>
          </div>

          <div class="search-box">
            <i class="fas fa-search"></i>
            <input type="text" v-model="searchKeyword" placeholder="搜索公文标题...">
          </div>

          <div class="filter-bar">
            <select v-model="filterType">
              <option value="">全部类型</option>
              <option value="通知">通知</option>
              <option value="请示">请示</option>
              <option value="报告">报告</option>
              <option value="通报">通报</option>
              <option value="决定">决定</option>
            </select>
            <select v-model="filterStatus">
              <option value="">全部状态</option>
              <option value="draft">草稿</option>
              <option value="pending">待审批</option>
              <option value="approved">已通过</option>
              <option value="rejected">已驳回</option>
            </select>
            <input type="date" v-model="startDate" placeholder="开始日期">
            <input type="date" v-model="endDate" placeholder="结束日期">
            <button class="btn btn-secondary" @click="handleFilter">筛选</button>
          </div>

          <table class="table">
            <thead>
              <tr>
                <th>编号</th>
                <th>标题</th>
                <th>类型</th>
                <th>发文单位</th>
                <th>发文日期</th>
                <th>状态</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="doc in filteredDocuments" :key="doc.id">
                <td>{{ doc.code }}</td>
                <td>{{ doc.title }}</td>
                <td>{{ doc.type }}</td>
                <td>{{ doc.department }}</td>
                <td>{{ doc.date }}</td>
                <td><span :class="['status', doc.status]">{{ getStatusText(doc.status) }}</span></td>
                <td>
                  <button 
                    v-if="doc.status !== 'approved'" 
                    class="btn btn-primary" 
                    style="padding: 4px 10px; font-size: 12px; margin-right: 5px;"
                    @click="editDocument(doc)"
                  >
                    编辑
                  </button>
                  <button class="btn btn-secondary" style="padding: 4px 10px; font-size: 12px;" @click="viewDocument(doc)">
                    {{ doc.status === 'approved' ? '打印' : '查看' }}
                  </button>
                </td>
              </tr>
            </tbody>
          </table>

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
        <h3>{{ editingDoc ? '编辑公文' : '新建公文' }}</h3>
        <button class="modal-close" @click="showCreateModal = false">×</button>
      </div>
      <div class="modal-body">
        <div class="form-group">
          <label>公文标题</label>
          <input type="text" v-model="form.title" placeholder="请输入公文标题">
        </div>
        <div class="form-group">
          <label>公文类型</label>
          <select v-model="form.type">
            <option>通知</option>
            <option>请示</option>
            <option>报告</option>
            <option>通报</option>
            <option>决定</option>
          </select>
        </div>
        <div class="form-group">
          <label>发文单位</label>
          <input type="text" v-model="form.department" placeholder="请输入发文单位">
        </div>
        <div class="form-group">
          <label>主送机关</label>
          <input type="text" v-model="form.recipient" placeholder="请输入主送机关">
        </div>
        <div class="form-group">
          <label>公文内容</label>
          <textarea v-model="form.content" placeholder="请输入公文内容"></textarea>
        </div>
        <div class="form-group">
          <label>附件</label>
          <input type="file" multiple>
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn btn-secondary" @click="showCreateModal = false">取消</button>
        <button class="btn btn-primary" @click="saveDocument">保存并提交</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, reactive } from 'vue'
import Sidebar from '@/components/Sidebar.vue'
import TopNav from '@/components/TopNav.vue'

const searchKeyword = ref('')
const filterType = ref('')
const filterStatus = ref('')
const startDate = ref('')
const endDate = ref('')
const currentPage = ref(1)
const showCreateModal = ref(false)
const editingDoc = ref(null)

const form = reactive({
  title: '',
  type: '通知',
  department: '',
  recipient: '',
  content: ''
})

const documents = ref([
  { id: 1, code: '2024-001', title: '关于开展年度训练考核的通知', type: '通知', department: '司令部', date: '2024-01-15', status: 'pending' },
  { id: 2, code: '2024-002', title: '关于申请采购办公设备的请示', type: '请示', department: '后勤处', date: '2024-01-14', status: 'processing' },
  { id: 3, code: '2024-003', title: '关于表彰优秀士兵的通报', type: '通报', department: '政治处', date: '2024-01-13', status: 'approved' },
  { id: 4, code: '2024-004', title: '关于加强安全管理的决定', type: '决定', department: '办公室', date: '2024-01-12', status: 'rejected' },
  { id: 5, code: '2024-005', title: '关于年度工作总结的报告', type: '报告', department: '政治处', date: '2024-01-11', status: 'approved' }
])

const filteredDocuments = computed(() => {
  return documents.value.filter(doc => {
    const matchKeyword = !searchKeyword.value || doc.title.includes(searchKeyword.value)
    const matchType = !filterType.value || doc.type === filterType.value
    const matchStatus = !filterStatus.value || doc.status === filterStatus.value
    return matchKeyword && matchType && matchStatus
  })
})

const totalPages = computed(() => Math.ceil(filteredDocuments.value.length / 5))

const getStatusText = (status) => {
  const statusMap = {
    draft: '草稿',
    pending: '待审批',
    processing: '处理中',
    approved: '已通过',
    rejected: '已驳回'
  }
  return statusMap[status] || status
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

const editDocument = (doc) => {
  editingDoc.value = doc
  form.title = doc.title
  form.type = doc.type
  form.department = doc.department
  showCreateModal.value = true
}

const viewDocument = (doc) => {
  console.log('View document:', doc.title)
}

const saveDocument = () => {
  if (editingDoc.value) {
    const index = documents.value.findIndex(d => d.id === editingDoc.value.id)
    if (index !== -1) {
      documents.value[index] = { ...documents.value[index], ...form }
    }
  } else {
    documents.value.unshift({
      id: Date.now(),
      code: `2024-${String(documents.value.length + 1).padStart(3, '0')}`,
      date: new Date().toISOString().split('T')[0],
      status: 'pending',
      ...form
    })
  }
  showCreateModal.value = false
  editingDoc.value = null
  Object.keys(form).forEach(key => form[key] = '')
  form.type = '通知'
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

.search-box {
  position: relative;
  margin-bottom: 20px;
}

.search-box input {
  width: 100%;
  padding: 10px 14px 10px 40px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
}

.search-box i {
  position: absolute;
  left: 14px;
  top: 50%;
  transform: translateY(-50%);
  color: #999;
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
</style>
