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
      <el-button type="primary" size="small" @click="showAddForm">新增入住登记</el-button>
    </div>

    <!-- 搜索表单 -->
    <div class="searchForm">
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="客户姓名">
          <el-input v-model="searchForm.customerName" placeholder="客户姓名" clearable></el-input>
        </el-form-item>
        <el-form-item label="客户电话">
          <el-input v-model="searchForm.customerPhone" placeholder="客户电话" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="small" @click="search">查询</el-button>
          <el-button size="small" @click="clearSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 入住登记表格 -->
    <div class="tableBox">
      <el-table :data="tableData" style="width: 100%" :empty-text="loading ? '加载中...' : '暂无数据'">
        <el-table-column prop="checkInId" label="登记ID" width="100"></el-table-column>
        <el-table-column prop="customerName" label="客户姓名" width="150"></el-table-column>
        <el-table-column prop="customerPhone" label="客户电话" width="180"></el-table-column>
        <el-table-column prop="roomType" label="客房类型" width="180"></el-table-column>
        <el-table-column prop="roomNumber" label="客房编号" width="150"></el-table-column>
        <el-table-column prop="actualCheckIn" label="入住日期" width="200" :formatter="formatDate"></el-table-column>
        <el-table-column prop="actualCheckOut" label="退房日期" width="200" :formatter="formatDate"></el-table-column>
        <el-table-column prop="actualCheckOut" label="状态" width="120">
          <template slot-scope="scope">
            <el-tag :type="scope.row.actualCheckOut ? 'success' : 'warning'" style="min-width: 60px; text-align: center;">
              {{ scope.row.actualCheckOut ? '已退房' : '入住中' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button 
              v-if="!scope.row.actualCheckOut" 
              type="warning" 
              size="small" 
              @click="handleCheckOut(scope.row)"
              :loading="loading"
            >退房</el-button>
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
          :page-sizes="[5, 10, 20, 50]"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        ></el-pagination>
      </div>
    </div>

    <!-- 新增入住登记表单对话框 -->
    <el-dialog 
      title="新增入住登记" 
      :visible.sync="dialogVisible" 
      width="50%"
      :close-on-click-modal="false"
    >
      <el-form :model="addForm" :rules="rules" ref="addFormRef" label-width="100px" size="small">
        <el-form-item label="客户姓名" prop="customerName">
          <el-input v-model="addForm.customerName" placeholder="请输入客户姓名" clearable></el-input>
        </el-form-item>
        <el-form-item label="客户电话" prop="customerPhone">
          <el-input v-model="addForm.customerPhone" placeholder="请输入客户电话" clearable></el-input>
        </el-form-item>
        <el-form-item label="客房类型" prop="roomType">
          <el-select 
            v-model="addForm.roomType" 
            placeholder="请选择客房类型" 
            clearable
            @change="onRoomTypeChange"
          >
            <el-option
              v-for="item in roomTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="客房编号" prop="roomNumber">
          <el-select 
            v-model="addForm.roomNumber" 
            placeholder="请选择客房编号" 
            clearable
          >
            <el-option
              v-for="item in availableRooms"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" size="small" @click="submitAddForm" :loading="submitting">确定</el-button>
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
        roomType: '',
        roomNumber: ''
      },
      // 客房类型选项
      roomTypeOptions: [],
      // 空闲客房列表
      availableRooms: [],
      // 表单验证规则
      rules: {
        customerName: [
          { required: true, message: '请输入客户姓名', trigger: 'blur' },
          { min: 2, max: 20, message: '客户姓名长度应在2-20个字符之间', trigger: 'blur' }
        ],
        customerPhone: [
          { required: true, message: '请输入客户电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号码', trigger: 'blur' }
        ],
        roomType: [
          { required: true, message: '请选择客房类型', trigger: 'change' }
        ],
        roomNumber: [
            { required: true, message: '请选择客房编号', trigger: 'change' }
          ]
      },
      // 表格数据
      tableData: [],
      // 分页参数
      pageIndex: 1,
      pageSize: 10,
      total: 0,
      // 对话框显示状态
      dialogVisible: false,
      // 加载状态
      loading: false,
      // 提交状态
      submitting: false
    };
  },
  created() {
    // 组件创建时获取数据
    this.getData();
    this.loadRoomTypes();
  },
  methods: {
    // 格式化日期
    formatDate(row, column, cellValue) {
      const date = cellValue || row[column.property];
      if (!date || date === null || date === '') {
        return '';
      }
      
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
    
    // 根据客户姓名和电话搜索
    async searchByCustomerInfo() {
      this.loading = true;
      const params = {
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
        customerName: this.searchForm.customerName,
        customerPhone: this.searchForm.customerPhone
      };

      try {
        const res = await ajaxGet('/checkin/selectbynameandphone', params);
        if (res && res.records) {
          this.tableData = res.records;
          this.total = res.total || 0;
          this.pageSize = res.pageSize || this.pageSize;
          this.pageIndex = res.pageIndex || this.pageIndex;
        } else {
          this.$message.error('获取入住数据失败');
          this.tableData = [];
          this.total = 0;
        }
      } catch (err) {
        console.error('搜索入住数据异常:', err);
        this.$message.error('搜索入住数据异常');
        this.tableData = [];
        this.total = 0;
      } finally {
        this.loading = false;
      }
    },
    
    // 处理退房
    async handleCheckOut(row) {
      try {
        await this.$confirm('确定要为该客户办理退房吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });
        
        this.loading = true;
        const res = await ajaxPost(`/checkin/checkOut`, { checkInId: row.checkInId });
        
        if (res && res.code === '200') {
          this.$message.success('退房成功');
          await this.getData();
        } else {
          this.$message.error(res.message || '退房失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('退房失败:', error);
          this.$message.error('退房失败');
        } else {
          this.$message.info('已取消退房');
        }
      } finally {
        this.loading = false;
      }
    },
    
    // 获取入住登记数据
    async getData() {
      this.loading = true;
      const params = {
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
        customerName: this.searchForm.customerName,
        customerPhone: this.searchForm.customerPhone
      };
      
      try {
        const res = await ajaxGet('/checkin/allCheckin', params);
        if (res && res.code === '200') {
          this.tableData = res.records || [];
          this.total = res.total || 0;
        } else {
          this.$message.error(res.message || '获取数据失败');
          this.tableData = [];
          this.total = 0;
        }
      } catch (err) {
        console.error('获取数据失败:', err);
        this.$message.error('获取数据失败');
        this.tableData = [];
        this.total = 0;
      } finally {
        this.loading = false;
      }
    },
    
    // 查询
    search() {
      this.pageIndex = 1;
      this.searchByCustomerInfo();
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
          roomType: '',
          roomNumber: ''
        };
        this.dialogVisible = true;
      },
      
      // 加载客房类型 - 从客房表获取
      async loadRoomTypes() {
        try {
          // 调用后端API获取客房表中的客房类型数据
          const response = await ajaxGet('/room/types');
          
          // 增强数据格式处理，支持多种后端返回格式
          let types = [];
          
          // 尝试不同的数据格式解析方式
          if (Array.isArray(response)) {
            // 直接返回数组
            types = response;
          } else if (response && response.data) {
            // 带data字段
            if (Array.isArray(response.data)) {
              types = response.data;
            } else if (typeof response.data === 'string') {
              // 如果data是字符串，尝试解析为JSON
              try {
                const parsed = JSON.parse(response.data);
                types = Array.isArray(parsed) ? parsed : [response.data];
              } catch {
                types = [response.data];
              }
            }
          } else if (response && response.success && response.data) {
            // 标准成功格式 {success: true, data: [...]}
            types = Array.isArray(response.data) ? response.data : [response.data];
          } else if (response && response.code === '200' && response.data) {
            // 可能的状态码格式 {code: '200', data: [...]}
            types = Array.isArray(response.data) ? response.data : [response.data];
          }
          
          // 如果还是没有获取到数据，使用默认值
          if (!types || types.length === 0) {
            console.warn('未能从客房表获取类型，使用默认类型');
            types = ['标准间', '大床房', '豪华套房', '商务套房'];
          }
          
          console.log('获取到的原始客房类型数据:', types);
          
          // 数据规范化处理
          const normalizedTypes = [];
          for (const item of types) {
            if (item && typeof item === 'string') {
              // 字符串类型
              normalizedTypes.push(item);
            } else if (item && typeof item === 'object') {
              // 对象类型，尝试提取有意义的字段
              if (item.roomType) normalizedTypes.push(item.roomType);
              else if (item.value) normalizedTypes.push(item.value);
              else if (item.name) normalizedTypes.push(item.name);
              else if (item.type) normalizedTypes.push(item.type);
            }
          }
          
          // 将数据转换为select组件需要的格式
          this.roomTypeOptions = normalizedTypes
            .filter(type => type && typeof type === 'string' && type.trim())
            .map(type => ({
              value: type.trim(),
              label: type.trim()
            }));
            
          // 如果转换后没有数据，使用默认选项
          if (this.roomTypeOptions.length === 0) {
            console.warn('客房类型数据为空，使用默认类型');
            this.roomTypeOptions = [
              { value: '标准间', label: '标准间' },
              { value: '大床房', label: '大床房' },
              { value: '豪华套房', label: '豪华套房' },
              { value: '商务套房', label: '商务套房' }
            ];
          }
          
        } catch (error) {
          console.error('加载客房类型失败:', error);
          this.$message.error('加载客房类型失败，使用默认类型');
          // 出错时提供默认值，确保系统可用
          this.roomTypeOptions = [
            { value: '标准间', label: '标准间' },
            { value: '大床房', label: '大床房' },
            { value: '豪华套房', label: '豪华套房' },
            { value: '商务套房', label: '商务套房' }
          ];
        }
      },
      
      // 客房类型变更时加载空闲客房
      async onRoomTypeChange(value) {
        // 重置客房编号
        this.addForm.roomNumber = '';
        // 清空之前加载的选项
        this.availableRooms = [];
        
        if (!value) {
          return;
        }
        
        try {
          // 调用后端API获取指定类型且状态为空闲的客房
          const response = await ajaxGet('/room/selectRoomByroomTypeAndStatus', {
              roomType: value,
              status: 1 // 明确指定查询空闲状态的客房
          });
          
          let rooms = [];
          console.log(response)
          // 增强数据格式处理，支持多种后端返回格式
          if (Array.isArray(response)) {
            // 直接返回数组
            rooms = response;
          } else if (response && response.data) {
            // 带data字段
            if (Array.isArray(response.data)) {
              rooms = response.data;
            } else if (typeof response.data === 'string') {
              // 如果data是字符串，尝试解析为JSON
              try {
                const parsed = JSON.parse(response.data);
                rooms = Array.isArray(parsed) ? parsed : [parsed];
              } catch {
                rooms = [];
              }
            } else if (typeof response.data === 'object') {
              // 如果data是对象，检查是否有records等字段
              if (response.data.records && Array.isArray(response.data.records)) {
                rooms = response.data.records;
              } else {
                rooms = [response.data];
              }
            }
          } else if (response && response.success && response.data) {
            // 标准成功格式 {success: true, data: [...]}
            rooms = Array.isArray(response.data) ? response.data : [response.data];
          } else if (response && response.code === '200' && response.data) {
            // 可能的状态码格式 {code: '200', data: [...]}
            rooms = Array.isArray(response.data) ? response.data : [response.data];
          }
          
          console.log('获取到的原始客房数据:', rooms);
          
          // 确保rooms是数组格式
          if (!Array.isArray(rooms)) {
            rooms = [];
          }
          
          // 提取客房数据并创建下拉选项
          console.log('处理前的客房数据数量:', rooms.length);
          

          // 数据规范化处理
          const normalizedRooms = [];
          for (const item of rooms) {
            if (item && typeof item === 'string') {
              // 字符串类型
              normalizedRooms.push(item);
            } else if (item && typeof item === 'object') {
              // 对象类型，尝试提取有意义的字段
              if (item.roomType) normalizedRooms.push(item.roomType);
              else if (item.value) normalizedRooms.push(item.value);
              else if (item.name) normalizedRooms.push(item.name);
              else if (item.type) normalizedRooms.push(item.type);
            }
          }

          // 将数据转换为select组件需要的格式
          this.availableRooms = normalizedRooms
              .filter(type => type && typeof type === 'string' && type.trim())
              .map(type => ({
                value: type.trim(),
                label: type.trim()
              }));


          
          console.log('处理后的可用客房数量:', this.availableRooms.length);
          console.log('处理后的可用客房数据:', this.availableRooms);
          
          // 如果没有找到对应类型的空闲客房，显示提示信息
          if (this.availableRooms.length === 0) {
            this.$message.warning(`当前${value}类型暂无空闲客房或数据格式不匹配`);
          }
          
        } catch (error) {
          console.error('加载空闲客房失败:', error);
          this.$message.error('加载空闲客房失败，请稍后重试');
          this.availableRooms = [];
        }
      },
    
    // 提交新增表单
    async submitAddForm() {
      try {
        const valid = await this.$refs.addFormRef.validate();
        if (valid) {
          this.submitting = true;
          const res = await ajaxPost('/checkin/add', {
            customerName: this.addForm.customerName,
            customerPhone: this.addForm.customerPhone,
            roomType: this.addForm.roomType,
            roomNumber: this.addForm.roomNumber
          });
          
          if (res && res.code === '200') {
            this.$message.success('新增成功');
            this.dialogVisible = false;
            await this.getData();
          } else {
            this.$message.error(res.message || '新增失败');
          }
        }
      } catch (err) {
        if (err !== false) { // 排除表单验证失败的情况
          console.error('新增失败:', err);
          this.$message.error('新增失败');
        }
      } finally {
        this.submitting = false;
      }
    },
    
    // 删除功能已移除
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
  display: flex;
  align-items: center;
  gap: 10px;
}

.searchForm {
  background-color: #fff;
  padding: 20px;
  border-radius: 5px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.tableBox {
  background-color: #fff;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

/* 优化表格样式 */
.el-table {
  border: none;
}

.el-table th {
  background-color: #f5f7fa;
  font-weight: 500;
  padding: 8px;
}

.el-table td {
  padding: 8px;
}

.el-table tr:hover {
  background-color: #f5f7fa;
}

/* 优化按钮间距 */
.el-table-column--operation .el-button {
  margin-right: 5px;
}

/* 紧凑表格布局 */
.el-table .cell {
  padding: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .app-container {
    padding: 10px;
  }
  
  .searchForm {
    padding: 15px;
  }
  
  .tableBox {
    padding: 15px;
    overflow-x: auto;
  }
  
  .el-form--inline .el-form-item {
    display: block;
    margin-right: 0;
    margin-bottom: 10px;
  }
  
  .pagination {
    text-align: center;
    margin-top: 15px;
  }
  
  .el-table {
    font-size: 12px;
  }
  
  .el-table .cell {
    padding: 0 8px;
  }
}
</style>