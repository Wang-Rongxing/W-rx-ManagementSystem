<template>
  <div class="app-container">
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 入住登记
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 操作按钮 -->
    <div class="operateButtons">
      <el-button type="primary" @click="showAddForm">新增入住登记</el-button>
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

    <!-- 入住登记表格 -->
    <div class="tableBox">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="checkInId" label="登记ID" width="80"></el-table-column>
        <el-table-column prop="customerName" label="客户姓名" width="120"></el-table-column>
        <el-table-column prop="customerPhone" label="客户电话" width="150"></el-table-column>
        <el-table-column prop="roomNumber" label="客房编号" width="120"></el-table-column>
        <el-table-column prop="actualCheckIn" label="入住日期" width="180" :formatter="formatDate"></el-table-column>
        <el-table-column prop="actualCheckOut" label="退房日期" width="180" :formatter="formatDate"></el-table-column>
        <el-table-column prop="actualCheckOut" label="状态" width="100" :formatter="formatStatus"></el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
            <template slot-scope="scope">
              <el-button v-if="!scope.row.actualCheckOut" type="warning" size="small" @click="handleCheckOut(scope.row)">退房</el-button>
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

    <!-- 新增入住登记表单对话框 -->
    <el-dialog title="新增入住登记" :visible.sync="dialogVisible" width="50%">
      <el-form :model="addForm" :rules="rules" ref="addFormRef" label-width="100px">
        <el-form-item label="客户姓名" prop="customerName">
          <el-input v-model="addForm.customerName" placeholder="请输入客户姓名"></el-input>
        </el-form-item>
        <el-form-item label="客户电话" prop="customerPhone">
          <el-input v-model="addForm.customerPhone" placeholder="请输入客户电话"></el-input>
        </el-form-item>
        <el-form-item label="客房编号" prop="roomNumber">
          <el-input v-model="addForm.roomNumber" placeholder="请输入客房编号"></el-input>
        </el-form-item>
        <el-form-item label="入住日期" prop="checkInDate">
          <el-date-picker
            v-model="addForm.checkInDate"
            type="datetime"
            placeholder="选择入住时间"
            style="width: 100%"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="预计退房日期" prop="checkOutDate">
          <el-date-picker
            v-model="addForm.checkOutDate"
            type="datetime"
            placeholder="选择预计退房时间"
            style="width: 100%"
          ></el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAddForm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ajaxGet, ajaxDelete, ajaxPost, ajaxPut } from '../../api/index.js';

export default {
  name: 'Checkin',
  data() {
    return {
      // 搜索表单
      searchForm: {
        customerName: '',
        customerPhone: ''
      },
      // 新增表单
      addForm: {
        customerName: '',
        customerPhone: '',
        roomNumber: '',
        checkInDate: '',
        checkOutDate: ''
      },
      // 表单验证规则
      rules: {
        customerName: [
          { required: true, message: '请输入客户姓名', trigger: 'blur' }
        ],
        customerPhone: [
          { required: true, message: '请输入客户电话', trigger: 'blur' }
        ],
        roomNumber: [
          { required: true, message: '请输入客房编号', trigger: 'blur' }
        ],
        checkInDate: [
          { required: true, message: '请选择入住日期', trigger: 'change' }
        ],
        checkOutDate: [
          { required: true, message: '请选择预计退房日期', trigger: 'change' }
        ]
      },
      // 表格数据
      tableData: [],
      // 分页参数
      pageIndex: 1,
      pageSize: 10,
      total: 0,
      // 对话框显示状态
      dialogVisible: false
    };
  },
  created() {
    // 组件创建时获取数据
    this.getData();
  },
  methods: {
      // 格式化日期为2025-10-27 00:00:00格式
      formatDate(row, column) {
        const date = row[column.property];
        if (date === null || date === '') {
          return '';
        }
        const d = new Date(date);
        const year = d.getFullYear();
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');
        const hours = String(d.getHours()).padStart(2, '0');
        const minutes = String(d.getMinutes()).padStart(2, '0');
        const seconds = String(d.getSeconds()).padStart(2, '0');
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
      },
      
      // 格式化状态
      formatStatus(row, column) {
        const checkOutDate = row.actualCheckOut;
        // 如果退房日期有数据，显示已退房，否则显示入住中
        if (checkOutDate && checkOutDate !== null && checkOutDate !== '') {
          return '已退房';
        } else {
          return '入住中';
        }
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
      ajaxGet('/checkin/selectbynameandphone', params)
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
      
      // 处理退房
      handleCheckOut(row) {
        this.$confirm('确定要为该客户办理退房吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 这里使用ajaxPut发送退房请求，更新实际退房时间
          ajaxPost(`/checkin/checkOut`, {checkInId: row.checkInId}).then(res => {
            if (res && res.code === '200') {
              this.$message.success('退房成功');
              this.getData();
            } else {
              this.$message.error(res.message || '退房失败');
            }
          }).catch(err => {
            console.error('退房失败:', err);
            this.$message.error('退房失败');
          });
        }).catch(() => {
          this.$message.info('已取消退房');
        });
      },
      
      // 获取入住登记数据
      getData() {
        const params = {
          pageIndex: this.pageIndex,
          pageSize: this.pageSize,
          customerName: this.searchForm.customerName,
          customerPhone: this.searchForm.customerPhone
        };
        
        ajaxGet('/checkin/allCheckin', params)
          .then(res => {
            if (res && res.code === '200') {
              this.tableData = res.records || [];
              this.total = res.total || 0;
            } else {
              this.$message.error(res.message || '获取数据失败');
              this.tableData = [];
              this.total = 0;
            }
          })
          .catch(err => {
            console.error('获取数据失败:', err);
            this.$message.error('获取数据失败');
            this.tableData = [];
            this.total = 0;
          });
      },
    
    // 查询
    search() {
      this.pageIndex = 1;
      this.selectbynameandphone();
    },
    
    // 重置搜索条件
    clearSearch() {
      this.searchForm = {
        customerName: '',
        customerPhone: ''
      };
      this.pageIndex = 1;
      this.getData();
    },
    
    // 分页处理 - 当前页码变化
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
    
    // 显示新增表单
    showAddForm() {
      // 重置表单
      this.$refs.addFormRef && this.$refs.addFormRef.resetFields();
      this.addForm = {
        customerName: '',
        customerPhone: '',
        roomNumber: '',
        checkInDate: '',
        checkOutDate: ''
      };
      this.dialogVisible = true;
    },
    
    // 提交新增表单
    submitAddForm() {
      this.$refs.addFormRef.validate((valid) => {
        if (valid) {
          // 验证通过，提交表单
          ajaxPost('/checkin/add', this.addForm)
            .then(res => {
              if (res && res.code === '200') {
                this.$message.success('新增成功');
                this.dialogVisible = false;
                this.getData(); // 重新获取数据
              } else {
                this.$message.error(res.message || '新增失败');
              }
            })
            .catch(err => {
              console.error('新增失败:', err);
              this.$message.error('新增失败');
            });
        } else {
          return false;
        }
      });
    },
    
    // 删除入住登记
    handleDelete(row) {
      this.$confirm('确定要删除该入住登记信息吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          ajaxDelete('/checkin/delete', row.checkInId)
            .then(res => {
              if (res && res.code === '200') {
                this.$message.success('删除成功');
                this.getData(); // 重新获取数据
              } else {
                this.$message.error(res.message || '删除失败');
              }
            })
            .catch(err => {
              console.error('删除失败:', err);
              this.$message.error('删除失败');
            });
        })
        .catch(() => {
          // 用户取消操作
        });
    }
  }
};
</script>

<style scoped>
.app-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.crumbs {
  margin-bottom: 20px;
}

.operateButtons {
  margin-bottom: 20px;
}

.searchForm {
  background-color: #fff;
  padding: 20px;
  border-radius: 5px;
  margin-bottom: 20px;
}

.tableBox {
  background-color: #fff;
  padding: 20px;
  border-radius: 5px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>