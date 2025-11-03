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
      // 密码修改相关数据
      passwordVisible: false,
      passwordForm: {
        newPassword: '',
        confirmPassword: ''
      },
      passwordRules: {
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度在6-20个字符之间', trigger: 'blur' },
          { pattern: /^[a-zA-Z0-9_\-\.]{6,20}$/, message: '密码只能包含字母、数字、下划线、横线和点', trigger: 'blur' }
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
      
      if (!this.userInfo.phone || !/^1[3-9]\d{9}$/.test(this.userInfo.phone)) {
        this.$message.error('请输入正确的手机号码');
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
    
    // 验证确认密码
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
      // 重置表单验证状态
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
      // 执行基本的表单验证
      if (!this.passwordForm.newPassword) {
        this.$message.error('请输入新密码');
        return;
      }
      
      if (this.passwordForm.newPassword.length < 6 || this.passwordForm.newPassword.length > 20) {
        this.$message.error('密码长度在6-20个字符之间');
        return;
      }
      
      if (!/^[a-zA-Z0-9_\-\.]{6,20}$/.test(this.passwordForm.newPassword)) {
        this.$message.error('密码只能包含字母、数字、下划线、横线和点');
        return;
      }
      
      if (!this.passwordForm.confirmPassword) {
        this.$message.error('请确认新密码');
        return;
      }
      
      if (this.passwordForm.newPassword !== this.passwordForm.confirmPassword) {
        this.$message.error('两次输入的密码不一致');
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
    }
  }
};
</script>

<template>
  <div class="profile-page">
    <div class="profile-container">
      <div class="profile-form" v-loading="loading">
        <!-- 头像部分 -->
        <div class="avatar-section">
          <div class="avatar-wrapper" @click="chooseAvatar">
            <img src="../../assets/img/img.jpg" alt="用户头像" class="user-avatar" />
          </div>
        </div>
        
        <!-- 表单部分 -->
        <div class="form-section">
          <div class="form-group">
            <label>用户名</label>
            <input 
              type="text" 
              v-model="userInfo.customerId"
              class="form-input" 
              readonly
            />
          </div>
          
          <div class="form-group">
            <label>姓名</label>
            <input 
              type="text" 
              v-model="userInfo.name" 
              class="form-input"
              placeholder="请输入姓名"
            />
          </div>
          
          <div class="form-group">
            <label>电话</label>
            <input 
              type="tel" 
              v-model="userInfo.phone" 
              class="form-input"
              placeholder="请输入手机号码"
            />
          </div>
          

          
          <!-- 按钮部分 -->
          <div class="form-actions">
            <button 
              class="btn-save" 
              @click="saveProfile" 
              :disabled="isSubmitting"
            >
              {{ isSubmitting ? '保存中...' : '保存' }}
            </button>
            <button class="btn-password" @click="changePassword">
              修改密码
            </button>
          </div>
        </div>
      </div>
    </div>
      
    <!-- 修改密码弹窗 -->
    <div class="password-dialog" v-if="passwordVisible">
      <div class="dialog-mask" @click="closePasswordDialog"></div>
      <div class="dialog-content">
        <div class="dialog-header">
          <h3>修改密码</h3>
          <button class="dialog-close" @click="closePasswordDialog">&times;</button>
        </div>
        
        <div class="dialog-body">
          <form ref="passwordForm">
            <div class="form-group">
              <label>新密码</label>
              <input 
                type="password" 
                v-model="passwordForm.newPassword" 
                class="form-input"
                placeholder="请输入新密码（6-20位字母、数字、下划线）"
              />
            </div>
            
            <div class="form-group">
              <label>确认新密码</label>
              <input 
                type="password" 
                v-model="passwordForm.confirmPassword" 
                class="form-input"
                placeholder="请再次输入新密码"
              />
            </div>
          </form>
        </div>
        
        <div class="dialog-footer">
          <button class="btn-cancel" @click="closePasswordDialog">取消</button>
          <button class="btn-confirm" @click="submitPasswordChange">确定</button>
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
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: 20px;
  box-sizing: border-box;
}

.profile-container {
  min-height: calc(100vh - 60px);
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
  padding: 20px;
}

.profile-form {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 40px;
  width: 100%;
  max-width: 600px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* 头像部分 */
.avatar-section {
  margin-bottom: 30px;
}

.avatar-wrapper {
  position: relative;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;
  border: 2px solid #e4e7ed;
  transition: border-color 0.3s;
}

.avatar-wrapper:hover {
  border-color: #409eff;
}

.user-avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 表单部分 */
.form-section {
  width: 100%;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #303133;
  font-size: 14px;
}

.form-input {
  width: 100%;
  height: 40px;
  padding: 0 15px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
  color: #606266;
  background-color: #fff;
  transition: border-color 0.3s;
}

.form-input:focus {
  outline: none;
  border-color: #409eff;
}

.form-input:read-only {
  background-color: #f5f7fa;
  cursor: not-allowed;
}

/* 按钮部分 */
.form-actions {
  display: flex;
  gap: 16px;
  margin-top: 30px;
}

.btn-save,
.btn-password {
  height: 40px;
  padding: 0 20px;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
  border: none;
}

.btn-save {
  background-color: #409eff;
  color: #fff;
}

.btn-save:hover:not(:disabled) {
  background-color: #66b1ff;
}

.btn-save:disabled {
  background-color: #c0c4cc;
  cursor: not-allowed;
}

.btn-password {
  background-color: #fff;
  color: #409eff;
  border: 1px solid #dcdfe6;
}

.btn-password:hover {
  color: #66b1ff;
  border-color: #c6e2ff;
  background-color: #ecf5ff;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .profile-form {
    padding: 30px 20px;
  }
  
  .avatar-wrapper {
    width: 80px;
    height: 80px;
  }
  
  .form-actions {
    flex-direction: column;
  }
  
  .btn-save,
  .btn-password {
    width: 100%;
  }
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
}

.dialog-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}

.dialog-content {
  position: relative;
  width: 100%;
  max-width: 500px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  overflow: hidden;
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #ebeef5;
}

.dialog-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 500;
  color: #303133;
}

.dialog-close {
  width: 32px;
  height: 32px;
  padding: 0;
  border: none;
  background: none;
  font-size: 24px;
  color: #909399;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  transition: all 0.3s;
}

.dialog-close:hover {
  background-color: #f5f7fa;
  color: #606266;
}

.dialog-body {
  padding: 24px;
}

.dialog-body .form-group {
  margin-bottom: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 16px 24px;
  border-top: 1px solid #ebeef5;
  gap: 12px;
}

.btn-cancel,
.btn-confirm {
  height: 36px;
  padding: 0 20px;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #dcdfe6;
}

.btn-cancel {
  background-color: #fff;
  color: #606266;
}

.btn-cancel:hover {
  color: #409eff;
  border-color: #c6e2ff;
  background-color: #ecf5ff;
}

.btn-confirm {
  background-color: #409eff;
  color: #fff;
  border-color: #409eff;
}

.btn-confirm:hover {
  background-color: #66b1ff;
  border-color: #66b1ff;
}

/* 响应式弹窗样式 */
@media (max-width: 768px) {
  .dialog-content {
    width: 90%;
    margin: 0 auto;
  }
  
  .dialog-header,
  .dialog-body,
  .dialog-footer {
    padding: 16px;
  }
  
  .dialog-footer {
    flex-direction: column-reverse;
  }
  
  .btn-cancel,
  .btn-confirm {
    width: 100%;
  }
}
</style>