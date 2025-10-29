<template>
  <div class="home-user">
    <div class="user-header">
      <h2>欢迎回来，{{ userName }}</h2>
    </div>
    <div class="user-content">
      <el-card class="user-info-card">
        <div slot="header" class="card-header">
          <span>个人信息</span>
        </div>
        <div class="user-info">
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
          <el-button type="primary" icon="el-icon-user">个人中心</el-button>
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
      const userStr = sessionStorage.getItem('user');
      if (userStr) {
        const user = JSON.parse(userStr);
        this.userInfo = user;
        this.userName = user.username || '用户';
        this.userRoles = user.roles ? user.roles.join('、') : '普通用户';
      }
    }
  }
}
</script>

<style scoped>
.home-user {
  padding: 20px;
  min-height: calc(100vh - 60px);
  background-color: #f5f7fa;
}

.user-header {
  margin-bottom: 20px;
}

.user-content {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.user-info-card,
.quick-actions {
  flex: 1;
  min-width: 300px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-info {
  padding: 10px 0;
}

.user-info p {
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