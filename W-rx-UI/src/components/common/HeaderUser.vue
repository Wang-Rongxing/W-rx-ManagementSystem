<template>
	<div class="header">
		<!-- 折叠按钮 -->
<!--		<div class="collapse-btn" @click="collapseChage">-->
<!--			<i v-if="!collapse" class="el-icon-s-fold"></i>-->
<!--			<i v-else class="el-icon-s-unfold"></i>-->
<!--		</div>-->
		<div class="logo">
      <img src="../../../public/logo.png" alt="logo">
      飛鱼酒店网
    </div>
    <div class="menu-container">
      <el-menu :default-active="activeIndex" mode="horizontal" @select="handleSelect" class="horizontal-menu">
        <el-menu-item index="1">首页</el-menu-item>
        <el-menu-item index="2">预定客房</el-menu-item>
        <el-menu-item index="3">我的订单</el-menu-item>
        <el-menu-item index="4">历史入住</el-menu-item>
        <el-menu-item index="5">个人中心</el-menu-item>
      </el-menu>
    </div>
		<div class="header-right">
			<div class="header-employee-con">
				<!-- 全屏显示 -->
				<div class="btn-fullscreen" @click="handleFullScreen">
					<el-tooltip effect="dark" :content="fullscreen?`取消全屏`:`全屏`" placement="bottom">
						<i class="el-icon-rank"></i>
					</el-tooltip>
				</div>

				<!-- 用户头像 -->
				<div class="employee-avator">
					<img src="../../assets/img/img.jpg" />
				</div>
				<!-- 用户名下拉菜单 -->
				<el-dropdown class="employee-name" trigger="click" @command="handleCommand">
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
        activeIndex: '1', // 默认激活首页
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
				employee: JSON.parse(sessionStorage.getItem('user'))
			};
		},
		computed: {
			username() {
				let username = this.employee.name;
				return username ? username : this.name;
			}
		},
		methods: {
      handleSelect(key, keyPath) {
        console.log('选中菜单:', key, keyPath);
        // 处理菜单点击事件
        switch(key) {
          case '1':
            this.$router.push('/HomeUser/home');
            break;
          case '2':
            this.$router.push('/HomeUser/reserve');
            break;
          case '3':
            this.$router.push('/HomeUser/orders');
            break;
          case '4':
            this.$router.push('/HomeUser/history');
            break;
          case '5':
            this.$router.push('/HomeUser/profile');
            break;
        }
      },
			cancelEdit() {
				this.$refs.form.resetFields();
				this.editVisible = false;
			},
			saveEdit() {
				// console.log(this.employee);
				this.$refs.form.validate((valid) => {
					if (valid) {
						let data = {
							jobId: this.employee.jobId,
							password: this.form.password
						};
						ajaxPost("/employee/updateUserPassword", data).then(res => {
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
					sessionStorage.removeItem('employee');
					this.$store.commit('setRoles', []);
					this.$router.push('/login');
					//this.$router.replace({path: '/login'});
					//location.reload();
				} else if (command == 'updatePassword') {
					this.editVisible = true;
				}
			},
			// 侧边栏折叠
			// collapseChage() {
			// 	this.collapse = !this.collapse;
			// 	bus.$emit('collapse', this.collapse);
			// },
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
	color: black;
    background-color: white;
    display: flex;
    align-items: center;
    justify-content: space-between;
}

	.collapse-btn {
		float: left;
		padding: 0 21px;
		cursor: pointer;
		line-height: 70px;
	}

	.header .logo {
width: 250px;
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
	padding-right: 50px;
}

	.header-employee-con {
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
		color: black;
	}

	.btn-bell .el-icon-bell {
		color: black;
	}

	.employee-name {
		margin-left: 10px;
	}

	.employee-avator {
		margin-left: 20px;
	}

	.employee-avator img {
		display: block;
		width: 40px;
		height: 40px;
		border-radius: 50%;
	}

	.el-dropdown-link {
		color: black;
		cursor: pointer;
	}

	.el-dropdown-menu__item {
		text-align: center;
	}
  .menu-container {
    flex: 1;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .horizontal-menu {
    background-color: transparent;
    color: black;
    height: 100%;
    line-height: 70px;
  }

  .horizontal-menu .el-menu-item {
    color: black;
    height: 70px;
    line-height: 70px;
    border-bottom: 2px solid transparent;
  }

  .horizontal-menu .el-menu-item:hover {
    background-color: rgba(255, 255, 255, 0.1);
  }

  .horizontal-menu .el-menu-item.is-active {
    color: black;
    background-color: rgba(255, 255, 255, 0.1);
    border-bottom: 2px solid #000;
  }
</style>
