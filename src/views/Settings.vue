<template>
  <div class="layout-container">
    <Sidebar />
    <main class="main-content">
      <TopNav title="系统设置" />
      <div class="content-area">
        <div class="breadcrumb">
          <router-link to="/dashboard">首页</router-link>
          <span><i class="fas fa-angle-right"></i></span>
          <span>系统设置</span>
        </div>

        <div class="settings-layout">
          <aside class="settings-sidebar">
            <h4>设置菜单</h4>
            <nav>
              <button 
                v-for="menu in settingsMenu" 
                :key="menu.key"
                :class="['menu-item', { active: activeMenu === menu.key }]"
                @click="activeMenu = menu.key"
              >
                <i :class="menu.icon"></i>
                {{ menu.label }}
              </button>
            </nav>
          </aside>

          <div class="settings-content">
            <div class="card" v-if="activeMenu === 'users'">
              <div class="card-header">
                <div class="card-title">用户管理</div>
                <div class="card-actions">
                  <button class="btn btn-primary" @click="showCreateUserModal = true">
                    <i class="fas fa-plus"></i> 新增用户
                  </button>
                </div>
              </div>

              <div class="search-box">
                <i class="fas fa-search"></i>
                <input type="text" v-model="searchKeyword" placeholder="搜索用户名...">
              </div>

              <table class="table">
                <thead>
                  <tr>
                    <th>序号</th>
                    <th>用户名</th>
                    <th>姓名</th>
                    <th>部门</th>
                    <th>角色</th>
                    <th>状态</th>
                    <th>操作</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(user, index) in filteredUsers" :key="user.id">
                    <td>{{ index + 1 }}</td>
                    <td>{{ user.username }}</td>
                    <td>{{ user.name }}</td>
                    <td>{{ user.department }}</td>
                    <td>{{ user.role }}</td>
                    <td><span :class="['status', user.status]">{{ user.status === 'enabled' ? '启用' : '禁用' }}</span></td>
                    <td>
                      <button class="btn btn-secondary" style="padding: 4px 10px; font-size: 12px; margin-right: 5px;" @click="editUser(user)">编辑</button>
                      <button 
                        class="btn" 
                        :class="user.status === 'enabled' ? 'btn-danger' : 'btn-success'" 
                        style="padding: 4px 10px; font-size: 12px;"
                        @click="toggleUserStatus(user)"
                      >{{ user.status === 'enabled' ? '禁用' : '启用' }}</button>
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

            <div class="card" v-if="activeMenu === 'roles'">
              <div class="card-header">
                <div class="card-title">角色权限</div>
                <div class="card-actions">
                  <button class="btn btn-primary">
                    <i class="fas fa-plus"></i> 新增角色
                  </button>
                </div>
              </div>
              <div style="padding: 20px; text-align: center; color: #666;">角色权限管理功能开发中...</div>
            </div>

            <div class="card" v-if="activeMenu === 'departments'">
              <div class="card-header">
                <div class="card-title">部门管理</div>
                <div class="card-actions">
                  <button class="btn btn-primary">
                    <i class="fas fa-plus"></i> 新增部门
                  </button>
                </div>
              </div>
              <div style="padding: 20px; text-align: center; color: #666;">部门管理功能开发中...</div>
            </div>

            <div class="card" v-if="activeMenu === 'system'">
              <div class="card-header">
                <div class="card-title">系统配置</div>
              </div>
              <div style="padding: 20px; text-align: center; color: #666;">系统配置功能开发中...</div>
            </div>

            <div class="card" v-if="activeMenu === 'backup'">
              <div class="card-header">
                <div class="card-title">数据备份</div>
              </div>
              <div style="padding: 20px; text-align: center; color: #666;">数据备份功能开发中...</div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>

  <div v-if="showCreateUserModal" class="modal-overlay" @click.self="showCreateUserModal = false">
    <div class="modal">
      <div class="modal-header">
        <h3>{{ editingUser ? '编辑用户' : '新增用户' }}</h3>
        <button class="modal-close" @click="closeModal">×</button>
      </div>
      <div class="modal-body">
        <div class="form-group">
          <label>用户名</label>
          <input type="text" v-model="form.username" placeholder="请输入用户名">
        </div>
        <div class="form-group">
          <label>姓名</label>
          <input type="text" v-model="form.name" placeholder="请输入姓名">
        </div>
        <div class="form-group" v-if="!editingUser">
          <label>密码</label>
          <input type="password" v-model="form.password" placeholder="请输入密码">
        </div>
        <div class="form-group" v-if="!editingUser">
          <label>确认密码</label>
          <input type="password" v-model="form.confirmPassword" placeholder="请确认密码">
        </div>
        <div class="form-group">
          <label>部门</label>
          <select v-model="form.department">
            <option>办公室</option>
            <option>司令部</option>
            <option>政治处</option>
            <option>后勤处</option>
          </select>
        </div>
        <div class="form-group">
          <label>角色</label>
          <select v-model="form.role">
            <option>超级管理员</option>
            <option>部门管理员</option>
            <option>普通用户</option>
          </select>
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn btn-secondary" @click="closeModal">取消</button>
        <button class="btn btn-primary" @click="saveUser">{{ editingUser ? '更新用户' : '创建用户' }}</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import Sidebar from '@/components/Sidebar.vue'
import TopNav from '@/components/TopNav.vue'

const activeMenu = ref('users')
const searchKeyword = ref('')
const currentPage = ref(1)
const showCreateUserModal = ref(false)
const editingUser = ref(null)

const form = reactive({
  username: '',
  name: '',
  password: '',
  confirmPassword: '',
  department: '办公室',
  role: '普通用户'
})

const settingsMenu = [
  { key: 'users', label: '用户管理', icon: 'fas fa-users' },
  { key: 'roles', label: '角色权限', icon: 'fas fa-user-tag' },
  { key: 'departments', label: '部门管理', icon: 'fas fa-building' },
  { key: 'system', label: '系统配置', icon: 'fas fa-cog' },
  { key: 'backup', label: '数据备份', icon: 'fas fa-file-export' }
]

const users = ref([
  { id: 1, username: 'admin', name: '管理员', department: '办公室', role: '超级管理员', status: 'enabled' },
  { id: 2, username: 'zhangxx', name: '张XX', department: '司令部', role: '部门管理员', status: 'enabled' },
  { id: 3, username: 'lixx', name: '李XX', department: '政治处', role: '普通用户', status: 'enabled' },
  { id: 4, username: 'wangxx', name: '王XX', department: '后勤处', role: '普通用户', status: 'disabled' }
])

const filteredUsers = computed(() => {
  return users.value.filter(user => {
    return !searchKeyword.value || user.username.includes(searchKeyword.value) || user.name.includes(searchKeyword.value)
  })
})

const totalPages = computed(() => Math.ceil(filteredUsers.value.length / 5))

const prevPage = () => {
  if (currentPage.value > 1) currentPage.value--
}

const nextPage = () => {
  if (currentPage.value < totalPages.value) currentPage.value++
}

const editUser = (user) => {
  editingUser.value = user
  form.username = user.username
  form.name = user.name
  form.department = user.department
  form.role = user.role
  showCreateUserModal.value = true
}

const toggleUserStatus = (user) => {
  user.status = user.status === 'enabled' ? 'disabled' : 'enabled'
}

const closeModal = () => {
  showCreateUserModal.value = false
  editingUser.value = null
  Object.keys(form).forEach(key => form[key] = '')
  form.department = '办公室'
  form.role = '普通用户'
}

const saveUser = () => {
  if (editingUser.value) {
    const index = users.value.findIndex(u => u.id === editingUser.value.id)
    if (index !== -1) {
      users.value[index] = { ...users.value[index], ...form }
    }
  } else {
    users.value.unshift({
      id: Date.now(),
      username: form.username,
      name: form.name,
      department: form.department,
      role: form.role,
      status: 'enabled'
    })
  }
  closeModal()
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

.settings-layout {
  display: grid;
  grid-template-columns: 200px 1fr;
  gap: 24px;
}

.settings-sidebar {
  background: white;
  border-radius: 12px;
  padding: 16px;
}

.settings-sidebar h4 {
  font-weight: 600;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #eee;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 10px 12px;
  width: 100%;
  text-align: left;
  border: none;
  background: transparent;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #333;
}

.menu-item:hover {
  background: #f5f7fa;
}

.menu-item.active {
  background: #1e3c72;
  color: white;
}

.menu-item i {
  margin-right: 10px;
}

.settings-content {
  flex: 1;
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

.btn-danger {
  background: linear-gradient(135deg, #dc3545 0%, #e63946 100%);
  color: white;
}

.btn-success {
  background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
  color: white;
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

.status {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.status.enabled { background: #d4edda; color: #155724; }
.status.disabled { background: #f8d7da; color: #721c24; }

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
  max-width: 500px;
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

.modal-header h3 { font-size: 18px; font-weight: 600; }

.modal-close {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #999;
}

.modal-body { padding: 20px; }

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

.form-group input, .form-group select {
  width: 100%;
  padding: 10px 14px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

@media (max-width: 768px) {
  .settings-layout {
    grid-template-columns: 1fr;
  }
}
</style>
