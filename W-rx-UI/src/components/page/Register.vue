<template>
  <div class="login-wrap">
    <div class="login-bg-overlay"></div>
    <div class="ms-login">
      <div class="ms-title">飛鱼酒店客户注册</div>
      <el-form :model="param" :rules="rules" ref="login" label-width="0px" class="ms-content">
        <el-form-item prop="name">
          <el-input v-model="param.name" placeholder="姓名" class="login-input" prefix-icon="el-icon-lx-people"></el-input>
        </el-form-item>
        <el-form-item prop="customerId">
          <el-input v-model="param.customerId" placeholder="账号" class="login-input" prefix-icon="el-icon-lx-people"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" placeholder="密码" v-model="param.password" show-password class="login-input" prefix-icon="el-icon-lx-lock"></el-input>
        </el-form-item>
        <el-form-item prop="password_repeat">
          <el-input type="password" placeholder="确认密码" v-model="param.password_repeat" show-password
                    @keyup.enter.native="handleRegister()" class="login-input" prefix-icon="el-icon-lx-lock"></el-input>
        </el-form-item>
        <el-form-item prop="phone">
          <el-input v-model="param.phone" placeholder="手机号" class="login-input" prefix-icon="el-icon-phone"></el-input>
        </el-form-item>
        <el-select v-model="param.role" placeholder="请选择角色" class="role-select">
          <el-option label="客户" value="customer"/>
        </el-select>
        <div class="login-btn">
          <el-button type="primary" :loading="loginLoading" @click="handleRegister()" class="submit-btn">注册</el-button>
        </div>
        <div class="login-footer">
          <span class="register-link" @click="goToLogin()">返回登录</span>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import {
  ajaxGet,
  ajaxPost
} from "../../api/index";
import{getDynamicMenu} from  "../../router/dynamicMenu.js";
export default {
  data: function() {
    return {
      flag: false,
      loginLoading:false,
      param: {
        name: '',
        customerId: '',
        password: '',
        password_repeat: '',
        phone: '',
        role: '',
      },
      rules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' },
          { min: 2, max: 20, message: '姓名长度应在2-20个字符之间', trigger: 'blur' }
        ],
        customerId: [
          { required: true, message: '请输入账号', trigger: 'blur' },
          { min: 3, max: 8, message: '账号长度应在3-8个字符之间', trigger: 'blur' },
          { pattern: /^[a-zA-Z]+$/, message: '账号只能由字母组成', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 18, message: '密码长度应在6-18个字符之间', trigger: 'blur' },
          { pattern: /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6,18}$/, message: '密码必须由字母和数字组成', trigger: 'blur' }
        ],
        password_repeat: [
          { required: true, message: '请确认密码', trigger: ['blur', 'input'] },
          { validator: (rule, value, callback) => {
              if (value !== '' && value !== this.param.password) {
                callback(new Error('两次输入的密码不一致'));
              } else {
                callback();
              }
            }, trigger: ['blur', 'input'] }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号', trigger: 'blur' }
        ],
        role: [
          { required: true, message: '请选择角色', trigger: 'change' }
        ]
      },
    };
  },

  methods: {
      submitFormCustomer() {
        this.$refs.login.validate(valid => {
          if (valid) {
            this.loginLoading = true;
            
            // 调用客户注册接口
            ajaxPost('/customer/register', this.param).then(res => {
              if (res && res.success) {
                this.$message.success('注册成功！即将跳转到登录页面');
                // 注册成功后跳转到登录页面
                setTimeout(() => {
                  this.$router.push('/Login');
                }, 1000);
              }
            })
            .catch(error => {
              console.error('注册请求失败:', error);
              this.$message.error('网络错误，请稍后重试');
            })
            .finally(() => {
              this.loginLoading = false;
            });
          } else {
            console.log('表单验证失败');
            return false;
          }
        });
      },
      goToLogin() {
        this.$router.push('/Login');
      },
      handleRegister() {
        switch (this.param.role) {
          case 'customer':
            this.submitFormCustomer();
            break;
          default:
            this.$message.warning('请选择有效角色');
        }
      }
  },
};
</script>

<style scoped>
.login-wrap {
  position: relative;
  width: 100%;
  height: 100vh;
  background-image: url(../../assets/img/login-bg.jpg);
  background-size: cover;
  background-position: center;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-bg-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(5px);
}

.ms-login {
  position: relative;
  width: 400px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.95);
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
}

.ms-login:hover {
  box-shadow: 0 12px 48px rgba(0, 0, 0, 0.3);
  transform: translateY(-2px);
}

.ms-title {
  width: 100%;
  line-height: 60px;
  text-align: center;
  font-size: 22px;
  font-weight: bold;
  color: #1890ff;
  background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%);
  color: white;
  letter-spacing: 2px;
}

.ms-content {
  padding: 40px 30px;
}

.login-input {
  margin-bottom: 20px;
  transition: all 0.3s ease;
}

.login-input:hover {
  border-color: #1890ff;
}

.role-select {
  margin-bottom: 20px;
  width: 100%;
}

.login-btn {
  text-align: center;
  margin-bottom: 20px;
}

.submit-btn {
  width: 100%;
  height: 40px;
  font-size: 16px;
  transition: all 0.3s ease;
}

.submit-btn:hover {
  transform: scale(1.02);
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.3);
}

.login-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  padding-top: 10px;
  border-top: 1px solid #f0f0f0;
}

.register-link {
  color: #1890ff;
  cursor: pointer;
  transition: all 0.3s ease;
}

.register-link:hover {
  color: #40a9ff;
  text-decoration: underline;
}

.forget-password {
  color: #909399;
  cursor: pointer;
  transition: all 0.3s ease;
}

.forget-password:hover {
  color: #606266;
}

/* 响应式设计 */
@media (max-width: 450px) {
  .ms-login {
    width: 90%;
    margin: 0 10px;
  }

  .ms-content {
    padding: 30px 20px;
  }
}
</style>
