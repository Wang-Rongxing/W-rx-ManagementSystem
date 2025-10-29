<template>
	<div>
		<div class="crumbs">
			<el-breadcrumb separator="/">
				<el-breadcrumb-item>
					<i class="el-icon-lx-cascades"></i> 权限管理
				</el-breadcrumb-item>
			</el-breadcrumb>
		</div>
		<div class="container">
			<div class="handle-box">
				<template>
					<el-popconfirm @confirm="handleInitialPerminssion" confirm-button-text='确定' cancel-button-text='取消'
						icon="el-icon-info" icon-color="red" title="所有没有权限的用户会被授权为监考老师">
						<el-button size="mini" slot="reference" type="primary" icon="el-icon-s-tools" class="mr10">批量授权
						</el-button>
					</el-popconfirm>
				</template>
				<!-- <el-button type="primary" icon="el-icon-delete" class="handle-del mr10" @click="delAllSelection">批量删除
				</el-button> -->
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
					<el-table-column prop="jobId" label="工号" width="155"></el-table-column>
					<el-table-column prop="username" label="用户名" width="260"></el-table-column>
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

					<el-table-column label="权限">
						<template slot-scope="scope">
							<li v-for="site in scope.row.sysRoleList" class="li">
								{{ site.name }}
							</li>
						</template>

					</el-table-column>
					<el-table-column label="权限管理" width="180" align="center">
						<template slot-scope="scope">
							<el-button type="text" icon="el-icon-menu" class="mr10" :disabled="scope.row.username=='admin'"
								@click="setRoles(scope.$index, scope.row)">修改权限
							</el-button>
							<!-- <template>
								<el-popconfirm @confirm="handleDelete(scope.$index, scope.row)" confirm-button-text='确定'
									cancel-button-text='取消' icon-color="red" title="确定要删除吗?">
									<el-button slot="reference" type="text" icon="el-icon-delete" class="red">删除
									</el-button>
								</el-popconfirm>
							</template> -->
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

		<!-- 编辑弹出框 ref="form"  :model="form" -->
		<el-dialog size="mini" title="修改权限" :visible.sync="editVisible" width="30%" :before-close="cancelEdit">
			<el-form label-width="70px">
				<el-form-item size="mini" label="姓名:">
					{{form.username}}
				</el-form-item>
				<el-form-item size="mini" label="工号:">
					{{form.jobId}}
				</el-form-item>
				<el-form-item size="mini" label="权限:">
<!--					 :default-checked-keys="roleCheck"   v-model="form.sysRoleList"-->
					<el-tree :data="roleData" :props="defaultProps"
						@check="handleTreeChecked" :default-expand-all="true" ref="tree" node-key="id" show-checkbox>
					</el-tree>
				</el-form-item>
			</el-form>

			<span slot="footer" class="dialog-footer">
				<el-button size="mini" @click="cancelEdit">取 消</el-button>
				<el-button size="mini" type="primary" @click="saveEdit">确 定</el-button>
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
				pageTotal: 0,
				pages: 0,

				form: {
					tempRoleList: [],
				},
				roleData: [{
					id: 0,
					name: '选择权限',
					children: [{
							id: 1,
							name: '监考老师'
						},
						{
							id: 2,
							name: '教务老师'
						},
						{
							id: 3,
							name: '系统管理员'
						}
					]
				}],
				defaultProps: {
					children: 'children',
					label: 'name'
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

				ajaxGet("/user/userWithRoleByPage", this.query).then(res => {
					console.log(res);
					if (res.records) {
						this.tableShow = true;
						this.tableData = res.records;
						this.pageTotal = res.total;
						this.pages = res.pages;
					}
				});
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
						if (this.tableData.length === 1 && this.query.pageIndex !== 1 && this.query.pageIndex ===
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
			handleInitialPerminssion() {
				ajaxPost('/user/initialPerminssion').then(res => {
					if (res) {
						this.$message.success({
							message: '批量教师权限成功',
							center: true
						});
					} else {
						this.$message.error({
							message: '批量授权失败',
							center: true
						});
					}
				})


			},
			// 修改角色权限
			setRoles(index, row) {
				this.form = row;
				this.form.tempRoleList = row.sysRoleList;
				this.editVisible = true;
				// let roleChecked = [];
				// row.sysRoleList.forEach(role => {
				// 	if (role.name === '监考老师') {
				// 		roleChecked.push(1);
				// 	} else if (role.name === '教务老师') {
				// 		roleChecked.push(2);
				// 	} else {
				// 		roleChecked.push(3);
				// 	}
				// });
				//设置选中
				this.$nextTick(() => {
					this.$refs.tree.setCheckedNodes(row.sysRoleList);
					//this.$refs.tree.setCheckedKeys(roleChecked);
				});
			},
			//tree中checkBox被选中事件处理
			handleTreeChecked() {
				this.form.sysRoleList = this.$refs.tree.getCheckedNodes(true, true);
			},
			// 提交修改角色权限
			saveEdit() {
				console.log(this.form);
				
				ajaxPost("/user/updateUserRole", this.form).then(res => {
					if (res) {
						this.editVisible = false;
						this.$message.success(this.form.username+'老师的角色权限修改成功');
					} else {
						this.editVisible = false;
						this.form.sysRoleList = this.form.tempRoleList;
						this.$message.error(this.form.username+'老师的角色权限修改失败');
					}
					//this.$set(this.tableData, this.idx, this.form);
				})
				// .catch(error => {
				// 	this.editVisible = false;
				// 	this.form.sysRoleList = this.form.tempRoleList;
				// 	this.$message.error(`服务器异常`);
				// })
			},
			//取消权限设置
			cancelEdit() {
				this.form.sysRoleList = this.form.tempRoleList;
				this.editVisible = false;
			},
			//关闭对话框
			// handleDialogClose() {
			// 	this.cancelEdit();
			// },
			// 分页导航
			handlePageChange(val) {
				this.$set(this.query, 'pageIndex', val);
				this.getData();
			},

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

	.li {
		display: inline;
		/* display: inline-block; */
		margin-right: 10px;
		/* list-style: none; */
		/* CSS注释：加list-style:none去掉li默认产生”点“ */
	}
</style>
