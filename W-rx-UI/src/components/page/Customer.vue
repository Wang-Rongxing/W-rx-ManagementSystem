<template>
  <div class="customer-management">
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 客户管理
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    
    <div class="container">
      <!-- 操作按钮区域 -->
      <div class="handle-box">
        <el-button size="small" type="primary" icon="el-icon-circle-plus-outline" @click="handleAddUser">新增客户</el-button>
      </div>

      <!-- 搜索区域 -->
      <div class="search-box">
        <el-input size="small" v-model="query.username" placeholder="用户名" class="handle-input mr10"></el-input>
        <el-input size="small" v-model="query.customerId" placeholder="账号" class="handle-input mr10"></el-input>
        <el-button size="small" type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
        <el-button size="small" @click="handlerest">重置</el-button>
      </div>

      <!-- 表格区域 -->
      <div v-if="tableShow" class="table-container">
        <el-table 
          :data="tableData" 
          class="table"
          v-loading="loading"
          element-loading-text="加载中..."
          empty-text="暂无数据"
        >
          <el-table-column prop="id" label="ID" width="60" align="center"></el-table-column>
          <el-table-column prop="name" label="用户名" min-width="120"></el-table-column>
          <el-table-column prop="customerId" label="账号" min-width="100"></el-table-column>
          <el-table-column prop="phone" label="电话" min-width="120"></el-table-column>
          <el-table-column label="操作" width="180" align="center">
            <template slot-scope="scope">
              <el-popconfirm 
                @confirm="resetUserPassword(scope.$index, scope.row)" 
                confirm-button-text='确定'
                cancel-button-text='取消' 
                icon-color="#f56c6c" 
                title="确定要编辑该客户信息吗?"
              >
                <el-button slot="reference" type="primary" size="small" icon="el-icon-edit" class="mr10">编辑</el-button>
              </el-popconfirm>
              
              <el-popconfirm 
                @confirm="handleDelete(scope.$index, scope.row)" 
                confirm-button-text='确定'
                cancel-button-text='取消' 
                icon-color="#f56c6c" 
                title="确定要删除该客户吗?删除后数据不可恢复!"
              >
                <el-button slot="reference" type="danger" size="small" icon="el-icon-delete">删除</el-button>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
        
        <!-- 分页控件 -->
        <div class="pagination">
          <el-pagination 
            background 
            layout="total, prev, pager, next, jumper" 
            :current-page="query.pageIndex"
            :page-size="query.pageSize" 
            :total="pageTotal" 
            @current-change="handlePageChange"
            @size-change="handleSizeChange"
          ></el-pagination>
        </div>
      </div>

      <!-- 加载状态显示 -->
      <div v-else-if="loading" class="loading-container">
        <el-loading-spinner></el-loading-spinner>
        <span class="loading-text">正在加载数据...</span>
      </div>
    </div>

    <!-- 编辑客户弹出框 -->
    <el-dialog 
      title="修改客户信息" 
      :visible.sync="editVisible" 
      width="400px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" status-icon>
        <el-form-item label="姓名:" prop="name">
          <el-input v-model="form.name" placeholder="请输入客户姓名"></el-input>
        </el-form-item>
        <el-form-item label="账号:" prop="customerId">
          <el-input v-model="form.customerId" placeholder="请输入客户账号" disabled></el-input>
        </el-form-item>
        <el-form-item label="密码:" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="电话:" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelEdit">取消</el-button>
        <el-button type="primary" @click="saveEdit">确定</el-button>
      </div>
    </el-dialog>
    
    <!-- 新增客户弹出框 -->
    <el-dialog 
      title="新增客户" 
      :visible.sync="addVisible" 
      width="400px" 
      :before-close="cancelInsertUser"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form ref="formAdd" :model="formAdd" :rules="rules" label-width="100px" status-icon>
        <el-form-item label="姓名:" prop="name">
          <el-input v-model="formAdd.name" placeholder="请输入客户姓名"></el-input>
        </el-form-item>
        <el-form-item label="账号:" prop="customerId">
          <el-input v-model="formAdd.customerId" placeholder="请输入客户账号"></el-input>
        </el-form-item>
        <el-form-item label="密码:" prop="password">
          <el-input v-model="formAdd.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="电话:" prop="phone">
          <el-input v-model="formAdd.phone" placeholder="请输入联系电话"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelInsertUser">取消</el-button>
        <el-button type="primary" @click="insertUser">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  ajaxPost,
  ajaxGet,
  ajaxDelete
} from '../../api/index';

export default {
  name: 'Customer',
  data() {
    // 账号验证规则
    const validateCustomerId = (rule, value, callback) => {
      const reg = /^[a-zA-Z0-9]*$/;
      if (!reg.test(value)) {
        callback(new Error('账号必须是数字符号'));
      } else {
        callback();
      }
    };
    
    // 手机号验证规则
    const validatePhone = (rule, value, callback) => {
      const reg = /^1[3-9]\d{9}$/;
      if (value && !reg.test(value)) {
        callback(new Error('请输入有效的手机号'));
      } else {
        callback();
      }
    };
    
    return {
      // 查询参数
      query: {
        customerId: '',
        username: '',
        pageIndex: 1,
        pageSize: 10
      },
      // 表格数据
      tableData: [],
      // 显示控制
      tableShow: false,
      loading: false,
      editVisible: false,
      addVisible: false,
      // 分页信息
      pageTotal: 0,
      pages: 0,
      // 编辑表单数据
      form: {
        id: '',
        name: '',
        customerId: '',
        password: '',
        phone: ''
      },
      // 新增表单数据
      formAdd: {
        name: '',
        customerId: '',
        password: '',
        phone: ''
      },
      // 表单验证规则
      rules: {
        name: [
          {
            required: true,
            message: '请输入客户姓名',
            trigger: 'blur'
          },
          {
            min: 1,
            max: 20,
            message: '姓名长度应在1-20个字符之间',
            trigger: 'blur'
          }
        ],
        customerId: [
          {
            required: true,
            message: '请输入客户账号',
            trigger: 'blur'
          },
          {
            validator: validateCustomerId,
            trigger: 'blur'
          }
        ],
        password: [
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur'
          },
          {
            min: 6,
            max: 20,
            message: '密码长度应在6-20个字符之间',
            trigger: 'blur'
          }
        ],
        phone: [
          {
            validator: validatePhone,
            trigger: 'blur'
          }
        ]
      },
      // 编辑索引
      idx: -1
    };
  },
  
  created() {
    // 组件创建时获取数据
    this.getData();
  },
  
  methods: {
    // 获取客户列表数据
    async getData() {
      try {
        this.loading = true;
        const res = await ajaxGet("/customer/allUser", this.query);
        
        if (res && res.records) {
          this.tableShow = true;
          this.tableData = res.records;
          this.pageTotal = res.total || 0;
          this.pages = res.pages || 0;
        } else {
          this.tableShow = false;
          this.tableData = [];
          this.pageTotal = 0;
        }
      } catch (error) {
        console.error("获取客户列表失败:", error);
        this.$message.error("获取数据失败，请稍后重试");
        this.tableShow = false;
        this.tableData = [];
      } finally {
        this.loading = false;
      }
    },
    
    // 根据账号和姓名查询
    async getDataByIdOrName() {
      try {
        this.loading = true;
        
        // 构造正确的参数格式
        const searchParams = {
          customerId: this.query.customerId,
          name: this.query.username
        };
        
        const res = await ajaxPost("/customer/selectCustomerByIdOrName", searchParams);
        
        if (res && res.records) {
          this.tableShow = true;
          this.tableData = res.records;
          this.pageTotal = res.total || 0;

          if (this.tableData.length === 0) {
            this.$message.info("未找到匹配的客户信息");
          }
        } else {
          this.tableShow = false;
          this.tableData = [];
          this.pageTotal = 0;
          this.$message.info("未找到匹配的客户信息");
        }
      } catch (error) {
        console.error("搜索请求失败:", error);
        this.$message.error("搜索操作异常，请稍后重试");
        this.tableShow = false;
        this.tableData = [];
        this.pageTotal = 0;
      } finally {
        this.loading = false;
      }
    },
    
    // 新增客户
    async insertUser() {
      try {
        const valid = await this.$refs.formAdd.validate();
        
        if (!valid) return;
        
        const data = {
          "name": this.formAdd.name,
          "customerId": this.formAdd.customerId,
          "password": this.formAdd.password,
          "phone": this.formAdd.phone
        };
        
        const res = await ajaxPost("/customer/insertUser", data);
        
        if (res) {
          this.$message.success('添加成功');
          this.$refs.formAdd.resetFields();
          this.addVisible = false;
          this.getData(); // 重新获取数据以更新列表
        } else {
          this.$message.error('添加失败');
        }
      } catch (error) {
        console.error("添加客户失败:", error);
        this.$message.error('添加操作异常，请稍后重试');
      }
    },
    
    // 取消新增
    cancelInsertUser() {
      this.$refs.formAdd.resetFields();
      this.addVisible = false;
    },
    
    // 打开新增对话框
    handleAddUser() {
      this.addVisible = true;
    },
    
    // 触发搜索按钮
    handleSearch() {
      this.query.pageIndex = 1;
      this.getDataByIdOrName();
    },
    
    // 重置搜索条件
    handlerest() {
      this.query.customerId = '';
      this.query.username = '';
      this.query.pageIndex = 1;
      this.getData();
    },
    
    // 删除操作
    async handleDelete(index, row) {
      if (!row || !row.id) {
        this.$message.error('无效的客户数据');
        return;
      }
      
      try {
        const res = await ajaxDelete('/customer/delete', row.id);
        
        if (res) {
          this.$message.success('删除成功');
          
          // 处理最后一页只有一条数据的情况
          if (this.tableData.length === 1 && this.query.pageIndex !== 1 && this.query.pageIndex <= this.pages) {
            this.query.pageIndex -= 1;
          }
          
          // 重新获取数据更新列表
          this.getData();
        } else {
          this.$message.error('删除失败，请重试');
        }
      } catch (error) {
        console.error('删除操作失败:', error);
        this.$message.error('删除操作异常，请稍后重试');
      }
    },

    // 编辑客户
    resetUserPassword(index, row) {
      if (!row || !row.id) {
        this.$message.error('无效的客户数据');
        return;
      }
      
      this.idx = index;
      this.form = { ...row };
      this.editVisible = true;
    },
    
    // 保存编辑
    async saveEdit() {
      try {
        const valid = await this.$refs.form.validate();
        
        if (!valid) return;
        
        const data = {
          "id": this.form.id,
          "name": this.form.name,
          "customerId": this.form.customerId,
          "password": this.form.password,
          "phone": this.form.phone
        };
        
        const res = await ajaxPost("/customer/updateUser", data);
        
        if (res) {
          this.editVisible = false;
          this.$message.success('修改成功');
          this.getData(); // 重新获取数据更新列表
        } else {
          this.$message.error('修改失败');
        }
      } catch (error) {
        console.error('编辑操作失败:', error);
        this.$message.error('编辑操作异常，请稍后重试');
      }
    },
    
    // 取消编辑
    cancelEdit() {
      this.$refs.form.resetFields();
      this.editVisible = false;
    },
    
    // 分页导航 - 页码变化
    handlePageChange(val) {
      this.query.pageIndex = val;
      this.getData();
    },
    
    // 分页导航 - 每页大小变化
    handleSizeChange(val) {
      this.query.pageSize = val;
      this.query.pageIndex = 1;
      this.getData();
    }
  }
};
</script>

<style scoped>
.customer-management {
  padding: 15px;
}

.crumbs {
  margin-bottom: 20px;
}

.container {
  background-color: #fff;
  border-radius: 6px;
  padding: 20px;
}

.handle-box {
  margin-bottom: 20px;
}

.search-box {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 6px;
}

.handle-input {
  width: 200px;
  display: inline-block;
}

.table-container {
  margin-top: 15px;
}

.table {
  width: 100%;
  font-size: 14px;
}

/* 表格行悬停效果 */
:deep(.el-table__row:hover) {
  background-color: #f5f7fa;
}

/* 表格头部样式 */
:deep(.el-table__header th) {
  background-color: #fafafa;
  font-weight: 600;
  color: #303133;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 0;
  color: #909399;
}

.loading-text {
  margin-top: 10px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  padding-top: 15px;
}

/* 按钮样式优化 */
:deep(.el-button--primary) {
  background-color: #409eff;
  border-color: #409eff;
}

:deep(.el-button--primary:hover) {
  background-color: #66b1ff;
  border-color: #66b1ff;
}

:deep(.el-button--danger) {
  background-color: #f56c6c;
  border-color: #f56c6c;
}

:deep(.el-button--danger:hover) {
  background-color: #f78989;
  border-color: #f78989;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .customer-management {
    padding: 10px;
  }
  
  .container {
    padding: 10px;
  }
  
  .handle-input {
    width: 100%;
    margin-bottom: 10px;
  }
  
  .search-box {
    display: flex;
    flex-wrap: wrap;
  }
  
  .table {
    font-size: 12px;
  }
  
  .pagination {
    text-align: center;
  }
}

.mr10 {
  margin-right: 10px;
}
</style>
