<template>
  <div class="layout-container">
    <Sidebar />
    <main class="main-content">
      <TopNav title="知识文档" />
      <div class="content-area">
        <div class="breadcrumb">
          <router-link to="/dashboard">首页</router-link>
          <span><i class="fas fa-angle-right"></i></span>
          <span>知识文档</span>
        </div>

        <div class="card">
          <div class="card-header">
            <div class="card-title">文档库</div>
            <div class="card-actions">
              <button class="btn btn-primary" @click="showUploadModal = true">
                <i class="fas fa-upload"></i> 上传文档
              </button>
              <button class="btn btn-secondary">新建文件夹</button>
            </div>
          </div>

          <div class="search-box">
            <i class="fas fa-search"></i>
            <input type="text" v-model="searchKeyword" placeholder="搜索文档...">
          </div>

          <div class="filter-tabs">
            <button 
              v-for="tab in tabs" 
              :key="tab.value"
              :class="['tab-btn', { active: activeTab === tab.value }]"
              @click="activeTab = tab.value"
            >
              {{ tab.label }}
            </button>
          </div>

          <div class="folder-grid">
            <div 
              class="folder-card" 
              v-for="folder in folders" 
              :key="folder.name"
              @click="openFolder(folder)"
            >
              <div :class="['folder-icon', folder.color]">
                <i class="fas fa-folder"></i>
              </div>
              <div class="folder-name">{{ folder.name }}</div>
              <div class="folder-count">{{ folder.count }}个文件</div>
            </div>
          </div>

          <div class="recent-section">
            <h4>最近更新</h4>
            <table class="table">
              <thead>
                <tr>
                  <th>文件名</th>
                  <th>类型</th>
                  <th>大小</th>
                  <th>更新时间</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="file in recentFiles" :key="file.id">
                  <td>
                    <div class="file-info">
                      <i :class="['file-icon', file.iconClass]" :style="{ color: file.iconColor }"></i>
                      <span>{{ file.name }}</span>
                    </div>
                  </td>
                  <td>{{ file.type }}</td>
                  <td>{{ file.size }}</td>
                  <td>{{ file.time }}</td>
                  <td>
                    <button class="btn btn-secondary" style="padding: 4px 10px; font-size: 12px; margin-right: 5px;">下载</button>
                    <button class="btn btn-secondary" style="padding: 4px 10px; font-size: 12px;">分享</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </main>
  </div>

  <div v-if="showUploadModal" class="modal-overlay" @click.self="showUploadModal = false">
    <div class="modal">
      <div class="modal-header">
        <h3>上传文档</h3>
        <button class="modal-close" @click="showUploadModal = false">×</button>
      </div>
      <div class="modal-body">
        <div class="upload-area">
          <i class="fas fa-cloud-upload-alt"></i>
          <div>点击或拖拽文件到此处上传</div>
          <div>支持 doc, docx, pdf, xls, xlsx, ppt, pptx, jpg, png 等格式</div>
          <input type="file" multiple style="display: none;" id="fileInput">
        </div>
        <div class="form-group">
          <label>选择文件夹</label>
          <select v-model="uploadForm.folder">
            <option>请选择文件夹</option>
            <option>规章制度</option>
            <option>训练资料</option>
            <option>会议纪要</option>
            <option>工作总结</option>
          </select>
        </div>
        <div class="form-group">
          <label>描述</label>
          <textarea v-model="uploadForm.description" placeholder="请输入文档描述"></textarea>
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn btn-secondary" @click="showUploadModal = false">取消</button>
        <button class="btn btn-primary" @click="uploadFile">上传</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import Sidebar from '@/components/Sidebar.vue'
import TopNav from '@/components/TopNav.vue'

const searchKeyword = ref('')
const activeTab = ref('all')
const showUploadModal = ref(false)

const uploadForm = reactive({
  folder: '',
  description: ''
})

const tabs = [
  { label: '全部文档', value: 'all' },
  { label: '我的文档', value: 'mine' },
  { label: '共享文档', value: 'shared' },
  { label: '部门文档', value: 'department' }
]

const folders = [
  { name: '规章制度', count: 12, color: 'blue' },
  { name: '训练资料', count: 28, color: 'green' },
  { name: '会议纪要', count: 15, color: 'orange' },
  { name: '工作总结', count: 8, color: 'red' }
]

const recentFiles = ref([
  { id: 1, name: '2024年度训练计划.docx', type: 'Word文档', size: '256 KB', time: '2024-01-15 10:30', iconClass: 'fas fa-file-word', iconColor: '#1e3c72' },
  { id: 2, name: '安全管理制度.pdf', type: 'PDF文档', size: '1.2 MB', time: '2024-01-14 15:20', iconClass: 'fas fa-file-pdf', iconColor: '#dc3545' },
  { id: 3, name: '物资采购清单.xlsx', type: 'Excel表格', size: '128 KB', time: '2024-01-13 09:15', iconClass: 'fas fa-file-excel', iconColor: '#28a745' }
])

const openFolder = (folder) => {
  console.log('Open folder:', folder.name)
}

const uploadFile = () => {
  showUploadModal.value = false
  uploadForm.folder = ''
  uploadForm.description = ''
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

.filter-tabs {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
}

.tab-btn {
  padding: 8px 20px;
  border: none;
  background: #f5f7fa;
  color: #666;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
}

.tab-btn.active {
  background: #1e3c72;
  color: white;
}

.tab-btn:hover:not(.active) {
  background: #eee;
}

.folder-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}

.folder-card {
  background: #f9fafb;
  border-radius: 12px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.folder-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

.folder-icon {
  font-size: 40px;
  margin-bottom: 12px;
}

.folder-icon.blue {
  color: #1e3c72;
}

.folder-icon.green {
  color: #28a745;
}

.folder-icon.orange {
  color: #ff9800;
}

.folder-icon.red {
  color: #dc3545;
}

.folder-name {
  font-weight: 600;
  margin-bottom: 4px;
}

.folder-count {
  font-size: 12px;
  color: #666;
}

.recent-section {
  margin-top: 30px;
}

.recent-section h4 {
  font-weight: 600;
  margin-bottom: 16px;
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

.file-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.file-icon {
  font-size: 20px;
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

.upload-area {
  border: 2px dashed #ddd;
  border-radius: 12px;
  padding: 40px;
  text-align: center;
  margin-bottom: 20px;
}

.upload-area i {
  font-size: 48px;
  color: #999;
  margin-bottom: 16px;
}

.upload-area div:first-of-type {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 8px;
}

.upload-area div:last-of-type {
  font-size: 13px;
  color: #666;
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
  .folder-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
