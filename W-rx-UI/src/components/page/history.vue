<script>
import { ajaxGet } from '../../api/index';

export default {
  data() {
    return {
      historyList: [],
      total: 0,
      currentPage: 1,
      pageSize: 10,
      loading: false
    };
  },
  created() {
    // 页面加载时获取历史入住记录
    this.getHistoryList();
  },
  methods: {
    async getHistoryList() {
      this.loading = true;
      try {
        // 从sessionStorage获取用户信息
        const userSession = sessionStorage.getItem('user');
        if (!userSession) {
          this.$message.error('用户未登录，请重新登录');
          this.$router.push('/login');
          return;
        }
        
        const userData = JSON.parse(userSession);
        
        // 构建查询参数
        const params = {
          customerId: userData.employeeId,
          page: this.currentPage,
          pageSize: this.pageSize
        };
        
        // 调用后端API获取历史记录
        const response = await ajaxGet('/customer/getHistory', params);
        // 处理响应数据
        if (response && response.records) {
          this.historyList = response.records;
          this.total = response.total || 0;
        }
      } catch (error) {
        console.error('获取历史入住记录失败:', error);
        this.$message.error('获取历史记录失败，请稍后重试');
      } finally {
        this.loading = false;
      }
    },
    
    // 分页改变事件处理
    handlePageChange(page) {
      this.currentPage = page;
      this.getHistoryList();
    }
  }
};
</script>

<template>
  <div class="history-container">
    <div class="history-content" v-loading="loading">
      <!-- 历史记录表格 -->
      <table class="history-table">
        <thead>
          <tr>
            <th>订单编号</th>
            <th>客户姓名</th>
            <th>客房类型</th>
            <th>房间号</th>
            <th>入住时间</th>
            <th>退房时间</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in historyList" :key="item.checkInId">
            <td>{{ item.checkInId }}</td>
            <td>{{ item.customerName }}</td>
            <td>{{ item.roomType }}</td>
            <td>{{ item.roomNumber }}</td>
            <td>{{ item.actualCheckIn }}</td>
            <td>{{ item.actualCheckOut || '入住中' }}</td>
          </tr>
          <tr v-if="historyList.length === 0">
            <td colspan="6" class="empty-message">暂无历史入住记录</td>
          </tr>
        </tbody>
      </table>
      
      <!-- 分页控件 -->
      <div class="pagination" v-if="total > 0">
        <div class="page-info">
          <span>共 {{ total }} 条</span>
        </div>
        <div class="page-controls">
          <button 
            class="page-btn" 
            :disabled="currentPage === 1"
            @click="handlePageChange(currentPage - 1)"
          >
            &lt;
          </button>
          <button 
            v-for="page in Math.min(5, Math.ceil(total / pageSize))"
            :key="page"
            class="page-btn"
            :class="{ active: currentPage === page }"
            @click="handlePageChange(page)"
          >
            {{ page }}
          </button>
          <button 
            class="page-btn" 
            :disabled="currentPage >= Math.ceil(total / pageSize)"
            @click="handlePageChange(currentPage + 1)"
          >
            &gt;
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.history-container {
  min-height: calc(100vh - 60px);
  background-color: #f5f7fa;
  padding: 20px;
}

.history-content {
  max-width: 1200px;
  margin: 0 auto;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.history-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.history-table th,
.history-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ebeef5;
}

.history-table th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #606266;
  font-size: 14px;
}

.history-table td {
  color: #303133;
  font-size: 14px;
}

.history-table tr:hover {
  background-color: #f5f7fa;
}

.empty-message {
  text-align: center;
  color: #909399;
  padding: 40px 0;
}

.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
}

.page-info {
  color: #909399;
  font-size: 14px;
}

.page-controls {
  display: flex;
  gap: 5px;
}

.page-btn {
  padding: 5px 12px;
  border: 1px solid #dcdfe6;
  background-color: #fff;
  color: #606266;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.3s;
  font-size: 14px;
}

.page-btn:hover:not(:disabled) {
  border-color: #409eff;
  color: #409eff;
}

.page-btn.active {
  background-color: #409eff;
  color: #fff;
  border-color: #409eff;
}

.page-btn:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .history-container {
    padding: 10px;
  }
  
  .history-content {
    padding: 10px;
  }
  
  .history-table {
    font-size: 12px;
  }
  
  .history-table th,
  .history-table td {
    padding: 8px;
  }
  
  .pagination {
    flex-direction: column;
    gap: 10px;
    align-items: center;
  }
}
</style>