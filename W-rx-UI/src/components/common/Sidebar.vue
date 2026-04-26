<template>
	<div class="sidebar">
		<el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" background-color="#324157"
			text-color="#bfcbd9" active-text-color="#20a0ff" unique-opened router>
			<template v-for="item in items">
				<template v-if="item.subs">
					<el-submenu :index="item.index" :key="item.index">
						<template slot="title">
							<i :class="item.icon"></i>
							<span slot="title">{{ item.title }}</span>
						</template>
						<template v-for="subItem in item.subs">
							<el-submenu v-if="subItem.subs" :index="subItem.index" :key="subItem.index">
								<template slot="title">{{ subItem.title }}</template>
								<el-menu-item v-for="(threeItem,i) in subItem.subs" :key="i" :index="threeItem.index">
									{{ threeItem.title }}
								</el-menu-item>
							</el-submenu>
							<el-menu-item v-else :index="subItem.index" :key="subItem.index">{{ subItem.title }}
							</el-menu-item>
						</template>
					</el-submenu>
				</template>
				<template v-else>
					<el-menu-item :index="item.index" :key="item.index">
						<i :class="item.icon"></i>
						<span slot="title">{{ item.title }}</span>
					</el-menu-item>
				</template>
			</template>
		</el-menu>
	</div>
</template>

<script>
	import bus from '../common/bus';
	export default {
		data() {
			return {
				collapse: false,
				items: []
			};
		},
		computed: {
			onRoutes() {
				 return this.$route.path.replace('/ht/', '');
			}
		},
		created() {
			// 通过 Event Bus 进行组件间通信，来折叠侧边栏
			bus.$on('collapse', msg => {
				this.collapse = msg;
				bus.$emit('collapse-content', msg);
			});
			console.log(this.$store.state.roles);
			let temp =JSON.parse(JSON.stringify(this.$store.state.items)); 
			
			this.items = temp.filter(item => {
				let flag = false;
				if (item.index) {
					this.$store.state.roles.forEach(role => {
						if (item.meta.roles) {
							for (let r1 of item.meta.roles) {
								if (r1 === role) {
									flag = true;
									break;
								}
							}
						} else {
							flag = true;
						}

					})
				}
				if (flag && item.subs) {
					item.subs = item.subs.filter(sub => {
						let b = false;
						sub.meta.roles.forEach(role => {
							for (let r2 of this.$store.state.roles) {
								if (r2 === role) {
									b = true;
									break;
								}
							}
						})

						return b;
					});
				}
				return flag;
			})
			console.log(this.items);
		}
	};
</script>

<style scoped>
	.sidebar {
		display: block;
		position: absolute;
		left: 0;
		top: 70px;
		bottom: 0;
		overflow-y: scroll;
	}

	.sidebar::-webkit-scrollbar {
		width: 0;
	}

	.sidebar-el-menu:not(.el-menu--collapse) {
		width: 200px;
	}

	.sidebar>ul {
		height: 100%;
	}
</style>
