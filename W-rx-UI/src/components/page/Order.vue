<template>
  <div class="app-container">
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 订单管理
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 搜索表单 -->
    <div class="searchForm">
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="客户姓名">
          <el-input v-model="searchForm.customerName" placeholder="客户姓名"></el-input>
        </el-form-item>
        <el-form-item label="客户电话">
          <el-input v-model="searchForm.customerPhone" placeholder="客户电话"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
          <el-button @click="clearSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 订单表格 -->
    <div class="tableBox">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="orderId" label="订单ID" width="80"></el-table-column>
        <el-table-column prop="customerName" label="客户姓名" width="120"></el-table-column>
        <el-table-column prop="customerPhone" label="客户电话" width="150"></el-table-column>
        <el-table-column prop="roomType" label="客房类型" width="150"></el-table-column>
        <el-table-column prop="roomNumber" label="客房编号" width="120"></el-table-column>
        <el-table-column prop="checkInDate" label="入住日期" width="180" :formatter="formatDate"></el-table-column>
        <el-table-column prop="checkOutDate" label="退房日期" width="180" :formatter="formatDate"></el-table-column>
        <el-table-column prop="totalPrice" label="订单金额" width="120"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" :formatter="formatDate"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button 
              type="primary" 
              size="small" 
              @click="handleCheckIn(scope.row)"
            >办理入住</el-button>
            <el-button type="danger" size="small" @click="handleCancelOrder(scope.row)">取消订单</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination">
        <el-pagination
          background
          layout="prev, pager, next, jumper, total"
          :total="total"
          :page-size="pageSize"
          :current-page="pageIndex"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        ></el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import { ajaxGet, ajaxDelete, ajaxPost } from '../../api/index.js';

export default {
  name: 'Order',
  data() {
    return {
      // 搜索表单
      searchForm: {
        customerName: '',
        customerPhone: ''
      },
      // 订单数据
      tableData: [],
      // 分页参数
      pageIndex: 1,
      pageSize: 10,
      total: 0,

    };
  },
  created() {
    // 组件创建时获取数据
    this.getData();
  },
  methods: {
    // 获取订单数据
    getData() {
      const params = {
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
        customerName: this.searchForm.customerName,
        customerPhone: this.searchForm.customerPhone
      };

      ajaxGet('/orders/allOrders', params)
        .then(res => {
          if (res && res.records) {
            this.tableData = res.records;
            this.total = res.total;
            this.pageSize = res.pageSize;
            this.pageIndex = res.pageIndex;
          } else {
            this.$message.error('获取订单数据失败');
          }
        })
        .catch(err => {
          console.error('获取订单数据异常:', err);
          this.$message.error('获取订单数据异常');
        });
    },
    selectbynameandphone(){
      // 准备搜索参数
      const params = {
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
        customerName: this.searchForm.customerName,
        customerPhone: this.searchForm.customerPhone
      };

      // 发送请求获取订单数据
      ajaxGet('/orders/selectbynameandphone', params)
        .then(res => {
          if (res && res.records) {
            this.tableData = res.records;
            this.total = res.total;
            this.pageSize = res.pageSize;
            this.pageIndex = res.pageIndex;
          } else {
            this.$message.error('获取订单数据失败');
            this.tableData = [];
            this.total = 0;
          }
        })
        .catch(err => {
          console.error('搜索订单数据异常:', err);
          this.$message.error('搜索订单数据异常');
          this.tableData = [];
          this.total = 0;
        });
    },
    // 办理入住
    handleCheckIn(row) {
      this.$confirm('确定要为该订单办理入住吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      })
        .then(() => {
          // 调用办理入住的接口，使用路径参数
          ajaxPost(`/orders/checkIn/${row.orderId}`)
            .then(res => {
              if (res && res.code === '200') {
                this.$message.success('办理入住成功');
                this.getData(); // 重新获取数据
              } else {
                this.$message.error(res.message || '办理入住失败');
              }
            })
            .catch(err => {
              console.error('办理入住失败:', err);
              this.$message.error('办理入住失败');
            });
        })
        .catch(() => {
          this.$message.info('已取消办理入住');
        });
    },
    // 搜索
    search() {
      this.pageIndex = 1; // 重置页码
      this.selectbynameandphone();
    },
    // 重置搜索条件
    clearSearch() {
      this.searchForm = {
        customerName: '',
        customerPhone: '',
        orderStatus: ''
      };
      this.pageIndex = 1;
      this.getData();
    },
    // 分页处理 - 页码变化
    handleCurrentChange(val) {
      this.pageIndex = val;
      this.getData();
    },
    // 分页处理 - 每页大小变化
    handleSizeChange(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.getData();
    },
    // 取消订单
    handleCancelOrder(row) {
      this.$confirm('确定要取消该订单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          ajaxDelete('/orders/delete', row.orderId)
            .then(res => {
              if (res && res.code === '200') {
                this.$message.success('取消订单成功');
                this.getData(); // 重新获取数据
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
    // 格式化日期
    formatDate(row, column, cellValue) {
      // 注意：在Element UI表格中，formatter函数接收三个参数：row, column, cellValue
      // 所以我们应该使用cellValue而不是直接使用date参数
      const date = cellValue;
      
      if (!date) return '';
      
      try {
        let d;
        if (typeof date === 'string') {
          // 处理各种字符串格式
          const dateStr = date.replace('T', ' ').replace(/\.[0-9]+/, '');
          d = new Date(dateStr);
        } else if (typeof date === 'object') {
          d = new Date(date);
        } else {
          return String(date);
        }
        
        // 检查是否是有效日期
        if (isNaN(d.getTime())) {
          return String(date);
        }
        
        // 获取各个时间部分
        const year = d.getFullYear();
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');
        const hours = String(d.getHours()).padStart(2, '0');
        const minutes = String(d.getMinutes()).padStart(2, '0');
        const seconds = String(d.getSeconds()).padStart(2, '0');
        
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
      } catch (error) {
        console.error('日期格式化错误:', error);
        return date ? String(date) : '';
      }
    },

  }
};
</script>

<style scoped>
.crumbs {
  margin-bottom: 20px;
}
.searchForm {
  background-color: #f5f7fa;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 4px;
}
.tableBox {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
}
.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>