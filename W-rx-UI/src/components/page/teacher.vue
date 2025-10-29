<template>
	<div>
		<div class="crumbs">
			<el-breadcrumb separator="/">
				<el-breadcrumb-item>
					<i class="el-icon-lx-cascades"></i> 教师管理
				</el-breadcrumb-item>
			</el-breadcrumb>
		</div>
		<div class="container">
			<div class="handle-box">
				<template>
					<el-popconfirm @confirm="delAllSelection" confirm-button-text='确定' cancel-button-text='取消'
						icon="el-icon-info" icon-color="red" title="确定要删除吗?">
						<el-button size="mini" slot="reference" type="primary" icon="el-icon-delete" class="bgred mr10">
							批量删除</el-button>
					</el-popconfirm>
				</template>
				<!-- <el-button type="primary" icon="el-icon-delete" class="handle-del mr10" @click="delAllSelection">批量删除
			 </el-button> -->
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
				<el-table :data="tableData" border class="table" ref="multipleTable"
					header-cell-class-name="table-header" @selection-change="handleSelectionChange">
					<el-table-column type="selection" width="55" align="center"></el-table-column>
					<el-table-column prop="id" label="ID" width="55" align="center"></el-table-column>
					<el-table-column prop="jobId" label="工号"></el-table-column>
					<el-table-column prop="username" label="用户名"></el-table-column>
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

					<el-table-column prop="date" label="注册时间"></el-table-column>
					<el-table-column label="操作" width="180" align="center">
						<template slot-scope="scope">
							<el-button type="text" icon="el-icon-edit" class="mr10"
								@click="resetUserPassword(scope.$index, scope.row)">重置密码
							</el-button>
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
		<el-dialog title="新增监考老师" :visible.sync="addVisible" width="360px" :before-close="cancelInsertUser">
			<el-form ref="formAdd" :model="formAdd" status-icon :rules="rules" label-width="70px">
				<el-form-item label="姓名:" prop="username">
					<el-input size="mini" v-model="formAdd.username" class="handle-dialog-input mr10"></el-input>
				</el-form-item>
				<el-form-item label="工号:" prop="jobId">
					<el-input size="mini" v-model="formAdd.jobId" class="handle-dialog-input mr10"></el-input>
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
				multipleSelection: [],
				delList: [],
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
					username: '',
					jobId: '',
				},
				rules: {
					username: [{
						required: true,
						message: '请输入姓名',
						trigger: 'blur'
					}],
					jobId: [{
							required: true,
							message: '请输入工号',
							trigger: 'blur'
						},
						{
							validator: validateJobid,
							trigger: 'blur'
						}
					]
				},
				idx: -1,
				id: -1
			};
		},
		// created() {
		// 	this.getData();
		// },
		methods: {
			// 获取 easy-mock 的模拟数据
			getData() {

				ajaxGet("/user/allUser", this.query).then(res => {
					console.log(res);
					if (res.records) {
						this.tableShow = true;
						this.tableData = res.records;
						this.pageTotal = res.total;
						this.pages = res.pages;
					}
				});
			},
			insertUser() {
				this.$refs.formAdd.validate((valid) => {
					if (valid) {
						let data = {
							"username": this.formAdd.username,
							"jobId": this.formAdd.jobId
						};
						ajaxPost("/user/insertUser", data).then(res => {
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
				//this.$set(this.query, 'pageIndex', 1);
				this.query.pageIndex = 1;
				this.query.pageSize = 6;
				this.getData();
			},
			//重置
			handlerest() {
				this.tableData = [];
				this.tableShow = false;
				this.query.jobId = '';
				this.query.username = '';
				this.query.pageIndex = 1;
			},
			// 删除操作
			handleDelete(index, row) {

				console.log(row.id);
				ajaxDelete('/user', row.id).then(res => {
					if (res) {
						this.$message.success('删除成功');
						if (this.tableData.length === 1 && this.query.pageIndex !== 1 && this.query
							.pageIndex ===
							this.pages)
							this.query.pageIndex -= 1;
						console.log(this.query.pageIndex);
						this.getData();
						// this.tableData.splice(index, 1);
					} else {
						this.$message.error('删除失败');
					}
				})
			},
			// 多选操作
			handleSelectionChange(val) {
				this.multipleSelection = val;
			},
			delAllSelection() {
				const length = this.multipleSelection.length;
				// this.tableData.index
				if (length === 0) {
					this.$message('没有选中项');
				} else {
					var ids = this.multipleSelection.map(item => item.id);
					ajaxPost('/user/deleteAll', ids).then(res => {
						if (res) {

							this.$message.success('删除成功');
							console.log(this.tableData.length);
							if (this.tableData.length === this.multipleSelection.length && this.query
								.pageIndex !==
								1 && this.query.pageIndex === this.pages)
								this.query.pageIndex -= 1;

							this.getData();
							this.multipleSelection = [];
						} else {
							this.$message.error('删除失败');
						}
					})
				}
			},
			// 重置密码
			resetUserPassword(index, row) {
				let date = {
					"id": row.id,
					"jobId": row.jobId
				};
				ajaxGet("/user/restUserPassword", date).then(res => {
					if (res) {
						this.$message.success(`密码重置成功`);
					} else {
						this.$message.error(`密码重置失败`);
					}
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
				ajaxPost("/user/updateUser", data).then(res => {
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
