<template>

	<div>

		<div class="crumbs">
			<el-breadcrumb separator="/">
				<el-breadcrumb-item>
					<i class="el-icon-lx-cascades"></i> 成绩录入
				</el-breadcrumb-item>
			</el-breadcrumb>
		</div>

		<div class="container">


			<div class="handle-box">

				<!-- <el-button type="primary" icon="el-icon-delete" class="handle-del mr10" @click="delAllSelection">批量删除
				</el-button> -->
				<!-- <el-select v-model="query.address" placeholder="地址" class="handle-select mr10">
					<el-option key="1" label="广东省" value="广东省"></el-option>
					<el-option key="2" label="湖南省" value="湖南省"></el-option>
				</el-select> -->
				<font size="2">查询学生: </font>
				<el-input v-model="query.sno" size="mini" placeholder="考生号" class="handle-input-sno mr10"></el-input>
				<el-input v-model="query.idcard" size="mini" placeholder="身份证" class="handle-input-idcard mr10">
				</el-input>
				<el-input v-model="query.username" size="mini" placeholder="姓名" class="handle-input-username mr10">
				</el-input>
				<el-button type="primary" size="mini" icon="el-icon-search" @click="handleSearch">搜索</el-button>
				<el-button type="primary" size="mini" class="yel" @click="handlerest">重置</el-button>

			</div>
			<!-- class="table"  header-cell-class-name="table-header"-->
			<div v-show="tableShow" class="handle-box">
				<el-table :data="tableData" border class="table" ref="multipleTable"
					header-cell-class-name="table-header" @selection-change="handleSelectionChange">
					<el-table-column prop="sno" label="考试号" width="90px" align="center"></el-table-column>
					<el-table-column prop="username" label="姓名"></el-table-column>
					<el-table-column prop="sex" width="50px" label="性别"></el-table-column>
					<el-table-column prop="idcard" label="身份证" width="150px" align="center"></el-table-column>
					<af-table-column prop="subject" label="科类"></af-table-column>
					<af-table-column prop="tcs" label="特长"></af-table-column>
					<el-table-column prop="major" width="150px" label="专业名称"></el-table-column>
					<el-table-column prop="testdate" width="150px" label="考试日期"></el-table-column>
					<el-table-column prop="result" label="成绩状态">
						<template slot-scope="scope">
							<!-- <el-tag :type="scope.row.result==null||scope.row.result.length==0?'success':'danger'">
								{{scope.row.result==null||scope.row.result.length==0?'可录入':'已录入'}}
							</el-tag> -->
							<el-tag :type="scope.row.result==null?'success':'danger'">
								{{scope.row.result==null?'可录入':'已录入'}}
							</el-tag>
						</template>
					</el-table-column>
				</el-table>
			</div>
			<div v-show="testTableShow" class="handle-box">

				<font size="2">输入学生成绩: </font>

				<el-form size="mini" :model="testTableData" :inline="true" status-icon :rules="rules" ref="ruleForm"
					class="demo-form-inline">
					<!-- 注：对于80分以下或90分以上考生需要在备注中做出说明,不在选项中的说明请直接在备注中输入 -->
					<el-tag class="mr10"> 注：对于80分以下或90分以上考生需要在备注中做出说明 </el-tag>
					<el-form-item label="备注:" prop="remarks" class=" mr10">
						<el-select size="mini" clearable filterable allow-create default-first-option
							v-model="testTableData.remarks" placeholder="其它情况的备注请在此处直接输入" class="handle-input-remakes mr10">
							<el-option size="mini" label="身体有明显缺陷" value="身体有明显缺陷"></el-option>
							<el-option size="mini" label="语言及表达交流障碍" value="语言及表达交流障碍"></el-option>
							<el-option size="mini" label="三观及精神状态明显不正常" value="三观及精神状态明显不正常"></el-option>
							<el-option size="mini" label="获奖较多" value="获奖较多"></el-option>
							<el-option size="mini" label="文体特长明显" value="文体特长明显"></el-option>
						</el-select>
					</el-form-item>

					<el-form-item label="成绩:" prop="result" >
						<el-input v-model.number="testTableData.result"  maxlength="3"  class="handle-input-result mr10">
						</el-input>
					</el-form-item>

					<el-form-item>
						<el-popconfirm @confirm="handletest" confirm-button-text='确定'
							cancel-button-text='取消' icon-color="red" title="成绩提交后不能修改,确定提交吗?">
							<el-button size="mini"  slot="reference" type="primary" class="mr10">提交
							</el-button>
						</el-popconfirm>
					</el-form-item>
				</el-form>
				<!-- <el-table :data="testTableData" border class="table">
					<el-table-column prop="remarks" label="备注">
						<template slot-scope="scope">
							<el-input size="mini" v-model="scope.row.remarks"></el-input>
						</template>
					</el-table-column>
					<el-table-column width="80px" label="成绩">
						<template slot-scope="scope">
							<el-input size="mini"  v-model="scope.row.result"></el-input>
						</template>
					</el-table-column>

					<el-table-column label="操作" width="100" align="center">
						<template slot-scope="scope">
							 <el-button size="mini" type="text" icon="el-icon-edit" class="mr10"
								@click="handleEdit(scope.$index, scope.row)">编辑
							</el-button> 
						<template>
								<el-popconfirm @confirm="handletest(scope.$index, scope.row)" confirm-button-text='确定'
									cancel-button-text='取消' icon-color="red" title="成绩提交后不能修改,确定提交吗?">
									<el-button size="mini" slot="reference" type="text" class="red">提交
									</el-button>
								</el-popconfirm>
							</template>
							 <el-button type="text" icon="el-icon-delete" class="red"
								@click="handleDelete(scope.$index, scope.row)">删除</el-button> 
						</template>
					</el-table-column>
				</el-table> -->

			</div>
		</div>

		<div class="container">
			<div class="handle-box">
				<font size="2">已录入学生: </font>
				<el-table :data="testedTableData" border class="table" ref="multipleTable"
					header-cell-class-name="table-header" @selection-change="handleSelectionChange">
					<el-table-column prop="sno" label="考试号" width="90px" align="center"></el-table-column>
					<el-table-column prop="username" label="姓名"></el-table-column>
					<el-table-column prop="sex" width="50px" label="性别"></el-table-column>
					<el-table-column prop="idcard" label="身份证" width="150px" align="center"></el-table-column>
					<af-table-column prop="subject" label="科类"></af-table-column>
					<af-table-column prop="tcs" label="特长"></af-table-column>
					<el-table-column prop="major" width="150px" label="专业名称"></el-table-column>
					<el-table-column prop="result" label="成绩"></el-table-column>
					<el-table-column prop="remarks" label="备注"></el-table-column>
					<el-table-column prop="committime" width="140px" label="考试时间"></el-table-column>
				</el-table>
				<div class="pagination">
					<el-pagination background layout="total, prev, pager, next" :current-page="testedquery.pageIndex"
						:page-size="testedquery.pageSize" :total="pageTotal" @current-change="handlePageChange">
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

			var checkResult = (rule, value, callback) => {
				if (value===null||value==='') {
					 callback(new Error('成绩不能为空'));
				}else if (!Number.isInteger(value)) {
					callback(new Error('请输入数字值'));
				} else {
					if (value < 0 || value > 100) {
						callback(new Error('成绩必须是0~100'));
					} else if (value < 80) {
						if (!this.testTableData.remarks || this.testTableData.remarks === '获奖较多' || this.testTableData
							.remarks === '文体特长明显') {
							callback(new Error('成绩80分以下需要正确备注'));
						}
					} else if (value >= 90) {
						if (!this.testTableData.remarks || this.testTableData.remarks === '身体有明显缺陷' || this
							.testTableData.remarks === '语言及表达交流障碍' || this.testTableData.remarks === '三观及精神状态明显不正常') {
							callback(new Error('成绩90分以上需要正确备注'));
						}
					} else {
						if (this.testTableData.remarks) {
							callback(new Error('成绩在80至90分之间不需要备注'));
						}
					}
					callback();
				}
			};
			var checkRemarks = (rule, value, callback) => {
				if (this.testTableData.result!==null) {
					this.$refs.ruleForm.validateField('result');
				}
				callback();
			};
			return {
				
				testTableData: {
					result: '',
					remarks: ''
				},
				rules: {
					result: [{required:true,
						validator: checkResult,
						trigger: 'blur'
					}],
					remarks: [{
						validator: checkRemarks,
						trigger: 'change'
					}]
				},
				query: {
					sno: '',
					idcard: '',
					username: '',
					pageIndex: 1,
					pageSize: 2
				},
				testedquery: {
					pageIndex: 1,
					pageSize: 3
				},
				headerObj: {
					token: JSON.parse(window.sessionStorage.getItem("user")).token
				},
				tableData: [],

				testedTableData: [],
				multipleSelection: [],
				delList: [],
				tableShow: false,
				testTableShow: false,
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
		created() {
			this.getTestedData();
		},
		methods: {
			// 获取 easy-mock 的模拟数据
			getTestedData() {

				ajaxGet("/student/selectStudentsByTested", this.testedquery).then(res => {
					console.log(res);
					if (res.records) {
						this.testedTableData = res.records;
						this.pageTotal = res.total;
						this.pages = res.pages;
					}
				});
			},
			getData() {

				ajaxPost("/student/selectStudentsByTest", this.query).then(res => {
					if (res.records) {
						this.tableShow = true;
						this.tableData = res.records;
						//this.testTableData = JSON.parse(JSON.stringify(res.records));
						// this.pageTotal = res.total;
						// this.pages = res.pages;
						//console.log(this.tableData[0]);
						if (this.tableData.length === 0) {
							this.testTableShow = false;
						} else if (this.tableData.length === 1) {
							this.$refs.ruleForm.resetFields();
							if (this.tableData[0].result==null) {
								this.testTableData = JSON.parse(JSON.stringify(this.tableData[0]));
								this.testTableShow = true;
							}else{
								this.testTableShow = false;
							}
						} else if (this.tableData.length > 1) {
							this.$message.warning("搜索出多个学生，请输入更具体的条件查询");
							this.testTableShow = false;
						}
					}
					// else{
					// 	this.tableShow = false;
					// 	this.testTableShow = false;
					// 	this.tableData=[];
					// 	this.testTableData=[];
					// }
				});
			},
			//上传
			handelUpload(param) {
				//http://localhost:9090/ssm_demo/student/import
				const formData = new FormData();
				//console.log(param);
				formData.append('file', param.file);
				ajaxPost("/student/import", formData).then(res => {
					if (res) {
						this.$message.success("上传成功");
						this.getData();
					}
				})

			},
			//上传成功
			handleUploadSuc() {
				this.$message.success("上传成功");
				this.getData();
			},

			// 触发搜索按钮
			handleSearch() {
				//this.$set(this.query, 'pageIndex', 1);
				this.query.pageIndex = 1;
				this.getData();
			},
			//重置
			handlerest() {
				this.tableData = [];
				//this.testTableData.remarks='';
				//this.testTableData.result='';
				this.$refs.ruleForm.resetFields();
				this.tableShow = false;
				this.testTableShow = false;
				this.query.pageIndex = 1;
				this.query.sno = '';
				this.query.idcard = '';
				this.query.username = ''
			},
			//  提交考试成绩
			handletest(formName) {
				this.$refs.ruleForm.validate((valid) => {
					if (valid) {
						ajaxPost('/student/updateTest', this.testTableData).then(res => {
							if (res) {
								this.$message.success('提交成功');
								this.getData();
								this.getTestedData();
								this.testTableShow = false;
							} else {
								this.$message.error('提交失败');
							}
						})
					} else {
						console.log('error submit!!');
            this.$message.error('提交的信息不合格');
						return false;
					}
				});
			},
			// handletest(index, row) {

			// 	console.log(row.id);
			// 	ajaxPost('/student/updateTest', row).then(res => {
			// 		if (res) {
			// 			this.$message.success('提交成功');
			// 			this.getData();
			// 			this.getTestedData();
			// 			this.testTableShow = false;
			// 		} else {
			// 			this.$message.error('提交失败');
			// 		}
			// 	})
			// },
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
							if (this.tableData.length === this.multipleSelection.length && this.query.pageIndex !==
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
				this.$set(this.testedquery, 'pageIndex', val);
				this.getTestedData();
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

	.handle-input-result {
		width: 100px;
		display: inline-block;
	}

	.handle-input-idcard {
		width: 170px;
		display: inline-block;
	}
	.handle-input-remakes {
		width: 270px;
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
