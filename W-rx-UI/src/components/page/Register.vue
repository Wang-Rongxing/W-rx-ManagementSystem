<template>
  <div class="login-wrap">
    <div class="login-bg-overlay"></div>
    <div class="ms-login">
      <div class="ms-title">飛鱼酒店客户注册</div>
      <el-form :model="param" :rules="rules" ref="login" label-width="0px" class="ms-content">
        <el-form-item prop="jobId">
          <el-input v-model="param.jobId" placeholder="账号" class="login-input">
            <el-button slot="prepend" icon="el-icon-lx-people" plain circle></el-button>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" placeholder="密码" v-model="param.password" show-password class="login-input">
            <el-button slot="prepend" icon="el-icon-lx-lock" plain circle></el-button>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" placeholder="确认密码" v-model="param.password" show-password
                    @keyup.enter.native="handleLogin()" class="login-input">
            <el-button slot="prepend" icon="el-icon-lx-lock" plain circle></el-button>
          </el-input>
        </el-form-item>
        <el-select v-model="param.role" placeholder="请选择角色" class="role-select">
          <el-option label="客户" value="customer"/>
        </el-select>
        <div class="login-btn">
          <el-button type="primary" :loading="loginLoading" @click="handleLogin()" class="submit-btn">登录</el-button>
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
        jobId: '',
        password: '',
        role: '',
      },
      rules: {
        jobId: [{
          required: true,
          message: '请输入工号',
          trigger: 'blur'
        }],
        password: [{
          required: true,
          message: '请输入密码',
          trigger: 'blur'
        }],
      },
    };
  },

  methods: {
    submitFormCustomer() {
      //location.reload();
      this.$refs.login.validate(valid => {
        if (valid) {
          this.loginLoading=true;


          ajaxPost('/user/login', this.param).then(res => {
            this.flag = res ? true : false;
            if (this.flag) {

              //localStorage.setItem('user', JSON.stringify(res));
              if(res.roles!=null&&res.roles.length>0){

                sessionStorage.setItem('user', JSON.stringify(res));
                this.$store.commit('setRoles',res.roles);
                // this.$store.commit('setFlag', true);
                getDynamicMenu();
                // console.log(res.roles);
                this.$message.success('登录成功');
                //console.log(localStorage.getItem("user"));
                //localStorage.setItem("ms_username",res.data.username);
                this.$router.push('/HomeUser/home');
              }else{
                this.$message.error({message:'您没有权限访问系统',center: true});
              }

            } else {
              this.$message.error({message:'工号或密码错误',center: true});

              return false;
            }
          })
              .catch(error => {
                this.loginLoading=false;

              });
        } else {
          //this.$message.error('请输入账号和密码');
          console.log('error submit!!');
          return false;
        }
      });
    },
    goToLogin(){
      this.$router.push('/Login');
    },
    handleLogin() {
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
