<script>
import { ajaxGet } from '../../api/index';

export default {
  data() {
    return {
      historyList: [],
      total: 0,
      currentPage: 1,
      pageSize: 10,
      loading: false,
      jumpPage: 1
    };
  },
  created() {
    // 页面加载时获取历史入住记录
    this.getHistoryList();
  },
  computed: {
    // 计算总页数
    totalPages() {
      return Math.ceil(this.total / this.pageSize);
    },
    
    // 计算显示的页码范围
    displayPages() {
      const pages = [];
      let startPage = Math.max(2, this.currentPage - 1);
      let endPage = Math.min(this.totalPages - 1, this.currentPage + 1);
      
      // 调整起始页，确保显示3个页码（除非总页数较少）
      if (endPage - startPage < 2) {
        if (startPage === 2 && endPage < this.totalPages - 1) {
          endPage = Math.min(startPage + 2, this.totalPages - 1);
        } else if (endPage === this.totalPages - 1 && startPage > 2) {
          startPage = Math.max(endPage - 2, 2);
        }
      }
      
      for (let i = startPage; i <= endPage; i++) {
        pages.push(i);
      }
      
      return pages;
    },
    
    // 是否有正在入住的记录
    hasActiveCheckins() {
      return this.historyList.some(item => !item.actualCheckOut);
    },
    
    // 计算入住中的记录数量
    activeCheckinsCount() {
      return this.historyList.filter(item => !item.actualCheckOut).length;
    }
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
      if (page < 1 || page > this.totalPages) return;
      this.currentPage = page;
      this.jumpPage = page; // 同步跳转页码输入框
      this.getHistoryList();
    },
    
    // 页码跳转
    goToPage() {
      let page = parseInt(this.jumpPage);
      if (isNaN(page) || page < 1) {
        page = 1;
      } else if (page > this.totalPages) {
        page = this.totalPages;
      }
      this.jumpPage = page;
      this.handlePageChange(page);
    },
    
    // 格式化日期显示
    formatDate(dateStr) {
      if (!dateStr) return '';
      
      const date = new Date(dateStr);
      if (isNaN(date.getTime())) return dateStr;
      
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      
      return `${year}-${month}-${day} ${hours}:${minutes}`;
    }
  }
};
</script>

<template>
  <div class="history-container">
    <div class="history-header">
      <h1 class="page-title">历史入住记录</h1>
      <p class="page-subtitle">查看您的所有入住历史信息</p>
    </div>
    
    <div class="history-content" v-loading="loading">
      <!-- 卡片式容器 -->
      <div class="data-card">
        <!-- 表格头部区域 -->
        <div class="table-header">
          <h2 class="card-title">入住记录列表</h2>
          <div class="record-count">共 {{ total }} 条记录</div>
        </div>
        
        <!-- 历史记录表格 -->
        <div class="table-wrapper">
          <table class="history-table">
            <thead>
              <tr>
                <th class="table-header-cell">
                  <span class="header-text">订单编号</span>
                </th>
                <th class="table-header-cell">
                  <span class="header-text">客户姓名</span>
                </th>
                <th class="table-header-cell">
                  <span class="header-text">客房类型</span>
                </th>
                <th class="table-header-cell">
                  <span class="header-text">房间号</span>
                </th>
                <th class="table-header-cell">
                  <span class="header-text">入住时间</span>
                </th>
                <th class="table-header-cell">
                  <span class="header-text">退房时间</span>
                  <span class="status-badge" v-if="hasActiveCheckins">
                    有 {{ activeCheckinsCount }} 条入住中
                  </span>
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in historyList" :key="item.checkInId" class="table-row">
                <td class="table-cell">
                  <span class="order-id">{{ item.checkInId }}</span>
                </td>
                <td class="table-cell">
                  <div class="customer-info">
                    <span class="customer-name">{{ item.customerName }}</span>
                  </div>
                </td>
                <td class="table-cell">
                  <span class="room-type">{{ item.roomType }}</span>
                </td>
                <td class="table-cell">
                  <span class="room-number">{{ item.roomNumber }}</span>
                </td>
                <td class="table-cell">
                  <span class="date-text">{{ formatDate(item.actualCheckIn) }}</span>
                </td>
                <td class="table-cell">
                  <div class="checkout-status">
                    <span 
                      class="checkout-time"
                      :class="{ 'checkout-active': !item.actualCheckOut }"
                    >
                      {{ item.actualCheckOut ? formatDate(item.actualCheckOut) : '入住中' }}
                    </span>
                    <span v-if="!item.actualCheckOut" class="status-badge active">
                    </span>
                  </div>
                </td>
              </tr>
              <tr v-if="historyList.length === 0">
                <td colspan="6" class="empty-message">
                  <div class="empty-state">
                    <div class="empty-icon">📋</div>
                    <h3 class="empty-title">暂无历史入住记录</h3>
                    <p class="empty-description">您还没有任何入住历史</p>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      
      <!-- 分页控件 -->
      <div class="pagination" v-if="total > 0">
        <div class="pagination-info">
          <span>显示第 {{ (currentPage - 1) * pageSize + 1 }} 至 
            {{ Math.min(currentPage * pageSize, total) }} 条，共 {{ total }} 条</span>
        </div>
        <div class="page-controls">
          <button 
            class="page-btn page-prev" 
            :disabled="currentPage === 1"
            @click="handlePageChange(currentPage - 1)"
            title="上一页"
          >
            <svg class="icon" viewBox="0 0 16 16" width="16" height="16">
              <path fill="currentColor" d="M11.354 1.646a.5.5 0 0 1 0 .708L5.707 8l5.647 5.646a.5.5 0 0 1-.708.708l-6-6a.5.5 0 0 1 0-.708l6-6a.5.5 0 0 1 .708 0z"/>
            </svg>
            <span>上一页</span>
          </button>
          
          <!-- 页码显示逻辑 -->
          <template v-if="totalPages > 1">
            <!-- 第一页 -->
            <button 
              class="page-btn page-number"
              :class="{ active: currentPage === 1 }"
              @click="handlePageChange(1)"
            >
              1
            </button>
            
            <!-- 省略号逻辑 -->
            <span v-if="currentPage > 4" class="page-ellipsis">...</span>
            
            <!-- 中间页码 -->
            <button 
              v-for="page in displayPages"
              :key="page"
              class="page-btn page-number"
              :class="{ active: currentPage === page }"
              @click="handlePageChange(page)"
            >
              {{ page }}
            </button>
            
            <!-- 省略号逻辑 -->
            <span v-if="currentPage < totalPages - 3" class="page-ellipsis">...</span>
            
            <!-- 最后一页 -->
            <button 
              v-if="totalPages > 1"
              class="page-btn page-number"
              :class="{ active: currentPage === totalPages }"
              @click="handlePageChange(totalPages)"
            >
              {{ totalPages }}
            </button>
          </template>
          
          <button 
            class="page-btn page-next" 
            :disabled="currentPage >= totalPages"
            @click="handlePageChange(currentPage + 1)"
            title="下一页"
          >
            <span>下一页</span>
            <svg class="icon" viewBox="0 0 16 16" width="16" height="16">
              <path fill="currentColor" d="M4.646 1.646a.5.5 0 0 1 .708 0l6 6a.5.5 0 0 1 0 .708l-6 6a.5.5 0 0 1-.708-.708L10.293 8 4.646 2.354a.5.5 0 0 1 0-.708z"/>
            </svg>
          </button>
          
          <!-- 页码跳转 -->
          <div class="page-jump">
            <span>前往</span>
            <input 
              type="number" 
              v-model.number="jumpPage" 
              class="page-jump-input"
              :min="1" 
              :max="totalPages"
              @keyup.enter="goToPage"
            />
            <span>页</span>
            <button class="page-jump-btn" @click="goToPage">跳转</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 根容器样式 */
.history-container {
  min-height: calc(100vh - 60px);
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* 页面头部 */
.history-header {
  max-width: 1200px;
  margin: 0 auto 24px;
  text-align: center;
}

.page-title {
  margin: 0 0 8px;
  font-size: 32px;
  font-weight: 700;
  color: #303133;
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.page-subtitle {
  margin: 0;
  font-size: 16px;
  color: #909399;
}

/* 内容区域 */
.history-content {
  max-width: 1200px;
  margin: 0 auto;
}

/* 数据卡片 */
.data-card {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: box-shadow 0.3s ease;
}

.data-card:hover {
  box-shadow: 0 15px 45px rgba(0, 0, 0, 0.15);
}

/* 表格头部 */
.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px 30px;
  border-bottom: 1px solid #ebeef5;
  background-color: #fafafa;
}

.card-title {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.record-count {
  font-size: 14px;
  color: #606266;
  padding: 6px 12px;
  background-color: #ecf5ff;
  border-radius: 20px;
  border: 1px solid #d9ecff;
}

/* 表格容器 */
.table-wrapper {
  overflow-x: auto;
  scrollbar-width: thin;
  scrollbar-color: #c0c4cc #f0f0f0;
}

.table-wrapper::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.table-wrapper::-webkit-scrollbar-track {
  background: #f0f0f0;
  border-radius: 4px;
}

.table-wrapper::-webkit-scrollbar-thumb {
  background-color: #c0c4cc;
  border-radius: 4px;
}

.table-wrapper::-webkit-scrollbar-thumb:hover {
  background-color: #909399;
}

/* 表格样式 */
.history-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

/* 表头单元格 */
.table-header-cell {
  padding: 16px 30px;
  text-align: left;
  background-color: #f8f9fa;
  font-weight: 600;
  color: #606266;
  border-bottom: 2px solid #ebeef5;
  position: relative;
  white-space: nowrap;
}

.header-text {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 表格行 */
.table-row {
  transition: all 0.3s ease;
  border-bottom: 1px solid #f0f2f5;
}

.table-row:hover {
  background-color: #f5f7fa;
  transform: translateY(-1px);
}

.table-row:last-child {
  border-bottom: none;
}

/* 表格单元格 */
.table-cell {
  padding: 16px 30px;
  color: #303133;
  position: relative;
  white-space: nowrap;
}

/* 订单ID样式 */
.order-id {
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 13px;
  color: #606266;
  background-color: #f4f4f5;
  padding: 2px 8px;
  border-radius: 4px;
  border: 1px solid #ebeef5;
}

/* 客户信息 */
.customer-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.customer-name {
  font-weight: 500;
  color: #303133;
}

/* 房间类型 */
.room-type {
  display: inline-block;
  padding: 4px 12px;
  background-color: #f0f9ff;
  color: #1989fa;
  border-radius: 4px;
  font-size: 13px;
  border: 1px solid #e0f2fe;
}

/* 房间号 */
.room-number {
  font-weight: 600;
  color: #303133;
  font-size: 15px;
}

/* 日期文本 */
.date-text {
  color: #606266;
  font-size: 13px;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
}

/* 退房状态 */
.checkout-status {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.checkout-time {
  color: #606266;
  font-size: 13px;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
}

.checkout-active {
  color: #67c23a;
  font-weight: 600;
}

/* 状态标签 */
.status-badge {
  display: inline-flex;
  align-items: center;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 11px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.status-badge.active {
  background-color: #f0f9ff;
  color: #1989fa;
  border: 1px solid #e0f2fe;
}

/* 空状态 */
.empty-message {
  text-align: center;
  padding: 80px 20px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.empty-icon {
  font-size: 48px;
  opacity: 0.5;
}

.empty-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #909399;
}

.empty-description {
  margin: 0;
  font-size: 14px;
  color: #c0c4cc;
}

/* 分页样式 */
.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.pagination-info {
  font-size: 14px;
  color: #909399;
  background-color: #fafafa;
  padding: 8px 16px;
  border-radius: 6px;
  border: 1px solid #ebeef5;
}

.page-controls {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.page-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 8px 16px;
  border: 1px solid #dcdfe6;
  background-color: #fff;
  color: #606266;
  cursor: pointer;
  border-radius: 6px;
  transition: all 0.3s ease;
  font-size: 14px;
  min-width: 40px;
  height: 40px;
}

.page-btn:hover:not(:disabled) {
  border-color: #409eff;
  color: #409eff;
  background-color: #f5f7fa;
  transform: translateY(-1px);
}

.page-btn.active {
  background-color: #409eff;
  color: #fff;
  border-color: #409eff;
  font-weight: 600;
}

.page-btn:disabled {
  cursor: not-allowed;
  opacity: 0.5;
  background-color: #f5f7fa;
  color: #c0c4cc;
  border-color: #ebeef5;
  transform: none;
}

.page-prev, .page-next {
  font-weight: 500;
}

/* 图标样式 */
.icon {
  width: 16px;
  height: 16px;
  flex-shrink: 0;
}

/* 省略号 */
.page-ellipsis {
  padding: 0 8px;
  color: #909399;
  font-size: 14px;
}

/* 页码跳转 */
.page-jump {
  display: flex;
  align-items: center;
  gap: 8px;
  background-color: #fafafa;
  padding: 6px 12px;
  border-radius: 6px;
  border: 1px solid #ebeef5;
}

.page-jump span {
  color: #606266;
  font-size: 14px;
}

.page-jump-input {
  width: 60px;
  height: 32px;
  padding: 0 8px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  text-align: center;
  font-size: 14px;
  transition: border-color 0.3s ease;
}

.page-jump-input:focus {
  outline: none;
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
}

.page-jump-btn {
  padding: 6px 12px;
  border: 1px solid #dcdfe6;
  background-color: #fff;
  color: #606266;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.3s ease;
  font-size: 14px;
}

.page-jump-btn:hover {
  border-color: #409eff;
  color: #409eff;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .table-header-cell,
  .table-cell {
    padding: 16px 20px;
  }
}

@media (max-width: 768px) {
  .history-container {
    padding: 20px 10px;
  }
  
  .history-header {
    margin-bottom: 16px;
  }
  
  .page-title {
    font-size: 24px;
  }
  
  .page-subtitle {
    font-size: 14px;
  }
  
  .table-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
    padding: 20px;
  }
  
  .card-title {
    font-size: 18px;
  }
  
  .table-header-cell,
  .table-cell {
    padding: 12px 16px;
    font-size: 13px;
  }
  
  .empty-message {
    padding: 60px 16px;
  }
  
  .empty-icon {
    font-size: 36px;
  }
  
  .empty-title {
    font-size: 16px;
  }
  
  .pagination {
    flex-direction: column;
    align-items: center;
    gap: 16px;
  }
  
  .page-controls {
    justify-content: center;
  }
  
  .page-btn {
    min-width: 36px;
    height: 36px;
    padding: 8px 12px;
    font-size: 13px;
  }
  
  .page-prev span,
  .page-next span {
    display: none;
  }
  
  .page-jump {
    flex-wrap: wrap;
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .table-header-cell,
  .table-cell {
    padding: 10px 12px;
    font-size: 12px;
  }
  
  .order-id,
  .date-text,
  .checkout-time {
    font-size: 11px;
  }
  
  .page-btn {
    min-width: 32px;
    height: 32px;
    padding: 6px 8px;
  }
  
  .page-jump-input {
    width: 50px;
    height: 28px;
  }
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.table-row {
  animation: fadeIn 0.3s ease-out;
}

/* 加载状态 */
:deep(.el-loading-spinner) {
  font-size: 16px;
}

:deep(.el-loading-text) {
  color: #409eff;
}
</style>