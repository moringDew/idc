<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <div class="logo">
          <i class="fas fa-shield-alt"></i>
        </div>
        <h1>武警贵州总队六盘水支队</h1>
        <p>协同管理平台</p>
      </div>
      <form class="login-form" @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">用户名</label>
          <input
            type="text"
            id="username"
            v-model="form.username"
            placeholder="请输入用户名"
            required
          />
        </div>
        <div class="form-group">
          <label for="password">密码</label>
          <input
            type="password"
            id="password"
            v-model="form.password"
            placeholder="请输入密码"
            required
          />
        </div>
        <div class="form-group">
          <label for="captcha">验证码</label>
          <div class="captcha-row">
            <input
              type="text"
              id="captcha"
              v-model="form.captcha"
              placeholder="请输入验证码"
              required
            />
            <img :src="captchaImage" alt="验证码" class="captcha-img" />
          </div>
        </div>
        <button type="submit" class="login-btn">登录</button>
      </form>
      <div class="login-links">
        <a href="#">忘记密码?</a>
        <a href="#">联系管理员</a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const form = reactive({
  username: '',
  password: '',
  captcha: ''
})

const captchaImage = ref('data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyMDAiIGhlaWdodD0iNjAiIHZpZXdCb3g9IjAgMCAyMDAgNjAiPjxyZWN0IHdpZHRoPSIxMDAlIiBoZWlnaHQ9IjEwMCUiIGZpbGw9IiNmNWY3ZmEiLz48dGV4dCB4PSIxMDBweSIgeT0iMzZweCIgZm9udC1zaXplPSIyMCIgZm9udC1mYW1pbHk9IkFyaWFsLCBzYW5zLXNlcmlmIj5TR04gMTAwPC90ZXh0Pjwvc3ZnPg==')

const handleLogin = () => {
  router.push('/dashboard')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
}

.login-box {
  background: white;
  border-radius: 12px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  padding: 40px;
  width: 100%;
  max-width: 420px;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo {
  font-size: 48px;
  margin-bottom: 12px;
  color: #1e3c72;
}

.login-header h1 {
  color: #1e3c72;
  font-size: 24px;
  margin-bottom: 8px;
}

.login-header p {
  color: #666;
  font-size: 14px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
}

.form-group input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s ease;
}

.form-group input:focus {
  outline: none;
  border-color: #1e3c72;
  box-shadow: 0 0 0 3px rgba(30, 60, 114, 0.1);
}

.captcha-row {
  display: flex;
  gap: 10px;
}

.captcha-row input {
  flex: 1;
}

.captcha-img {
  width: 100px;
  height: 44px;
  border-radius: 6px;
}

.login-btn {
  width: 100%;
  padding: 14px;
  background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(30, 60, 114, 0.4);
}

.login-links {
  display: flex;
  justify-content: space-between;
  margin-top: 16px;
  font-size: 13px;
}

.login-links a {
  color: #666;
  text-decoration: none;
}

.login-links a:hover {
  color: #1e3c72;
  text-decoration: underline;
}
</style>
