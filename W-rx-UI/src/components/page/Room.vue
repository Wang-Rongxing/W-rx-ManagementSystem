<script>
import {ajaxDelete, ajaxGet, ajaxPost} from "@/api";

export default {
  data() {
    return {
      tableData: [],
      total: 0,
      pageIndex: 1,
      pageSize: 10,
      searchForm: {
        roomNumber: '',
        roomType: '',
        status: ''
      },
      editVisible: false,
      addVisible: false,
      form: {
        roomId: '',
        roomNumber: '',
        roomType: '',
        price: '',
        status: '',
        description: ''
      },
      idx: 0,
      statusOptions: [
        { label: '全部', value: '' },
        { label: '空闲', value: 1 },
        { label: '已预订', value: 2 },
        { label: '已入住', value: 3 },
        { label: '待清洁', value: 4 }
      ],
      roomTypeOptions: [
        { label: '全部', value: '' },
        { label: '单人间', value: '单人间' },
        { label: '双人间', value: '双人间' },
        { label: '豪华间', value: '豪华间' },
        { label: '大床房', value: '大床房' }
      ]
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    // 获取表格数据
    getData() {
      let params = {
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
        roomNumber: this.searchForm.roomNumber,
        roomType: this.searchForm.roomType,
        status: this.searchForm.status
      }
      ajaxGet('/room/allRooms', { params: params })
        .then(res => {
          if (res && res.records) {
            this.tableData = res.records || []
            this.total = res.total || 0
          }
        })
        .catch(err => {
          console.log('获取客房数据失败', err)
          this.$message.error('获取客房数据失败')
        })
    },
    selectbyidandtypeandstatus(){
      let params = {
        roomNumber: this.searchForm.roomNumber,
        roomType: this.searchForm.roomType,
        status: this.searchForm.status
      }
      ajaxPost('/room/selectRoomByCondition', params)
        .then(res => {
          if (res && res.records) {
            this.tableData = res.records || []
            this.total = res.total || 0
          }
        })
        .catch(err => {
          console.log('根据条件查询客房失败', err)
          this.$message.error('根据条件查询客房失败')
        })
    },
    // 搜索
    search() {
      this.pageIndex = 1
      this.selectbyidandtypeandstatus()
    },
    // 清空搜索条件
    clearSearch() {
      this.searchForm = {
        roomNumber: '',
        roomType: '',
        status: ''
      }
      this.pageIndex = 1
      this.selectbyidandtypeandstatus()
    },
    // 分页
    handleCurrentChange(val) {
      this.pageIndex = val
      this.selectbyidandtypeandstatus()
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.selectbyidandtypeandstatus()
    },
    // 显示新增弹窗
    showAddDialog() {
      this.addVisible = true
      this.form = {
        roomNumber: '',
        roomType: '',
        price: '',
        status: 1,
        description: ''
      }
    },
    // 保存新增客房
    saveAdd() {
      if (!this.form.roomNumber) {
        this.$message.error('请输入客房编号')
        return
      }
      if (!this.form.roomType) {
        this.$message.error('请选择客房类型')
        return
      }
      if (!this.form.price || this.form.price <= 0) {
        this.$message.error('请输入有效的价格')
        return
      }
      
      ajaxPost('/room/insertRoom', this.form)
        .then(res => {
          if (res) {
            this.$message.success('新增客房成功')
            this.addVisible = false
            this.selectbyidandtypeandstatus()
          } else {
            this.$message.error('新增客房失败，可能客房编号已存在')
          }
        })
        .catch(err => {
          console.log('新增客房失败', err)
          this.$message.error('新增客房失败')
        })
    },
    // 显示编辑弹窗
    handleEdit(row, index) {
      this.editVisible = true
      this.idx = index
      this.form = {
        roomId: row.roomId,
        roomNumber: row.roomNumber,
        roomType: row.roomType,
        price: row.price,
        status: row.status,
        description: row.description
      }
    },
    // 保存编辑客房
    saveEdit() {
      if (!this.form.roomId) {
        this.$message.error('无效的客房数据')
        return
      }
      if (!this.form.roomNumber) {
        this.$message.error('请输入客房编号')
        return
      }
      if (!this.form.roomType) {
        this.$message.error('请选择客房类型')
        return
      }
      if (!this.form.price || this.form.price <= 0) {
        this.$message.error('请输入有效的价格')
        return
      }
      
      ajaxPost('/room/updateRoom', this.form)
        .then(res => {
          if (res) {
            this.$message.success('更新客房成功')
            this.editVisible = false
            this.selectbyidandtypeandstatus()
          } else {
            this.$message.error('更新客房失败')
          }
        })
        .catch(err => {
          console.log('更新客房失败', err)
          this.$message.error('更新客房失败')
        })
    },
    // 取消编辑
    cancelEdit() {
      this.editVisible = false
    },
    // 删除客房
    handleDelete(row) {
      this.$confirm('确定要删除该客房吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
          ajaxDelete('/room/delete', row.roomId)
            .then(res => {
              if (res) {
                this.$message.success('删除成功')
                this.getData()
              } else {
                this.$message.error('删除失败')
              }
            })
          .catch(err => {
            console.log('删除失败', err)
            this.$message.error('删除失败')
          })
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },
    // 获取状态文本
    getStatusText(status) {
      const statusMap = {
        1: '空闲',
        2: '已预订',
        3: '已入住',
        4: '待清洁'
      }
      return statusMap[status] || '未知'
    }
  }
}
</script>

<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 客房管理
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="searchForm">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="客房编号">
          <el-input v-model="searchForm.roomNumber" placeholder="请输入客房编号"></el-input>
        </el-form-item>
        <el-form-item label="客房类型">
          <el-select v-model="searchForm.roomType" placeholder="请选择客房类型">
            <el-option v-for="item in roomTypeOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态">
            <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">搜索</el-button>
          <el-button @click="clearSearch">清空</el-button>
          <el-button type="success" @click="showAddDialog">新增客房</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="tableBox">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="roomId" label="ID" width="80"></el-table-column>
        <el-table-column prop="roomNumber" label="客房编号" width="120"></el-table-column>
        <el-table-column prop="roomType" label="客房类型" width="120"></el-table-column>
        <el-table-column prop="price" label="价格(元/天)" width="120">
          <template slot-scope="scope">
            {{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            {{ getStatusText(scope.row.status) }}
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述"></el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row, scope.$index)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          background
          layout="prev, pager, next, jumper, sizes, total"
          :total="total"
          :page-size="pageSize"
          :page-sizes="[5, 10, 20, 50]"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        ></el-pagination>
      </div>
    </div>
    <!-- 新增客房弹窗 -->
    <el-dialog title="新增客房" :visible.sync="addVisible" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="客房编号">
          <el-input v-model="form.roomNumber" placeholder="请输入客房编号"></el-input>
        </el-form-item>
        <el-form-item label="客房类型">
          <el-select v-model="form.roomType" placeholder="请选择客房类型">
            <el-option label="单人间" value="单人间"></el-option>
            <el-option label="双人间" value="双人间"></el-option>
            <el-option label="豪华间" value="豪华间"></el-option>
            <el-option label="大床房" value="大床房"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价格(元/天)">
          <el-input v-model.number="form.price" type="number" min="0" placeholder="请输入价格"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="空闲" value="1"></el-option>
            <el-option label="已预订" value="2"></el-option>
            <el-option label="已入住" value="3"></el-option>
            <el-option label="待清洁" value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" placeholder="请输入客房描述"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAdd">确定</el-button>
      </div>
    </el-dialog>
    <!-- 编辑客房弹窗 -->
    <el-dialog title="编辑客房" :visible.sync="editVisible" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="客房编号">
          <el-input v-model="form.roomNumber" placeholder="请输入客房编号"></el-input>
        </el-form-item>
        <el-form-item label="客房类型">
          <el-select v-model="form.roomType" placeholder="请选择客房类型">
            <el-option label="单人间" value="单人间"></el-option>
            <el-option label="双人间" value="双人间"></el-option>
            <el-option label="豪华间" value="豪华间"></el-option>
            <el-option label="大床房" value="大床房"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价格(元/天)">
          <el-input v-model.number="form.price" type="number" min="0" placeholder="请输入价格"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="空闲" value="1"></el-option>
            <el-option label="已预订" value="2"></el-option>
            <el-option label="已入住" value="3"></el-option>
            <el-option label="待清洁" value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" placeholder="请输入客房描述"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelEdit">取消</el-button>
        <el-button type="primary" @click="saveEdit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

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