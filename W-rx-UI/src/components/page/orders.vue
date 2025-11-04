<script>
import {ajaxDelete, ajaxGet, ajaxPost} from '../../api/index';

export default {
  data() {
    return {
      orderList: [],
      total: 0,
      currentPage: 1,
      pageSize: 10,
      loading: false,
      pageRange: [],
      totalPages: 0
    };
  },
  created() {
    this.getOrderList();
  },
  computed: {
    // 计算订单统计信息
    orderStats() {
      return {
        total: this.orderList.length
      };
    },
    
    // 格式化日期
    formatDate() {
      return (dateString) => {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleDateString('zh-CN', {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit'
        });
      };
    }
  },
  watch: {
    // 监听分页相关变化，更新页码范围
    total() {
      this.updatePageRange();
    },
    currentPage() {
      this.updatePageRange();
    },
    pageSize() {
      this.updatePageRange();
    }
  },
  methods: {
    async getOrderList() {
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
        
        // 调用后端API获取订单列表
        const response = await ajaxGet('/orders/getOrders', params);
        
        // 处理响应数据
        if (response && response.records) {
          this.orderList = response.records || [];
          this.total = response.total || 0;
        }
      } catch (error) {
        console.error('获取订单列表失败:', error);
        this.$message.error('获取订单列表失败，请稍后重试');
      } finally {
        this.loading = false;
        this.updatePageRange();
      }
    },

    // 更新页码范围
    updatePageRange() {
      this.totalPages = Math.ceil(this.total / this.pageSize);
      const range = [];
      let start = Math.max(1, this.currentPage - 2);
      let end = Math.min(this.totalPages, start + 4);
      
      if (end - start < 4 && start > 1) {
        start = Math.max(1, end - 4);
      }
      
      for (let i = start; i <= end; i++) {
        range.push(i);
      }
      
      this.pageRange = range;
    },
    
    // 跳转到指定页码
    jumpToPage(page) {
      if (page < 1 || page > this.totalPages) return;
      this.currentPage = page;
      this.getOrderList();
    },
    
    // 分页改变事件处理
    handlePageChange(page) {
      this.currentPage = page;
      this.getOrderList();
    },
    
    // 取消订单
    cancelOrder(orderId) {
      this.$confirm('确定要取消该订单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
          .then(() => {
            ajaxDelete('/orders/delete', orderId)
                .then(res => {
                  if (res && res.code === '200') {
                    this.$message.success('取消订单成功');
                    this.getOrderList(); // 重新获取数据
                  } else {
                    this.$message.error(res.message || '取消订单失败');
                  }
                })
                .catch(err => {
                  console.error('取消订单失败:', err);
                  this.$message.error('取消订单失败');
                });
          })
          .catch(() => {
            this.$message.info('已取消删除');
          });
    },
    

    

    
  }
};
</script>

<template>
  <div class="orders-container">
    <div class="orders-content" v-loading="loading">
      <!-- 页面标题和统计信息 -->
      <div class="page-header">
        <div class="header-left">
          <h2 class="page-title">我的订单</h2>
          <div class="order-stats">
            <div class="stat-item">
              <span class="stat-label">总订单</span>
              <span class="stat-value">{{ orderStats.total }}</span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 订单列表表格 -->
      <div class="table-container">
        <div class="table-wrapper">
          <table class="orders-table">
            <thead>
              <tr>
                <th>订单ID</th>
                <th>客户姓名</th>
                <th>客户电话</th>
                <th>客房类型</th>
                <th>客房编号</th>
                <th>入住日期</th>
                <th>退房日期</th>
                <th>订单金额</th>
                <th>创建时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in orderList" :key="item.orderId" class="order-row">
                <td class="order-id">{{ item.orderId }}</td>
                <td class="customer-name">{{ item.customerName }}</td>
                <td class="customer-phone">{{ item.customerPhone }}</td>
                <td class="room-type">{{ item.roomType }}</td>
                <td class="room-number">{{ item.roomNumber }}</td>
                <td class="check-in-time">{{ item.checkInDate }}</td>
                <td class="check-out-time">{{ item.checkOutDate }}</td>
                <td class="order-price">¥{{ item.totalPrice }}</td>
                <td class="create-time">{{ item.createTime }}</td>
                <td class="action-buttons">
                  <button class="btn btn-cancel" @click="cancelOrder(item.orderId)">
                    <svg class="btn-icon" viewBox="0 0 24 24" width="14" height="14">
                      <path fill="currentColor" d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
                    </svg>
                    取消订单
                  </button>
                </td>
              </tr>
              <tr v-if="orderList.length === 0" class="empty-row">
                <td colspan="10" class="empty-cell">
                  <div class="empty-state">
                    <svg class="empty-icon" viewBox="0 0 24 24" width="64" height="64">
                      <path fill="#ccc" d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-5 14h-2v-4H8v-2h4V7h2v4h4v2h-4v4z"/>
                    </svg>
                    <p class="empty-text">暂无订单记录</p>
                    <p class="empty-subtitle">您还没有任何订单，可以去预订心仪的房间</p>
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
          <span>共 {{ total }} 条记录，第 {{ currentPage }} / {{ totalPages }} 页</span>
        </div>
        <div class="pagination-controls">
          <button 
            class="page-btn page-prev" 
            :disabled="currentPage === 1"
            @click="handlePageChange(currentPage - 1)"
            title="上一页"
          >
            <svg viewBox="0 0 24 24" width="16" height="16">
              <path fill="currentColor" d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z"/>
            </svg>
          </button>
          
          <button 
            v-if="pageRange[0] > 1"
            class="page-btn page-jump"
            @click="jumpToPage(1)"
          >
            1
          </button>
          
          <span v-if="pageRange[0] > 2" class="page-ellipsis">...</span>
          
          <button 
            v-for="page in pageRange"
            :key="page"
            class="page-btn"
            :class="{ active: currentPage === page }"
            @click="handlePageChange(page)"
          >
            {{ page }}
          </button>
          
          <span v-if="pageRange[pageRange.length - 1] < totalPages - 1" class="page-ellipsis">...</span>
          
          <button 
            v-if="pageRange[pageRange.length - 1] < totalPages"
            class="page-btn page-jump"
            @click="jumpToPage(totalPages)"
          >
            {{ totalPages }}
          </button>
          
          <button 
            class="page-btn page-next" 
            :disabled="currentPage >= totalPages"
            @click="handlePageChange(currentPage + 1)"
            title="下一页"
          >
            <svg viewBox="0 0 24 24" width="16" height="16">
              <path fill="currentColor" d="M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z"/>
            </svg>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 全局样式重置和容器 */
.orders-container {
  min-height: calc(100vh - 60px);
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.orders-content {
  max-width: 1400px;
  margin: 0 auto;
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  animation: fadeIn 0.5s ease-out;
}

/* 页面标题和统计信息 */
.page-header {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e8eaf6;
}

.header-left {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.order-stats {
  display: flex;
  gap: 24px;
  flex-wrap: wrap;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  background: #f8f9fa;
  padding: 12px 20px;
  border-radius: 8px;
  min-width: 100px;
  transition: transform 0.2s, box-shadow 0.2s;
}

.stat-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 4px;
}

.stat-value {
  font-size: 20px;
  font-weight: 600;
  color: #333;
}





/* 表格容器和滚动 */
.table-container {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.table-wrapper {
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
  scrollbar-width: thin;
  scrollbar-color: #dcdfe6 #f5f7fa;
}

.table-wrapper::-webkit-scrollbar {
  height: 6px;
}

.table-wrapper::-webkit-scrollbar-track {
  background: #f5f7fa;
}

.table-wrapper::-webkit-scrollbar-thumb {
  background: #dcdfe6;
  border-radius: 3px;
}

.table-wrapper::-webkit-scrollbar-thumb:hover {
  background: #c0c4cc;
}

/* 表格样式 */
.orders-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.orders-table thead {
  background: linear-gradient(135deg, #f8f9fa, #e9ecef);
  position: sticky;
  top: 0;
  z-index: 10;
}

.orders-table th {
  padding: 16px 12px;
  text-align: center;
  font-weight: 600;
  color: #606266;
  white-space: nowrap;
  border-bottom: 2px solid #e0e0e0;
  transition: background 0.3s;
}

.orders-table th:hover {
  background: linear-gradient(135deg, #e9ecef, #dee2e6);
}

.order-row {
  transition: all 0.3s ease;
  border-bottom: 1px solid #f0f0f0;
}

.order-row:hover {
  background: #fafafa;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.orders-table td {
  padding: 16px 12px;
  text-align: center;
  color: #303133;
  vertical-align: middle;
}

.order-price {
  font-weight: 600;
  color: #e6a23c;
}



/* 操作按钮样式 */
.action-buttons {
  display: flex;
  gap: 8px;
  justify-content: center;
  flex-wrap: wrap;
}

.btn {
  position: relative;
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  font-weight: 500;
  transition: all 0.3s ease;
  overflow: hidden;
}

.btn::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  transform: translate(-50%, -50%);
  transition: width 0.6s, height 0.6s;
}

.btn:hover::before {
  width: 300px;
  height: 300px;
}

.btn-cancel {
  background-color: #ff4d4f;
  color: white;
}

.btn-cancel:hover {
  background-color: #ff7875;
  box-shadow: 0 2px 8px rgba(255, 77, 79, 0.3);
}





.btn-icon {
  width: 14px;
  height: 14px;
  pointer-events: none;
}

/* 空状态样式 */
.empty-row {
  height: 400px;
}

.empty-cell {
  padding: 40px 0 !important;
  vertical-align: middle;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: #909399;
}

.empty-icon {
  margin-bottom: 16px;
  opacity: 0.5;
}

.empty-text {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 8px;
  color: #606266;
}

.empty-subtitle {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

/* 分页控件样式 */
.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid #e8eaf6;
}

.pagination-info {
  color: #606266;
  font-size: 14px;
}

.pagination-controls {
  display: flex;
  align-items: center;
  gap: 8px;
}

.page-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border: 1px solid #dcdfe6;
  background-color: #fff;
  color: #606266;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.3s ease;
  font-size: 14px;
}

.page-btn:hover:not(:disabled) {
  border-color: #409eff;
  color: #409eff;
  background: #ecf5ff;
}

.page-btn.active {
  background-color: #409eff;
  color: #fff;
  border-color: #409eff;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
}

.page-btn:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.page-prev, .page-next {
  font-size: 16px;
}

.page-ellipsis {
  display: flex;
  align-items: center;
  padding: 0 8px;
  color: #909399;
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes ripple {
  to {
    transform: scale(4);
    opacity: 0;
  }
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .orders-table {
    font-size: 13px;
  }
  
  .orders-table th,
  .orders-table td {
    padding: 12px 8px;
  }
  
  .order-stats {
    gap: 16px;
  }
  
  .stat-item {
    min-width: 80px;
    padding: 10px 16px;
  }
}

@media (max-width: 768px) {
  .orders-container {
    padding: 10px;
  }
  
  .orders-content {
    padding: 16px;
    border-radius: 8px;
  }
  
  .page-title {
    font-size: 20px;
  }
  
  .order-stats {
    gap: 12px;
  }
  
  .stat-item {
    min-width: 70px;
    padding: 8px 12px;
  }
  
  .stat-value {
    font-size: 16px;
  }
  
  .search-input-wrapper {
    flex-direction: column;
    max-width: none;
  }
  
  .search-input {
    width: 100%;
    margin-bottom: 12px;
  }
  
  .search-btn {
    width: 100%;
    margin-left: 0;
  }
  
  .pagination {
    flex-direction: column;
    gap: 16px;
    align-items: center;
  }
  
  .pagination-info {
    order: 2;
  }
  
  .pagination-controls {
    order: 1;
  }
  
  .action-buttons {
    flex-direction: column;
    align-items: center;
  }
  
  .btn {
    width: 100%;
    justify-content: center;
  }
}
</style>