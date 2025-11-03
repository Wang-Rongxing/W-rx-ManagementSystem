<template>
  <div class="permission-management">
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 权限管理
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    
    <div class="container">
      <!-- 搜索区域 -->
      <div class="search-box">
        <el-input size="small" v-model="query.name" placeholder="用户名" class="handle-input mr10"></el-input>
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
          <el-table-column prop="name" label="用户名" min-width="150"></el-table-column>
          <el-table-column prop="employeeId" label="工号" min-width="100"></el-table-column>
          
          <el-table-column label="权限" min-width="180">
            <template slot-scope="scope">
              <div class="role-list">
                <el-tag 
                  v-for="(role, index) in scope.row.roleList" 
                  :key="index"
                  size="small"
                  type="info"
                  class="role-tag"
                >
                  {{ role.name }}
                </el-tag>
                <span v-if="!scope.row.roleList || scope.row.roleList.length === 0" class="no-role">无权限</span>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column label="权限管理" width="120" align="center">
            <template slot-scope="scope">
              <el-popconfirm 
                @confirm="setRoles(scope.$index, scope.row)" 
                confirm-button-text='确定'
                cancel-button-text='取消' 
                icon-color="#f56c6c" 
                title="确定要修改该用户的权限吗?"
              >
                <el-button 
                  slot="reference" 
                  type="primary" 
                  size="small" 
                  icon="el-icon-menu" 
                  :disabled="scope.row.name=='admin'"
                >
                  修改权限
                </el-button>
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

    <!-- 修改权限弹出框 -->
    <el-dialog 
      title="修改权限" 
      :visible.sync="editVisible" 
      width="400px" 
      :before-close="cancelEdit"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form label-width="100px">
        <el-form-item label="姓名:">
          <el-input v-model="form.name" disabled></el-input>
        </el-form-item>
        <el-form-item label="工号:">
          <el-input v-model="form.employeeId" disabled></el-input>
        </el-form-item>
        <el-form-item label="权限:">
          <el-tree 
            :data="roleData" 
            :props="defaultProps"
            @check="handleTreeChecked" 
            :default-expand-all="true" 
            ref="tree" 
            node-key="id" 
            show-checkbox
            check-strictly
          ></el-tree>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelEdit">取消</el-button>
        <el-button type="primary" @click="saveEdit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  ajaxPost,
  ajaxGet
} from '../../api/index';

export default {
  name: 'Permission',
  data() {
    return {
      // 查询参数
      query: {
        employeeId: '',
        name: '',
        pageIndex: 1,
        pageSize: 10
      },
      // 表格数据
      tableData: [],
      // 显示控制
      tableShow: false,
      loading: false,
      editVisible: false,
      // 分页信息
      pageTotal: 0,
      pages: 0,
      // 表单数据
      form: {
        id: '',
        name: '',
        employeeId: '',
        roleList: [],
        tempRoleList: []
      },
      // 权限树数据
      roleData: [
        {
          id: 1,
          name: '经理'
        },
        {
          id: 2,
          name: '前台'
        },
        {
          id: 3,
          name: '客房管理员'
        }
      ],
      // 权限树配置
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    };
  },
  
  created() {
    // 组件创建时获取数据
    this.getData();
  },
  
  methods: {
    // 获取用户权限列表数据
    async getData() {
      try {
        this.loading = true;
        const res = await ajaxGet("/employee/userWithRoleByPage", this.query);
        
        if (res && res.records) {
          // 确保每个员工都有roleList字段
          const recordsWithRoleList = res.records.map(employee => ({
            ...employee,
            roleList: employee.roleList || []
          }));
          
          this.tableShow = true;
          this.tableData = recordsWithRoleList;
          this.pageTotal = res.total || 0;
          this.pages = res.pages || 0;
        } else {
          this.tableShow = false;
          this.tableData = [];
          this.pageTotal = 0;
        }
      } catch (error) {
        console.error("获取用户权限列表失败:", error);
        this.$message.error("获取数据失败，请稍后重试");
        this.tableShow = false;
        this.tableData = [];
      } finally {
        this.loading = false;
      }
    },
    
    // 根据工号和姓名查询
    async getDataRoleByIdOrName() {
      try {
        this.loading = true;
        
        // 构造正确的参数格式
        const searchParams = {
          employeeId: this.query.employeeId,
          name: this.query.name
        };
        
        const res = await ajaxPost("/employee/selectEmployeeByIdOrName", searchParams);
        
        if (res && res.records) {
          // 确保每个员工都有roleList字段
          const recordsWithRoleList = res.records.map(employee => ({
            ...employee,
            roleList: employee.roleList || []
          }));
          
          this.tableShow = true;
          this.tableData = recordsWithRoleList;
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
    
    // 触发搜索按钮
    handleSearch() {
      this.query.pageIndex = 1;
      this.getDataRoleByIdOrName();
    },
    
    // 重置搜索条件
    handlerest() {
      this.query.employeeId = '';
      this.query.name = '';
      this.query.pageIndex = 1;
      this.getData();
    },
    
    // 修改角色权限
    setRoles(index, row) {
      // 深拷贝，避免修改原数据
      this.form = JSON.parse(JSON.stringify(row));
      this.form.tempRoleList = JSON.parse(JSON.stringify(row.roleList || []));
      this.editVisible = true;
      
      // 下一帧设置选中项
      this.$nextTick(() => {
        if (this.$refs.tree) {
          // 清空所有选中
          this.$refs.tree.setCheckedKeys([]);
          // 设置当前用户的权限
          if (row.roleList && row.roleList.length > 0) {
            const roleIds = row.roleList.map(role => role.id);
            this.$refs.tree.setCheckedKeys(roleIds);
          }
        }
      });
    },
    
    // 权限树选择变化处理
    handleTreeChecked() {
      if (this.$refs.tree) {
        // 获取选中的权限节点
        this.form.roleList = this.$refs.tree.getCheckedNodes(false, true);
      }
    },
    
    // 提交修改角色权限
    async saveEdit() {
      try {
        // 检查是否选择了权限
        if (!this.form.roleList || this.form.roleList.length === 0) {
          this.$message.warning('请至少选择一个权限');
          return;
        }
        
        const res = await ajaxPost("/employee/updateUserRole", this.form);
        
        if (res) {
          this.editVisible = false;
          this.$message.success(`${this.form.name}的角色权限修改成功`);
          // 重新获取数据更新列表
          this.getData();
        } else {
          this.editVisible = false;
          this.$message.error(`${this.form.name}的角色权限修改失败`);
        }
      } catch (error) {
        console.error("更新用户权限失败:", error);
        this.editVisible = false;
        this.$message.error('权限修改操作异常，请稍后重试');
      }
    },
    
    // 取消权限设置
    cancelEdit() {
      this.form.roleList = this.form.tempRoleList;
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
.permission-management {
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

/* 角色标签样式 */
.role-list {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.role-tag {
  margin-bottom: 5px;
}

.no-role {
  color: #909399;
  font-size: 12px;
  font-style: italic;
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

/* 权限树样式 */
:deep(.el-tree) {
  max-height: 300px;
  overflow-y: auto;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .permission-management {
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
  
  .role-list {
    justify-content: center;
  }
}

.mr10 {
  margin-right: 10px;
}
</style>
