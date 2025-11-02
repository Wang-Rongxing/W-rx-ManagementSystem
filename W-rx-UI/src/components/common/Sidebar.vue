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
				items: [
					 //     {
						// path: '/ht/dashboard',
						// component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/Dashboard.vue'),
						// meta: { title: '系统首页' },
					 //       icon: 'el-icon-lx-home',
					 //       index: 'dashboard',
					 //       title: '系统首页'
					 //     },
					//      {
					// 	  path: '/ht/table',
					// 	                   component: () => import(/* webpackChunkName: "table" */ '../components/page/BaseTable.vue'),
					// 	                   meta: { title: '基础表格' ,roles:['ROLE_edu_admin','ROLE_sys_admin']},
					//          icon: 'el-icon-lx-cascades',
					//          index: 'table',
					//          title: '基础表格'
					//      },
					//  	{
					// 	path: '/ht/test',
					// 		    component: () => import(/* webpackChunkName: "table" */ '../components/page/test.vue'),
					// 		    meta: { title: '单招考试',roles:['ROLE_teacher'] },
					//  	    icon: 'el-icon-lx-cascades',
					//  	    index: 'test',
					//  	    title: '单招考试'
					//  	},
					//  	{
					//  	    icon: 'el-icon-lx-calendar',
					//  	   index: '1',
					//  	    title: '考试管理',
					// 	 meta: { roles:['ROLe_edu_admin','ROLE_sys_admin'] },
					//  	    subs: [
					//  	        {
					// 			path: '/ht/teacher',
					// 				    component: () => import(/* webpackChunkName: "table" */ '../components/page/teacher.vue'),
					// 				    meta: { title: '教师管理' , roles:['ROLE_edu_admin']},
					//  	            index: 'teacher',
					//  	            title: '教师管理'
					//  	        },

					//  	        {
					// 			path: '/ht/student',
					// 				    component: () => import(/* webpackChunkName: "table" */ '../components/page/Customer.vue'),
					// 				    meta: { title: '学生管理', roles:['ROLE_sys_admin'] },
					//  	            index: 'student',
					//  	            title: '学生管理'
					//  	        } ,
					//  			{
					// 			path: '/ht/score',
					// 				    component: () => import(/* webpackChunkName: "table" */ '../components/page/score.vue'),
					// 				    meta: { title: '成绩管理' , roles:['ROLE_sys_admin']},
					//  			    index: 'score',
					//  			    title: '成绩管理'
					//  			} 
					//  	    ]
					//  	},
					// { path: '/ht/tabs',
					// 		                 component: () => import(/* webpackChunkName: "tabs" */ '../components/page/Tabs.vue'),
					// 		                 meta: { title: 'tab选项卡', roles:['ROLE_edu_admin'] },
					//         icon: 'el-icon-lx-copy',
					//         index: 'tabs',
					//         title: 'tab选项卡'
					//     },
					// 	{
					// 		path: '/ht/404',
					// 		component: () => import( /* webpackChunkName: "404" */ '../components/page/ht404.vue'),
					// 		meta: {
					// 			title: '404'
					// 		}
					// 	},
					// 	{
					// 		path: '/ht/403',
					// 		component: () => import( /* webpackChunkName: "403" */ '../components/page/403.vue'),
					// 		meta: {
					// 			title: '403'
					// 		}
					// 	}

				]

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
							// if(item.subs)
							// {
							// 	item.subs=item.subs.filter(sub=>{
							// 		let b=false;
							// 		sub.meta.roles.forEach(role=>{
							// 			for(let r2 of this.$store.state.roles)
							// 			{if(r2===role)
							// 			  {
							// 				b=true ;
							// 				break;
							// 			  }
							// 			}
							// 		})

							// 		return b;
							// 	});
							// }

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
