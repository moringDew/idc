import axios from 'axios'

const instance = axios.create({
  baseURL: '/api',
  timeout: 10000
})

instance.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    let message = '网络请求失败'
    if (error.response) {
      const { status, data } = error.response
      if (data && data.message) {
        message = data.message
      } else {
        switch (status) {
          case 401:
            message = '未登录或登录已过期，请重新登录'
            break
          case 403:
            message = '权限不足，无法访问'
            break
          case 404:
            message = '请求的资源不存在'
            break
          case 500:
            message = '服务器内部错误'
            break
          default:
            message = `请求错误，状态码：${status}`
        }
      }
    } else if (error.message) {
      message = error.message
    }
    showError(message)
    return Promise.reject(error)
  }
)

function showError(message) {
  if (document.querySelector('.error-toast')) {
    document.querySelector('.error-toast').remove()
  }
  const toast = document.createElement('div')
  toast.className = 'error-toast'
  toast.innerHTML = `
    <div class="toast-content">
      <i class="fas fa-exclamation-circle"></i>
      <span>${message}</span>
    </div>
  `
  document.body.appendChild(toast)
  
  setTimeout(() => {
    toast.classList.add('fade-out')
    setTimeout(() => {
      toast.remove()
    }, 300)
  }, 3000)
}

export default instance