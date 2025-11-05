<script>
import { ajaxGet, ajaxPost } from '../../api/index';

export default {
  data() {
    return {
      userInfo: {
        customerId: '',
        name: '',
        phone: '',
        avatar: ''
      },
      isSubmitting: false,
      loading: false,
      phoneError: false,
      phoneErrorMessage: ''
      ,
      // 密码修改相关数据
      passwordVisible: false,
      passwordForm: {
        newPassword: '',
        confirmPassword: ''
      },
      newPasswordError: false,
      newPasswordErrorMessage: '',
      confirmPasswordError: false,
      confirmPasswordErrorMessage: '',
      passwordRules: {
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, max: 18, message: '密码长度在6-18个字符之间', trigger: 'blur' },
          { pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,18}$/, message: '密码只能包含字母和数字', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认新密码', trigger: 'blur' },
          { validator: this.validateConfirmPassword, trigger: 'blur' }
        ]
      }
    };
  },
  created() {
    // 页面加载时从后端获取用户信息
    this.getUserInfo();
  },
  methods: {
    // 获取用户信息
    async getUserInfo() {
      this.loading = true;
      try {
        // 从sessionStorage获取用户信息
        const userSession = sessionStorage.getItem('user');
        if (userSession) {
          const userData = JSON.parse(userSession);

          // 调用后端API获取详细用户信息
          const response = await ajaxGet('/customer/getUserInfo', { customerId: userData.employeeId });

          // 处理不同的响应格式
          if (response && response.data && typeof response.data === 'object') {
            // 新的响应格式：数据嵌套在response.data中
            this.userInfo = {
              customerId: response.data.customerId || userData.employeeId,
              name: response.data.name || '',
              phone: response.data.phone || '',
            };
          } else if (response && typeof response === 'object') {
            // 旧的响应格式：数据直接在response中
            this.userInfo = {
              customerId: response.customerId || userData.employeeId,
              name: response.name || '',
              phone: response.phone || '',
            };
          } else {
            // 如果获取失败，使用sessionStorage中的基本信息
            this.userInfo.customerId = userData.employeeId || '';
            this.$message.warning('获取用户详细信息失败，使用基本信息');
          }
        } else {
          this.$message.error('用户未登录，请重新登录');
          this.$router.push('/login');
        }
      } catch (error) {
        console.error('获取用户信息失败:', error);
        this.$message.error('获取用户信息失败，请稍后重试');

        // 尝试从sessionStorage获取基本信息
        const userSession = sessionStorage.getItem('user');
        if (userSession) {
          const userData = JSON.parse(userSession);
          this.userInfo.customerId = userData.employeeId || '';
        }
      } finally {
        this.loading = false;
      }
    },

    // 保存个人信息
    async saveProfile() {
      // 表单验证
      if (!this.userInfo.name) {
        this.$message.error('请输入姓名');
        return;
      }
      
      // 执行电话验证
      if (!this.validatePhone()) {
        return;
      }

      try {
        // 显示确认对话框
        await this.$confirm('确定要保存个人信息吗？', '确认操作', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });
        
        // 用户确认后设置提交状态
        this.isSubmitting = true;
        
        // 调用后端API保存用户信息
        const response = await ajaxPost('/customer/updateUserInfo', this.userInfo);

        // 处理后端返回的响应格式
        if (response && response.success) {
          this.$message.success(response.message || '保存成功');

          // 更新sessionStorage中的用户信息
          const userSession = sessionStorage.getItem('user');
          if (userSession) {
            const userData = JSON.parse(userSession);
            userData.name = this.userInfo.name;
            userData.phone = this.userInfo.phone;
            sessionStorage.setItem('user', JSON.stringify(userData));
          }
        } else {
          this.$message.error(response.message || '保存失败');
        }
      } catch (error) {
        // 处理确认对话框取消或保存失败的情况
        if (error === 'cancel') {
          this.$message.info('已取消保存');
        } else {
          console.error('保存用户信息失败:', error);
          this.$message.error('保存失败，请稍后重试');
        }
      } finally {
        // 无论成功失败都重置提交状态
        this.isSubmitting = false;
      }
    },
    
    // 验证密码格式
    validatePassword() {
      this.newPasswordError = false;
      this.newPasswordErrorMessage = '';
      
      if (!this.passwordForm.newPassword) {
        this.newPasswordError = true;
        this.newPasswordErrorMessage = '请输入新密码';
        return false;
      }
      
      if (this.passwordForm.newPassword.length < 6 || this.passwordForm.newPassword.length > 18) {
        this.newPasswordError = true;
        this.newPasswordErrorMessage = '密码长度在6-18个字符之间';
        return false;
      }
      
      if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,18}$/.test(this.passwordForm.newPassword)) {
        this.newPasswordError = true;
        this.newPasswordErrorMessage = '密码必须同时包含字母和数字';
        return false;
      }
      
      return true;
    },
    
    // 验证确认密码
    validateConfirm() {
      this.confirmPasswordError = false;
      this.confirmPasswordErrorMessage = '';
      
      if (!this.passwordForm.confirmPassword) {
        this.confirmPasswordError = true;
        this.confirmPasswordErrorMessage = '请确认新密码';
        return false;
      }
      
      if (this.passwordForm.confirmPassword !== this.passwordForm.newPassword) {
        this.confirmPasswordError = true;
        this.confirmPasswordErrorMessage = '两次输入的密码不一致';
        return false;
      }
      
      return true;
    },
    
    // 验证确认密码(表单规则)
    validateConfirmPassword(rule, value, callback) {
      if (value === '') {
        callback(new Error('请确认新密码'));
      } else if (value !== this.passwordForm.newPassword) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    },
    
    // 打开修改密码弹窗
    changePassword() {
      this.passwordForm = {
        newPassword: '',
        confirmPassword: ''
      };
      // 重置表单验证状态和错误信息
      this.newPasswordError = false;
      this.newPasswordErrorMessage = '';
      this.confirmPasswordError = false;
      this.confirmPasswordErrorMessage = '';
      
      if (this.$refs.passwordForm) {
        this.$refs.passwordForm.resetFields();
      }
      this.passwordVisible = true;
    },
    
    // 关闭修改密码弹窗
    closePasswordDialog() {
      this.passwordVisible = false;
      if (this.$refs.passwordForm) {
        this.$refs.passwordForm.resetFields();
      }
    },
    
    // 提交密码修改
    async submitPasswordChange() {
      // 执行统一的表单验证
      if (!this.validatePassword()) {
        return;
      }
      
      if (!this.validateConfirm()) {
        return;
      }
      
      try {
        // 显示确认对话框
        await this.$confirm('确定要修改密码吗？', '确认操作', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });
        
        // 构建请求参数
        const passwordData = {
          customerId: this.userInfo.customerId,
          password: this.passwordForm.newPassword
        };
        
        // 调用修改密码接口
        const response = await ajaxPost('/customer/updatePassword', passwordData);
        
        if (response && response.success) {
          this.$message.success(response.message || '密码修改成功');
          this.passwordVisible = false;
        } else {
          this.$message.error(response.message || '密码修改失败');
        }
      } catch (error) {
        if (error === 'cancel') {
          this.$message.info('已取消修改');
        } else {
          console.error('修改密码失败:', error);
          this.$message.error('修改密码失败，请稍后重试');
        }
      }
    },
    
    // 选择头像
    chooseAvatar() {
      // 这里可以实现文件上传功能
      this.$message.info('头像上传功能开发中');
    },
    
    // 验证电话号码
    validatePhone() {
      this.phoneError = false;
      this.phoneErrorMessage = '';
      
      if (!this.userInfo.phone) {
        this.phoneError = true;
        this.phoneErrorMessage = '请输入手机号';
        return false;
      }
      
      if (!/^1[3-9]\d{9}$/.test(this.userInfo.phone)) {
        this.phoneError = true;
        this.phoneErrorMessage = '请输入正确的手机号码';
        return false;
      }
      
      return true;
    }
  }
};
</script>

<template>
  <div class="profile-page">
    <div class="profile-container">
      <div class="profile-card" v-loading="loading">
        <!-- 卡片头部 -->
        <div class="card-header">
          <h2 class="card-title">个人信息</h2>
          <div class="card-subtitle">管理您的个人资料和账户设置</div>
        </div>
        
        <!-- 卡片内容 -->
        <div class="card-body">
          <!-- 头像部分 -->
          <div class="avatar-section">
            <div class="avatar-wrapper" @click="chooseAvatar">
              <img src="../../assets/img/img.jpg" alt="用户头像" class="user-avatar" />
              <div class="avatar-overlay">
                <span class="avatar-text">更换头像</span>
              </div>
            </div>
          </div>
          
        <!-- 表单部分 -->
        <div class="form-section">
            <div class="form-row">
              <div class="form-group">
                <label class="form-label">
                  <span class="label-text">用户名</span>
                  <span class="label-desc">账号登录名</span>
                </label>
                <input 
                  type="text" 
                  v-model="userInfo.customerId"
                  class="form-input" 
                  readonly
                  :class="{ 'form-input-readonly': true }"
                />
              </div>
            </div>
            
            <div class="form-row">
              <div class="form-group">
                <label class="form-label">
                  <span class="label-text">姓名</span>
                  <span class="label-desc">请输入真实姓名</span>
                </label>
                <input 
                  type="text" 
                  v-model="userInfo.name" 
                  class="form-input"
                  placeholder="请输入姓名"
                  maxlength="20"
                />
              </div>
            </div>
            
            <div class="form-row">
              <div class="form-group">
                <label class="form-label">
                  <span class="label-text">电话</span>
                  <span class="label-desc">用于接收订单通知</span>
                </label>
                <input 
                  type="tel" 
                  v-model="userInfo.phone" 
                  class="form-input"
                  :class="{ 'form-input-error': phoneError }"
                  placeholder="请输入手机号码"
                  maxlength="11"
                  @blur="validatePhone"
                />
                <div v-if="phoneError" class="error-message">{{ phoneErrorMessage }}</div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 卡片底部 -->
        <div class="card-footer">
          <div class="form-actions">
            <button 
              class="btn btn-save" 
              @click="saveProfile" 
              :disabled="isSubmitting"
            >
              <i class="btn-icon save-icon"></i>
              {{ isSubmitting ? '保存中...' : '保存' }}
            </button>
            <button class="btn btn-password" @click="changePassword">
              <i class="btn-icon password-icon"></i>
              修改密码
            </button>
          </div>
        </div>
      </div>
    </div>
      
    <!-- 修改密码弹窗 -->
    <div class="password-dialog" v-if="passwordVisible" @click.self="closePasswordDialog">
      <div class="dialog-mask" @click="closePasswordDialog"></div>
      <div class="dialog-content">
        <div class="dialog-header">
          <h3 class="dialog-title">修改密码</h3>
          <button class="dialog-close" @click="closePasswordDialog" title="关闭">
            <svg class="close-icon" viewBox="0 0 16 16" width="16" height="16">
              <path fill="currentColor" d="M8.94 8.94a1.5 1.5 0 1 1-2.121-2.122 1.5 1.5 0 0 1 2.122 2.122zm.534-.536a.5.5 0 0 0-.707 0L5.707 10.707a.5.5 0 0 0 .707.707L8.5 8.707l2.086 2.086a.5.5 0 0 0 .707-.707L9.207 8l2.086-2.086a.5.5 0 0 0-.707-.707L8.5 7.293 6.414 5.207a.5.5 0 1 0-.707.707L7.793 8l-2.086 2.086a.5.5 0 0 0 .707.707L8.5 8.707l2.086 2.086a.5.5 0 0 0 .707-.707L9.47 8.404z"/>
            </svg>
          </button>
        </div>
        
        <div class="dialog-body">
          <form ref="passwordForm">
            <div class="form-group">
              <label class="form-label dialog-label">新密码</label>
              <input 
                  type="password" 
                  v-model="passwordForm.newPassword" 
                  class="form-input dialog-input"
                  :class="{ 'form-input-error': newPasswordError }"
                  placeholder="请输入新密码（6-18位，必须同时包含字母和数字）"
                   maxlength="18"
                  @blur="validatePassword"
                  @input="validatePassword"
                />
              <div v-if="newPasswordError" class="error-message">{{ newPasswordErrorMessage }}</div>
            </div>
            
            <div class="form-group">
              <label class="form-label dialog-label">确认新密码</label>
              <input 
                type="password" 
                v-model="passwordForm.confirmPassword" 
                class="form-input dialog-input"
                :class="{ 'form-input-error': confirmPasswordError }"
                placeholder="请再次输入新密码"
                maxlength="18"
                @blur="validateConfirm"
                @input="validateConfirm"
              />
              <div v-if="confirmPasswordError" class="error-message">{{ confirmPasswordErrorMessage }}</div>
            </div>
            
            <div class="password-tips">
              <span class="tip-text">密码建议：</span>
              <ul class="tip-list">
                <li>长度在6-18个字符之间</li>
                <li>必须同时包含字母和数字</li>
                <li>不要使用过于简单的密码</li>
              </ul>
            </div>
          </form>
        </div>
        
        <div class="dialog-footer">
          <button class="btn btn-cancel" @click="closePasswordDialog">取消</button>
          <button class="btn btn-confirm" @click="submitPasswordChange">确定</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 根容器样式 */
.profile-page {
  width: 100%;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: 40px 20px;
  box-sizing: border-box;
}

.profile-container {
  width: 100%;
  max-width: 800px;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 卡片样式 */
.profile-card {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  width: 100%;
  transform: translateY(0);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.profile-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 45px rgba(0, 0, 0, 0.2);
}

/* 卡片头部 */
.card-header {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  color: white;
  padding: 30px 40px;
  text-align: center;
}

.card-title {
  margin: 0 0 10px 0;
  font-size: 28px;
  font-weight: 600;
  letter-spacing: 0.5px;
}

.card-subtitle {
  margin: 0;
  font-size: 14px;
  opacity: 0.9;
  letter-spacing: 0.3px;
}

/* 卡片内容 */
.card-body {
  padding: 40px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* 头像部分 */
.avatar-section {
  margin-bottom: 40px;
  position: relative;
  transform: translateY(-70px);
  z-index: 1;
}

.avatar-wrapper {
  position: relative;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;
  border: 4px solid #fff;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}

.avatar-wrapper:hover {
  transform: scale(1.05);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
}

.avatar-wrapper:hover .avatar-overlay {
  opacity: 1;
}

.user-avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.avatar-wrapper:hover .user-avatar {
  transform: scale(1.1);
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.avatar-text {
  color: white;
  font-size: 12px;
  font-weight: 500;
  text-align: center;
  line-height: 1.4;
}

/* 表单部分 */
  .form-section {
    width: 100%;
    max-width: 500px;
    transform: translateY(-30px);
  }
  
  /* 表单行样式 */
  .form-row {
    margin-bottom: 24px;
  }
  
  /* 表单组样式 */
  .form-group {
    position: relative;
  }
  
  /* 表单输入错误状态 */
  .form-input-error {
    border-color: #f56c6c !important;
    box-shadow: 0 0 0 2px rgba(245, 108, 108, 0.2) !important;
  }
  
  .form-input-error:focus {
    border-color: #f56c6c !important;
    box-shadow: 0 0 0 2px rgba(245, 108, 108, 0.3) !important;
  }
  
  /* 错误提示样式 */
  .error-message {
    color: #f56c6c;
    font-size: 12px;
    line-height: 1;
    padding-top: 4px;
  }
  
  /* 重新添加form-group样式以确保完整性 */
  .form-group {
  position: relative;
}

.form-label {
  display: block;
  margin-bottom: 8px;
}

.label-text {
  display: block;
  font-weight: 600;
  color: #303133;
  font-size: 15px;
  margin-bottom: 4px;
}

.label-desc {
  display: block;
  font-size: 12px;
  color: #909399;
  font-weight: 400;
}

.form-input {
  width: 100%;
  height: 48px;
  padding: 0 16px;
  border: 2px solid #e4e7ed;
  border-radius: 8px;
  font-size: 15px;
  color: #303133;
  background-color: #fff;
  transition: all 0.3s ease;
  box-sizing: border-box;
}

.form-input:focus {
  outline: none;
  border-color: #409eff;
  box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.1);
}

.form-input::placeholder {
  color: #c0c4cc;
}

.form-input-readonly {
  background-color: #f8f9fa;
  color: #909399;
  cursor: not-allowed;
  border-color: #dcdfe6;
}

/* 卡片底部 */
.card-footer {
  background-color: #fafafa;
  padding: 24px 40px;
  border-top: 1px solid #ebeef5;
}

/* 按钮部分 */
.form-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
}

.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 44px;
  padding: 0 24px;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
  gap: 8px;
  letter-spacing: 0.5px;
}

.btn-save {
  background-color: #409eff;
  color: #fff;
}

.btn-save:hover:not(:disabled) {
  background-color: #66b1ff;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.btn-save:disabled {
  background-color: #c0c4cc;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.btn-password {
  background-color: #fff;
  color: #409eff;
  border: 2px solid #409eff;
}

.btn-password:hover {
  color: #fff;
  background-color: #409eff;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.btn-icon {
  display: inline-block;
  width: 18px;
  height: 18px;
}

/* 保存图标样式 */
.save-icon::before {
  content: '💾';
}

/* 密码图标样式 */
.password-icon::before {
  content: '🔒';
}

/* 修改密码弹窗样式 */
.password-dialog {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.dialog-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(4px);
  animation: maskFadeIn 0.3s ease;
}

@keyframes maskFadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.dialog-content {
  position: relative;
  width: 100%;
  max-width: 540px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 15px 50px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  animation: slideUp 0.3s ease;
  margin: 20px;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px 28px;
  border-bottom: 1px solid #ebeef5;
  background-color: #fafafa;
}

.dialog-title {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.dialog-close {
  width: 36px;
  height: 36px;
  padding: 0;
  border: none;
  background: none;
  color: #909399;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 6px;
  transition: all 0.3s ease;
  flex-shrink: 0;
}

.dialog-close:hover {
  background-color: #f5f7fa;
  color: #606266;
  transform: rotate(90deg);
}

.close-icon {
  width: 20px;
  height: 20px;
}

.dialog-body {
  padding: 30px 28px;
}

.dialog-label {
  margin-bottom: 10px;
}

.dialog-input {
  height: 46px;
  padding: 0 16px;
  border: 2px solid #e4e7ed;
  border-radius: 8px;
  font-size: 15px;
}

.password-tips {
  margin-top: 20px;
  padding: 16px;
  background-color: #f8f9fa;
  border-radius: 8px;
  border-left: 4px solid #409eff;
}

.tip-text {
  display: block;
  font-weight: 600;
  color: #303133;
  font-size: 14px;
  margin-bottom: 8px;
}

.tip-list {
  margin: 0;
  padding-left: 20px;
  font-size: 13px;
  color: #606266;
  line-height: 1.8;
}

.tip-list li {
  margin-bottom: 4px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 20px 28px;
  border-top: 1px solid #ebeef5;
  gap: 12px;
  background-color: #fafafa;
}

.btn-cancel {
  background-color: #fff;
  color: #606266;
  border: 2px solid #dcdfe6;
}

.btn-cancel:hover {
  color: #409eff;
  border-color: #409eff;
  background-color: #f5f7ff;
  transform: translateY(-1px);
}

.btn-confirm {
  background-color: #409eff;
  color: #fff;
  border: 2px solid #409eff;
}

.btn-confirm:hover {
  background-color: #66b1ff;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .profile-page {
    padding: 20px 10px;
  }
  
  .card-header {
    padding: 24px 20px;
  }
  
  .card-title {
    font-size: 24px;
  }
  
  .card-body {
    padding: 30px 20px;
  }
  
  .avatar-wrapper {
    width: 100px;
    height: 100px;
  }
  
  .form-section {
    max-width: 100%;
  }
  
  .form-input {
    height: 44px;
    padding: 0 14px;
    font-size: 14px;
  }
  
  .card-footer {
    padding: 20px;
  }
  
  .form-actions {
    flex-direction: column;
  }
  
  .btn {
    width: 100%;
    height: 44px;
    padding: 0 20px;
  }
  
  .dialog-content {
    margin: 10px;
  }
  
  .dialog-header,
  .dialog-body,
  .dialog-footer {
    padding: 20px;
  }
  
  .dialog-footer {
    flex-direction: column-reverse;
  }
  
  .btn-cancel,
  .btn-confirm {
    width: 100%;
  }
}

@media (max-width: 480px) {
  .card-title {
    font-size: 20px;
  }
  
  .avatar-section {
    transform: translateY(-60px);
  }
  
  .avatar-wrapper {
    width: 90px;
    height: 90px;
    border-width: 3px;
  }
  
  .form-section {
    transform: translateY(-20px);
  }
  
  .dialog-title {
    font-size: 18px;
  }
}

/* 动画效果 */
.form-input,
.btn {
  position: relative;
  overflow: hidden;
}

.form-input::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 5px;
  height: 5px;
  background: rgba(255, 255, 255, 0.5);
  opacity: 0;
  border-radius: 100%;
  transform: scale(1, 1) translate(-50%, -50%);
  transform-origin: 50% 50%;
}

.form-input:focus::after {
  animation: ripple 0.5s ease-out;
}

@keyframes ripple {
  0% {
    transform: scale(0, 0);
    opacity: 0.5;
  }
  100% {
    transform: scale(200, 200);
    opacity: 0;
  }
}

.btn::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 5px;
  height: 5px;
  background: rgba(255, 255, 255, 0.5);
  opacity: 0;
  border-radius: 100%;
  transform: scale(1, 1) translate(-50%, -50%);
  transform-origin: 50% 50%;
}

.btn:hover::after {
  animation: ripple 0.5s ease-out;
}
</style>
</style>