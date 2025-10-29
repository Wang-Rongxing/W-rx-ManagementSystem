<template>
	<div class="header">
		<!-- 折叠按钮 -->
		<div class="collapse-btn" @click="collapseChage">
			<i v-if="!collapse" class="el-icon-s-fold"></i>
			<i v-else class="el-icon-s-unfold"></i>
		</div>
		<div class="logo">
      <img src="../../../public/logo.png" alt="logo">
      飛鱼酒店后台管理系统</div>
		<div class="header-right">
			<div class="header-user-con">
				<!-- 全屏显示 -->
				<div class="btn-fullscreen" @click="handleFullScreen">
					<el-tooltip effect="dark" :content="fullscreen?`取消全屏`:`全屏`" placement="bottom">
						<i class="el-icon-rank"></i>
					</el-tooltip>
				</div>
				<!-- 消息中心 -->
				<!-- <div class="btn-bell">
					<el-tooltip effect="dark" :content="message?`有${message}条未读消息`:`消息中心`" placement="bottom">
						<router-link to="/tabs">
							<i class="el-icon-bell"></i>
						</router-link>
					</el-tooltip>
					<span class="btn-bell-badge" v-if="message"></span>
				</div> -->
				<!-- 用户头像 -->
				<div class="user-avator">
					<img src="../../assets/img/img.jpg" />
				</div>
				<!-- 用户名下拉菜单 -->
				<el-dropdown class="user-name" trigger="click" @command="handleCommand">
					<span class="el-dropdown-link">
						{{username}}
						<i class="el-icon-caret-bottom"></i>
					</span>
					<el-dropdown-menu slot="dropdown">
						<el-dropdown-item divided command="updatePassword">修改密码</el-dropdown-item>
						<el-dropdown-item divided command="loginout">退出登录</el-dropdown-item>
					</el-dropdown-menu>
				</el-dropdown>
			</div>
		</div>
		<el-dialog title="修改密码" :visible.sync="editVisible" width="30%" :before-close="cancelEdit">
			<el-form size="mini" ref="form" status-icon :rules="rules" :model="form" label-width="90px">
				<el-form-item prop="password" label="密  码:"  class="handle-input-password">
					<el-input type="password" v-model="form.password" ></el-input>
				</el-form-item>
				<el-form-item prop="rpassword" label="确认密码:"  class="handle-input-password">
					<el-input type="password" v-model="form.rpassword"></el-input>
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
	import bus from '../common/bus';
	import {
		fetchData,
		ajaxPost,
		ajaxGet,
		ajaxDelete
	} from '../../api/index';
	export default {
		data() {
			var validatePassword = (rule, value, callback) => {
				if (value === '') {
					callback(new Error('请输入密码'));
				} else {
					if (this.form.rpassword !== '') {
						this.$refs.form.validateField('rpassword');
					}
					callback();
				}
			};
			var validateRpassword = (rule, value, callback) => {
				if (value === '') {
					callback(new Error('请再次输入密码'));
				} else if (value !== this.form.password) {
					callback(new Error('两次输入密码不一致!'));
				} else {
					callback();
				}
			};
			return {
				collapse: true,
				fullscreen: false,
				name: 'linxin',
				message: 2,
				editVisible: false,
				form: {
					password: '',
					rpassword: ''
				},
				rules: {
					password: [{required:true,
							validator: validatePassword,
							trigger: 'blur'
						},
						{
							min: 6,
							max: 8,
							message: '长度在 6 到 8 个字符',
							trigger: 'blur'
						}
					],
					rpassword: [{required:true,
						validator: validateRpassword,
						trigger: 'blur'
					}]
				},
				user: JSON.parse(sessionStorage.getItem('user'))
			};
		},
		computed: {
			username() {
				let username = this.user.username;
				return username ? username : this.name;
			}
		},
		methods: {
			
			cancelEdit() {
				this.$refs.form.resetFields();
				this.editVisible = false;
			},
			saveEdit() {
				// console.log(this.user);
				this.$refs.form.validate((valid) => {
					if (valid) {
						let data = {
							jobId: this.user.jobId,
							password: this.form.password
						};
						ajaxPost("/user/updateUserPassword", data).then(res => {
							if (res) {
								this.$message.success(`密码修改成功`);
								this.editVisible = false;
								this.$refs.form.resetFields();
								// this.form.password = '';
								// this.form.rpassword = '';
							} else {
								this.$message.error(`密码修改失败`);
							}
						})
					} else {
						return false;
					}
				})
			},

			// 用户名下拉菜单选择事件
			handleCommand(command) {
				if (command == 'loginout') {
					sessionStorage.removeItem('user');
					this.$store.commit('setRoles', []);
					this.$router.push('/login');
					//this.$router.replace({path: '/login'});
					//location.reload();
				} else if (command == 'updatePassword') {
					this.editVisible = true;
				}
			},
			// 侧边栏折叠
			collapseChage() {
				this.collapse = !this.collapse;
				bus.$emit('collapse', this.collapse);
			},
			// 全屏事件
			handleFullScreen() {
				// this.fullscreen=sessionStorage.getItem("fullscreen")?JSON.parse(sessionStorage.getItem("fullscreen")):this.fullscreen;

				let element = document.documentElement;
				if (this.fullscreen) {
					if (document.exitFullscreen) {
						document.exitFullscreen();
					} else if (document.webkitCancelFullScreen) {
						document.webkitCancelFullScreen();
					} else if (document.mozCancelFullScreen) {
						document.mozCancelFullScreen();
					} else if (document.msExitFullscreen) {
						document.msExitFullscreen();
					}
				} else {
					if (element.requestFullscreen) {
						element.requestFullscreen();
					} else if (element.webkitRequestFullScreen) {
						element.webkitRequestFullScreen();
					} else if (element.mozRequestFullScreen) {
						element.mozRequestFullScreen();
					} else if (element.msRequestFullscreen) {
						// IE11
						element.msRequestFullscreen();
					}
				}

				this.fullscreen = !this.fullscreen;
			}
		},
		mounted() {
			if (document.body.clientWidth < 1500) {
				this.collapseChage();
			}


		},
		created() {
			// window.addEventListener("beforeunload", () => {
			//  sessionStorage.setItem("fullscreen", JSON.stringify(this.fullscreen));
			// });


		}
	};
</script>
<style scoped>
	.header {
		position: relative;
		box-sizing: border-box;
		width: 100%;
		height: 70px;
		font-size: 22px;
		color: #fff;
	}

	.collapse-btn {
		float: left;
		padding: 0 21px;
		cursor: pointer;
		line-height: 70px;
	}

	.header .logo {
	float: left;
	width: 260px;
	height: 70px;
	display: flex;
	align-items: center;
	justify-content: center;
}

.header .logo img {
	max-height: 40px;
	max-width: 200px;
	object-fit: contain;
}

	.header-right {
		float: right;
		padding-right: 50px;
	}

	.header-user-con {
		display: flex;
		height: 70px;
		align-items: center;
	}

	.btn-fullscreen {
		transform: rotate(45deg);
		margin-right: 5px;
		font-size: 24px;
	}

	.btn-bell,
	.btn-fullscreen {
		position: relative;
		width: 30px;
		height: 30px;
		text-align: center;
		border-radius: 15px;
		cursor: pointer;
	}
   
   .handle-input-password {
   	width: 290px;
   	display: inline-block;
   }
   

	.btn-bell-badge {
		position: absolute;
		right: 0;
		top: -2px;
		width: 8px;
		height: 8px;
		border-radius: 4px;
		background: #f56c6c;
		color: #fff;
	}

	.btn-bell .el-icon-bell {
		color: #fff;
	}

	.user-name {
		margin-left: 10px;
	}

	.user-avator {
		margin-left: 20px;
	}

	.user-avator img {
		display: block;
		width: 40px;
		height: 40px;
		border-radius: 50%;
	}

	.el-dropdown-link {
		color: #fff;
		cursor: pointer;
	}

	.el-dropdown-menu__item {
		text-align: center;
	}
</style>
