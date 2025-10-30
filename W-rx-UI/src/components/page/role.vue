<template>
	<div>
		<div class="crumbs">
			<el-breadcrumb separator="/">
				<el-breadcrumb-item>
					<i class="el-icon-lx-cascades"></i> 角色管理
				</el-breadcrumb-item>
			</el-breadcrumb>
		</div>
		<div class="container">
			<!-- <div class="handle-box">
				<template>
					<el-popconfirm @confirm="delAllSelection" confirm-button-text='确定' cancel-button-text='取消'
						icon="el-icon-info" icon-color="red" title="确定要删除吗?">
						<el-button size="mini" slot="reference" type="primary" icon="el-icon-delete" class="bgred">批量删除</el-button>
					</el-popconfirm>
				</template>
				<el-button type="primary" icon="el-icon-delete" class="handle-del mr10" @click="delAllSelection">批量删除
				</el-button>
				<el-select v-model="query.address" placeholder="地址" class="handle-select mr10">
					<el-option key="1" label="广东省" value="广东省"></el-option>
					<el-option key="2" label="湖南省" value="湖南省"></el-option>
				</el-select>
				<el-input v-model="query.username" placeholder="用户名" class="handle-input mr10"></el-input>
				<el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
				<el-button type="primary" class="yel" @click="handlerest">重置</el-button>
			</div> -->
			<div v-show="tableShow">
				<el-table :data="tableData" border class="table" ref="multipleTable"
					header-cell-class-name="table-header" @selection-change="handleSelectionChange">
					<el-table-column type="selection" width="55" align="center"></el-table-column>
					<el-table-column prop="id" label="ID" width="55" align="center"></el-table-column>
					<el-table-column prop="name" label="角色名"></el-table-column>
					
					<el-table-column prop="roleKey" label="角色标识"></el-table-column>
				<!-- 	<el-table-column label="状态" align="center">
						<template slot-scope="scope">
							<el-tag :type="scope.row.state==='成功'?'success':(scope.row.state==='失败'?'danger':'')">
								{{scope.row.state}}
							</el-tag>
						</template>
					</el-table-column>

					<el-table-column prop="date" label="注册时间"></el-table-column> -->
					<el-table-column label="操作" width="180" align="center">
						<template slot-scope="scope">
							<el-button disabled  type="text" icon="el-icon-edit" class="mr10"
								@click="handleEdit(scope.$index, scope.row)">编辑
							</el-button>
							<template>
								<el-popconfirm @confirm="handleDelete(scope.$index, scope.row)" confirm-button-text='确定'
									cancel-button-text='取消' icon-color="red" title="确定要删除吗?">
									<el-button disabled slot="reference" type="text" icon="el-icon-delete" class="red">删除
									</el-button>
								</el-popconfirm>
							</template>
							<!-- <el-button type="text" icon="el-icon-delete" class="red"
								@click="handleDelete(scope.$index, scope.row)">删除</el-button> -->
						</template>
					</el-table-column>
				</el-table>
			<!-- 	<div class="pagination">
					<el-pagination background layout="total, prev, pager, next" :current-page="query.pageIndex"
						:page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange">
					</el-pagination>
				</div> -->
			</div>
		</div>

		<!-- 编辑弹出框 -->
		<el-dialog title="编辑" :visible.sync="editVisible" width="30%">
			<el-form ref="form" :model="form" label-width="70px">
				<el-form-item label="用户名">
					<el-input v-model="form.username"></el-input>
				</el-form-item>
				<el-form-item label="密码">
					<el-input v-model="form.password"></el-input>
				</el-form-item>
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button @click="cancelEdit">取 消</el-button>
				<el-button type="primary" @click="saveEdit">确 定</el-button>
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
			return {
				query: {
					address: '',
					username: '',
					pageIndex: 1,
					pageSize: 6
				},
				tableData: [],
				multipleSelection: [],
				delList: [],
				tableShow: true,
				editVisible: false,
				pageTotal: 0,
				pages:0,
				form: {
					temppassword: '',
					tempusername: '',
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

				ajaxGet("/sysRole/roles").then(res => {
					console.log(res);
					if (res) {
						this.tableData = res;
					}
				});
			},
			// 触发搜索按钮
			handleSearch() {
				//this.$set(this.query, 'pageIndex', 1);
				this.query.pageIndex=1;
				this.query.pageSize=6;
				this.getData();
			},
			//重置
			handlerest() {
				this.tableData = [];
				this.tableShow = false;
				this.query.address = '';
				this.query.username = '';
				this.query.pageIndex=1;
			},
			// 删除操作
			handleDelete(index, row) {
				console.log(row.id);
				ajaxDelete('/employee', row.id).then(res => {
					if (res) {
						this.$message.success('删除成功');
						if(this.tableData.length===1&&this.query.pageIndex!=1&&this.query.pageIndex==this.pages)
						this.query.pageIndex-=1;
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
					ajaxPost('/employee/deleteAll', ids).then(res => {
						if (res) {
							this.$message.success('删除成功');
							console.log(this.tableData.length);
							if(this.tableData.length===this.multipleSelection.length&&this.query.pageIndex!=1&&this.query.pageIndex===this.pages)
							this.query.pageIndex-=1;
							this.getData();
							this.multipleSelection = [];
							// for (let i = 0; i < ids.length; i++) {
							// 	for (let j = 0; j < this.tableData.length; j++) {
							// 		if (ids[i] === this.tableData[j].id) {
							// 			this.tableData.splice(j, 1);
							// 			break;
							// 		}
							// 	}
							// }
						} else {
							this.$message.error('删除失败');
						}
					})
					//let str = '';
					//this.delList = this.delList.concat(this.multipleSelection);
					// for (let i = 0; i < length; i++) {
					// 	str += this.multipleSelection[i].name + ' ';
					// }
					// this.$message.error(`删除了${str}`);
				}
			},
			// 编辑操作
			handleEdit(index, row) {
				this.idx = index;
				this.form = row;
				this.form.tempusername = row.username;
				this.form.temppassword = row.password;
				console.log(this.form);
				this.editVisible = true;
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
				// .catch(error => {
				// 	this.editVisible = false;
				// 	this.$message.error(`服务器异常`);
				// })
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
		width: 300px;
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

	.table-td-thumb {
		display: block;
		margin: auto;
		width: 40px;
		height: 40px;
	}
</style>
