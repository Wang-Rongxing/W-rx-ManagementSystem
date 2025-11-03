<template>
  <div class="employee-management">
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 酒店员工
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    
    <div class="container">
      <!-- 操作按钮区域 -->
      <div class="handle-box">
        <el-button size="small" type="primary" icon="el-icon-circle-plus-outline" @click="handleAddUser">新增员工</el-button>
      </div>

      <!-- 搜索区域 -->
      <div class="search-box">
        <el-input size="small" v-model="query.username" placeholder="用户名" class="handle-input mr10"></el-input>
        <el-input size="small" v-model="query.employeeId" placeholder="工号" class="handle-input mr10"></el-input>
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
          <el-table-column prop="employeeId" label="工号" min-width="100"></el-table-column>
          <el-table-column prop="phone" label="电话" min-width="120"></el-table-column>
          <el-table-column label="操作" width="200" align="center">
              <template slot-scope="scope">
                <div style="display: flex; justify-content: center; gap: 10px;">
                  <el-popconfirm 
                    @confirm="resetUserPassword(scope.$index, scope.row)" 
                    confirm-button-text='确定'
                    cancel-button-text='取消' 
                    icon-color="#f56c6c" 
                    title="确定要重置该员工密码吗?"
                  >
                    <el-button slot="reference" type="primary" size="small" icon="el-icon-edit">重置密码</el-button>
                  </el-popconfirm>
                  
                  <el-popconfirm 
                    @confirm="handleDelete(scope.$index, scope.row)" 
                    confirm-button-text='确定'
                    cancel-button-text='取消' 
                    icon-color="#f56c6c" 
                    title="确定要删除该员工吗?删除后数据不可恢复!"
                  >
                    <el-button slot="reference" type="danger" size="small" icon="el-icon-delete">删除</el-button>
                  </el-popconfirm>
                </div>
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

    <!-- 新增员工弹出框 -->
    <el-dialog 
      title="新增员工" 
      :visible.sync="addVisible" 
      width="400px" 
      :before-close="cancelInsertUser"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form ref="formAdd" :model="formAdd" :rules="rules" label-width="100px" status-icon>
        <el-form-item label="姓名:" prop="name">
          <el-input v-model="formAdd.name" placeholder="请输入员工姓名"></el-input>
        </el-form-item>
        <el-form-item label="工号:" prop="employeeId">
          <el-input v-model="formAdd.employeeId" placeholder="请输入员工工号"></el-input>
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
  name: 'Employee',
  data() {
    // 工号验证规则
    const validateEmployeeId = (rule, value, callback) => {
      const reg = /^[0-9]*$/;
      if (!reg.test(value)) {
        callback(new Error('工号必须是数字符号'));
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
        employeeId: '',
        username: '',
        pageIndex: 1,
        pageSize: 10
      },
      // 表格数据
      tableData: [],
      // 显示控制
      tableShow: false,
      loading: false,
      addVisible: false,
      // 分页信息
      pageTotal: 0,
      pages: 0,
      // 新增表单数据
      formAdd: {
        name: '',
        employeeId: '',
        password: '',
        phone: ''
      },
      // 表单验证规则
      rules: {
        name: [
          {
            required: true,
            message: '请输入员工姓名',
            trigger: 'blur'
          },
          {
            min: 1,
            max: 20,
            message: '姓名长度应在1-20个字符之间',
            trigger: 'blur'
          }
        ],
        employeeId: [
          {
            required: true,
            message: '请输入员工工号',
            trigger: 'blur'
          },
          {
            validator: validateEmployeeId,
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
      }
    };
  },
  
  created() {
    // 组件创建时获取数据
    this.getData();
  },
  
  methods: {
    // 获取员工列表数据
    async getData() {
      try {
        this.loading = true;
        const res = await ajaxGet("/employee/allUser", this.query);
        
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
        console.error("获取员工列表失败:", error);
        this.$message.error("获取数据失败，请稍后重试");
        this.tableShow = false;
        this.tableData = [];
      } finally {
        this.loading = false;
      }
    },
    
    // 根据工号和姓名查询
    async getDataByIdOrName() {
      try {
        this.loading = true;
        
        // 构造正确的参数格式
        const searchParams = {
          employeeId: this.query.employeeId,
          name: this.query.username
        };
        
        const res = await ajaxPost("/employee/selectEmployeeByIdOrName", searchParams);
        
        if (res && res.records) {
          this.tableShow = true;
          this.tableData = res.records;
          this.pageTotal = res.total || 0;

          if (this.tableData.length === 0) {
            this.$message.info("未找到匹配的员工信息");
          }
        } else {
          this.tableShow = false;
          this.tableData = [];
          this.pageTotal = 0;
          this.$message.info("未找到匹配的员工信息");
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
    
    // 新增员工
    async insertUser() {
      try {
        const valid = await this.$refs.formAdd.validate();
        
        if (!valid) return;
        
        const data = {
          "name": this.formAdd.name,
          "employeeId": this.formAdd.employeeId,
          "password": this.formAdd.password,
          "phone": this.formAdd.phone
        };
        
        const res = await ajaxPost("/employee/insertUser", data);
        
        if (res) {
          this.$message.success('添加成功');
          this.$refs.formAdd.resetFields();
          this.addVisible = false;
          this.getData(); // 重新获取数据以更新列表
        } else {
          this.$message.error('添加失败');
        }
      } catch (error) {
        console.error("添加员工失败:", error);
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
      this.query.employeeId = '';
      this.query.username = '';
      this.query.pageIndex = 1;
      this.getData();
    },
    
    // 删除操作
    async handleDelete(index, row) {
      if (!row || !row.id) {
        this.$message.error('无效的员工数据');
        return;
      }
      
      try {
        const res = await ajaxDelete('/employee/delete', row.id);
        
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

    // 重置密码
    async resetUserPassword(index, row) {
      if (!row || !row.id) {
        this.$message.error('无效的员工数据');
        return;
      }
      
      try {
        const data = {
          "id": row.id,
          "employeeId": row.employeeId
        };
        
        const res = await ajaxGet("/employee/resetUserPassword", data);
        
        if (res) {
          this.$message.success('密码重置成功');
        } else {
          this.$message.error('密码重置失败');
        }
      } catch (error) {
        console.error('密码重置操作失败:', error);
        this.$message.error('密码重置操作异常，请稍后重试');
      }
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
.employee-management {
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
  .employee-management {
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
