<template>
  <div class="home-employee">
    <div class="employee-header">
      <h2>欢迎回来，{{ userName }}</h2>
    </div>
    <div class="employee-content">
      <el-card class="employee-info-card">
        <div slot="header" class="card-header">
          <span>个人信息</span>
        </div>
        <div class="employee-info">
          <p><strong>账号：</strong>{{ userInfo.jobId }}</p>
          <p><strong>用户名：</strong>{{ userInfo.username }}</p>
          <p><strong>角色：</strong>{{ userRoles }}</p>
        </div>
      </el-card>
      
      <el-card class="quick-actions">
        <div slot="header" class="card-header">
          <span>快捷操作</span>
        </div>
        <div class="action-buttons">
          <el-button type="primary" icon="el-icon-employee">个人中心</el-button>
          <el-button type="success" icon="el-icon-document">我的任务</el-button>
          <el-button type="info" icon="el-icon-setting">系统设置</el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HomeUser',
  data() {
    return {
      userInfo: {},
      userName: '',
      userRoles: ''
    }
  },
  mounted() {
    this.loadUserInfo();
  },
  methods: {
    loadUserInfo() {
      // 从sessionStorage获取用户信息
      const userStr = sessionStorage.getItem('employee');
      if (userStr) {
        const employee = JSON.parse(userStr);
        this.userInfo = employee;
        this.userName = employee.username || '用户';
        this.userRoles = employee.roles ? employee.roles.join('、') : '普通用户';
      }
    }
  }
}
</script>

<style scoped>
.home-employee {
  padding: 20px;
  min-height: calc(100vh - 60px);
  background-color: #f5f7fa;
}

.employee-header {
  margin-bottom: 20px;
}

.employee-content {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.employee-info-card,
.quick-actions {
  flex: 1;
  min-width: 300px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.employee-info {
  padding: 10px 0;
}

.employee-info p {
  margin: 10px 0;
  line-height: 1.5;
}

.action-buttons {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

.action-buttons .el-button {
  flex: 1;
  min-width: 120px;
}
</style>