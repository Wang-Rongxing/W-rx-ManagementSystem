<template>
	<div>
		<div class="crumbs">
			<el-breadcrumb separator="/">
				<el-breadcrumb-item>
					<i class="el-icon-lx-cascades"></i> 酒店员工
				</el-breadcrumb-item>
			</el-breadcrumb>
		</div>
		<div class="container">
			<div class="handle-box">
				<el-button size="mini" type="primary" icon="el-icon-circle-plus-outline" @click="handleAddUser">新增
				</el-button>
			</div>

			<div class="handle-box">

				<el-input size="mini" v-model="query.jobId" placeholder="工号" class="handle-select mr10"></el-input>
				<el-input size="mini" v-model="query.username" placeholder="用户名" class="handle-input mr10"></el-input>
				<el-button size="mini" type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
				<el-button size="mini" type="primary" class="yel" @click="handlerest">重置</el-button>
			</div>


			<div v-show="tableShow">
				<el-table :data="tableData" border class="table">
					<el-table-column prop="id" label="ID" width="55" align="center"></el-table-column>
          <el-table-column prop="name" label="用户名"></el-table-column>
					<el-table-column prop="employeeId" label="工号"></el-table-column>
					<!-- <el-table-column label="头像(查看大图)" align="center">
						<template slot-scope="scope">
							<el-image class="table-td-thumb" :src="scope.row.thumb"
								:preview-src-list="[scope.row.thumb]">
							</el-image>
						</template>
					</el-table-column>
					<el-table-column prop="address" label="地址"></el-table-column>
					<el-table-column label="状态" align="center">
						<template slot-scope="scope">
							<el-tag :type="scope.row.state==='成功'?'success':(scope.row.state==='失败'?'danger':'')">
								{{scope.row.state}}
							</el-tag>
						</template>
					</el-table-column> -->

					<el-table-column prop="phone" label="电话"></el-table-column>
					<el-table-column label="操作" width="180" align="center">
						<template slot-scope="scope">
							<el-popconfirm @confirm="resetUserPassword(scope.$index, scope.row)" confirm-button-text='确定'
								cancel-button-text='取消' icon-color="#f56c6c" title="确定要重置密码吗?">
								<el-button slot="reference" type="text" icon="el-icon-edit" class="mr10">重置密码
								</el-button>
							</el-popconfirm>
							<template>
								<el-popconfirm @confirm="handleDelete(scope.$index, scope.row)" confirm-button-text='确定'
									cancel-button-text='取消' icon-color="red" title="确定要删除吗?">
									<el-button slot="reference" type="text" icon="el-icon-delete" class="red">删除
									</el-button>
								</el-popconfirm>
							</template>
							<!-- <el-button type="text" icon="el-icon-delete" class="red"
								@click="handleDelete(scope.$index, scope.row)">删除</el-button> -->
						</template>
					</el-table-column>
				</el-table>
				<div class="pagination">
					<el-pagination background layout="total, prev, pager, next" :current-page="query.pageIndex"
						:page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange">
					</el-pagination>
				</div>
			</div>
		</div>

		<!-- 编辑弹出框 -->
		<el-dialog title="修改教师信息" :visible.sync="editVisible" width="360px">
			<el-form ref="form" :model="form" label-width="70px">
				<el-form-item label="姓名:">
					<el-input size="mini" v-model="form.username" class="handle-dialog-input mr10"></el-input>
				</el-form-item>
				<el-form-item label="工号:">
					<el-input size="mini" v-model="form.jobId" class="handle-dialog-input mr10"></el-input>
				</el-form-item>
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button size="mini" @click="cancelEdit">取 消</el-button>
				<el-button size="mini" type="primary" @click="saveEdit">确 定</el-button>
			</span>
		</el-dialog>
		<el-dialog title="新增员工" :visible.sync="addVisible" width="360px" :before-close="cancelInsertUser">
			<el-form ref="formAdd" :model="formAdd" status-icon :rules="rules" label-width="70px">
				<el-form-item label="姓名:" prop="name">
					<el-input size="mini" v-model="formAdd.name" class="handle-dialog-input mr10"></el-input>
				</el-form-item>
				<el-form-item label="工号:" prop="employeeId">
					<el-input size="mini" v-model="formAdd.employeeId" class="handle-dialog-input mr10"></el-input>
				</el-form-item>
        <el-form-item label="密码:" prop="password">
          <el-input size="mini" v-model="formAdd.password" type="password" class="handle-dialog-input mr10"></el-input>
        </el-form-item>
        <el-form-item label="电话:" prop="phone">
            <el-input size="mini" v-model="formAdd.phone" class="handle-dialog-input mr10"></el-input>
        </el-form-item>
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button size="mini" @click="cancelInsertUser">取 消</el-button>
				<el-button size="mini" type="primary" @click="insertUser">确 定</el-button>
			</span>

		</el-dialog>
	</div>
</template>

<script>
	import {
		fetchData,
		ajaxPost,
		ajaxGet,
		ajaxDelete
	} from '../../api/index';
	// import {
	// 	ajaxPost
	// } from '../../api/index';
	export default {
		name: 'basetable',
		data() {
				var validateJobid = (rule, value, callback) => {
					let reg = /^[0-9]*$/;
					if (!reg.test(value))
						return callback(new Error('工号必须是数字符号'));
					else
						return callback();
				};
				return {
					query: {
						jobId: '',
						username: '',
						pageIndex: 1,
						pageSize: 6
					},
					tableData: [],
					tableShow: false,
					editVisible: false,
					addVisible: false,
					pageTotal: 0,
					pages: 0,
					form: {
						temppassword: '',
						tempusername: '',
					},
					formAdd: {
						name: '',
						employeeId: '',
						password: '',
						phone: '',
					},
					rules: {
						name: [{
							required: true,
							message: '请输入姓名',
							trigger: 'blur'
						}],
						employeeId: [{
							required: true,
							message: '请输入工号',
							trigger: 'blur'
						},
							{
								validator: validateJobid,
								trigger: 'blur'
							}
						],
						password: [{
							required: true,
							message: '请输入密码',
							trigger: 'blur'
						}]
					},
					idx: -1,
					id: -1
			};
		},
		created() {
			this.getData();
		},
		methods: {
			// 获取 easy-mock 的模拟数据
				getData() {
					ajaxGet("/employee/allUser", this.query).then(res => {
						console.log(res);
						if (res && res.records) {
							this.tableShow = true;
							this.tableData = res.records;
							this.pageTotal = res.total || 0;
							this.pages = res.pages || 0;
						} else {
							this.tableShow = false;
							this.tableData = [];
						}
					}).catch(error => {
						console.error("获取员工列表失败:", error);
						this.$message.error("获取数据失败，请稍后重试");
					});
				},
      //根据id和name查询
      getDataByIdOrName() {
        // 构造正确的参数格式
        let searchParams = {
          employeeId: this.query.jobId,
          name: this.query.username
        };
        ajaxPost("/employee/selectEmployeeByIdOrName", searchParams).then(res => {
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
        }).catch(error => {
          console.error("搜索请求失败:", error);
          this.$message.error("搜索操作异常，请稍后重试");
        });
      },
			insertUser() {
				this.$refs.formAdd.validate((valid) => {
					if (valid) {
						let data = {
							"name": this.formAdd.name,
							"employeeId": this.formAdd.employeeId,
              "password": this.formAdd.password,
              "phone": this.formAdd.phone
						};
						ajaxPost("/employee/insertUser", data).then(res => {
							if (res) {
								this.$message.success({
									message: '添加成功',
									center: true
								});
								this.$refs.formAdd.resetFields();
								// this.formAdd.username = '';
								// this.formAdd.jobId = '';
								this.addVisible = false;
							} else {
								this.$message.error({
									message: '添加失败',
									center: true
								});
							}
						});
					} else {
						return false;
					}
				});
			},
			cancelInsertUser() {
				this.$refs.formAdd.resetFields();
				// this.formAdd.username = '';
				// this.formAdd.jobId = '';
				this.addVisible = false;
			},
			handleAddUser() {
					this.addVisible = true;
				},
			// 触发搜索按钮
				handleSearch() {
					this.query.pageIndex = 1;
					this.query.pageSize = 6;
					this.getDataByIdOrName();
				},
			//重置
			handlerest() {
				this.query.jobId = '';
				this.query.username = '';
				this.query.pageIndex = 1;
				this.getData();
			},
			// 删除操作
			handleDelete(index, row) {
				if (!row || !row.id) {
					this.$message.error('无效的员工数据');
					return;
				}
				console.log('删除员工ID:', row.id);
				ajaxDelete('/employee/delete', row.id).then(res => {
					if (res) {
						this.$message.success('删除成功');
						// 处理最后一页只有一条数据的情况
						if (this.tableData.length === 1 && this.query.pageIndex !== 1 && this.query.pageIndex === this.pages) {
							this.query.pageIndex -= 1;
						}
						// 重新获取数据更新列表
						this.getData();
					} else {
						this.$message.error('删除失败，请重试');
					}
				}).catch(error => {
					console.error('删除操作失败:', error);
					this.$message.error('删除操作异常，请稍后重试');
				})
			},

			// 重置密码
			resetUserPassword(index, row) {
				if (!row || !row.id) {
					this.$message.error('无效的员工数据');
					return;
				}
				let data = {
					"id": row.id,
					"employeeId": row.employeeId
				};
				console.log('重置密码请求:', data);
				ajaxGet("/employee/resetUserPassword", data).then(res => {
					if (res) {
						this.$message.success(`密码重置成功`);
					} else {
						this.$message.error(`密码重置失败`);
					}
				}).catch(error => {
					console.error('密码重置操作失败:', error);
					this.$message.error('密码重置操作异常，请稍后重试');
				})
			},
			// 保存编辑
			saveEdit() {
				console.log(this.form);
				let data = {
					"id": this.form.id,
					"username": this.form.username,
					"password": this.form.password
				};
				ajaxPost("/employee/updateUser", data).then(res => {
					if (res) {
						this.editVisible = false;
						this.$message.success(`修改第 ${this.idx + 1} 行成功`);
					} else {
						this.editVisible = false;
						this.form.username = this.form.tempusername;
						this.form.password = this.form.temppassword;
						this.$message.success(`修改第 ${this.idx + 1} 行失败`);
					}
					this.$set(this.tableData, this.idx, this.form);
				})
			},
			//取消编辑
			cancelEdit() {
				this.editVisible = false;
				this.form.username = this.form.tempusername;
				this.form.password = this.form.temppassword;
				this.$set(this.tableData, this.idx, this.form);
			},
			// 分页导航
			handlePageChange(val) {
				this.$set(this.query, 'pageIndex', val);
				this.getData();
			}
		}
	};
</script>

<style scoped>
	.handle-box {
		margin-bottom: 20px;
	}

	.handle-select {
		width: 120px;
	}

	.handle-input {
		width: 180px;
		display: inline-block;
	}

	.handle-dialog-input {
		width: 200px;
		display: inline-block;
	}

	.table {
		width: 100%;
		font-size: 10px;
	}

	.red {
		color: #ff0000;

	}

	.yel {
		background-color: #ffaa00;

	}

	.bgred {
		background-color: #ff6d53;
	}

	.mr10 {
		margin-right: 10px;
	}
</style>
