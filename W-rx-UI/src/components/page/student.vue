<template>

	<div>

		<div class="crumbs">
			<el-breadcrumb separator="/">
				<el-breadcrumb-item>
					<i class="el-icon-lx-cascades"></i> 学生管理
				</el-breadcrumb-item>
			</el-breadcrumb>
		</div>

		<div class="container">

			
			<div class="handle-box">
				<font  size="2">  搜索学生:</font>
				<el-select size="mini" v-model="query.resultIsNull"  clearable placeholder="成绩分类" class="handle-select mr10">
					<el-option key="1" label="已录入成绩" value="notNull"></el-option>
					<el-option key="2" label="未录入成绩" value="null"></el-option>
					
				</el-select>
				<el-input v-model="query.sno"  size="mini" placeholder="考生号" class="handle-input-sno mr10"></el-input>
				<el-input v-model="query.idcard" size="mini"  placeholder="身份证" class="handle-input-idcard mr10"></el-input>
				<el-input v-model="query.username" size="mini"   placeholder="姓名" class="handle-input-username mr10"></el-input>
				<el-button type="primary" size="mini" icon="el-icon-search" @click="handleSearch">
					{{query.sno==''&&query.idcard==''&&query.username==''&&query.resultIsNull==''?'搜索全部':'根据条件搜索'}}
					</el-button>
				<el-button type="primary" size="mini" class="yel" @click="handlerest">重置</el-button>
			</div>
			
			<div class="handle-box">
				<template>
					<el-popconfirm @confirm="delAllSelection" confirm-button-text='确定' cancel-button-text='取消'
						icon="el-icon-info" icon-color="red" title="确定要删除吗?">
						<el-button slot="reference" type="primary" size="mini" icon="el-icon-delete" class="bgred mr10">批量删除
						</el-button>
					</el-popconfirm>
				</template>
				<el-upload :http-request="handelUpload" action="string" :headers="headerObj" class="inline-block" accept=".xlsx" :on-success="handleUploadSuc"
					:show-file-list="false">
					<el-button type="primary" size="mini" class="mr10" icon="el-icon-upload2" >导入学生信息</el-button>
				</el-upload>
			</div>
<!-- class="table"  header-cell-class-name="table-header"-->
			<div v-show="tableShow">
				<el-table   :data="tableData" border class="table" ref="multipleTable" 
				header-cell-class-name="table-header"	 @selection-change="handleSelectionChange">
					<el-table-column type="selection"  align="center" ></el-table-column>
					<el-table-column prop="sno" label="考试号" width="90px" align="center"></el-table-column>
					<el-table-column prop="username" label="姓名"></el-table-column>
					<el-table-column prop="sex" width="50px" label="性别"></el-table-column>
					<el-table-column prop="idcard" label="身份证" width="150px" align="center"></el-table-column>
					<af-table-column prop="subject" label="科类"></af-table-column>
					<af-table-column prop="tcs"  label="特长"></af-table-column>
					<el-table-column prop="result" width="50px" label="成绩"></el-table-column>
					<!-- <af-table-column prop="major"  label="专业名称"></af-table-column> -->
					<el-table-column prop="major" width="160" label="专业名称"></el-table-column>
					
					<el-table-column prop="testdate" width="90px" label="考试日期"></el-table-column>
					<el-table-column prop="testtime" width="110px" label="面试时间"></el-table-column>
					<el-table-column prop="classroom" label="侯考室"></el-table-column>	
					<el-table-column prop="temperature" label="体温℃"></el-table-column>	
					
					<el-table-column prop="remarks" label="备注"></el-table-column>
					<el-table-column prop="committime" width="140px" label="考试时间"></el-table-column>
				
					<el-table-column label="操作" width="180" align="center">
						<template slot-scope="scope">
							<el-button size="mini" type="text" icon="el-icon-edit" class="mr10"
								@click="handleEdit(scope.$index, scope.row)">编辑
							</el-button>
							<template>
								<el-popconfirm @confirm="handleDelete(scope.$index, scope.row)" confirm-button-text='确定'
									cancel-button-text='取消' icon-color="red" title="确定要删除吗?">
									<el-button size="mini" slot="reference" type="text" icon="el-icon-delete" class="red">删除
									</el-button>
								</el-popconfirm>
							</template>
							<el-button type="text" icon="el-icon-delete" class="red"
								@click="handleDelete(scope.$index, scope.row)">删除</el-button>
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
					sno:'',
					idcard:'',
					username:'',
					resultIsNull:'',
					pageIndex: 1,
					pageSize: 10
				},
				headerObj: {
				        token: JSON.parse(window.sessionStorage.getItem("user")).token
				      },
				tableData: [],
				multipleSelection: [],
				delList: [],
				tableShow: false,
				editVisible: false,
				pageTotal: 0,
				pages: 0,
				form: {
					temppassword: '',
					tempusername: '',
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

				ajaxPost("/student/selectStudents", this.query).then(res => {
					console.log(res);
					if (res.records) {
						this.tableShow = true;
						this.tableData = res.records;
						this.pageTotal = res.total;
						this.pages = res.pages;
					}
				});
			},
			//上传
			handelUpload(param){
				//http://localhost:9090/ssm_demo/student/import
			const formData=	new FormData();
			//console.log(param);
			formData.append('file',param.file);
			ajaxPost("/student/import",formData).then(res=>{
				if(res){
					this.$message.success("上传成功");
					this.getData();
				}
			})
				
			},
			//上传成功
			handleUploadSuc(){
			this.$message.success("上传成功");
			this.getData();
			},

			// 触发搜索按钮
			handleSearch() {
				//this.$set(this.query, 'pageIndex', 1);
				this.query.pageIndex=1;
				this.getData();
			},
			//重置
			handlerest() {
				this.tableData = [];
				this.tableShow = false;
				this.query.pageIndex = 1;
				this.query.sno='';
				this.query.idcard='';
				this.query.username='';
				this.query.resultIsNull=''
			},
			// 删除操作
			handleDelete(index, row) {

				console.log(row.id);
				ajaxDelete('/student/user', row.id).then(res => {
					if (res) {
						this.$message.success('删除成功');
						if (this.tableData.length === 1 && this.query.pageIndex != 1 && this.query.pageIndex ==
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
							if (this.tableData.length === this.multipleSelection.length && this.query.pageIndex !=
								1 && this.query.pageIndex === this.pages)
								this.query.pageIndex -= 1;

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
		margin-bottom: 10px;
	}

	.handle-select {
		width: 120px;
	}

	.handle-input-sno {
		width: 110px;
		display: inline-block;
	}

	.handle-input-idcard {
			width: 170px;
			display: inline-block;
	}
	.handle-input-username {
		width: 80px;
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

	.inline-block {
		height: 10px;
		display: inline-block;
	}

	.margin-change {
		display: inline-block;
		margin-left: 10px;
	}

	.bgred {
		background-color: #ff6d53;
	}

	.bggre {
		background-color: #55aa00;
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
